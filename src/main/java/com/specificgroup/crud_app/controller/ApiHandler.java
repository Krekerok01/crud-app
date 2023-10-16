package com.specificgroup.crud_app.controller;


import com.specificgroup.crud_app.exception.EntityNotFoundException;
import com.specificgroup.crud_app.exception.ValidationException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.specificgroup.crud_app.util.HTTPConstants.HttpResponseStatus.STATUS_BAD_REQUEST;
import static com.specificgroup.crud_app.util.HTTPConstants.HttpResponseStatus.STATUS_NOT_FOUND;

public record ApiHandler(Controller controller) {

    public void handle(final HttpExchange httpExchange) {
        try {
            controller.execute(httpExchange);
        } catch (final Exception e) {
            handleException(e, httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    private void handleException(Exception e, HttpExchange httpExchange){
        try {
            if (e instanceof ValidationException) {
                httpExchange.sendResponseHeaders(STATUS_BAD_REQUEST, 0);
            } else if (e instanceof EntityNotFoundException) {
                httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
            } else {
                httpExchange.sendResponseHeaders(STATUS_BAD_REQUEST, 0);
            }
        } catch (IOException exception){
            exception.printStackTrace();
        }

    }
}