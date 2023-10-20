package com.specificgroup.crud_app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.specificgroup.crud_app.dao.StudentDao;
import com.specificgroup.crud_app.dao.impl.StudentDaoImpl;
import com.specificgroup.crud_app.dao.impl.specification.StudentSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.exception.ValidationException;
import com.specificgroup.crud_app.service.impl.StudentServiceImpl;
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

public class StudentServiceImplTest {

    private final StudentDao studentDao = Mockito.mock(StudentDaoImpl.class);
    private final StudentSpecification specification = Mockito.mock(StudentSpecification.class);
    @InjectMocks
    private StudentService studentService = new StudentServiceImpl(studentDao);

    @Test
    @DisplayName("Create a student. Successful request")
    void createStudentTest_SuccessfulRequest(){
        CreateRequest createRequest = new CreateRequest.Builder()
                .name("Test")
                .age("18")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();
        Mockito.doReturn(1L).when(studentDao).create(createRequest);

        assertEquals(1L, studentService.create(createRequest));
        verify(studentDao, times(1)).create(createRequest);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    @DisplayName("Create a student. Unsuccessful requests")
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
            assertThrowsExactly(ValidationException.class, () -> studentService.create(nameValidationError));
            assertThrowsExactly(ValidationException.class, () -> studentService.create(ageValidationError));
            assertThrowsExactly(ValidationException.class, () -> studentService.create(phoneValidationError));
            assertThrowsExactly(ValidationException.class, () -> studentService.create(emailValidationError));
        });
    }

    @Test
    @DisplayName("Get students by attributes. Successful request")
    void getStudentsByAttributesTest_SuccessfulRequest(){
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(ID,"1");
        attributes.put(NAME,"Test");
        attributes.put(AGE,"18");
        List<Student> students = new ArrayList<>();
        List<JsonObject> expected = new ArrayList<>();

        Mockito.doReturn(students).when(studentDao).getBySpecification(specification);

        List<JsonObject> result = studentService.get(attributes);

        assertEquals(expected, result);
        verify(studentDao, times(1)).getBySpecification(any(StudentSpecification.class));
        verify(studentDao, times(0)).get();
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    @DisplayName("Get students. Successful request")
    void getStudentsTest_SuccessfulRequest(){
        Map<Attributes, String> attributes = new HashMap<>();
        List<Student> students = new ArrayList<>();
        List<JsonObject> expected = new ArrayList<>();

        Mockito.doReturn(students).when(studentDao).get();

        List<JsonObject> result = studentService.get(attributes);

        assertEquals(expected, result);
        verify(studentDao, times(1)).get();
        verify(studentDao, times(0)).getBySpecification(any(StudentSpecification.class));
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    @DisplayName("Get students by attributes. Unsuccessful request")
    void getStudentsByAttributesTest_UnsuccessfulRequests(){
        Map<Attributes, String> wrongId = Map.of(ID, "A");
        Map<Attributes, String> wrongName = Map.of(NAME,"123658");
        Map<Attributes, String> wrongAge = Map.of(AGE,"Test");
        Map<Attributes, String> wrongPhone = Map.of(PHONE,"375(99)9999999");
        Map<Attributes, String> wrongEmail = Map.of(EMAIL,"wrong email");

        assertAll(() -> {
            assertTrue(studentService.get(wrongId).isEmpty());
            assertTrue(studentService.get(wrongName).isEmpty());
            assertTrue(studentService.get(wrongAge).isEmpty());
            assertTrue(studentService.get(wrongPhone).isEmpty());
            assertTrue(studentService.get(wrongEmail).isEmpty());
        });
    }

    @Test
    @DisplayName("Update the student. Successful request")
    void updateStudentTest_SuccessfulRequest(){
        UpdateRequest updateRequest = new UpdateRequest.Builder()
                .id("1")
                .name("Test")
                .age("18")
                .phone("+000000000000")
                .email("test@gmail.com")
                .build();

        Mockito.doReturn(1L).when(studentDao).getContactDetailsIdByStudentId(anyLong());
        Mockito.doReturn(1L).when(studentDao).update(updateRequest, 1L);

        assertEquals(1L, studentService.update(updateRequest));
        verify(studentDao, times(1)).getContactDetailsIdByStudentId(anyLong());
        verify(studentDao, times(1)).update(updateRequest, 1L);
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    @DisplayName("Update the student. Unsuccessful requests")
    void updateStudentTest_UnsuccessfulRequests(){
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
            assertThrowsExactly(ValidationException.class, () -> studentService.update(idValidationError));
            assertThrowsExactly(ValidationException.class, () -> studentService.update(nameValidationError));
            assertThrowsExactly(ValidationException.class, () -> studentService.update(ageValidationError));
            assertThrowsExactly(ValidationException.class, () -> studentService.update(phoneValidationError));
            assertThrowsExactly(ValidationException.class, () -> studentService.update(emailValidationError));
        });
    }

    @Test
    @DisplayName("Delete the student by id. Successful request")
    void deleteByIdTest() {
        Mockito.doReturn(1L).when(studentDao).getContactDetailsIdByStudentId(anyLong());
        Mockito.doReturn(true).when(studentDao).deleteByContactDetailId(anyLong());

        assertTrue(studentService.deleteById("1"));
        verify(studentDao, times(1)).getContactDetailsIdByStudentId(anyLong());
        verify(studentDao, times(1)).deleteByContactDetailId(anyLong());
        verifyNoMoreInteractions(studentDao);
    }
}