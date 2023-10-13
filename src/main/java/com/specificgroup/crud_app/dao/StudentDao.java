package com.specificgroup.crud_app.dao;

import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.util.ConnectionPool;
import com.specificgroup.crud_app.util.ConnectionPoolAbstract;
import com.specificgroup.crud_app.util.JdbcSpecification;
import com.specificgroup.crud_app.util.JdbcUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.specificgroup.crud_app.util.SqlCommand.Constant.INVALID_RESULT;
import static com.specificgroup.crud_app.util.SqlCommand.Insert.*;
import static com.specificgroup.crud_app.util.SqlCommand.Select.SELECT;
import static com.specificgroup.crud_app.util.SqlCommand.Select.SELECT_SETTING_STUDENTS;
import static com.specificgroup.crud_app.util.SqlCommand.Tables.TABLE_CONTACTS;
import static com.specificgroup.crud_app.util.SqlCommand.Tables.TABLE_STUDENTS;
import static com.specificgroup.crud_app.util.SqlCommand.Update.*;


public class StudentDao implements Dao<Student> {
    private static ConnectionPool connectionPool;
    private final Logger logger =  LogManager.getLogger();

    private StudentDao(final Builder builder) {
        if (connectionPool == null) {
            connectionPool = ConnectionPoolAbstract.connectionPool(builder.type, builder.properties);
        }
    }

    @Override
    public Long create(CreateRequest request) {
        long result = INVALID_RESULT;
        String studentQuery = INSERT_COMMON_ENTITY.formatted(TABLE_STUDENTS, INSERT_SETTING_STUDENTS);

        try (Connection connection = connectionPool.openConnection();
             PreparedStatement studentStatement = connection.prepareStatement(studentQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement contactStatement = connection.prepareStatement(INSERT_CONTACTS_DETAIL, Statement.RETURN_GENERATED_KEYS)) {
            
            Object[] contactInfo = {request.getPhone(), request.getEmail()};
            JdbcUtil.setStatement(contactStatement, contactInfo);
            contactStatement.execute();
            ResultSet contactsKeys = contactStatement.getGeneratedKeys();
            
            if (contactsKeys.next()) {
                Long contactId = contactsKeys.getLong(1);
                Object[] seq = {request.getName(), Integer.parseInt(request.getAge()),contactId};
                JdbcUtil.setStatement(studentStatement, seq);
                studentStatement.execute();
             
                try (ResultSet generatedKeys = studentStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result = generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<Student> get(JdbcSpecification<Student> specification) {
        String sql = SELECT.formatted(SELECT_SETTING_STUDENTS, TABLE_STUDENTS);
        List<Student> students = new ArrayList<>();
        try (Connection connection = connectionPool.openConnection()) {
            students.addAll(specification.searchFilter(connection, sql));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return students;
    }

    @Override
    public Long update(UpdateRequest request) {
        long result = INVALID_RESULT;
        String studentsSqlQuery = buildStudentsSqlQuery();
        String contactsSqlQuery = buildContactsSqlQuery();

        Object[] studentsParams = {
                request.getName(),
                Integer.parseInt(request.getAge()),
                Long.parseLong(request.getId())
        };

        Object[] contactsParams = {
                request.getPhone(),
                request.getEmail(),
                Long.parseLong(request.getId())
        };

        try (Connection connection = connectionPool.openConnection();
             PreparedStatement studentsPreparedStatement = connection.prepareStatement(studentsSqlQuery);
             PreparedStatement contactsPreparedStatement = connection.prepareStatement(contactsSqlQuery)) {

            JdbcUtil.setStatement(contactsPreparedStatement, contactsParams);
            int contactsUpdateResult = contactsPreparedStatement.executeUpdate();

            if (contactsUpdateResult == 1){
                JdbcUtil.setStatement(studentsPreparedStatement, studentsParams);
                result = studentsPreparedStatement.executeUpdate() == 1 ? Long.parseLong(request.getId()) : result;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    private String buildContactsSqlQuery() {
        String sql = UPDATE.formatted(TABLE_CONTACTS);
        StringBuilder sqlBuilder = new StringBuilder(sql);
        sqlBuilder.append(UPDATE_PHONE).append(UPDATE_COMMA)
                .append(UPDATE_EMAIL)
                .append(UPDATE_WHERE_ID);
        return sqlBuilder.toString();
    }

    private String buildStudentsSqlQuery() {
        String sql = UPDATE.formatted(TABLE_STUDENTS);
        StringBuilder sqlBuilder = new StringBuilder(sql);
        sqlBuilder.append(UPDATE_NAME).append(UPDATE_COMMA)
                .append(UPDATE_AGE)
                .append(UPDATE_WHERE_ID);
        return sqlBuilder.toString();
    }

    @Override
    public boolean delete(long id) {
        return true;
    }

    public static class Builder {

        private final Map<String, String> properties = new HashMap<>();
        private ConnectionPoolAbstract.Type type;

        public Builder property(String key, String value) {
            properties.put(key, value);
            return this;
        }

        public Builder property(Map<String, String> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder type(ConnectionPoolAbstract.Type type) {
            this.type = type;
            return this;
        }

        public StudentDao build() {
            return new StudentDao(this);
        }
    }
}
