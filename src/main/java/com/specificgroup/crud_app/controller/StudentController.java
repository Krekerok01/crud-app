package com.specificgroup.crud_app.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.service.Service;
import com.specificgroup.crud_app.util.Attributes;
import com.sun.net.httpserver.HttpExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.specificgroup.crud_app.util.Attributes.*;


class StudentController extends Controller {

    private final Service service;
    private final Logger logger =  LogManager.getLogger();

    public StudentController(final Service service) {
        this.service = service;
    }

    @Override
    Long create(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        CreateRequest createRequest = CreateRequest.builder()
                .name(jsonObject.getString(NAME))
                .age(jsonObject.getString(AGE))
                .specialization(jsonObject.getString(SPECIALIZATION))
                .phone(jsonObject.getString(PHONE))
                .email(jsonObject.getString(EMAIL))
                .build();

        logger.info("Creating a student:{}", createRequest);

        return service.create(createRequest);
    }

    @Override
    List<JsonObject> get(HttpExchange httpExchange) {
        URI requestURI = httpExchange.getRequestURI();
        Map<Attributes, String> attributes = new HashMap<>();
        searchAttributeUrl(requestURI, attributes);
        return service.get(attributes);
    }

    @Override
    Long update(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        UpdateRequest build = UpdateRequest.builder()
                .id(jsonObject.getString(ID))
                .name(jsonObject.getString(NAME))
                .age(jsonObject.getString(AGE))
                .specialization(jsonObject.getString(SPECIALIZATION))
                .phone(jsonObject.getString(PHONE))
                .email(jsonObject.getString(EMAIL))
                .build();

        return service.update(build);
    }

    @Override
    boolean delete(final HttpExchange httpExchange) {
        URI uri = httpExchange.getRequestURI();
        Optional<String> id = readAttributes(uri, ID);
        boolean result = false;
        if (id.isPresent()) {
            result = service.deleteById(id.get());
        }
        return result;
    }
}