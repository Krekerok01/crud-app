package com.specificgroup.crud_app.controller;

import com.specificgroup.crud_app.dao.StudentDao;
import com.specificgroup.crud_app.dao.TutorDao;
import com.specificgroup.crud_app.service.impl.StudentService;
import com.specificgroup.crud_app.service.impl.TutorService;

import java.util.Map;

import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract.Type.DEFAULT;
import static com.specificgroup.crud_app.util.database.connection.ConnectionPoolAbstract.Type.FLEXIBLE;

public class ControllerFactory {

    public static Controller newTutorController() {
        return new TutorController(new TutorService(new TutorDao.Builder().type(DEFAULT).build()));
    }

    public static Controller newStudentController() {
        return new StudentController(new StudentService(new StudentDao.Builder().type(DEFAULT).build()));
    }

    public static Controller flexibleTutorController(Map<String, String> properties) {
        return new TutorController(new TutorService(new TutorDao.Builder().type(FLEXIBLE).property(properties).build()));
    }

    public static Controller flexibleStudentController(Map<String, String> properties) {
        return new StudentController(new StudentService(new StudentDao.Builder().type(FLEXIBLE).property(properties).build()));
    }
}