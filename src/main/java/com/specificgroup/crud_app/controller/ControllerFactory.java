package com.specificgroup.crud_app.controller;

import com.specificgroup.crud_app.dao.StudentDao;
import com.specificgroup.crud_app.dao.TutorDao;
import com.specificgroup.crud_app.service.impl.StudentService;
import com.specificgroup.crud_app.service.impl.TutorService;

import static com.specificgroup.crud_app.util.ConnectionPoolAbstract.Type.DEFAULT;

public class ControllerFactory {

    public static Controller newTutorController() {
        return new TutorController(new TutorService(new TutorDao.Builder().type(DEFAULT).build()));
    }

    public static Controller newStudentController() {
        return new StudentController(new StudentService(new StudentDao.Builder().type(DEFAULT).build()));
    }
}