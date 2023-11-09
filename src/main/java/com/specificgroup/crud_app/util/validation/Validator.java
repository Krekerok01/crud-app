package com.specificgroup.crud_app.util.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Provides method for validation input data
 *
 * @param <T>
 */
public class Validator<T> {

    private final T object;
    private final List<String> messages = new ArrayList<>();

    private Validator(T object) {
        this.object = object;
    }

    public static <T> Validator<T> of(T object) {
        return new Validator<>(Objects.requireNonNull(object));
    }

    /**
     * Validation input data depends on predicate
     *
     * @param predicate a state of validation data
     * @param message   a string ,message for wrong data
     * @return itself
     */
    public Validator<T> validator(Predicate<T> predicate, String message) {
        if (!predicate.test(object)) {
            messages.add(message);
        }
        return this;
    }

    /**
     * Validate the correctness of the data
     *
     * @return a boolean result
     */
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    /**
     * Return result if data will be correct
     *
     * @return a result
     */
    public T get() {
        return object;
    }

    /**
     * Return list of the messages
     *
     * @return a list of strings
     */
    public List<String> getMessages(){
        return messages;
    }

    /**
     * Return messages concatenated in one line
     *
     * @return a string result
     */
    public String getMessagesString(){
        return messages.stream()
                .collect(Collectors.joining("\n"));
    }
}