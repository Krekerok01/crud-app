package com.specificgroup.crud_app.dao.impl.specification;


import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.util.Attributes;
import com.specificgroup.crud_app.util.Mapper;
import com.specificgroup.crud_app.util.database.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * Provides method for concat sql request and search filter.
 */
public class StudentSpecification {

    private final Map<Attributes, String> attributes;

    public StudentSpecification(final Map<Attributes, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * Create sql request in database for search elements.
     * @param connection connection to database
     * @return a list of element
     * @throws SQLException a sql exception
     */
    public List<Student> searchFilter(Connection connection, String sql) throws SQLException {
        List<Student> students = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder(sql);
        Queue<Object> queue = new ArrayDeque<>();
        JdbcUtil.preparedRequest(sqlBuilder, queue, attributes);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
            final int size = queue.size();
            for (int i = 1; i < size + 1; i++) {
                preparedStatement.setObject(i, queue.poll());
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                students.add(Mapper.mapStudent(resultSet));
            }
        }
        return students;
    }

}
