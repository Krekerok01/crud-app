CREATE TABLE IF NOT EXISTS contacts
(
    id  BIGSERIAL PRIMARY KEY,
    phone VARCHAR(13)  NOT NULL,
    email VARCHAR(256)  NOT NULL
);

CREATE TABLE IF NOT EXISTS students
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    age INTEGER NOT NULL,
    contact_id BIGINT REFERENCES contacts (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tutors
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    specialization VARCHAR(128) NOT NULL,
    contact_id BIGINT REFERENCES contacts (id) ON DELETE CASCADE
);


