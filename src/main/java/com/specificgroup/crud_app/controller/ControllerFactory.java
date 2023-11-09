package com.specificgroup.crud_app.controller;

import com.specificgroup.crud_app.dao.impl.StudentDaoImpl;
import com.specificgroup.crud_app.dao.impl.TutorDaoImpl;
import com.specificgroup.crud_app.service.impl.StudentServiceImpl;
import com.specificgroup.crud_app.service.impl.TutorServiceImpl;

import java.util.Map;

import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract.Type.DEFAULT;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract.Type.FLEXIBLE;

public class ControllerFactory {

    public static Controller newTutorController() {
        return new TutorController(new TutorServiceImpl(new TutorDaoImpl.Builder().type(DEFAULT).build()));
    }

    public static Controller newStudentController() {
        return new StudentController(new StudentServiceImpl(new StudentDaoImpl.Builder().type(DEFAULT).build()));
    }

    public static Controller flexibleTutorController(Map<String, String> properties) {
        return new TutorController(
                new TutorServiceImpl(new TutorDaoImpl.Builder().type(FLEXIBLE).property(properties).build()));
    }

    public static Controller flexibleStudentController(Map<String, String> properties) {
        return new StudentController(
                new StudentServiceImpl(new StudentDaoImpl.Builder().type(FLEXIBLE).property(properties).build()));
    }
}