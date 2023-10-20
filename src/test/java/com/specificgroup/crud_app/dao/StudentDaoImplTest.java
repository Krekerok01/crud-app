package com.specificgroup.crud_app.dao;

import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract.Type.FLEXIBLE;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.PASSWORD_KEY;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.POOL_SIZE;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.URL_KEY;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.USERNAME_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.specificgroup.crud_app.dao.impl.StudentDaoImpl;
import com.specificgroup.crud_app.dao.impl.specification.StudentSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.ContactDetails;
import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.util.Attributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentDaoImplTest {

    @Container
    private static final PostgreSQLContainer<TestContainer> postgreSQLContainer = TestContainer.getContainer();
    private static StudentDao studentDao;

    @BeforeAll
    @DisplayName("Create db")
    void start() {
        postgreSQLContainer.start();
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PASSWORD_KEY, postgreSQLContainer.getPassword());
        attributes.put(USERNAME_KEY, postgreSQLContainer.getUsername());
        attributes.put(URL_KEY, postgreSQLContainer.getJdbcUrl());
        attributes.put(POOL_SIZE, "5");
        studentDao = new StudentDaoImpl.Builder().type(FLEXIBLE).property(attributes).build();
    }

    @AfterAll
    void stop() {
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Get students by id")
    @Order(1)
    void getBySpecificationTest_IdAttribute() {
        List<Student> expected = List.of(buildKate());
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.ID, "1");
        List<Student> result = studentDao.getBySpecification(new StudentSpecification(attributes));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Get students by name")
    @Order(2)
    void getBySpecificationTest_NameAttribute() {
        List<Student> expected = List.of(buildNikita());
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.NAME, "Nikita");
        List<Student> result = studentDao.getBySpecification(new StudentSpecification(attributes));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Get students")
    @Order(3)
    void getTest_WithoutAttributes() {
        List<Student> expected = List.of(buildKate(), buildNikita());

        List<Student> result = studentDao.get();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Create new student")
    @Order(4)
    void createNewStudentTest() {
        CreateRequest createRequest = new CreateRequest.Builder()
                .name("Varvara")
                .age("22")
                .phone("+375777777777")
                .email("test@gmail.com")
                .build();

        Long result = studentDao.create(createRequest);

        assertNotNull(result);
        assertEquals(3L, result);
    }

    @Test
    @DisplayName("Update the student. Successful request")
    void updateStudentTest_SuccessfulRequest() {
        Long expected = 1L;
        UpdateRequest updateRequest = new UpdateRequest.Builder()
            .id(String.valueOf(expected))
            .name("Kate")
            .age("21")
            .phone("+375888888888")
            .email("new.test1@gmail.com")
            .build();

        Long result = studentDao.update(updateRequest, 1L);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Update the student. Unsuccessful request")
    void updateStudentTest_UnsuccessfulRequest() {
        Long expected = -1L;
        UpdateRequest updateRequest = new UpdateRequest.Builder()
            .id("20")
            .name("Kate")
            .age("21")
            .phone("+375888888888")
            .email("new.test1@gmail.com")
            .build();

        Long result = studentDao.update(updateRequest, 1L);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Get contact detail id by student id")
    void getContactDetailsIdByMainEntityIdTest(){
        Long expected = 2L;

        Long result = studentDao.getContactDetailsIdByStudentId(2L);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Delete student by contact detail id")
    void deleteStudentByContactDetailIdTest() {
        assertTrue(studentDao.deleteByContactDetailId(3L));
    }

    private Student buildKate(){
        return new Student(1L, "Kate", 20,
            new ContactDetails(1L, "+375111111111", "test1@gmail.com"));
    }

    private Student buildNikita(){
        return new Student(2L, "Nikita", 21,
            new ContactDetails(2L, "+375222222222", "test2@gmail.com"));
    }
}