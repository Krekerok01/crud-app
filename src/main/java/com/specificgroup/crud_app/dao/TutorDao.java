package com.specificgroup.crud_app.dao;

import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Tutor;
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
import static com.specificgroup.crud_app.util.SqlCommand.Delete.DELETE_BY_ID;
import static com.specificgroup.crud_app.util.SqlCommand.Insert.*;
import static com.specificgroup.crud_app.util.SqlCommand.Select.SELECT;
import static com.specificgroup.crud_app.util.SqlCommand.Select.SELECT_CONTACT_DETAIL_ID_BY_ENTITY_ID;
import static com.specificgroup.crud_app.util.SqlCommand.Select.SELECT_SETTING_TUTORS;
import static com.specificgroup.crud_app.util.SqlCommand.Tables.TABLE_CONTACTS;
import static com.specificgroup.crud_app.util.SqlCommand.Tables.TABLE_STUDENTS;
import static com.specificgroup.crud_app.util.SqlCommand.Tables.TABLE_TUTORS;
import static com.specificgroup.crud_app.util.SqlCommand.Update.*;

public class TutorDao implements Dao<Tutor> {
    private static ConnectionPool connectionPool;
    private final Logger logger =  LogManager.getLogger();

    private TutorDao(final Builder builder) {
        if (connectionPool == null) {
            connectionPool = ConnectionPoolAbstract.connectionPool(builder.type, builder.properties);
        }
    }

    @Override
    public Long create(CreateRequest request) {
        long result = INVALID_RESULT;
        String tutorQuery = INSERT_COMMON_ENTITY.formatted(TABLE_TUTORS, INSERT_SETTING_TUTORS);

        try (Connection connection = connectionPool.openConnection();
             PreparedStatement tutorStatement = connection.prepareStatement(tutorQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement contactStatement = connection.prepareStatement(INSERT_CONTACTS_DETAIL, Statement.RETURN_GENERATED_KEYS)) {

            Object[] contactInfo = {request.getPhone(), request.getEmail()};
            JdbcUtil.setStatement(contactStatement, contactInfo);
            contactStatement.execute();
            ResultSet contactsKeys = contactStatement.getGeneratedKeys();

            if (contactsKeys.next()) {
                Long contactId = contactsKeys.getLong(1);
                Object[] seq = {request.getName(), request.getSpecialization(),contactId};
                JdbcUtil.setStatement(tutorStatement, seq);
                tutorStatement.execute();

                try (ResultSet generatedKeys = tutorStatement.getGeneratedKeys()) {
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
    public List<Tutor> get(JdbcSpecification<Tutor> specification) {
        String sql = SELECT.formatted(SELECT_SETTING_TUTORS, TABLE_TUTORS);
        List<Tutor> tutors = new ArrayList<>();
        try (Connection connection = connectionPool.openConnection()) {
            tutors.addAll(specification.searchFilter(connection, sql));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return tutors;
    }

    @Override
    public Long update(UpdateRequest request, Long contactDetailsId) {
        long result = INVALID_RESULT;
        String tutorsSqlQuery = buildTutorsSqlQuery();
        String contactsSqlQuery = buildContactsSqlQuery();

        Object[] tutorsParams = {
                request.getName(),
                request.getSpecialization(),
                Long.parseLong(request.getId())
        };

        Object[] contactsParams = {
                request.getPhone(),
                request.getEmail(),
                contactDetailsId
        };

        try (Connection connection = connectionPool.openConnection();
             PreparedStatement tutorsPreparedStatement = connection.prepareStatement(tutorsSqlQuery);
             PreparedStatement contactsPreparedStatement = connection.prepareStatement(contactsSqlQuery)) {

            JdbcUtil.setStatement(contactsPreparedStatement, contactsParams);
            int contactsUpdateResult = contactsPreparedStatement.executeUpdate();

            if (contactsUpdateResult == 1){
                JdbcUtil.setStatement(tutorsPreparedStatement, tutorsParams);
                result = tutorsPreparedStatement.executeUpdate() == 1 ? Long.parseLong(request.getId()) : result;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean delete(long id) {
        int result;
        //delete contact
        String delete = DELETE_BY_ID.formatted(TABLE_TUTORS);
        try (Connection connection = connectionPool.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setLong(1, id);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result == 1;
    }

    @Override
    public Long getContactDetailsIdByMainEntityId(Long id) {
        String sqlQuery = SELECT_CONTACT_DETAIL_ID_BY_ENTITY_ID.formatted(TABLE_TUTORS);
        Long contactDetailsId = null;
        try (Connection connection = connectionPool.openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                contactDetailsId = resultSet.getLong("contact_id");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return contactDetailsId;
    }

    private String buildContactsSqlQuery() {
        String sql = UPDATE.formatted(TABLE_CONTACTS);
        StringBuilder sqlBuilder = new StringBuilder(sql);
        sqlBuilder.append(UPDATE_PHONE).append(UPDATE_COMMA)
            .append(UPDATE_EMAIL)
            .append(UPDATE_WHERE_ID);
        return sqlBuilder.toString();
    }

    private String buildTutorsSqlQuery() {
        String sql = UPDATE.formatted(TABLE_TUTORS);
        StringBuilder sqlBuilder = new StringBuilder(sql);
        sqlBuilder.append(UPDATE_NAME).append(UPDATE_COMMA)
            .append(UPDATE_SPECIALIZATION)
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

        public TutorDao build() {
            return new TutorDao(this);
        }
    }
}
