package com.specificgroup.crud_app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.util.Attributes;
import java.util.List;
import java.util.Map;

/**
 * Provides methods for processing the input data from the controller and sending it to the dao
 */
public interface TutorService {

    /**
     * Validation CreateRequest data and sending request to the dao
     *
     * @param dto a request from the user
     * @return a long provides id element in element table
     */
    Long create(CreateRequest dto);

    /**
     * Validation input data and sending request to the dao
     *
     * @param attributes a set of request attributes
     * @return a list of json object
     */
    List<JsonObject> get(Map<Attributes, String> attributes);

    /**
     * Validation UpdateRequest data and sending request to the dao
     *
     * @param updateRequest a request from the user
     * @return a long provides id element in element table
     */
    Long update(UpdateRequest updateRequest);

    /**
     * Validation input data and sending request to the dao
     *
     * @param id an entity id to delete
     * @return a boolean result
     */
    boolean deleteById(String id);
}