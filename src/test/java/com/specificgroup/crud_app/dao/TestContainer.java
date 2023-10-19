package com.specificgroup.crud_app.dao;

import com.specificgroup.crud_app.util.database.connection.ConnectionPool;
import com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static com.specificgroup.crud_app.dao.TestContainer.InitDB.createDb;
import static com.specificgroup.crud_app.dao.TestContainer.InitDB.destroyDb;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract.Type.FLEXIBLE;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolFabric.PropertiesFile.*;

public class TestContainer extends PostgreSQLContainer<TestContainer> {
    private static final Map<String, String> attributes = new HashMap<>();
    private static TestContainer container;
    private static ConnectionPool connectionPool;

    private TestContainer() {
        super("postgres:latest");
    }

    @Override
    public void start() {
        super.start();
        attributes.put(PASSWORD_KEY, container.getPassword());
        attributes.put(USERNAME_KEY, container.getUsername());
        attributes.put(URL_KEY, container.getJdbcUrl());
        attributes.put(POOL_SIZE, "5");
        connectionPool = ConnectionPoolAbstract.connectionPool(FLEXIBLE, attributes);
        try (Connection connection = connectionPool.openConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(destroyDb);
            statement.execute(createDb);
            statement.executeUpdate(insertDb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        super.stop();
        connectionPool.destroyPool();
    }

    public static TestContainer getContainer() {
        if (container == null) {
            container = new TestContainer();
        }
        return container;
    }


    static class InitDB {

        public static final String destroyDb = """
                DROP TABLE IF EXISTS students;
                DROP TABLE IF EXISTS tutors;
                DROP TABLE IF EXISTS contact_details;  
                """;
        public static final String createDb = """
                CREATE TABLE IF NOT EXISTS contact_details
                (
                    id  BIGSERIAL PRIMARY KEY,
                    phone VARCHAR(13)  NOT NULL,
                    email VARCHAR(256)  NOT NULL
                );
                                
                CREATE TABLE IF NOT EXISTS students
                (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    age INTEGER NOT NULL,
                    contact_details_id BIGINT REFERENCES contact_details (id) ON DELETE CASCADE
                );
                                
                CREATE TABLE IF NOT EXISTS tutors
                (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    specialization VARCHAR(128) NOT NULL,
                    contact_details_id BIGINT REFERENCES contact_details (id) ON DELETE CASCADE
                );""";
    }

    public static final String insertDb = """
            INSERT INTO contact_details(phone, email)
            VALUES ('+375111111111', 'test1@gmail.com'),
                   ('+375222222222', 'test2@gmail.com'),
                   ('+375333333333', 'test3@gmail.com'),
                   ('+375444444444', 'test4@gmail.com');
                             
            INSERT INTO students(name, age, contact_details_id)
            VALUES ('Kate', 20, 1),
                   ('Nikita', 21, 2);
                        
            INSERT INTO tutors(name, specialization, contact_details_id)
            VALUES ('Vlad', 'QA', 3),
                   ('Ira', 'Developer', 4);
                       """;
}