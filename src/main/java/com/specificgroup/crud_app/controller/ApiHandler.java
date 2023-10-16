package com.specificgroup.crud_app.controller;


import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.specificgroup.crud_app.util.HTTPConstants.HttpResponseStatus.STATUS_NOT_FOUND;

public record ApiHandler(Controller controller) {

    public void handle(final HttpExchange httpExchange) {
        try {
            controller.execute(httpExchange);
        } catch (final Exception e) {
            handleException(httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    private void handleException(final HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
