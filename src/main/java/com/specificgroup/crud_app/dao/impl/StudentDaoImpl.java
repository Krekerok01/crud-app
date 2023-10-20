package com.specificgroup.crud_app.dao.impl;

import static com.specificgroup.crud_app.util.Constants.Constant.INVALID_RESULT;
import static com.specificgroup.crud_app.util.Constants.Delete.DELETE_BY_ID;
import static com.specificgroup.crud_app.util.Constants.Insert.INSERT_CONTACT_DETAILS;
import static com.specificgroup.crud_app.util.Constants.Insert.INSERT_MAIN_ENTITY;
import static com.specificgroup.crud_app.util.Constants.Insert.INSERT_SETTING_STUDENTS;
import static com.specificgroup.crud_app.util.Constants.Select.SELECT;
import static com.specificgroup.crud_app.util.Constants.Select.SELECT_CONTACT_DETAIL_ID_BY_ENTITY_ID;
import static com.specificgroup.crud_app.util.Constants.Select.SELECT_SETTING_STUDENTS;
import static com.specificgroup.crud_app.util.Constants.Select.SELECT_WITHOUT_ATTRIBUTES;
import static com.specificgroup.crud_app.util.Constants.Tables.TABLE_CONTACT_DETAILS;
import static com.specificgroup.crud_app.util.Constants.Tables.TABLE_STUDENTS;
import static com.specificgroup.crud_app.util.Constants.Update.UPDATE;
import static com.specificgroup.crud_app.util.Constants.Update.UPDATE_AGE;
import static com.specificgroup.crud_app.util.Constants.Update.UPDATE_COMMA;
import static com.specificgroup.crud_app.util.Constants.Update.UPDATE_EMAIL;
import static com.specificgroup.crud_app.util.Constants.Update.UPDATE_NAME;
import static com.specificgroup.crud_app.util.Constants.Update.UPDATE_PHONE;
import static com.specificgroup.crud_app.util.Constants.Update.UPDATE_WHERE_ID;

import com.specificgroup.crud_app.dao.StudentDao;
import com.specificgroup.crud_app.dao.impl.specification.StudentSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.util.Mapper;
import com.specificgroup.crud_app.util.database.JdbcUtil;
import com.specificgroup.crud_app.util.database.connection.ConnectionPool;
import com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class StudentDaoImpl implements StudentDao {
    private static ConnectionPool connectionPool;
    private final Logger logger =  Logger.getLogger(StudentDaoImpl.class.getName());

    private StudentDaoImpl(final Builder builder) {
        if (connectionPool == null) {
            connectionPool = ConnectionPoolAbstract.connectionPool(builder.type, builder.properties);
        }
    }

    @Override
    public Long create(CreateRequest request) {
        long result = INVALID_RESULT;
        String studentQuery = INSERT_MAIN_ENTITY.formatted(TABLE_STUDENTS, INSERT_SETTING_STUDENTS);

        Connection connection = connectionPool.openConnection();

        try (PreparedStatement studentStatement = connection.prepareStatement(studentQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement contactStatement = connection.prepareStatement(INSERT_CONTACT_DETAILS, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            Object[] contactInfo = {request.getPhone(), request.getEmail()};
            JdbcUtil.setStatement(contactStatement, contactInfo);
            contactStatement.execute();
            ResultSet contactsKeys = contactStatement.getGeneratedKeys();
            
            if (contactsKeys.next()) {
                Long contactDetailsId = contactsKeys.getLong(1);
                Object[] seq = {request.getName(), Integer.parseInt(request.getAge()),contactDetailsId};
                JdbcUtil.setStatement(studentStatement, seq);
                studentStatement.execute();
             
                try (ResultSet generatedKeys = studentStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result = generatedKeys.getLong(1);
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            logger.info("Exception: " + e.getMessage());
            JdbcUtil.rollback(connection, e);
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(connection);
        }
        return result;
    }

    @Override
    public List<Student> getBySpecification(StudentSpecification specification) {
        String sql = SELECT.formatted(SELECT_SETTING_STUDENTS, TABLE_STUDENTS);
        List<Student> students = new ArrayList<>();

        try (Connection connection = connectionPool.openConnection()) {
            students.addAll(specification.searchFilter(connection, sql));
        } catch (SQLException e) {
            logger.info("Getting students with attributes exception: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return students;
    }

    @Override
    public List<Student> get() {
        String sql = SELECT_WITHOUT_ATTRIBUTES.formatted(SELECT_SETTING_STUDENTS, TABLE_STUDENTS);
        List<Student> students = new ArrayList<>();

        try (Connection connection = connectionPool.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                students.add(Mapper.mapStudent(resultSet));
            }
        } catch (SQLException e) {
            logger.info("Getting students exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return students;
    }

    @Override
    public Long update(UpdateRequest request, Long contactDetailsId) {
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
                contactDetailsId
        };

        Connection connection = connectionPool.openConnection();

        try (PreparedStatement studentsPreparedStatement = connection.prepareStatement(studentsSqlQuery);
             PreparedStatement contactsPreparedStatement = connection.prepareStatement(contactsSqlQuery)) {
            connection.setAutoCommit(false);

            JdbcUtil.setStatement(contactsPreparedStatement, contactsParams);
            int contactsUpdateResult = contactsPreparedStatement.executeUpdate();

            if (contactsUpdateResult == 1){
                JdbcUtil.setStatement(studentsPreparedStatement, studentsParams);
                result = studentsPreparedStatement.executeUpdate() == 1 ? Long.parseLong(request.getId()) : result;
            }
            connection.commit();
        } catch (SQLException e) {
            logger.info(e.getMessage());
            JdbcUtil.rollback(connection, e);
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(connection);
        }
        return result;
    }

    @Override
    public boolean deleteByContactDetailId(Long contactDetailsId) {
        int result;
        String deleteSql = DELETE_BY_ID.formatted(TABLE_CONTACT_DETAILS);
        try (Connection connection = connectionPool.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
            preparedStatement.setLong(1, contactDetailsId);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        }
        return result == 1;
    }

    @Override
    public Long getContactDetailsIdByStudentId(Long id) {
        String sqlQuery = SELECT_CONTACT_DETAIL_ID_BY_ENTITY_ID.formatted(TABLE_STUDENTS);
        Long contactDetailsId = null;
        try (Connection connection = connectionPool.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                contactDetailsId = resultSet.getLong("contact_details_id");
        } catch (SQLException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        }
        return contactDetailsId;
    }

    private String buildContactsSqlQuery() {
        String sql = UPDATE.formatted(TABLE_CONTACT_DETAILS);
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

        public StudentDaoImpl build() {
            return new StudentDaoImpl(this);
        }
    }
}
