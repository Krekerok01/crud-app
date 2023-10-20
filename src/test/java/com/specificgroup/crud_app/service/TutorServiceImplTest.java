package com.specificgroup.crud_app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dao.TutorDao;
import com.specificgroup.crud_app.dao.impl.TutorDaoImpl;
import com.specificgroup.crud_app.dao.impl.specification.TutorSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Tutor;
import com.specificgroup.crud_app.exception.ValidationException;
import com.specificgroup.crud_app.service.impl.TutorServiceImpl;
import com.specificgroup.crud_app.util.Attributes;
import org.junit.jupiter.api.DisplayName;
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

public class TutorServiceImplTest {

    private final TutorDao tutorDao = Mockito.mock(TutorDaoImpl.class);
    private final TutorSpecification specification = Mockito.mock(TutorSpecification.class);
    @InjectMocks
    private TutorService tutorService = new TutorServiceImpl(tutorDao);

    @Test
    @DisplayName("Create a tutor. Successful request")
    void createTutorTest_SuccessfulRequest(){
        CreateRequest createRequest = new CreateRequest.Builder()
                .name("Test")
                .specialization("Specialization")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();
        Mockito.doReturn(1L).when(tutorDao).create(createRequest);

        assertEquals(1L, tutorService.create(createRequest));
        verify(tutorDao, times(1)).create(createRequest);
        verifyNoMoreInteractions(tutorDao);
    }

    @Test
    @DisplayName("Create a tutor. Unsuccessful requests")
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
            assertThrowsExactly(ValidationException.class, () -> tutorService.create(nameValidationError));
            assertThrowsExactly(ValidationException.class, () -> tutorService.create(specializationValidationError));
            assertThrowsExactly(ValidationException.class, () -> tutorService.create(phoneValidationError));
            assertThrowsExactly(ValidationException.class, () -> tutorService.create(emailValidationError));
        });
    }

    @Test
    @DisplayName("Get tutors by attributes. Successful request")
    void getTutorsByAttributesTest_SuccessfulRequest(){
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(ID,"1");
        attributes.put(NAME,"Test");
        attributes.put(SPECIALIZATION,"Specialization");
        List<Tutor> tutors = new ArrayList<>();
        List<JsonObject> expected = new ArrayList<>();

        Mockito.doReturn(tutors).when(tutorDao).getBySpecification(specification);

        List<JsonObject> result = tutorService.get(attributes);

        assertEquals(expected, result);
        verify(tutorDao, times(1)).getBySpecification(any(TutorSpecification.class));
        verify(tutorDao, times(0)).get();
        verifyNoMoreInteractions(tutorDao);
    }

    @Test
    @DisplayName("Get tutors. Successful request")
    void getTutorsTest_SuccessfulRequest(){
        Map<Attributes, String> attributes = new HashMap<>();
        List<Tutor> tutors = new ArrayList<>();
        List<JsonObject> expected = new ArrayList<>();

        Mockito.doReturn(tutors).when(tutorDao).get();

        List<JsonObject> result = tutorService.get(attributes);

        assertEquals(expected, result);
        verify(tutorDao, times(1)).get();
        verify(tutorDao, times(0)).getBySpecification(any(TutorSpecification.class));
        verifyNoMoreInteractions(tutorDao);
    }

    @Test
    @DisplayName("Get tutors by attributes. Unsuccessful request")
    void getTutorsByAttributesTest_UnsuccessfulRequests(){
        Map<Attributes, String> wrongId = Map.of(ID, "A");
        Map<Attributes, String> wrongName = Map.of(NAME,"123658");
        Map<Attributes, String> wrongSpecialization = Map.of(SPECIALIZATION,"1");
        Map<Attributes, String> wrongPhone = Map.of(PHONE,"375(99)9999999");
        Map<Attributes, String> wrongEmail = Map.of(EMAIL,"wrong email");

        assertAll(() -> {
            assertTrue(tutorService.get(wrongId).isEmpty());
            assertTrue(tutorService.get(wrongName).isEmpty());
            assertTrue(tutorService.get(wrongSpecialization).isEmpty());
            assertTrue(tutorService.get(wrongPhone).isEmpty());
            assertTrue(tutorService.get(wrongEmail).isEmpty());
        });
    }

    @Test
    @DisplayName("Update the tutor. Successful request")
    void updateTutorTest_SuccessfulRequest(){
        UpdateRequest updateRequest = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .specialization("Developer")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        Mockito.doReturn(1L).when(tutorDao).getContactDetailsIdByTutorId(anyLong());
        Mockito.doReturn(1L).when(tutorDao).update(updateRequest, 1L);

        assertEquals(1L, tutorService.update(updateRequest));
        verify(tutorDao, times(1)).getContactDetailsIdByTutorId(anyLong());
        verify(tutorDao, times(1)).update(updateRequest, 1L);
        verifyNoMoreInteractions(tutorDao);
    }

    @Test
    @DisplayName("Update the tutor. Unsuccessful requests")
    void updateTutorTest_UnsuccessfulRequests(){
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
            assertThrowsExactly(ValidationException.class, () -> tutorService.update(idValidationError));
            assertThrowsExactly(ValidationException.class, () -> tutorService.update(nameValidationError));
            assertThrowsExactly(ValidationException.class, () -> tutorService.update(specializationValidationError));
            assertThrowsExactly(ValidationException.class, () -> tutorService.update(phoneValidationError));
            assertThrowsExactly(ValidationException.class, () -> tutorService.update(emailValidationError));
        });
    }

    @Test
    @DisplayName("Delete the tutor by id. Successful request")
    void deleteByIdTest() {
        Mockito.doReturn(1L).when(tutorDao).getContactDetailsIdByTutorId(anyLong());
        Mockito.doReturn(true).when(tutorDao).deleteByContactDetailId(anyLong());

        assertTrue(tutorService.deleteById("1"));
        verify(tutorDao, times(1)).getContactDetailsIdByTutorId(anyLong());
        verify(tutorDao, times(1)).deleteByContactDetailId(anyLong());
        verifyNoMoreInteractions(tutorDao);
    }
}