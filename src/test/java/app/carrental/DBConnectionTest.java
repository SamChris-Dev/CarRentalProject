package app.carrental;


import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {

    @Test
    void testDatabaseConnection() {
        try (Connection con = DBConnection.getConnection()) {
            assertNotNull(con);
            assertFalse(con.isClosed());
        } catch (Exception e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }
}
