package org.iitrpr;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class academicSystemTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void main() throws SQLException, IOException {
        databaseSeeder db = new databaseSeeder();
        academicSystem acsys = new academicSystem();

        // Act
        Connection connection = db.connect();
        authentication auth = new authentication();
        auth.login(connection);

        // Assert
        assertNotNull(connection);
    }
}