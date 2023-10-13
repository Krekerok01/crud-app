package com.specificgroup.crud_app.util;



import com.specificgroup.crud_app.entity.ContactDetails;
import com.specificgroup.crud_app.entity.Student;
import com.specificgroup.crud_app.entity.Tutor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper {

    public static Student mapStudent(ResultSet resultSet) throws SQLException {
        return new Student(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getInt("age"),
                new ContactDetails(
                        resultSet.getLong("contact_id"),
                        resultSet.getString("phone"),
                        resultSet.getString("email")
                )
        );
    }

    public static Tutor mapTutor(ResultSet resultSet) throws SQLException {
        return new Tutor(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("specialization"),
                new ContactDetails(
                        resultSet.getLong("contact_id"),
                        resultSet.getString("phone"),
                        resultSet.getString("email")
                )
        );
    }
}
