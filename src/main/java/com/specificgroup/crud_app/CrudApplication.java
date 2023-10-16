package com.specificgroup.crud_app;

import com.specificgroup.crud_app.controller.ApiHandler;
import com.specificgroup.crud_app.controller.ControllerFactory;
import com.specificgroup.crud_app.server.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.specificgroup.crud_app.util.HTTPConstants.UrlPath.STUDENT_PATH;
import static com.specificgroup.crud_app.util.HTTPConstants.UrlPath.TUTOR_PATH;


public class CrudApplication {

    public static void main(String[] args) {
        Map<String, ApiHandler> handlers = new HashMap<>();
        handlers.put(STUDENT_PATH ,new ApiHandler(ControllerFactory.newStudentController()));
        handlers.put(TUTOR_PATH ,new ApiHandler(ControllerFactory.newTutorController()));
        Server server = new Server(handlers);
        server.start();

        System.out.println("To terminate the program press: q/Q ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            if ("q".equalsIgnoreCase(scanner.next())) {
                System.out.print("Termination of the program...");
                server.stop();
                return;
            } else {
                System.out.print("To terminate the program press: q/Q: ");
            }
        }
    }
}
