package com.specificgroup.crud_app.exception;

/**
 * Extends a standard <code>RuntimeException</code> to be used as an entity not found exception.
 */
public class EntityNotFoundException extends RuntimeException{

    /**
     * Constructs an exception.
     */
    public EntityNotFoundException() {
        super();
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message a message.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}