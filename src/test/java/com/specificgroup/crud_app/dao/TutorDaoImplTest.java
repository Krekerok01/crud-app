package com.specificgroup.crud_app.dao;

import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract.Type.FLEXIBLE;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.PASSWORD_KEY;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.POOL_SIZE;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.URL_KEY;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.USERNAME_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.specificgroup.crud_app.dao.impl.TutorDaoImpl;
import com.specificgroup.crud_app.dao.impl.specification.TutorSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.ContactDetails;
import com.specificgroup.crud_app.entity.Tutor;
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
public class TutorDaoImplTest {

    @Container
    private static final PostgreSQLContainer<TestContainer> postgreSQLContainer = TestContainer.getContainer();
    private static TutorDao tutorDao;

    @BeforeAll
    @DisplayName("Create db")
    void start() {
        postgreSQLContainer.start();
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PASSWORD_KEY, postgreSQLContainer.getPassword());
        attributes.put(USERNAME_KEY, postgreSQLContainer.getUsername());
        attributes.put(URL_KEY, postgreSQLContainer.getJdbcUrl());
        attributes.put(POOL_SIZE, "5");
        tutorDao = new TutorDaoImpl.Builder().type(FLEXIBLE).property(attributes).build();
    }

    @AfterAll
    void stop() {
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Get tutors by id")
    @Order(1)
    void getBySpecificationTest_IdAttribute() {
        List<Tutor> expected = List.of(buildVlad());
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.ID, "1");
        List<Tutor> result = tutorDao.getBySpecification(new TutorSpecification(attributes));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Get tutors by name")
    @Order(2)
    void getBySpecificationTest_NameAttribute() {
        List<Tutor> expected = List.of(buildIra());
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.NAME, "Ira");
        List<Tutor> result = tutorDao.getBySpecification(new TutorSpecification(attributes));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Get tutors")
    @Order(3)
    void getTest_WithoutAttributes() {
        List<Tutor> expected = List.of(buildVlad(), buildIra());

        List<Tutor> result = tutorDao.get();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Create new tutor")
    @Order(4)
    void createNewTutorTest() {
        CreateRequest createRequest = new CreateRequest.Builder()
                .name("Varvara")
                .specialization("Developer")
                .phone("+375777777777")
                .email("test@gmail.com")
                .build();

        Long result = tutorDao.create(createRequest);

        assertNotNull(result);
        assertEquals(3L, result);
    }

    @Test
    @DisplayName("Update the tutor. Successful request")
    void updateTutorTest_SuccessfulRequest() {
        Long expected = 1L;
        UpdateRequest updateRequest = new UpdateRequest.Builder()
            .id(String.valueOf(expected))
            .name("NeVlad")
            .specialization("QA")
            .phone("+375000000000")
            .email("nevlad.test3@gmail.com")
            .build();

        Long result = tutorDao.update(updateRequest, 3L);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Update the tutor. Unsuccessful request")
    void updateTutorTest_UnsuccessfulRequest() {
        Long expected = -1L;
        UpdateRequest updateRequest = new UpdateRequest.Builder()
            .id("20")
            .name("NeVlad")
            .specialization("QA")
            .phone("+375000000000")
            .email("nevlad.test3@gmail.com")
            .build();

        Long result = tutorDao.update(updateRequest, 3L);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Get contact detail id by tutor id")
    void getContactDetailsIdByMainEntityIdTest(){
        Long expected = 4L;

        Long result = tutorDao.getContactDetailsIdByTutorId(2L);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Delete tutor by contact detail id")
    void deleteTutorByContactDetailIdTest() {
        assertTrue(tutorDao.deleteByContactDetailId(3L));
    }

    private Tutor buildVlad(){
        return new Tutor(1L, "Vlad", "QA",
            new ContactDetails(3L, "+375333333333", "test3@gmail.com"));
    }

    private Tutor buildIra(){
        return new Tutor(2L, "Ira", "Developer",
            new ContactDetails(4L, "+375444444444", "test4@gmail.com"));
    }

}
