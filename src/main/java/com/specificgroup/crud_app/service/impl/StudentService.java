package com.specificgroup.crud_app.service.impl;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dao.Dao;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.service.Service;
import com.specificgroup.crud_app.util.Attributes;
import com.specificgroup.crud_app.util.StudentsSpecification;
import com.specificgroup.crud_app.util.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.specificgroup.crud_app.util.Attributes.*;
import static com.specificgroup.crud_app.util.ValidationConstants.*;

public class StudentService implements Service {

    private final Dao<Student> studentDao;
    private final Logger logger =  LogManager.getLogger();

    public StudentService(Dao<Student> studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Long create(CreateRequest createRequest) {
        logger.info("Creating a student:{}", createRequest);
        long result = INVALID_RESULT;
        if (createRequest != null) {
            try {
                Validator<CreateRequest> validator = Validator.of(createRequest)
                        .validator(request -> request.getName().matches(NAME_VALIDATION), "Name is not a string")
                        .validator(request -> request.getAge().matches(AGE_VALIDATION), "Age is not a digit")
                        .validator(request -> request.getEmail().matches(EMAIL_VALIDATION), "Valid email is required")
                        .validator(request -> request.getPhone().matches(PHONE_VALIDATION), "Valid phone is required. Example: +375294682593");
                validator.getMessages()
                        .forEach(System.out::println);
                result = validator.isEmpty() ? studentDao.create(createRequest) : result;
            }catch (NullPointerException e) {
                return result;
            }
        }
        return result;
    }

    @Override
    public List<JsonObject> get(Map<Attributes, String> attributes) {
        logger.info("Getting a list of students.");
        boolean validation;
        List<Student> result = new ArrayList<>();
        List<JsonObject> jsonObjects = new ArrayList<>();
        if (attributes != null) {
            Validator<Map<Attributes, String>> validator = Validator.of(attributes);
            try {
                attributes.forEach((k, v) -> {
                    switch (k) {
                        case ID -> validator.validator(var -> var.get(ID).matches(DIGIT), "Student id is not digit.");
                        case NAME -> validator.validator(var -> var.get(NAME).matches(NAME_VALIDATION), "Incorrect name");
                        case AGE -> validator.validator(var -> var.get(AGE).matches(AGE_VALIDATION), "Incorrect age");
                    }
                });
                validation = validator.isEmpty();
            }catch (NullPointerException e) {
                validation = false;
            }
            result = validation ? studentDao.get(new StudentsSpecification(attributes)) : result;
        }
        for (Student student : result) {
            JsonObject json = new JsonObject();
            json.put(ID, student.getId());
            json.put(NAME, student.getName());
            json.put(AGE, student.getAge());
            json.put(PHONE, student.getContactDetails().getPhone());
            json.put(EMAIL, student.getContactDetails().getEmail());
            jsonObjects.add(json);
        }
        return jsonObjects;
    }

    @Override
    public Long update(UpdateRequest updateRequest) {
        logger.info("Updating student information:{}", updateRequest);
        long result = INVALID_RESULT;
        if (updateRequest != null) {
            Validator<UpdateRequest> validator = Validator.of(updateRequest)
                    .validator(request -> request.getId().matches(DIGIT), "Student id is not digit.")
                    .validator(request -> request.getName().matches(NAME_VALIDATION), "Name is not a string")
                    .validator(request -> request.getAge().matches(AGE_VALIDATION), "Age is not a digit")
                    .validator(request -> request.getEmail().matches(EMAIL_VALIDATION), "Valid email is required")
                    .validator(request -> request.getPhone().matches(PHONE_VALIDATION), "Valid phone is required. Example: +375294682593");

            Long contactDetailsId = getContactDetailIdByStudentId(Long.parseLong(updateRequest.getId()));
            result = validator.isEmpty() ? studentDao.update(updateRequest, contactDetailsId) : result;
        }
        return result;
    }

    @Override
    public boolean deleteById(String id) {
        logger.info("Deleting a student with id=:{}", id);
        boolean result = false;
        if (id != null) {
            try {
                Validator<String> validatorId = Validator.of(id).validator(i -> i.matches(DIGIT), "Student id is not digit.");
                Long studentId = Long.parseLong(validatorId.get());
                result = validatorId.isEmpty() && studentDao.deleteByContactDetailId(getContactDetailIdByStudentId(studentId));
            } catch (NullPointerException e){
                return false;
            }
        }
        return result;
    }

    private Long getContactDetailIdByStudentId(Long id) {
        Long contactDetailsId = studentDao.getContactDetailsIdByMainEntityId(id);
        if (contactDetailsId == null) throw new RuntimeException("Not found contact detail id by student id");;
        return contactDetailsId;
    }
}
