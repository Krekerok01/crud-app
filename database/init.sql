CREATE TABLE IF NOT EXISTS contact_details
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
    contact_details_id BIGINT REFERENCES contact_details (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tutors
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    specialization VARCHAR(128) NOT NULL,
    contact_details_id BIGINT REFERENCES contact_details (id) ON DELETE CASCADE
);



