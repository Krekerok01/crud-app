package com.specificgroup.crud_app.exception;

public class ValidationException extends RuntimeException{
    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }
}