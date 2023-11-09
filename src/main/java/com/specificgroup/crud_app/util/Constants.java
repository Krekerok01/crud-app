package com.specificgroup.crud_app.util;

/**
 * Application constants.
 */
public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException();
    }

    /**
     * HTTP method constants for the application.
     */
    public static class HttpMethod {

        private HttpMethod() {
            throw new UnsupportedOperationException();
        }

        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    /**
     * HTTP response status constants for the application.
     */
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

    /**
     * Messages constants for the application.
     */
    public static class Message {

        private Message() {
            throw new UnsupportedOperationException();
        }

        public static final String WRONG_NAME = "Name can contains only letters";
        public static final String WRONG_AGE = "Age must be a positive number";
        public static final String WRONG_EMAIL = "Valid email is required";
        public static final String WRONG_PHONE = "Valid phone is required. Example: +375294682593";
        public static final String WRONG_ID = "Id must be a digit.";
        public static final String WRONG_SPECIALIZATION = "Specialization can contains only letters and spaces";
        public static final String EMPTY_SPECIALIZATION = "Specialization cannot be empty";
    }

    /**
     * URL constants for the application.
     */
    public static class UrlPath {

        private UrlPath() {
            throw new UnsupportedOperationException();
        }

        public static final String STUDENT_PATH = "/students";
        public static final String TUTOR_PATH = "/tutors";
    }

    /**
     * SQL Insert request constants for the application.
     */
    public final static class Insert {

        private Insert() {
            throw new UnsupportedOperationException();
        }
        public static final String INSERT_MAIN_ENTITY = """
                INSERT INTO %s(name, %s, contact_details_id)
                VALUES (?,?,?);""";
        public static final String INSERT_SETTING_STUDENTS = "age";
        public static final String INSERT_SETTING_TUTORS = "specialization";
        public static final String INSERT_CONTACT_DETAILS = """
                INSERT INTO contact_details(phone, email)
                VALUES (?,?);""";

    }

    /**
     * SQL Select request constants for the application.
     */
    public final static class Select {

        private Select() {
            throw new UnsupportedOperationException();
        }

        public static final String SELECT = """
                SELECT e.id, e.name, %s, c.id as contact_details_id, c.phone, c.email
                FROM %s e
                         JOIN contact_details c on c.id = e.contact_details_id
                WHERE 
                """;
        public static final String SELECT_WITHOUT_ATTRIBUTES = """
                SELECT e.id, e.name, %s, c.id as contact_details_id, c.phone, c.email
                FROM %s e
                         JOIN contact_details c on c.id = e.contact_details_id
                """;
        public static final String SELECT_CONTACT_DETAIL_ID_BY_ENTITY_ID =  """
                SELECT c.id as contact_details_id
                FROM %s e
                         JOIN contact_details c on c.id = e.contact_details_id
                WHERE e.id=?
                """;
        public static final String SELECT_ID = "e.id=?";
        public static final String SELECT_NAME = "e.name=?";
        public static final String SELECT_AGE = "e.age=?";
        public static final String SELECT_SPECIALIZATION = "e.specialization=?";
        public static final String SELECT_CONTACT_PHONE = "c.phone=?";
        public static final String SELECT_CONTACT_EMAIL = "c.email=?";
        public static final String SELECT_AND = " AND ";
        public static final String SELECT_SETTING_STUDENTS = "e.age";
        public static final String SELECT_SETTING_TUTORS = "e.specialization";

    }

    /**
     * SQL Update request constants for the application.
     */
    public final static class Update {

        private Update() {
            throw new UnsupportedOperationException();
        }

        public static final String UPDATE = """
                UPDATE %s
                SET
                """;
        public static final String UPDATE_NAME = "name=?";
        public static final String UPDATE_AGE = "age=?";
        public static final String UPDATE_SPECIALIZATION = "specialization=?";
        public static final String UPDATE_PHONE = "phone=?";
        public static final String UPDATE_EMAIL = "email=?";
        public static final String UPDATE_COMMA = ",";
        public static final String UPDATE_WHERE_ID = " WHERE id=?";

    }

    /**
     * SQL Delete request constants for the application.
     */
    public final static class Delete {
        private Delete() {
            throw new UnsupportedOperationException();
        }

        public static final String DELETE_BY_ID = "DELETE from %s WHERE id=?";

    }

    /**
     * SQL Table names constants for the application.
     */
    public final static class Tables {

        private Tables() {
            throw new UnsupportedOperationException();
        }

        public static final String TABLE_STUDENTS = "students";
        public static final String TABLE_TUTORS = "tutors";
        public static final String TABLE_CONTACT_DETAILS = "contact_details";
    }

    public final static class Constant {

        private Constant() { throw new UnsupportedOperationException();}

        public static final long INVALID_RESULT = -1;
    }
}