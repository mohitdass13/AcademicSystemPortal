DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;

CREATE TABLE course_catalog
(
    course_code VARCHAR(6),
    course_name VARCHAR(30),
    credit_strct VARCHAR(20),
    preRequisites TEXT[][],
    batch_onward INTEGER,
    PRIMARY KEY(course_code,batch_onward)
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
    PRIMARY KEY(course_code,instructor)
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
    course_code VARCHAR(6),
    minsem_req  INTEGER,
    core VARCHAR[],
    elective VARCHAR[]
);
CREATE TABLE event(
    regDreg INTEGER,
       floatInst INTEGER,
         year INTEGER,
        sem INTEGER

);




