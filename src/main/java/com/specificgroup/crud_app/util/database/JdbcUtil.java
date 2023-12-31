package com.specificgroup.crud_app.util.database;

import com.specificgroup.crud_app.util.Attributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Queue;

import static com.specificgroup.crud_app.util.Constants.Select.*;

/**
 * Util class provides methods for create request to database
 */
public class JdbcUtil {

    public static void setStatement(PreparedStatement preparedStatement, Object[] seq) throws SQLException {
        for (int i = 1; i < seq.length + 1; i++) {
            preparedStatement.setObject(i, seq[i - 1]);
        }
    }

    public static void preparedRequest(StringBuilder sqlBuilder,
                                       Queue<Object> queue, Map<Attributes, String> attributes) {
        for (Map.Entry<Attributes, String> next : attributes.entrySet()) {
            switch (next.getKey()) {
                case ID -> {
                    sqlBuilder.append(SELECT_ID).append(SELECT_AND);
                    queue.add(Long.parseLong(next.getValue()));
                }
                case NAME -> {
                    sqlBuilder.append(SELECT_NAME).append(SELECT_AND);
                    queue.add(next.getValue());
                }
                case AGE -> {
                    sqlBuilder.append(SELECT_AGE).append(SELECT_AND);
                    queue.add(Integer.parseInt(next.getValue()));
                }
                case SPECIALIZATION -> {
                    sqlBuilder.append(SELECT_SPECIALIZATION).append(SELECT_AND);
                    queue.add(next.getValue());
                }
                case PHONE -> {
                    sqlBuilder.append(SELECT_CONTACT_PHONE).append(SELECT_AND);
                    queue.add(next.getValue());
                }
                case EMAIL -> {
                    sqlBuilder.append(SELECT_CONTACT_EMAIL).append(SELECT_AND);
                    queue.add(next.getValue());
                }
            }
        }
        sqlBuilder.delete(sqlBuilder.length() - SELECT_AND.length(), sqlBuilder.length());
    }

    public static void rollback(Connection connection, SQLException e) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                System.out.println(("Database error: " + e.getMessage()));
            }

            throw new RuntimeException("Database error", e);
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (final SQLException e) {
                throw new RuntimeException("Database connection error.");
            }
        }
    }
}