package com.specificgroup.crud_app.util.database;

import com.specificgroup.crud_app.entity.Tutor;
import com.specificgroup.crud_app.util.Attributes;
import com.specificgroup.crud_app.util.Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TutorSpecification implements JdbcSpecification<Tutor> {

    private final Map<Attributes, String> attributes;

    public TutorSpecification(final Map<Attributes, String> attributes) {
        this.attributes = attributes;
    }


    @Override
    public List<Tutor> searchFilter(Connection connection, String sql) throws SQLException {
        List<Tutor> tutors = new ArrayList<>();
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
                tutors.add(Mapper.mapTutor(resultSet));
            }
        }
        return tutors;
    }
}