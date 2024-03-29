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
        System.out.println(sem);
        InputStream in=new ByteArrayInputStream(sem.getBytes());
        System.setIn(in);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
       boolean output= student.view_grades(connection,"2020csb1098");

        assertEquals(true,output);
    }

    @Test
    void deregisterCourse() throws SQLException, IOException {
        String input="CS304\n" +
                "DR.GUNTURI\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
       boolean check= student.deregisterCourse(connection,"2020csb1098");
       assertEquals(true,check);


    }

    @Test
    void ispreRequisites() throws SQLException {
        boolean check=student.ispreRequisites(connection,"CS304","2020CSB1098",1);
        assertEquals(true,check);
         check=student.ispreRequisites(connection,"CS303","2020CSB1098",1);
        assertEquals(true,check);
        check=student.ispreRequisites(connection,"CS909","2020CSB1098",1);
        assertEquals(false,check);



    }

    @Test
    void isCreditCriteria() throws SQLException {
         boolean check= student.isCreditCriteria(connection,"2020csb1098",1,"CS305");
         assertEquals(true,check);
        check= student.isCreditCriteria(connection,"2020csb1098",1,"HS104");
        assertEquals(true,check);
        check= student.isCreditCriteria(connection,"2020csb1098",1,"HS103");

        assertEquals(true,check);
        check=student.isCreditCriteria(connection,"2020csb1098",3,"HS104");

    }

    @Test
    void cgpa_calculate() throws SQLException {
        double gpa=student.cgpa_calculate(connection,"2020csb1098");
//        assertEquals(8.909090909090908,gpa);
        assertEquals(6.714285714285714,gpa);


    }

    @Test
    void isminSemCompleted() throws SQLException {
        boolean check=student.isminSemCompleted(connection,"2020csb1098","HS104");
        assertEquals(true,check);
        check=student.isminSemCompleted(connection,"2020csb1098","CS203");
        assertEquals(true,check);
    }

    @Test
    void isCourseFloated() throws SQLException {
        boolean check=student.isCourseFloated(connection,"CS309","DR.GUNTURI");
        assertEquals(false,check);
        check=student.isCourseFloated(connection,"CS123","DR.GUNTURI");
        assertEquals(true,check);
    }

    @Test
    void enroll_course() throws SQLException, IOException {
        int previousEvent = student.acaoff.regDreg;
        student.acaoff.regDreg = 1;
        String input="CS132\n" +
                "DR.GUNTURI\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        boolean check=student.enroll_course(connection,"2020CSB1098");

        assertEquals(true,check);
        input="HS103\n" +
                "DR.GUNTURI\n";

        in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        check=student.enroll_course(connection,"2020CSB1098");
        assertEquals(false,check);

        input="CS205\n" +
                "DR.GUNTURI\n";

        in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        check=student.enroll_course(connection,"2020CSB1098");
        assertEquals(false,check);

        input="CS132\n" +
                "DR.GUNTURI\n";
        in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        check=student.deregisterCourse(connection,"2020csb1098");
        assertEquals(true,check);
//        input="HS103\n" +
//                "DR.GUNTURI\n";
//
//        in=new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//        check=student.enroll_course(connection,"2020CSB1098");
//        assertEquals(false,check);


          student.acaoff.regDreg = previousEvent;


    }
}