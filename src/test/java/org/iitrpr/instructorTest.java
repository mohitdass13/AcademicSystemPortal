package org.iitrpr;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Result;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class instructorTest {
    instructor instruct=new instructor();
    databaseSeeder dataseed=new databaseSeeder();
    Connection connection=dataseed.connect();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateGrades() throws SQLException, IOException {
        String input="HS104";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Call the method to test

         boolean check=instruct.updateGrades(connection,"DR.GUNTURI");

       assertEquals(false,check);
    }

    @Test
    void viewGrades() throws SQLException, IOException {
        String entryNo="2020csb1098";
        InputStream in=new ByteArrayInputStream(entryNo.getBytes());
        System.setIn(in);
        String tocheck="CS304 NETWORK 1 A core " +
                "\n" +
                "HS104 PROFESSIONAL ETHICS 1 C elective " +
                "\n";
        boolean output=instruct.viewGrades(connection,"gunturi123");
        assertEquals(true,output);

        entryNo="2020csb1116";
        InputStream in2=new ByteArrayInputStream(entryNo.getBytes());
        System.setIn(in2);
        tocheck="This Student doesn't exists .. Please Enter the correct entry no\n";
        output=instruct.viewGrades(connection,"gunturi123");
        assertEquals(false,output);
    }

    @Test
    void dRegisterCourse() throws SQLException, IOException {
//        String code="CS119";
//        InputStream in=new ByteArrayInputStream(code.getBytes());
//        System.setIn(in);
//        boolean output=instruct.dRegisterCourse(connection,"gunturi123");
//        code="CS119";
//
//        InputStream in2=new ByteArrayInputStream(code.getBytes());
//        System.setIn(in2);
//        instruct.registerCourse(connection,"gunturi123");
//
////        System.out.println(output);
////        System.out.println(tocheck);
//        assertEquals(true,output);
    }
    @Test
    void coursesOffered() throws SQLException {
        String username="gunturi123";
//        String tocheck= "CS304 DR.GUNTURI gunturi123 0 0 {CSE} {MTH} \n";
        boolean output=instruct.coursesOffered(connection,username);
        assertEquals(true,output);
    }

    @Test
    void registerCourse() throws SQLException, IOException {
        String code="CS123";
        String input = "CS123\n"
                + "0.0\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        instruct.registerCourse(connection,"gunturi123");
        Integer count=0;
        String qry=String.format("SELECT count(*) FROM course_offering WHERE course_code='%s' AND instructor='DR.GUNTURI'",code);
        Statement stmt=connection.createStatement();
        ResultSet result=stmt.executeQuery(qry);
        while(result.next())
        {
            count=result.getInt("count");
        }
        assertEquals(1,count);


        input = "CS123\n";
        InputStream in2=new ByteArrayInputStream(input.getBytes());
        System.setIn(in2);
        instruct.dRegisterCourse(connection,"gunturi123");

    }
}