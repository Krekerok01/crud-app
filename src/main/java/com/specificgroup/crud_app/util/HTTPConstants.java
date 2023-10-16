package com.specificgroup.crud_app.util;

public final class HTTPConstants {

    private HTTPConstants() {
        throw new UnsupportedOperationException();
    }

    public static class HttpMethod {

        private HttpMethod() {
            throw new UnsupportedOperationException();
        }

        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String DELETE = "DELETE";
    }

    public static class HttpResponseStatus {

        private HttpResponseStatus() {
            throw new UnsupportedOperationException();
        }

        public static final int STATUS_OK = 200;
        public static final int STATUS_CREATED = 201;
        public static final int STATUS_NO_CONTENT = 204;
        public static final int STATUS_BAD_REQUEST = 400;
        public static final int STATUS_NOT_FOUND = 404;

    }

    public static class UrlPath {

        private UrlPath() {
            throw new UnsupportedOperationException();
        }

        public static final String STUDENT_PATH = "/students";
        public static final String TUTOR_PATH = "/tutors";
    }
}