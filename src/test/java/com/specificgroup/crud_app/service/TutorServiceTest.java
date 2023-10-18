package com.specificgroup.crud_app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dao.Dao;
import com.specificgroup.crud_app.dao.TutorDao;
import com.specificgroup.crud_app.dao.specification.JdbcSpecification;
import com.specificgroup.crud_app.dao.specification.StudentsSpecification;
import com.specificgroup.crud_app.dao.specification.TutorSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.entity.Tutor;
import com.specificgroup.crud_app.exception.ValidationException;
import com.specificgroup.crud_app.service.impl.StudentService;
import com.specificgroup.crud_app.service.impl.TutorService;
import com.specificgroup.crud_app.util.Attributes;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.specificgroup.crud_app.util.Attributes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TutorServiceTest {

    private final Dao<Tutor> tutorDao = Mockito.mock(TutorDao.class);
    private final JdbcSpecification<Tutor> specification = Mockito.mock(TutorSpecification.class);
    @InjectMocks
    private Service service = new TutorService(tutorDao);

    @Test
    void createTutorTest_SuccessfulRequest(){
        CreateRequest createRequest = new CreateRequest.Builder()
                .name("Test")
                .specialization("Specialization")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();
        Mockito.doReturn(1L).when(tutorDao).create(createRequest);

        assertEquals(1L, service.create(createRequest));
        verify(tutorDao, times(1)).create(createRequest);
        verifyNoMoreInteractions(tutorDao);
    }

    @Test
    void createTutorTest_UnsuccessfulRequests(){
        CreateRequest nameValidationError = new CreateRequest.Builder()
                .name("123645")
                .specialization("Specialization")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        CreateRequest specializationValidationError = new CreateRequest.Builder()
                .name("Test")
                .specialization("1")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        CreateRequest phoneValidationError = new CreateRequest.Builder()
                .name("Test")
                .specialization("Specialization")
                .phone("+XXXxxXXXxxxx")
                .email("test@gmail.com")
                .build();

        CreateRequest emailValidationError = new CreateRequest.Builder()
                .name("Test")
                .specialization("Specialization")
                .phone("+000000000000")
                .email("testgmail.com")
                .build();


        assertAll(() -> {
            assertThrowsExactly(ValidationException.class, () -> service.create(nameValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.create(specializationValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.create(phoneValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.create(emailValidationError));
        });
    }

    @Test
    void getTutorsByAttributesTest_SuccessfulRequest(){
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(ID,"1");
        attributes.put(NAME,"Test");
        attributes.put(SPECIALIZATION,"Specialization");
        List<Tutor> tutors = new ArrayList<>();
        List<JsonObject> expected = new ArrayList<>();

        Mockito.doReturn(tutors).when(tutorDao).getBySpecification(specification);

        List<JsonObject> result = service.get(attributes);

        assertEquals(expected, result);
        verify(tutorDao, times(1)).getBySpecification(any(TutorSpecification.class));
        verify(tutorDao, times(0)).get();
        verifyNoMoreInteractions(tutorDao);
    }

    @Test
    void getTutorsTest_SuccessfulRequest(){
        Map<Attributes, String> attributes = new HashMap<>();
        List<Tutor> tutors = new ArrayList<>();
        List<JsonObject> expected = new ArrayList<>();

        Mockito.doReturn(tutors).when(tutorDao).get();

        List<JsonObject> result = service.get(attributes);

        assertEquals(expected, result);
        verify(tutorDao, times(1)).get();
        verify(tutorDao, times(0)).getBySpecification(any(TutorSpecification.class));
        verifyNoMoreInteractions(tutorDao);
    }

    @Test
    void getTutorsByAttributesTest_UnsuccessfulRequests(){
        Map<Attributes, String> wrongId = Map.of(ID, "A");
        Map<Attributes, String> wrongName = Map.of(NAME,"123658");
        Map<Attributes, String> wrongSpecialization = Map.of(SPECIALIZATION,"1");
        Map<Attributes, String> wrongPhone = Map.of(PHONE,"375(99)9999999");
        Map<Attributes, String> wrongEmail = Map.of(EMAIL,"wrong email");

        assertAll(() -> {
            assertTrue(service.get(wrongId).isEmpty());
            assertTrue(service.get(wrongName).isEmpty());
            assertTrue(service.get(wrongSpecialization).isEmpty());
            assertTrue(service.get(wrongPhone).isEmpty());
            assertTrue(service.get(wrongEmail).isEmpty());
        });
    }

    @Test
    void updateTutorTest_SuccessfulRequest(){
        UpdateRequest updateRequest = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .specialization("Developer")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        Mockito.doReturn(1L).when(tutorDao).getContactDetailsIdByMainEntityId(anyLong());
        Mockito.doReturn(1L).when(tutorDao).update(updateRequest, 1L);

        assertEquals(1L, service.update(updateRequest));
        verify(tutorDao, times(1)).getContactDetailsIdByMainEntityId(anyLong());
        verify(tutorDao, times(1)).update(updateRequest, 1L);
        verifyNoMoreInteractions(tutorDao);
    }

    @Test
    void updateTutorTest_UnsuccessfulRequest(){
        UpdateRequest idValidationError = new UpdateRequest.Builder()
                .id("A")
                .name("Test")
                .specialization("Specialization")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        UpdateRequest nameValidationError = new UpdateRequest.Builder()
                .id("1")
                .name("123645")
                .specialization("Specialization")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        UpdateRequest specializationValidationError = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .specialization("1")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        UpdateRequest phoneValidationError = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .specialization("Specialization")
                .phone("+XXXxxXXXxxxx")
                .email("test@gmail.com")
                .build();

        UpdateRequest emailValidationError = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .specialization("Specialization")
                .phone("+000000000000")
                .email("testgmail.com")
                .build();

        assertAll(() -> {
            assertThrowsExactly(ValidationException.class, () -> service.update(idValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.update(nameValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.update(specializationValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.update(phoneValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.update(emailValidationError));
        });
    }

    @Test
    void deleteByIdTest() {
        Mockito.doReturn(1L).when(tutorDao).getContactDetailsIdByMainEntityId(anyLong());
        Mockito.doReturn(true).when(tutorDao).deleteByContactDetailId(anyLong());

        assertTrue(service.deleteById("1"));
        verify(tutorDao, times(1)).getContactDetailsIdByMainEntityId(anyLong());
        verify(tutorDao, times(1)).deleteByContactDetailId(anyLong());
        verifyNoMoreInteractions(tutorDao);
    }
}