package com.specificgroup.crud_app.controller;

import com.specificgroup.crud_app.dao.TestContainer;
import com.specificgroup.crud_app.server.Server;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;


import static com.specificgroup.crud_app.controller.ControllerFactory.flexibleStudentController;
import static com.specificgroup.crud_app.util.Constants.HttpMethod.*;
import static com.specificgroup.crud_app.util.Constants.HttpResponseStatus.*;
import static com.specificgroup.crud_app.util.Constants.UrlPath.STUDENT_PATH;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentControllerTest {

    @Container
    private static final PostgreSQLContainer<TestContainer> postgreSQLContainer = TestContainer.getContainer();
    private static Server server;

    @BeforeAll
    @DisplayName("Init test")
    void start() {
        postgreSQLContainer.start();
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PASSWORD_KEY, postgreSQLContainer.getPassword());
        attributes.put(USERNAME_KEY, postgreSQLContainer.getUsername());
        attributes.put(URL_KEY, postgreSQLContainer.getJdbcUrl());
        attributes.put(POOL_SIZE, "5");
        Map<String, ApiHandler> apiHandlerMap = new HashMap<>();
        apiHandlerMap.put(STUDENT_PATH, new ApiHandler(flexibleStudentController(attributes)));
        server = new Server(apiHandlerMap);
        server.start();
    }

    @AfterAll
    void stop() {
        server.stop();
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Create student request test")
    void createTest() {
        String content = """
                {\s
                    "name":"Inna",\s
                    "age":30,\s
                    "phone":"+375999999999",\s
                    "email":"innainna@gmail.com"\s
                }\s
                """;
        Client client = new Client.Builder()
                .url("http://localhost:8081/students/create")
                .requestMethod(POST)
                .content(content)
                .build();
        client.sendRequest();
        assertAll(() -> {
            assertEquals(STATUS_CREATED, client.getResponseCode());
        });
    }

    @Test
    @DisplayName("Find(get) students test")
    @Order(1)
    void getTest() {
        Client client = new Client.Builder()
                .url("http://localhost:8081/students")
                .getRequestUrl("/get?id=1&name=Kate")
                .requestMethod(GET)
                .build();
        client.sendRequest();
        assertAll(() -> {
            assertEquals("{\"phone\":\"+375111111111\",\"name\":\"Kate\",\"id\":1,\"age\":20,\"email\":\"test1@gmail.com\"}", client.getResponse());
            assertEquals(STATUS_OK, client.getResponseCode());
        });
    }

    @Test
    @DisplayName("Update student request test")
    void updateTest() {
        String content = """
                {\s
                    "id":1,\s
                    "name":"Kate",\s
                    "age":21,\s
                    "phone":"+399999999999",\s
                    "email":"katusha@gmail.com"\s
                }\s
                """;
        String result = "{\"id\":1}";
        Client client = new Client.Builder()
                .url("http://localhost:8081/students/update")
                .requestMethod(PUT)
                .content(content)
                .build();
        client.sendRequest();
        assertAll(() -> {
            assertEquals(STATUS_OK, client.getResponseCode());
            assertEquals(result, client.getResponse());
        });
    }

    @Test
    @DisplayName("Delete student request test")
    void deleteTest() {
        Client client = new Client.Builder()
                .url("http://localhost:8081/students/delete?id=2")
                .requestMethod(DELETE)
                .build();
        client.sendRequest();
        assertEquals(STATUS_NO_CONTENT, client.getResponseCode());
    }
}
