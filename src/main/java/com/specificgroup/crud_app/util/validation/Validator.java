package com.specificgroup.crud_app.util.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Validator<T> {

    private final T object;
    private final List<String> messages = new ArrayList<>();

    private Validator(T object) {
        this.object = object;
    }

    public static <T> Validator<T> of(T object) {
        return new Validator<>(Objects.requireNonNull(object));
    }


    public Validator<T> validator(Predicate<T> predicate, String message) {
        if (!predicate.test(object)) {
            messages.add(message);
        }
        return this;
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public T get() {
        return object;
    }

    public List<String> getMessages(){
        return messages;
    }

    public String getMessagesString(){
        return messages.stream()
                .collect(Collectors.joining("\n"));
    }
}