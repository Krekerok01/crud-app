package com.specificgroup.crud_app.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}