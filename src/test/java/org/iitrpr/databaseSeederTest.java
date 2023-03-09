package org.iitrpr;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;


class databaseSeederTest {
    databaseSeeder dbSeeder = new databaseSeeder();
    Connection connection = dbSeeder.connect();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void connect() {


        databaseSeeder dbSeeder = new databaseSeeder();
        Connection connection = dbSeeder.connect();
        // Act


        // Assert
        assertNotNull(connection);
    }

    @Test
    void generateSchema() {
    }

    @Test
    void dataseeder() throws SQLException {
//        dbSeeder.dataseeder(connection);
//
//        Integer count=0;
//        // verify that data was inserted correctly
//        String qery=String.format("select count(*) from students");
//        Statement stmnt=connection.createStatement();
//        ResultSet result=stmnt.executeQuery(qery);
//        while(result.next())
//        {
//            count=result.getInt("count");
//        }
//        assertEquals(55,count);

    }

    @Test
    void userseeder() throws SQLException {

        dbSeeder.userseeder(connection);

        // Assert
        // Check that the expected number of users were inserted into the database
        int expectedNumUsers = 4; // Assuming the users.csv file has 5 rows of data
        int actualNumUsers = countUsers(connection);
        assertEquals(expectedNumUsers, actualNumUsers);

    }
    private int countUsers(Connection connection) throws SQLException {
        int count = 0;
        PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        return count;
    }

    @Test
    void main() {
    }
}