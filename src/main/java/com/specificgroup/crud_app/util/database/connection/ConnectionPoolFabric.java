package com.specificgroup.crud_app.util.database.connection;

import com.specificgroup.crud_app.util.PropertiesUtil;

import java.util.Map;

import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.*;

public class ConnectionPoolFabric {

    private ConnectionPoolFabric() {
        throw new UnsupportedOperationException();
    }

    public static class PropertiesFile {

        public static final String PASSWORD_KEY = "db.password";
        public static final String USERNAME_KEY = "db.username";
        public static final String URL_KEY = "db.url";
        public static final String POOL_SIZE = "db.pool";

    }

    public static ConnectionPool defaultConnection() {
        return ConnectionPool.INSTANCE
                .urlKey(PropertiesUtil.get(URL_KEY))
                .passwordKey(PropertiesUtil.get(PASSWORD_KEY))
                .usernameKey(PropertiesUtil.get(USERNAME_KEY))
                .poolSize(PropertiesUtil.get(POOL_SIZE))
                .build();
    }

    public static ConnectionPool flexibleConnection(Map<String, String> properties) {
        return ConnectionPool.INSTANCE
                .urlKey(properties.get(URL_KEY))
                .passwordKey(properties.get(PASSWORD_KEY))
                .usernameKey(properties.get(USERNAME_KEY))
                .poolSize(properties.get(POOL_SIZE))
                .build();
    }
}