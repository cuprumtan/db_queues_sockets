DROP TABLE IF EXISTS perm_city_polyclinic_7_registry;
CREATE TABLE IF NOT EXISTS perm_city_polyclinic_7_registry (
     ID_doctor INTEGER NOT NULL,
     name_doctor VARCHAR(50) NOT NULL,
     birth_date_doctor TEXT NOT NULL,
     sex_doctor CHAR(1) NOT NULL,
     education_doctor VARCHAR(16) NOT NULL,
     position_doctor VARCHAR(40) NOT NULL,
     qualification_category_doctor CHAR(4) NOT NULL,
     department VARCHAR(64) NOT NULL,
     special_department VARCHAR(64) NOT NULL,
     name_patient VARCHAR(50) NOT NULL,
     birth_date_patient TEXT NOT NULL,
     sex_patient CHAR(1) NOT NULL,
     card_number INTEGER NOT NULL,
     visit_datetime VARCHAR NOT NULL,
     visit_status INTEGER NOT NULL,
     visit_comment TEXT NULL
);