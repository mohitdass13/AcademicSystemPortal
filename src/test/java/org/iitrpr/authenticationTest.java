package org.iitrpr;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class authenticationTest {
    authentication auth=new authentication();
    databaseSeeder dataseed=new databaseSeeder();

    Connection connection=dataseed.connect();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loginTest1() throws SQLException, IOException {
        String input="2020CSB1098\n" +
                "12345678\n" +
                "1\nCS116\nDR.GUNTURI" +
                "2\nCS116\nDR.GNUTURI" +
                "3\n1\n" +
                "4\n" +
                "5\n"
                ;

        InputStream in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        auth.login(connection);






    }

    @Test
    void loginTest2() throws SQLException, IOException {
        String input="gunturi123\n" +
                "12345678\n" +
//                        "1\nCS805\n0\n" +
                        "2\nCS805\n" +
//                        "3\n2020csb1098\n" +
                        "4\n" +
                        "5\nCS116\n./src/datafeed/grades.csv\n" +
                        "6\n"
                ;

        InputStream in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        //assertEquals("mohit",input);
        auth.login(connection);

    }
    @Test
    void loginTest3() throws SQLException, IOException {
        String input="kartikaySingh\n" +
                "12345678\n" +
//                "1\n1\nCS333\nRAND\n5-1-2-3-3\nCS203\n1\n2020\nCSE\nCIV\n" +
//                "2\nCS333\nRAND\n5-1-2-3-3\nCS203\n1\n2020\nCSE\nCIV\n" +
//                "2\n2020CSB1098\n" +
//                "3\n2020CSB1098\n1" +
//                "4\nCS116\n./src/datafeed/grades.csv\n" +
//                "4\n1\n" +
                "5\n" +
//                "6\n2020CSB1098"+
                "7\n"
                ;

        InputStream in=new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        //assertEquals("mohit",input);
        auth.login(connection);

    }

}