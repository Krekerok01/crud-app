package com.specificgroup.crud_app.server;

import com.specificgroup.crud_app.controller.ApiHandler;
import com.specificgroup.crud_app.util.database.connection.ConnectionPool;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class Server {

    private HttpServer server;
    private final Map<String, ApiHandler> apiHandlers = new HashMap<>();
    private final Logger logger =  Logger.getLogger(Server.class.getName());

    public Server(Map<String, ApiHandler> apiHandlers) {
        this.apiHandlers.putAll(apiHandlers);
    }

    public void start() {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(8081), 0);
            apiHandlers.forEach((path, hand) -> server.createContext(path, hand::handle));
            server.start();
            logger.info("Server successfully started");
        } catch (IOException e) {
            logger.info("Server start failed");
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        logger.info("Server stopping");
        if (ConnectionPool.INSTANCE == null){
            ConnectionPool.INSTANCE.destroyPool();
        }
        server.stop(1);
        logger.info("Server successfully stopped");
    }
}
