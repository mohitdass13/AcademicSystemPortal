DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;

CREATE TABLE course_catalog
(
    course_code VARCHAR(6),
    course_name VARCHAR(30),
    credit_strct VARCHAR(10),
    preRequisites TEXT[][],
    PRIMARY KEY(course_code)
);

CREATE TABLE course_offering
(
    course_code VARCHAR(6),
    instructor VARCHAR(50),
    username VARCHAR(50),
    cgpa_const DECIMAL,
    minsem_req  INTEGER,
    core TEXT ARRAY,
    elective TEXT ARRAY,
    PRIMARY KEY(course_code,instructor),
    FOREIGN KEY(course_code) REFERENCES course_catalog(course_code)
);

CREATE TABLE students
(
    name VARCHAR(50),
    emailid  VARCHAR(40),
    entry_no VARCHAR(12),
    dept VARCHAR(100),
    entryyr INTEGER,
    PRIMARY KEY(entry_no)
);
CREATE TABLE users
(
      username VARCHAR(50) PRIMARY KEY,
      password VARCHAR(50),
      name VARCHAR(50),
      role VARCHAR(30)
);
CREATE TABLE coreElective
(
    course_code VARCHAR(6) PRIMARY KEY,
    minsem_req  INTEGER,
    core VARCHAR[],
    elective VARCHAR[],
    FOREIGN KEY(course_code) REFERENCES course_catalog(course_code)
);



INSERT INTO course_catalog VALUES ('CS301','DATABASE SYSTEMS','6-1-2-3','{}');
INSERT INTO course_catalog VALUES ('HS104','PROFESSIONAL ETHICS','6-1-2-3','{}');

INSERT INTO coreElective VALUES('CS301',0,'{"CSE,MNC"}','{"CIV"}');
INSERT INTO coreElective VALUES('HS104',0,'{"CSE","MNC"}','{"CIV"}');



--INSERT INTO course_offering VALUES('CS301','DR.GUNTURI',0.0,1,'{"CSE","MNC"}','{"CIV"}');
--INSERT INTO course_offering VALUES('HS104','DR.SKUMAR',0.0,0,'{"CSE","MNC"}','{"CIV"}');

CREATE OR REPLACE FUNCTION calculate_sum_of_grades(
  table_name text,
  column_name text
) RETURNS integer AS $$
DECLARE
  sum integer := 0;
BEGIN
  EXECUTE 'SELECT SUM(CAST(' || quote_ident(column_name) || ' AS integer)) FROM ' || quote_ident(table_name) INTO sum;
  RETURN sum;
END;
$$ LANGUAGE plpgsql;



