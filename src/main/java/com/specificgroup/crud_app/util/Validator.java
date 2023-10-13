package com.specificgroup.crud_app.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


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

    class RegularExpression {
        public static final String NAME_VALIDATION = "[a-zA-Z]+";
        public static final String AGE_VALIDATION = "\\d{1,2}";
        public static final String EMAIL_VALIDATION = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        public static final String PHONE_VALIDATION = "\\+\\d{12}";



        public static final long INVALID_RESULT = -1;
    }
}
