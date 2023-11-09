package com.specificgroup.crud_app.service.impl;

import static com.specificgroup.crud_app.util.Attributes.EMAIL;
import static com.specificgroup.crud_app.util.Attributes.ID;
import static com.specificgroup.crud_app.util.Attributes.NAME;
import static com.specificgroup.crud_app.util.Attributes.PHONE;
import static com.specificgroup.crud_app.util.Attributes.SPECIALIZATION;
import static com.specificgroup.crud_app.util.Constants.ExceptionMessage.*;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.DIGIT;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.EMAIL_VALIDATION;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.INVALID_RESULT;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.NAME_VALIDATION;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.PHONE_VALIDATION;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.SPECIALIZATION_VALIDATION;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dao.TutorDao;
import com.specificgroup.crud_app.dao.impl.specification.TutorSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Tutor;
import com.specificgroup.crud_app.exception.EntityNotFoundException;
import com.specificgroup.crud_app.exception.ValidationException;
import com.specificgroup.crud_app.service.TutorService;
import com.specificgroup.crud_app.util.Attributes;
import com.specificgroup.crud_app.util.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TutorServiceImpl implements TutorService {

    private final TutorDao tutorDao;
    private final Logger logger =  Logger.getLogger(TutorServiceImpl.class.getName());

    public TutorServiceImpl(TutorDao tutorDao) {
        this.tutorDao = tutorDao;
    }

    @Override
    public Long create(CreateRequest createRequest) {
        logger.info("Creating a tutor.");
        long result = INVALID_RESULT;
        if (createRequest != null) {
            try {
                Validator<CreateRequest> validator = Validator.of(createRequest)
                        .validator(request -> request.getName().matches(NAME_VALIDATION), WRONG_NAME)
                        .validator(request -> request.getSpecialization().matches(SPECIALIZATION_VALIDATION), WRONG_SPECIALIZATION)
                        .validator(request -> !request.getSpecialization().trim().isEmpty(), EMPTY_SPECIALIZATION)
                        .validator(request -> request.getEmail().matches(EMAIL_VALIDATION), WRONG_EMAIL)
                        .validator(request -> request.getPhone().matches(PHONE_VALIDATION), WRONG_PHONE);
                if (!validator.isEmpty()) throw new ValidationException(validator.getMessagesString());

                result = tutorDao.create(createRequest);
            } catch (NullPointerException e) {
                throw new ValidationException("All fields are required.");
            }
        }
        return result;
    }

    @Override
    public List<JsonObject> get(Map<Attributes, String> attributes) {
        logger.info("Getting a list of tutors.");
        List<Tutor> result = new ArrayList<>();
        List<JsonObject> jsonObjects = new ArrayList<>();

        if (attributes != null && !attributes.isEmpty()) {
            result = getTutorsByAttributes(result, attributes);
        } else {
            result = tutorDao.get();
        }

        result.forEach(tutor -> {
            JsonObject json = new JsonObject();
            json.put(ID, tutor.getId());
            json.put(NAME, tutor.getName());
            json.put(SPECIALIZATION, tutor.getSpecialization());
            json.put(PHONE, tutor.getContactDetails().getPhone());
            json.put(EMAIL, tutor.getContactDetails().getEmail());
            jsonObjects.add(json);
        });

        return jsonObjects;
    }

    @Override
    public Long update(UpdateRequest updateRequest) {
        logger.info("Updating information for tutor with id=" + updateRequest.getId());
        long result = INVALID_RESULT;
        if (updateRequest != null) {
            try {
                Validator<UpdateRequest> validator = Validator.of(updateRequest)
                        .validator(request -> request.getId().matches(DIGIT), WRONG_ID)
                        .validator(request -> request.getName().matches(NAME_VALIDATION), WRONG_NAME)
                        .validator(request -> request.getSpecialization().matches(SPECIALIZATION_VALIDATION), WRONG_SPECIALIZATION)
                        .validator(request -> !request.getSpecialization().trim().isEmpty(), EMPTY_SPECIALIZATION)
                        .validator(request -> request.getEmail().matches(EMAIL_VALIDATION), WRONG_EMAIL)
                        .validator(request -> request.getPhone().matches(PHONE_VALIDATION), WRONG_PHONE);

                if (!validator.isEmpty()) throw new ValidationException(validator.getMessagesString());

                Long contactDetailsId = getContactDetailIdByTutorId(Long.parseLong(updateRequest.getId()));
                result = tutorDao.update(updateRequest, contactDetailsId);
            } catch (NullPointerException e){
                throw new ValidationException("All fields are required.");
            }
        }
        return result;
    }

    @Override
    public boolean deleteById(String id) {
        logger.info("Deleting a tutor with id=" + id);
        boolean result = false;
        if (id != null) {
            try {
                Validator<String> validatorId = Validator.of(id).validator(i -> i.matches(DIGIT), WRONG_ID);
                Long tutorId = Long.parseLong(id);
                result = validatorId.isEmpty() &&
                        tutorDao.deleteByContactDetailId(getContactDetailIdByTutorId(tutorId));
            } catch (NullPointerException e){
                return false;
            }
        }
        return result;
    }

    private List<Tutor> getTutorsByAttributes(List<Tutor> result, Map<Attributes, String> attributes) {
        Validator<Map<Attributes, String>> validator = Validator.of(attributes);
        boolean validation;
        try {
            attributes.forEach((k, v) -> {
                switch (k) {
                    case ID -> validator.validator(var -> var.get(ID).matches(DIGIT), WRONG_ID);
                    case NAME -> validator.validator(var -> var.get(NAME).matches(NAME_VALIDATION), WRONG_NAME);
                    case SPECIALIZATION -> validator.validator(var -> var.get(SPECIALIZATION).matches(SPECIALIZATION_VALIDATION), WRONG_SPECIALIZATION);
                    case PHONE -> validator.validator(var -> var.get(PHONE).matches(PHONE_VALIDATION), WRONG_PHONE);
                    case EMAIL -> validator.validator(var -> var.get(EMAIL).matches(EMAIL_VALIDATION), WRONG_EMAIL);
                }
            });
            validation = validator.isEmpty();
        }catch (NullPointerException e) {
            validation = false;
        }
        result = validation ? tutorDao.getBySpecification(new TutorSpecification(attributes)) : result;
        return result;
    }

    private Long getContactDetailIdByTutorId(Long id) {
        Long contactDetailsId = tutorDao.getContactDetailsIdByTutorId(id);
        if (contactDetailsId == null) throw new EntityNotFoundException("Not found tutor with id=" + id);
        return contactDetailsId;
    }
}
