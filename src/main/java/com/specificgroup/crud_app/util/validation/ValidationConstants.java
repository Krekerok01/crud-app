package com.specificgroup.crud_app.util.validation;

public class ValidationConstants {
    public static final String DIGIT = "\\d+";
    public static final String NAME_VALIDATION = "[a-zA-Z]+";
    public static final String AGE_VALIDATION = "\\d{1,2}";
    public static final String SPECIALIZATION_VALIDATION = "[a-zA-Z]+";
    public static final String EMAIL_VALIDATION = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    public static final String PHONE_VALIDATION = "\\+\\d{12}";

    public static final long INVALID_RESULT = -1;
}
