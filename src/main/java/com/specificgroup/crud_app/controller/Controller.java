package com.specificgroup.crud_app.controller;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.specificgroup.crud_app.util.Attributes;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.specificgroup.crud_app.util.Constants.HttpMethod.*;
import static com.specificgroup.crud_app.util.Constants.HttpResponseStatus.*;

/**
 * Provides methods CRUD and support methods for processing http request and response.
 */
public abstract class Controller {

    static final String CREATE_REQUEST = "/.+/create";
    static final String READ_REQUEST = "/.+/get";
    static final String UPDATE_REQUEST = "/.+/update";
    static final String DELETE_REQUEST = "/.+/delete";

    static final String REQUEST_ATTRIBUTES_DELIMITER = "&";
    static final String REQUEST_ATTRIBUTE_DELIMITER = "=";

    /**
     * Processing a http request for create element in database.
     *
     * @param httpExchange a http exchange.
     * @return a long result.
     */
    abstract Long create(final HttpExchange httpExchange);

    /**
     * Processing a http request for find(get) element in database.
     *
     * @param httpExchange a http exchange.
     * @return a list of json objects.
     */
    abstract List<JsonObject> get(final HttpExchange httpExchange);

    /**
     * Processing a http request for update element in database.
     *
     * @param httpExchange a http exchange.
     * @return a long result.
     */
    abstract Long update(final HttpExchange httpExchange);

    /**
     * Processing a http request for delete element in database.
     *
     * @param httpExchange a http exchange.
     * @return a boolean result.
     */
    abstract boolean delete(final HttpExchange httpExchange);

    /**
     * The main method that processing request method and delivering particular request in right method.
     *
     * @param httpExchange a http exchange.
     * @throws IOException when a controller encounters a problem.
     */
    public void execute(final HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String requestType = httpExchange.getRequestURI().getPath();
        int statusCode;

        switch (requestMethod) {
            case GET -> {
                if (requestType.matches(READ_REQUEST)) {
                    List<JsonObject> jsonObjects = get(httpExchange);
                    writeResponse(httpExchange, jsonObjects, STATUS_OK);
                } else {
                    response(httpExchange, STATUS_NOT_FOUND, -1);
                }
            }
            case POST -> {
                JsonObject jsonObject = new JsonObject();
                long id;
                int status;
                if (requestType.matches(CREATE_REQUEST)) {
                    id = create(httpExchange);
                    jsonObject.put(Attributes.ID,id);
                    status = id < 0 ? STATUS_NOT_FOUND : STATUS_CREATED;
                    writeResponse(httpExchange,List.of(jsonObject),status);
                } else {
                    notSupportRequest(httpExchange, requestType, "Request type {} does noy support");
                }
            }
            case PUT -> {
                JsonObject jsonObject = new JsonObject();
                long id;
                int status;
                if (requestType.matches(UPDATE_REQUEST)) {
                    id = update(httpExchange);
                    jsonObject.put(Attributes.ID,id);
                    status = id < 0 ? STATUS_NOT_FOUND : STATUS_OK;
                    writeResponse(httpExchange,List.of(jsonObject),status);
                } else {
                    notSupportRequest(httpExchange, requestType, "Request type {} does noy support");
                }
            }
            case DELETE -> {
                if (requestType.matches(DELETE_REQUEST)) {
                    statusCode = delete(httpExchange) ? STATUS_NO_CONTENT : STATUS_NOT_FOUND;
                } else {
                    statusCode = STATUS_NOT_FOUND;
                }
                response(httpExchange, statusCode, -1);
            }
            default -> notSupportRequest(httpExchange, requestMethod, "Method {} does not support");
        }
    }

    /**
     * The method reads attributes from url and parsing their.
     *
     * @param uri a url.
     * @param key a particular json attribute from attributes.
     * @return optional string attribute.
     */
    protected Optional<String> readAttributes(final URI uri, JsonKey key) {
        final String path = uri.getQuery();
        return path != null ? Arrays.stream(path.split(REQUEST_ATTRIBUTES_DELIMITER))
                .filter(a -> a.contains(key.getKey()))
                .map(p -> p.split(REQUEST_ATTRIBUTE_DELIMITER)[1])
                .findFirst() : Optional.empty();
    }

    /**
     * The method reads request from httpExchange and creates JsonObject.
     *
     * @param httpExchange a http exchange.
     * @return a json object.
     */
    protected JsonObject readRequestFromJson(final HttpExchange httpExchange) {
        JsonObject deserialize = null;
        try (InputStream requestBody = httpExchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))) {
            if (reader.ready()) {
                deserialize = (JsonObject) Jsoner.deserialize(reader);
            }
        } catch (IOException | JsonException e) {
            throw new RuntimeException(e);
        }
        return deserialize;
    }

    /**
     * The method search attributes from url.
     *
     * @param requestURI a url.
     * @param attributes a container for attributes.
     */
    protected void searchAttributeUrl(URI requestURI, Map<Attributes, String> attributes) {
        Arrays.stream(Attributes.values())
                .forEach(at -> {
                    Optional<String> attribute = readAttributes(requestURI, at);
                    attribute.ifPresent(p -> attributes.put(at, p));
                });
    }

    private void response(final HttpExchange httpExchange, final int status, final int responseLength) {
        try {
            httpExchange.sendResponseHeaders(status, responseLength);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeResponse(final HttpExchange httpExchange, List<JsonObject> jsonObjects, int status)
            throws IOException {
        Headers responseHeaders = httpExchange.getResponseHeaders();
        StringBuilder db = new StringBuilder();
        for (JsonObject jsonObject : jsonObjects) {
            db.append(jsonObject.toJson());
        }
        responseHeaders.add("Content-type", "application/json");
        response(httpExchange, status, db.toString().getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            responseBody.write(db.toString().getBytes(StandardCharsets.UTF_8));
            responseBody.flush();
        }
    }

    private void notSupportRequest(final HttpExchange httpExchange, final String value, final String message) {
        try {
            httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}