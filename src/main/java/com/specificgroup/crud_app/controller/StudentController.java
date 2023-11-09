package com.specificgroup.crud_app.controller;

import static com.specificgroup.crud_app.util.Attributes.AGE;
import static com.specificgroup.crud_app.util.Attributes.EMAIL;
import static com.specificgroup.crud_app.util.Attributes.ID;
import static com.specificgroup.crud_app.util.Attributes.NAME;
import static com.specificgroup.crud_app.util.Attributes.PHONE;
import static com.specificgroup.crud_app.util.Attributes.SPECIALIZATION;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.service.StudentService;
import com.specificgroup.crud_app.util.Attributes;
import com.sun.net.httpserver.HttpExchange;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
class StudentController extends Controller {

    private final StudentService studentService;

    public StudentController(final StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Long create(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        CreateRequest createRequest = new CreateRequest.Builder()
                .name(jsonObject.getString(NAME))
                .age(jsonObject.getString(AGE))
                .specialization(jsonObject.getString(SPECIALIZATION))
                .phone(jsonObject.getString(PHONE))
                .email(jsonObject.getString(EMAIL))
                .build();

        return studentService.create(createRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    List<JsonObject> get(HttpExchange httpExchange) {
        URI requestURI = httpExchange.getRequestURI();
        Map<Attributes, String> attributes = new HashMap<>();
        searchAttributeUrl(requestURI, attributes);
        return studentService.get(attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Long update(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        UpdateRequest build = new UpdateRequest.Builder()
                .id(jsonObject.getString(ID))
                .name(jsonObject.getString(NAME))
                .age(jsonObject.getString(AGE))
                .specialization(jsonObject.getString(SPECIALIZATION))
                .phone(jsonObject.getString(PHONE))
                .email(jsonObject.getString(EMAIL))
                .build();

        return studentService.update(build);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean delete(final HttpExchange httpExchange) {
        URI uri = httpExchange.getRequestURI();
        Optional<String> id = readAttributes(uri, ID);
        boolean result = false;
        if (id.isPresent()) {
            result = studentService.deleteById(id.get());
        }
        return result;
    }
}