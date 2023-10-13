package com.specificgroup.crud_app.util;

import com.github.cliftonlabs.json_simple.JsonKey;

public enum Attributes implements JsonKey {
    ID,
    NAME,
    AGE,
    SPECIALIZATION,
    PHONE,
    EMAIL;

    private static final String REQUEST_UNDERSCORE = "_";
    private static final String REQUEST_SCORE = "-";

    @Override
    public String getKey() {
        return this.name().toLowerCase().replaceAll(REQUEST_UNDERSCORE, REQUEST_SCORE);
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException();
    }
}