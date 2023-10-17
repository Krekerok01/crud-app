package com.specificgroup.crud_app.util.database.connection;

import java.util.Map;

/**
 * Provides design pattern abstract method for create connection poll
 */
public class ConnectionPoolAbstract {

    private ConnectionPoolAbstract() {
        throw new UnsupportedOperationException();
    }

    public enum Type {
        DEFAULT,
        FLEXIBLE
    }

    public static ConnectionPool connectionPool(Type type, Map<String, String> properties) {
        ConnectionPool result;
        switch (type) {
            case DEFAULT -> result = ConnectionPoolFabric.defaultConnection();
            case FLEXIBLE -> result = ConnectionPoolFabric.flexibleConnection(properties);
            default -> throw new UnsupportedOperationException();
        }
        return result;
    }
}