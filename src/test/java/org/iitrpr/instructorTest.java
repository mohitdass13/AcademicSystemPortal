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

         instruct.updateGrades(connection);

        PreparedStatement pstmt = connection.prepareStatement("SELECT grade FROM s2020csb1098 WHERE course_code='HS104'");
        ResultSet rs = pstmt.executeQuery();
        assertTrue(rs.next());
        assertEquals("C", rs.getString("grade"));
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
        String output=instruct.viewGrades(connection,"gunturi123");
        assertEquals(output,tocheck);

        entryNo="2020csb1110";
        InputStream in2=new ByteArrayInputStream(entryNo.getBytes());
        System.setIn(in2);
        tocheck="This Student doesn't exists .. Please Enter the correct entry no\n";
        output=instruct.viewGrades(connection,"gunturi123");
        assertEquals(output,tocheck);
    }

    @Test
    void dRegisterCourse() throws SQLException, IOException {
        String code="CS304";
        InputStream in=new ByteArrayInputStream(code.getBytes());
        System.setIn(in);
        String output="";
        instruct.dRegisterCourse(connection,"gunturi123");
        String tocheck= "";

        String qry=String.format("SELECT * FROM course_offering WHERE instructor='%s'","DR.GUNTURI");
        Statement stmt=connection.createStatement();
        ResultSet result= stmt.executeQuery(qry);
        ResultSetMetaData rsmd = result.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (result.next()) {
            for(int i = 1 ; i <= columnsNumber; i++){

                output+=(result.getString(i) + " ");

            }
            output+="\n";
        }
//        System.out.println(output);
//        System.out.println(tocheck);
        assertEquals(tocheck,output);
    }
    @Test
    void coursesOffered() throws SQLException {
        String username="gunturi123";
        String tocheck= "CS304 DR.GUNTURI gunturi123 0 0 {CSE} {MTH} \n";
        String output=instruct.coursesOffered(connection,username);
        assertEquals(output,tocheck);
    }

    @Test
    void registerCourse() throws SQLException, IOException {

        String input = "CS304\n"
                + "0.0\n";
        InputStream in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        instruct.registerCourse(connection,"gunturi123");
        Integer count=0;
        String qry=String.format("SELECT count(*) FROM course_offering WHERE course_code='CS304' AND instructor='DR.GUNTURI'");
        Statement stmt=connection.createStatement();
        ResultSet result=stmt.executeQuery(qry);
        while(result.next())
        {
            count=result.getInt("count");
        }
        assertEquals(1,count);

    }
}