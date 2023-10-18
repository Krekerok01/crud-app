package com.specificgroup.crud_app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dao.Dao;
import com.specificgroup.crud_app.dao.StudentDao;
import com.specificgroup.crud_app.dao.specification.JdbcSpecification;
import com.specificgroup.crud_app.dao.specification.StudentsSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.exception.ValidationException;
import com.specificgroup.crud_app.service.impl.StudentService;
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

public class StudentServiceTest {

    private final Dao<Student> studentDao = Mockito.mock(StudentDao.class);
    private final JdbcSpecification<Student> specification = Mockito.mock(StudentsSpecification.class);
    @InjectMocks
    private Service service = new StudentService(studentDao);

    @Test
    void createStudentTest_SuccessfulRequest(){
        CreateRequest createRequest = new CreateRequest.Builder()
                .name("Test")
                .age("18")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();
        Mockito.doReturn(1L).when(studentDao).create(createRequest);

        assertEquals(1L, service.create(createRequest));
        verify(studentDao, times(1)).create(createRequest);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void createStudentTest_UnsuccessfulRequests(){
        CreateRequest nameValidationError = new CreateRequest.Builder()
                .name("123645")
                .age("18")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        CreateRequest ageValidationError = new CreateRequest.Builder()
                .name("Test")
                .age("test")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        CreateRequest phoneValidationError = new CreateRequest.Builder()
                .name("Test")
                .age("18")
                .phone("+XXXxxXXXxxxx")
                .email("test@gmail.com")
                .build();

        CreateRequest emailValidationError = new CreateRequest.Builder()
                .name("Test")
                .age("18")
                .phone("+000000000000")
                .email("testgmail.com")
                .build();


        assertAll(() -> {
            assertThrowsExactly(ValidationException.class, () -> service.create(nameValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.create(ageValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.create(phoneValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.create(emailValidationError));
        });
    }

    @Test
    void getStudentsByAttributesTest_SuccessfulRequest(){
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(ID,"1");
        attributes.put(NAME,"Test");
        attributes.put(AGE,"18");
        List<Student> students = new ArrayList<>();
        List<JsonObject> expected = new ArrayList<>();

        Mockito.doReturn(students).when(studentDao).getBySpecification(specification);

        List<JsonObject> result = service.get(attributes);

        assertEquals(expected, result);
        verify(studentDao, times(1)).getBySpecification(any(StudentsSpecification.class));
        verify(studentDao, times(0)).get();
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void getStudentsTest_SuccessfulRequest(){
        Map<Attributes, String> attributes = new HashMap<>();
        List<Student> students = new ArrayList<>();
        List<JsonObject> expected = new ArrayList<>();

        Mockito.doReturn(students).when(studentDao).get();

        List<JsonObject> result = service.get(attributes);

        assertEquals(expected, result);
        verify(studentDao, times(1)).get();
        verify(studentDao, times(0)).getBySpecification(any(StudentsSpecification.class));
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void getStudentsByAttributesTest_UnsuccessfulRequests(){
        Map<Attributes, String> wrongId = Map.of(ID, "A");
        Map<Attributes, String> wrongName = Map.of(NAME,"123658");
        Map<Attributes, String> wrongAge = Map.of(AGE,"Test");
        Map<Attributes, String> wrongPhone = Map.of(PHONE,"375(99)9999999");
        Map<Attributes, String> wrongEmail = Map.of(EMAIL,"wrong email");

        assertAll(() -> {
            assertTrue(service.get(wrongId).isEmpty());
            assertTrue(service.get(wrongName).isEmpty());
            assertTrue(service.get(wrongAge).isEmpty());
            assertTrue(service.get(wrongPhone).isEmpty());
            assertTrue(service.get(wrongEmail).isEmpty());
        });
    }

    @Test
    void updateStudentTest_SuccessfulRequest(){
        UpdateRequest updateRequest = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .age("18")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        Mockito.doReturn(1L).when(studentDao).getContactDetailsIdByMainEntityId(anyLong());
        Mockito.doReturn(1L).when(studentDao).update(updateRequest, 1L);

        assertEquals(1L, service.update(updateRequest));
        verify(studentDao, times(1)).getContactDetailsIdByMainEntityId(anyLong());
        verify(studentDao, times(1)).update(updateRequest, 1L);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void updateStudentTest_UnsuccessfulRequest(){
        UpdateRequest idValidationError = new UpdateRequest.Builder()
                .id("A")
                .name("Test")
                .age("18")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        UpdateRequest nameValidationError = new UpdateRequest.Builder()
                .id("1")
                .name("123645")
                .age("18")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        UpdateRequest ageValidationError = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .age("test")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        UpdateRequest phoneValidationError = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .age("18")
                .phone("+XXXxxXXXxxxx")
                .email("test@gmail.com")
                .build();

        UpdateRequest emailValidationError = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .age("18")
                .phone("+000000000000")
                .email("testgmail.com")
                .build();

        assertAll(() -> {
            assertThrowsExactly(ValidationException.class, () -> service.update(idValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.update(nameValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.update(ageValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.update(phoneValidationError));
            assertThrowsExactly(ValidationException.class, () -> service.update(emailValidationError));
        });
    }

    @Test
    void deleteByIdTest() {
        Mockito.doReturn(1L).when(studentDao).getContactDetailsIdByMainEntityId(anyLong());
        Mockito.doReturn(true).when(studentDao).deleteByContactDetailId(anyLong());

        assertTrue(service.deleteById("1"));
        verify(studentDao, times(1)).getContactDetailsIdByMainEntityId(anyLong());
        verify(studentDao, times(1)).deleteByContactDetailId(anyLong());
        verifyNoMoreInteractions(studentDao);
    }
}