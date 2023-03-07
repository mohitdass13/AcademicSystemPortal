package org.iitrpr;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class studentsTest {
    students student=new students();
    databaseSeeder dataseed=new databaseSeeder();
    Connection connection=dataseed.connect();
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void idNotAreadyCompleted() throws SQLException {

        boolean out= student.idNotAreadyCompleted(connection,"2020csb1098","HS103",1);
       boolean temp=true;
        assertEquals(temp,out);

    }

    @Test
    void view_grades() throws SQLException, IOException {
        String out="";
        String sem="1";
        String tocheck="Enter the Semester you wanna see grades\n\n" +
                "CS304 A \n" +
                "\n" +
                "HS104 C \n\n";
        System.out.println(sem);
        InputStream in=new ByteArrayInputStream(sem.getBytes());
        System.setIn(in);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        student.view_grades(connection,"2020csb1098");
        String output = outContent.toString();
        assertEquals(tocheck,output);
    }

    @Test
    void deregisterCourse() throws SQLException, IOException {
        String input="CS304\n" +
                "DR.GUNTURI\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
       boolean check= student.deregisterCourse(connection,"2020csb1098");
       assertEquals(true,check);
        input="CS305\n" +
                "DR.GUNTURI\n";
         in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        check=student.deregisterCourse(connection,"2020csb1098");
        assertEquals(true,check);

    }

    @Test
    void ispreRequisites() throws SQLException {
        boolean check=student.ispreRequisites(connection,"CS304","2020CSB1098",1);
        assertEquals(true,check);

    }

    @Test
    void isCreditCriteria() {
    }

    @Test
    void cgpa_calculate() {
    }

    @Test
    void isminSemCompleted() {
    }

    @Test
    void isCourseFloated() {
    }

    @Test
    void enroll_course() {
    }
}