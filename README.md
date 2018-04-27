# Cloud Foundry Service Broker for a PostgreSQL
The broker currently publishes a simple service plan (no resource limit) for provisioning PostgreSQL databases.

## Design 

The broker uses a PostgreSQL database to manage it's service instance meta data. It has no dependencies besides PostgreSQL.

## Test

```
Retrieve Catalog:
curl -H "X-Broker-API-Version: 2.13" 'admin:pivotal@localhost:8080/v2/catalog'

Create Service Instance:
curl -X PUT -H "X-Broker-API-Version: 2.13" -H "Content-Type: application/json" 'admin:pivotal@localhost:8080/v2/service_instances/4cdc7c5e-5c97-4c1f-a9d1-86727d83dcaa?accepts_incomplete=true' -d '{"service_id": "f2aba675-fbe3-4400-adb5-25aaef9505b9","plan_id": "d18eb918-b9c1-458c-af2c-5435dfa41c80","context": {"platform": "cloudfoundry"},"organization_guid":"27c07924-05f2-4898-b434-e461de06abfd","space_guid": "59fbd1f8-62f9-4244-abfe-fb76cea86c07","parameters": {}}'

Bind Service Instance to Application Instance:
curl -X PUT -H "X-Broker-API-Version: 2.13" -H "Content-Type: application/json" 'admin:pivotal@localhost:8080/v2/service_instances/4cdc7c5e-5c97-4c1f-a9d1-86727d83dcaa/service_bindings/066ff85c-b3e8-4cd9-96d3-27f61f09be8d?accepts_incomplete=true' -d '{"service_id": "f2aba675-fbe3-4400-adb5-25aaef9505b9","plan_id": "d18eb918-b9c1-458c-af2c-5435dfa41c80","context": {"platform": "cloudfoundry"},"bind_resource":{"app_guid":"388fcdd3-4ae5-4b0b-b60a-0be50d8bf956"},"parameters": {}}'

Unbind Service Instance from Application Instance:
curl -X DELETE -H "X-Broker-API-Version: 2.13" 'admin:pivotal@localhost:8080/v2/service_instances/4cdc7c5e-5c97-4c1f-a9d1-86727d83dcaa/service_bindings/066ff85c-b3e8-4cd9-96d3-27f61f09be8d?accepts_incomplete=true&service_id=f2aba675-fbe3-4400-adb5-25aaef9505b9&plan_id=d18eb918-b9c1-458c-af2c-5435dfa41c80'

Delete Service Instance:
curl -X DELETE -H "X-Broker-API-Version: 2.13" 'admin:pivotal@localhost:8080/v2/service_instances/4cdc7c5e-5c97-4c1f-a9d1-86727d83dcaa?accepts_incomplete=true&service_id=f2aba675-fbe3-4400-adb5-25aaef9505b9&plan_id=d18eb918-b9c1-458c-af2c-5435dfa41c80'
```
