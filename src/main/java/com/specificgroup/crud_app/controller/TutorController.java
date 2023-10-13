package com.specificgroup.crud_app.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.service.Service;
import com.specificgroup.crud_app.util.Attributes;
import com.sun.net.httpserver.HttpExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.specificgroup.crud_app.util.Attributes.*;


public class TutorController extends Controller {

    private final Service service;
    private final Logger logger =  LogManager.getLogger();

    public TutorController(Service service) {
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
        logger.info("Creating a tutor:{}", createRequest);

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
        return 1L;
    }

    @Override
    boolean delete(final HttpExchange httpExchange) {
        return true;
    }
}
