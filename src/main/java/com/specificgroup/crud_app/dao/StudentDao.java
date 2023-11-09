package com.specificgroup.crud_app.dao;

import com.specificgroup.crud_app.dao.impl.specification.StudentSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Student;
import java.util.List;

/**
 * Provides dao methods to create, read, update and delete operations.
 */
public interface StudentDao {

    /**
     * Creates and saves a new student entity to the database.
     * @param request a CreateRequest object which contain fields that need to fill for element table.
     * @return a long provides id element in element table
     */
    Long create(CreateRequest request);

    /**
     * Returns students by specification.
     * @param specification represent criteria for search element
     * @return a list of students.
     */
    List<Student> getBySpecification(StudentSpecification specification);

    /**
     * Returns all students.
     *
     * @return a list of students.
     */
    List<Student> get();

    /**
     * Updates student entity in the database.
     * @param request a UpdateRequest object which contain fields that need to update for element table.
     * @param contactDetailsId a related table field. For consistent update.
     * @return a long provides id element in element table
     */
    Long update(UpdateRequest request, Long contactDetailsId);

    /**
     * Deletes a student entity from the database.
     * @param contactDetailsId a long is a related table field id in database, used for consistent deletion.
     * @return provides whether the request was successful
     */
    boolean deleteByContactDetailId(Long contactDetailsId);

    /**
     * Get related table ID by main table id.
     * @param id a long is a main entity id from the database.
     * @return a long provides id element in element table
     */
    Long getContactDetailsIdByStudentId(Long id);
}
