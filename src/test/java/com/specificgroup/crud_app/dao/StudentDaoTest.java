package com.specificgroup.crud_app.dao;

import com.specificgroup.crud_app.entity.Student;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract.Type.FLEXIBLE;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentDaoTest {

    @Container
    private static final PostgreSQLContainer<TestContainer> postgreSQLContainer = TestContainer.getContainer();
    private static Dao<Student> studentDao;

    @BeforeAll
    @DisplayName("Create db")
    void start() {
        postgreSQLContainer.start();
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PASSWORD_KEY, postgreSQLContainer.getPassword());
        attributes.put(USERNAME_KEY, postgreSQLContainer.getUsername());
        attributes.put(URL_KEY, postgreSQLContainer.getJdbcUrl());
        attributes.put(POOL_SIZE, "5");
        studentDao = new StudentDao.Builder().type(FLEXIBLE).property(attributes).build();
    }

    @AfterAll
    void stop() {
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Search capacitors by id")
    void searchId() {

    }
}
