package com.specificgroup.crud_app.service.impl;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dao.Dao;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Tutor;
import com.specificgroup.crud_app.service.Service;
import com.specificgroup.crud_app.util.Attributes;
import com.specificgroup.crud_app.util.TutorSpecification;
import com.specificgroup.crud_app.util.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.specificgroup.crud_app.util.Attributes.*;
import static com.specificgroup.crud_app.util.Attributes.EMAIL;
import static com.specificgroup.crud_app.util.ValidationConstants.*;

public class TutorService implements Service {

    private final Dao<Tutor> tutorDao;
    private final Logger logger =  LogManager.getLogger();

    public TutorService(Dao<Tutor> tutorDao) {
        this.tutorDao = tutorDao;
    }

    @Override
    public Long create(CreateRequest createRequest) {
        logger.info("Creating a tutor:{}", createRequest);
        long result = INVALID_RESULT;
        if (createRequest != null) {
            try {
                Validator<CreateRequest> validator = Validator.of(createRequest)
                        .validator(request -> request.getName().matches(NAME_VALIDATION), "Name is not a string")
                        .validator(request -> request.getSpecialization().matches(SPECIALIZATION_VALIDATION), "Specialization is not a string")
                        .validator(request -> request.getEmail().matches(EMAIL_VALIDATION), "Valid email is required")
                        .validator(request -> request.getPhone().matches(PHONE_VALIDATION), "Valid phone is required. Example: +375294682593");
                validator.getMessages()
                        .forEach(System.out::println);
                result = validator.isEmpty() ? tutorDao.create(createRequest) : result;
            }catch (NullPointerException e) {
                return result;
            }
        }
        return result;
    }

    @Override
    public List<JsonObject> get(Map<Attributes, String> attributes) {
        logger.info("Getting a list of tutors.");
        boolean validation;
        List<Tutor> result = new ArrayList<>();
        List<JsonObject> jsonObjects = new ArrayList<>();
        if (attributes != null) {
            Validator<Map<Attributes, String>> validator = Validator.of(attributes);
            try {
                attributes.forEach((k, v) -> {
                    switch (k) {
                        case ID -> validator.validator(var -> var.get(ID).matches(DIGIT), "Student id is not digit.");
                        case NAME -> validator.validator(var -> var.get(NAME).matches(NAME_VALIDATION), "Incorrect name");
                        case SPECIALIZATION -> validator.validator(var -> var.get(SPECIALIZATION).matches(SPECIALIZATION_VALIDATION), "Incorrect specialization");
                    }
                });
                validation = validator.isEmpty();
            }catch (NullPointerException e) {
                validation = false;
            }
            result = validation ? tutorDao.get(new TutorSpecification(attributes)) : result;
        }
        for (Tutor tutor: result) {
            JsonObject json = new JsonObject();
            json.put(ID, tutor.getId());
            json.put(NAME, tutor.getName());
            json.put(SPECIALIZATION, tutor.getSpecialization());
            json.put(PHONE, tutor.getContactDetails().getPhone());
            json.put(EMAIL, tutor.getContactDetails().getEmail());
            jsonObjects.add(json);
        }
        return jsonObjects;
    }

    @Override
    public Long update(UpdateRequest updateRequest) {
        logger.info("Updating tutor information:{}", updateRequest);
        long result = INVALID_RESULT;
        if (updateRequest != null) {
            Validator<UpdateRequest> validator = Validator.of(updateRequest)
                    .validator(request -> request.getId().matches(DIGIT), "Tutor id is not digit.")
                    .validator(request -> request.getName().matches(NAME_VALIDATION), "Name is not a string")
                    .validator(request -> request.getSpecialization().matches(SPECIALIZATION_VALIDATION), "Specialization is not a string")
                    .validator(request -> request.getEmail().matches(EMAIL_VALIDATION), "Valid email is required")
                    .validator(request -> request.getPhone().matches(PHONE_VALIDATION), "Valid phone is required. Example: +375294682593");

            Long contactDetailsId = getContactDetailIdByTutorId(Long.parseLong(updateRequest.getId()));
            result = validator.isEmpty() ? tutorDao.update(updateRequest, contactDetailsId) : result;
        }
        return result;
    }

    @Override
    public boolean deleteById(String id) {
        logger.info("Deleting a tutor with id=:{}", id);
        boolean result = false;
        if (id != null) {
            try {
                Validator<String> validatorId = Validator.of(id).validator(i -> i.matches(DIGIT), "Tutor id is not digit.");
                Long tutorId = Long.parseLong(id);
                result = validatorId.isEmpty() && tutorDao.deleteByContactDetailId(getContactDetailIdByTutorId(tutorId));
            } catch (NullPointerException e){
                return false;
            }
        }
        return result;
    }

    private Long getContactDetailIdByTutorId(Long id) {
        Long contactDetailsId = tutorDao.getContactDetailsIdByMainEntityId(id);
        if (contactDetailsId == null) throw new RuntimeException("Not found contact detail id by tutor id");;
        return contactDetailsId;
    }
}
