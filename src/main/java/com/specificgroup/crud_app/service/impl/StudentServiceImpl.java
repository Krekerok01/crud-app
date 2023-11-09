package com.specificgroup.crud_app.service.impl;

import static com.specificgroup.crud_app.util.Attributes.AGE;
import static com.specificgroup.crud_app.util.Attributes.EMAIL;
import static com.specificgroup.crud_app.util.Attributes.ID;
import static com.specificgroup.crud_app.util.Attributes.NAME;
import static com.specificgroup.crud_app.util.Attributes.PHONE;
import static com.specificgroup.crud_app.util.Constants.Message.*;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.AGE_VALIDATION;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.DIGIT;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.EMAIL_VALIDATION;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.INVALID_RESULT;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.NAME_VALIDATION;
import static com.specificgroup.crud_app.util.validation.ValidationConstants.PHONE_VALIDATION;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dao.StudentDao;
import com.specificgroup.crud_app.dao.impl.specification.StudentSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.exception.EntityNotFoundException;
import com.specificgroup.crud_app.exception.ValidationException;
import com.specificgroup.crud_app.service.StudentService;
import com.specificgroup.crud_app.util.Attributes;
import com.specificgroup.crud_app.util.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * {@inheritDoc}
 */
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;
    private final Logger logger =  Logger.getLogger(StudentServiceImpl.class.getName());

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long create(CreateRequest createRequest) {
        logger.info("Creating a student.");
        long result = INVALID_RESULT;
        if (createRequest != null) {
            try {
                Validator<CreateRequest> validator = Validator.of(createRequest)
                        .validator(request -> request.getName().matches(NAME_VALIDATION), WRONG_NAME)
                        .validator(request -> request.getAge().matches(AGE_VALIDATION), WRONG_AGE)
                        .validator(request -> request.getEmail().matches(EMAIL_VALIDATION), WRONG_EMAIL)
                        .validator(request -> request.getPhone().matches(PHONE_VALIDATION), WRONG_PHONE);

                if (!validator.isEmpty()) throw new ValidationException(validator.getMessagesString());

                result = studentDao.create(createRequest);
            } catch (NullPointerException e) {
                throw new ValidationException("All fields are required.");
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JsonObject> get(Map<Attributes, String> attributes) {
        logger.info("Getting a list of students.");
        List<Student> result = new ArrayList<>();
        List<JsonObject> jsonObjects = new ArrayList<>();

        if (attributes != null && !attributes.isEmpty()) {
            result = getStudentsByAttributes(result, attributes);
        } else {
            result = studentDao.get();
        }

        result.forEach(student -> {
            JsonObject json = new JsonObject();
            json.put(ID, student.getId());
            json.put(NAME, student.getName());
            json.put(AGE, student.getAge());
            json.put(PHONE, student.getContactDetails().getPhone());
            json.put(EMAIL, student.getContactDetails().getEmail());
            jsonObjects.add(json);
        });

        return jsonObjects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long update(UpdateRequest updateRequest) {
        logger.info("Updating information for a student." + updateRequest.getId());
        long result = INVALID_RESULT;
        if (updateRequest != null) {
            try {
                Validator<UpdateRequest> validator = Validator.of(updateRequest)
                        .validator(request -> request.getId().matches(DIGIT), WRONG_ID)
                        .validator(request -> request.getName().matches(NAME_VALIDATION), WRONG_NAME)
                        .validator(request -> request.getAge().matches(AGE_VALIDATION), WRONG_AGE)
                        .validator(request -> request.getEmail().matches(EMAIL_VALIDATION), WRONG_EMAIL)
                        .validator(request -> request.getPhone().matches(PHONE_VALIDATION), WRONG_PHONE);

                if (!validator.isEmpty()) throw new ValidationException(validator.getMessagesString());
                Long contactDetailsId = getContactDetailIdByStudentId(Long.parseLong(updateRequest.getId()));

                result = studentDao.update(updateRequest, contactDetailsId);
            } catch (NullPointerException e){
                throw new ValidationException("All fields are required.");
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(String id) {
        logger.info("Deleting a student with id=" + id);
        boolean result = false;
        if (id != null) {
            try {
                Validator<String> validatorId = Validator.of(id).validator(i -> i.matches(DIGIT), WRONG_ID);
                Long studentId = Long.parseLong(validatorId.get());
                result = validatorId.isEmpty() &&
                        studentDao.deleteByContactDetailId(getContactDetailIdByStudentId(studentId));
            } catch (NullPointerException e){
                return false;
            }
        }
        return result;
    }

    private List<Student> getStudentsByAttributes(List<Student> result, Map<Attributes, String> attributes) {
        Validator<Map<Attributes, String>> validator = Validator.of(attributes);
        boolean validation;
        try {
            attributes.forEach((k, v) -> {
                switch (k) {
                    case ID -> validator.validator(var -> var.get(ID).matches(DIGIT), WRONG_ID);
                    case NAME -> validator.validator(var -> var.get(NAME).matches(NAME_VALIDATION), WRONG_NAME);
                    case AGE -> validator.validator(var -> var.get(AGE).matches(AGE_VALIDATION), WRONG_AGE);
                    case PHONE -> validator.validator(var -> var.get(PHONE).matches(PHONE_VALIDATION), WRONG_PHONE);
                    case EMAIL -> validator.validator(var -> var.get(EMAIL).matches(EMAIL_VALIDATION), WRONG_EMAIL);
                }
            });
            validation = validator.isEmpty();
        }catch (NullPointerException e) {
            validation = false;
        }
        result = validation ? studentDao.getBySpecification(new StudentSpecification(attributes)) : result;
        return result;
    }

    private Long getContactDetailIdByStudentId(Long id) {
        Long contactDetailsId = studentDao.getContactDetailsIdByStudentId(id);
        if (contactDetailsId == null) throw new EntityNotFoundException("Not found student with id=" + id);
        return contactDetailsId;
    }
}
