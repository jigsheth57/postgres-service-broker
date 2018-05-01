package org.pcfapps.postgres.broker.service;

import org.hibernate.dialect.PostgreSQL95Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public class DatabaseService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.username:postgres}")
    private String dbadmin;

    public boolean createDatabaseForInstance (String instanceId) {
        try {
            jdbcTemplate.execute("CREATE DATABASE \""+instanceId+"\" WITH ENCODING = 'UTF8' CONNECTION LIMIT = -1");
            jdbcTemplate.execute("REVOKE all on database \"" + instanceId + "\" from public");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteDatabaseForInstance (String instanceId) {
        try {
            jdbcTemplate.execute("REVOKE CONNECT ON DATABASE \""+instanceId+"\" FROM PUBLIC");
            jdbcTemplate.execute("SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = '"+instanceId+"' AND pid <> pg_backend_pid()");
            jdbcTemplate.execute("DROP DATABASE IF EXISTS \""+instanceId+"\"");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createRoleForInstance (String instanceId, String userid, String passwd) {
        try {
            jdbcTemplate.execute("CREATE USER \""+userid+"\" WITH LOGIN INHERIT password '"+passwd+"'");
            jdbcTemplate.execute("GRANT \""+dbadmin+"\" to \""+userid+"\"");
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteRoleForInstance (String instanceId, String userid) {
        try {
            jdbcTemplate.execute("ALTER ROLE \""+userid+"\" NOLOGIN");
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return true;
    }
}
