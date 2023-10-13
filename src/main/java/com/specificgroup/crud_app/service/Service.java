package com.specificgroup.crud_app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.util.Attributes;

import java.util.List;
import java.util.Map;

public interface Service {

    Long create(CreateRequest dto);

    List<JsonObject> get(Map<Attributes, String> attributes);

    Long update();

    boolean deleteById(String id);
}
