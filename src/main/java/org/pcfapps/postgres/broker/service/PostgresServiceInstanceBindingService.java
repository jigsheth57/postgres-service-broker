package org.pcfapps.postgres.broker.service;

import org.pcfapps.postgres.broker.model.ServiceBinding;
import org.pcfapps.postgres.broker.repository.ServiceBindingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.model.binding.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostgresServiceInstanceBindingService implements ServiceInstanceBindingService {

    private static final String URI_KEY = "uri";
    private static final String JDBC_KEY = "jdbcUrl";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String DB_KEY = "name";
    private static final String HOST_KEY = "hostname";
    private static final String PORT_KEY = "port";

    @Value("${postgres.instance.hostname:localhost}")
    private String instanceIP;

    @Value("${postgres.instance.port:5432}")
    private String instancePort;

    private final ServiceBindingRepository bindingRepository;

    @Autowired
    DatabaseService dbs;

    public PostgresServiceInstanceBindingService(ServiceBindingRepository bindingRepository) {
        this.bindingRepository = bindingRepository;
    }

    @Override
    public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        CreateServiceInstanceAppBindingResponse.CreateServiceInstanceAppBindingResponseBuilder responseBuilder =
                CreateServiceInstanceAppBindingResponse.builder();
        String instanceId = request.getServiceInstanceId();
        Optional<ServiceBinding> binding = bindingRepository.findById(request.getBindingId());

        if (binding.isPresent()) {
            responseBuilder
                    .bindingExisted(true)
                    .credentials(binding.get().getCredentials());
        } else {
            String username = UUID.randomUUID().toString();
            String passwd = new BigInteger(130, new SecureRandom()).toString(32);

            Map<String, Object> credentials = new HashMap<>();
            credentials.put(JDBC_KEY, "jdbc:postgresql://"+instanceIP+":"+instancePort+"/"+instanceId+"?user="+username+"&password="+passwd+"&ssl=false");
            credentials.put(URI_KEY, "jdbc:postgresql://"+instanceIP+":"+instancePort+"/"+instanceId+"");
            credentials.put(USERNAME_KEY, username);
            credentials.put(PASSWORD_KEY, passwd);
            credentials.put(DB_KEY, instanceId);
            credentials.put(HOST_KEY, instanceIP);
            credentials.put(PORT_KEY, instancePort);

            if (dbs.createRoleForInstance(instanceId,username,passwd)) {
                saveBinding(request, credentials);
            } else
                throw new ServiceInstanceBindingDoesNotExistException(instanceId);

            responseBuilder
                    .bindingExisted(false)
                    .credentials(credentials);
        }

        return responseBuilder.build();
    }

    @Override
    public GetServiceInstanceBindingResponse getServiceInstanceBinding(GetServiceInstanceBindingRequest request) {
        String bindingId = request.getBindingId();

        Optional<ServiceBinding> serviceBinding = bindingRepository.findById(bindingId);

        if (serviceBinding.isPresent()) {
            return GetServiceInstanceAppBindingResponse.builder()
                    .parameters(serviceBinding.get().getParameters())
                    .credentials(serviceBinding.get().getCredentials())
                    .build();
        } else {
            throw new ServiceInstanceBindingDoesNotExistException(bindingId);
        }
    }

    @Override
    public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
        String bindingId = request.getBindingId();
        String instanceId = request.getServiceInstanceId();

        Optional<ServiceBinding> serviceBinding = bindingRepository.findById(bindingId);

        if (serviceBinding.isPresent()) {
            String username = serviceBinding.get().getCredentials().get(USERNAME_KEY).toString();
            dbs.deleteRoleForInstance(instanceId,username);
            bindingRepository.deleteById(bindingId);
        } else {
            throw new ServiceInstanceBindingDoesNotExistException(bindingId);
        }
    }

    private void saveBinding(CreateServiceInstanceBindingRequest request, Map<String, Object> credentials) {
        ServiceBinding serviceBinding =
                new ServiceBinding(request.getBindingId(), request.getParameters(), credentials);
        bindingRepository.save(serviceBinding);
    }
}
