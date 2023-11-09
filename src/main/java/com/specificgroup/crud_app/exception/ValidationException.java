package com.specificgroup.crud_app.exception;

/**
 * Extends a standard <code>RuntimeException</code> to be used as a validation exception.
 */
public class ValidationException extends RuntimeException{

    /**
     * Constructs an exception.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructs an exception with a message.
     *
     * @param message a message.
     */
    public ValidationException(String message) {
        super(message);
    }
}