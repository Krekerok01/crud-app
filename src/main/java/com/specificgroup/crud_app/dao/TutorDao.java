package com.specificgroup.crud_app.dao;

import com.specificgroup.crud_app.dao.impl.specification.TutorSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Tutor;
import java.util.List;

/**
 * Provides dao methods to create, read, update and delete operations.
 */
public interface TutorDao {

    /**
     * Creates and saves a new tutor entity to the database.
     * @param request a CreateRequest object which contain fields that need to fill for element table.
     * @return a long provides id element in element table
     */
    Long create(CreateRequest request);

    /**
     * Returns tutors by specification.
     * @param specification represent criteria for search element
     * @return a list of tutors.
     */
    List<Tutor> getBySpecification(TutorSpecification specification);

    /**
     * Returns all tutors.
     *
     * @return a list of tutors.
     */
    List<Tutor> get();

    /**
     * Updates tutor entity in the database.
     * @param request a UpdateRequest object which contain fields that need to update for element table.
     * @param contactDetailsId a related table field. For consistent update.
     * @return a long provides id element in element table
     */
    Long update(UpdateRequest request, Long contactDetailsId);

    /**
     * Deletes a tutor entity from the database.
     * @param contactDetailsId a long is a related table field id from the database, used for consistent deletion.
     * @return provides whether the request was successful
     */
    boolean deleteByContactDetailId(Long contactDetailsId);

    /**
     * Get related table ID by main table id.
     * @param id a long is a main entity id from the database.
     * @return a long provides id element in element table
     */
    Long getContactDetailsIdByTutorId(Long id);
}
