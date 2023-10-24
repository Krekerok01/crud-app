package com.specificgroup.crud_app.controller;


import com.specificgroup.crud_app.exception.EntityNotFoundException;
import com.specificgroup.crud_app.exception.ValidationException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

import static com.specificgroup.crud_app.util.Constants.HttpResponseStatus.STATUS_BAD_REQUEST;
import static com.specificgroup.crud_app.util.Constants.HttpResponseStatus.STATUS_NOT_FOUND;

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
                httpExchange.sendResponseHeaders(STATUS_BAD_REQUEST, e.getLocalizedMessage().length());
            } else if (e instanceof EntityNotFoundException) {
                httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, e.getLocalizedMessage().length());
            } else {
                httpExchange.sendResponseHeaders(STATUS_BAD_REQUEST, e.getLocalizedMessage().length());
            }
            addResponseBodyToHttpExchange(httpExchange, e.getLocalizedMessage().getBytes());
        } catch (IOException exception){
            exception.printStackTrace();
        }

    }

    private void addResponseBodyToHttpExchange(HttpExchange httpExchange, byte[] bytes) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        os.write(bytes);
        os.flush();
        os.close();
    }
}