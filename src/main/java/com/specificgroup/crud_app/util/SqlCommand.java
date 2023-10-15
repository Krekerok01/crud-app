package com.specificgroup.crud_app.util;


public final class SqlCommand {

    private SqlCommand() {
        throw new UnsupportedOperationException();
    }


    public final static class Insert {

        private Insert() {
            throw new UnsupportedOperationException();
        }
        public static final String INSERT_COMMON_ENTITY = """
                INSERT INTO %s(name, %s, contact_id)
                VALUES (?,?,?);""";
        public static final String INSERT_SETTING_STUDENTS = "age";
        public static final String INSERT_SETTING_TUTORS = "specialization";
        public static final String INSERT_CONTACTS_DETAIL = """
                INSERT INTO contacts(phone, email)
                VALUES (?,?);""";

    }

    public final static class Select {

        private Select() {
            throw new UnsupportedOperationException();
        }

        public static final String SELECT = """
                SELECT e.id, e.name, %s, c.id as contact_id, c.phone, c.email
                FROM %s e
                         JOIN contacts c on c.id = e.contact_id
                WHERE 
                """;
        public static final String SELECT_CONTACT_DETAIL_ID_BY_ENTITY_ID =  """
                SELECT c.id as contact_id
                FROM %s e
                         JOIN contacts c on c.id = e.contact_id
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

    public final static class Delete {
        private Delete() {
            throw new UnsupportedOperationException();
        }

        public static final String DELETE_BY_ID = "DELETE from %s WHERE id=?";

    }

    public final static class Tables {

        private Tables() {
            throw new UnsupportedOperationException();
        }

        public static final String TABLE_STUDENTS = "students";
        public static final String TABLE_TUTORS = "tutors";
        public static final String TABLE_CONTACTS = "contacts";
    }

    public final static class Constant {

        private Constant() { throw new UnsupportedOperationException();}

        public static final long INVALID_RESULT = -1;
    }
}
