-- liquibase formatted sql

-- changeset marina:1

CREATE TABLE users (
                       id BIGSERIAL NOT NULL PRIMARY KEY,
                       name varchar(50) NOT NULL,
                       phone_number varchar (20) NOT NULL,
                       shelter_id BIGINT NOT NULL
);

-- changeset marina:2

CREATE TABLE client (
                        id BIGSERIAL NOT NULL PRIMARY KEY,
                        name varchar(50) NOT NULL,
                        phone_number varchar (20) NOT NULL,
                        address varchar (60) NOT NULL
);

-- changeset marina:3

CREATE TABLE volunteer (
                           id BIGSERIAL NOT NULL PRIMARY KEY,
                           name varchar(50) NOT NULL,
                           telegram_nickname varchar(50) NOT NULL,
                           shelter_id BIGINT NOT NULL
);

-- changeset marina:4

CREATE TABLE pet_type (
                          id BIGINT NOT NULL PRIMARY KEY,
                          type_name varchar(16) NOT NULL
);

-- changeset marina:5

CREATE TABLE pet (
                     id BIGSERIAL NOT NULL PRIMARY KEY,
                     pet_type_id BIGINT NOT NULL,
                     pet_name varchar(50) NOT NULL,
                     breed varchar(60) NOT NULL,
                     client_id BIGINT,
                     volunteer_id BIGINT NOT NULL
);

-- changeset marina:6

ALTER TABLE pet ADD FOREIGN KEY (pet_type_id) REFERENCES pet_type (id);

-- changeset marina:7

ALTER TABLE pet ADD FOREIGN KEY (client_id) REFERENCES client (id);

-- changeset marina:8

ALTER TABLE pet ADD FOREIGN KEY (volunteer_id) REFERENCES volunteer (id);



-- changeset marina:9

CREATE TABLE shelter (
                         id BIGSERIAL NOT NULL PRIMARY KEY,
                         name varchar(50) NOT NULL,
                         address varchar (100) NOT NULL,
                         schedule varchar (100) NOT NULL,
                         about varchar (1000) NOT NULL,
                         guard varchar (1000) NOT NULL,
                         location_map varchar (100) NOT NULL,
                         pet_type_id BIGINT NOT NULL
);

-- changeset marina:10

ALTER TABLE shelter ADD FOREIGN KEY (pet_type_id) REFERENCES pet_type (id);

-- changeset marina:11

CREATE TABLE report (
                        id BIGSERIAL NOT NULL PRIMARY KEY,
                        pet_id BIGINT NOT NULL,
                        date_time TIMESTAMP NOT NULL,
                        description varchar(1000) NOT NULL,
                        photo varchar(1000) NOT NULL
);

-- changeset marina:12

ALTER TABLE report ADD FOREIGN KEY (pet_id) REFERENCES pet (id);

-- changeset marina:13

CREATE TABLE recommendation (
                                id BIGSERIAL NOT NULL PRIMARY KEY,
                                recommendation_name varchar(200) NOT NULL,
                                description varchar(1000) NOT NULL,
                                pet_type_id BIGINT
);

-- changeset marina:14

ALTER TABLE recommendation ADD FOREIGN KEY (pet_type_id) REFERENCES pet_type(id);