package org.iitrpr;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class academicOfficeTest {
    academicOffice acoff = new academicOffice();
    databaseSeeder dataseed = new databaseSeeder();
    Connection connection = dataseed.connect();
    PreparedStatement preparedStatement;

    @BeforeEach
    void setUp() {


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void startNewSemester() throws SQLException{
//        acoff.year = 2021;
//        acoff.sem = 1;
//
//        acoff.startNewSemester(connection);
//
//        assertEquals(2021, acoff.year);
//        assertEquals(2, acoff.sem);
//
//        acoff.startNewSemester(connection);
//
//        assertEquals(2022, acoff.year);
//        assertEquals(1, acoff.sem);
//        String qry=String.format("INsert into course_offering values('CS132','DR.GUNTURI','gunturi123',0,0,+'{\"CSE","MTH\"}','{\"ME\"}'");
//        Statement stmnt=connection.createStatement();
//        stmnt.execute(qry);


    }

    @Test
    void setEvent() throws SQLException {
        try {
            acoff.regDreg = 0;
            acoff.floatInst = 1;
            System.setIn(new ByteArrayInputStream("1".getBytes()));

            acoff.setEvent(connection);
            assertEquals(1, acoff.regDreg);
            assertEquals(0, acoff.floatInst);
            System.setIn(new ByteArrayInputStream("2\n".getBytes()));

            acoff.setEvent(connection);
            assertEquals(0, acoff.regDreg);
            assertEquals(1, acoff.floatInst);

            System.setIn(new ByteArrayInputStream("3\n".getBytes()));

            acoff.setEvent(connection);
            assertEquals(0, acoff.regDreg);
            assertEquals(acoff.floatInst, acoff.floatInst);

            // Set the System.in input stream to "4\n"
            System.setIn(new ByteArrayInputStream("4\n".getBytes()));

            acoff.setEvent(connection);

            assertEquals(acoff.regDreg, acoff.regDreg);
            assertEquals(0, acoff.floatInst);
            String qry=String.format("UPDATE event set regDreg=1");
            Statement stmnt=connection.createStatement();
            stmnt.execute(qry);

        }catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void isGraduated() throws SQLException, IOException {
        String input = "2020csb1098\n"; // Enter the entry no
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Call the method to test

        String output = acoff.isGraduated(connection);

        // Get the output of the method
        assertEquals(output, "NO!!! This guy has not completed the required credits for graduation");
        // Check if the output is correct

    }

    @Test
    void addNewCourse() throws SQLException, IOException {

        String input = "CS133\n"
                + "RANDOM COURSE\n"
                + "6-1-2-2-3\n"
                + "\"\"\n"
                + "0\n"
                + "2020\n"
                + "\"CSE\",\"MTH\"\n"
                + "\"ME\"\n";
        InputStream inp = new ByteArrayInputStream(input.getBytes());
        System.setIn(inp);

//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(out));

        // Act
        boolean result = acoff.addNewCourse(connection);

        // Assert
        assertEquals( true,result);
        String qry=String.format("DELETE FROM course_catalog WHERE course_code='CS133' AND batch_onward=2020");
        Statement smnt=connection.createStatement();
        smnt.execute(qry);

    }

    @Test
    void generateTranscripts() throws SQLException, IOException {
        String input = "2020csb1098\n" + "1\n";
        InputStream inp = new ByteArrayInputStream(input.getBytes());
        System.setIn(inp);

        String result = acoff.generateTranscripts(connection);
        assertEquals(result, "Transcript generated Successfully");


    }

    @Test
    void viewGradesAll() throws SQLException, IOException {
        Statement stmt = connection.createStatement();
//        String expectedOutput = "Enter the Entry No. of the Student You wanna see the grades\n\nHS104 PROFESSIONAL ETHICS 1 A elective\n\nCS304 NETWORK 1 A core\n\n";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ByteArrayInputStream in = new ByteArrayInputStream("2020csb1098".getBytes());
        System.setIn(in);

        // Act
        String result = acoff.viewGradesAll(connection);

        // Assert
        assertEquals("These are the grades", result);


        // Clean up
    }
    @Test
    void addStudent() throws SQLException {
        boolean result = acoff.addStudent(connection, "PRATHAM KUNDAN", "2020CSB1115@iitrpr.ac.in", "2020CSB1115", "CSE", 2020);

        // check if the return value is correct
        assertEquals(false, result);
         result = acoff.addStudent(connection, "PRATHAM KUNDAN", "2020CSB1147@iitrpr.ac.in", "2020CSB1147", "CSE", 2020);

        // check if the return value is correct
        assertEquals(true, result);

        String qry=String.format("DELETE FROM students WHERE entry_no='2020CSB1147'");
        Statement smnt=connection.createStatement();
        smnt.execute(qry);
        String qry2=String.format("Drop table s2020csb1147");
        Statement smnt2=connection.createStatement();
        smnt2.execute(qry2);
        // check if the student was added to the database
//        PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM students WHERE entry_no = ?");
//        pstmt.setString(1, "2020CSB1115");
//        ResultSet rs = pstmt.executeQuery();
//        assertTrue(rs.next());
//        assertEquals(1, rs.getInt(1));
//
//        // check if the student's table was created in the database
//        pstmt = connection.prepareStatement("SELECT table_name " +
//                "FROM information_schema.tables " +
//                "WHERE table_name = 's2020CSB1115' " +
//                "AND table_schema = 'public'");
//        rs = pstmt.executeQuery();
//        assertTrue(rs.next());
//        assertEquals("s2020csb1115", rs.getString(1));

    }
}