DROP TABLE IF EXISTS qualification_categories;
CREATE TABLE IF NOT EXISTS qualification_categories (
	ID SERIAL PRIMARY KEY,
	qualification_category CHAR(4) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS departments;
CREATE TABLE IF NOT EXISTS departments (
	ID SERIAL PRIMARY KEY,
	department VARCHAR(64) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS special_departments;
CREATE TABLE IF NOT EXISTS special_departments (
	ID SERIAL PRIMARY KEY,
	special_department VARCHAR(64) NOT NULL,
	ID_department INTEGER NOT NULL REFERENCES departments (ID)
);

DROP TABLE IF EXISTS doctors;
CREATE TABLE IF NOT EXISTS doctors (
	ID SERIAL PRIMARY KEY,
	last_name VARCHAR(16) NOT NULL,
	first_name VARCHAR(16) NOT NULL,
	patronymic VARCHAR(16) NULL,
	birth_date DATE NOT NULL,
	sex char(1) NOT NULL,
	education varchar(40) NOT NULL,
	position varchar(40) NOT NULL,
	ID_qualification_category INTEGER NOT NULL REFERENCES qualification_categories (ID),
	ID_special_department INTEGER NOT NULL REFERENCES special_departments (ID)
);

DROP TABLE IF EXISTS patients;
CREATE TABLE IF NOT EXISTS patients (
	card_number SERIAL PRIMARY KEY,
	last_name VARCHAR(16) NOT NULL,
	first_name VARCHAR(16) NOT NULL,
	patronymic VARCHAR(16) NULL,
	birth_date DATE NOT NULL,
	sex char(1) NOT NULL
);

DROP TABLE IF EXISTS visits;
CREATE TABLE IF NOT EXISTS visits (
    ID SERIAL PRIMARY KEY,
	visit_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
	ID_doctor INTEGER NOT NULL REFERENCES doctors (ID),
	card_number INTEGER NOT NULL REFERENCES patients (card_number),
	status BOOLEAN NOT NULL DEFAULT FALSE,
	visit_comment TEXT NULL
);