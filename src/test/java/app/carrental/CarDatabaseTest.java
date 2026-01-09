package app.carrental;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;


public class CarDatabaseTest {

    @Test
    void testInsertCar() throws Exception {

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO cars VALUES (car_seq.NEXTVAL,?,?,?,?, 'AVAILABLE')"
            );

            ps.setString(1, "TEST CAR");
            ps.setString(2, "Petrol");
            ps.setDouble(3, 99);
            ps.setString(4, "TEST");

            int rows = ps.executeUpdate();
            assertEquals(1, rows);

            // Verify it exists
            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM cars WHERE model='TEST CAR'");

            assertTrue(rs.next());

            //clean-up
            con.createStatement()
                    .executeUpdate("DELETE FROM cars WHERE model='TEST CAR'");
            //con.commit();
        }


    }

    @Test
    void testRentCar() throws Exception {

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            // Insert test car
            con.createStatement().executeUpdate(
                    "INSERT INTO cars VALUES (car_seq.NEXTVAL,'RENT TEST','Petrol',50,'NONE','AVAILABLE')"
            );

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT car_id FROM cars WHERE model='RENT TEST'");
            rs.next();
            int carId = rs.getInt(1);

            // Rent it
            PreparedStatement ps1 = con.prepareStatement(
                    "INSERT INTO rentals VALUES (rental_seq.NEXTVAL,'JUnit','000',?,?,?,SYSDATE)"
            );
            ps1.setInt(1, carId);
            ps1.setInt(2, 2);
            ps1.setDouble(3, 100);

            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE cars SET status='RENTED' WHERE car_id=?"
            );
            ps2.setInt(1, carId);

            ps1.executeUpdate();
            ps2.executeUpdate();
            con.commit();

            // Verify status
            rs = con.createStatement()
                    .executeQuery("SELECT status FROM cars WHERE car_id=" + carId);
            rs.next();
            assertEquals("RENTED", rs.getString(1));

            // Cleanup
            con.createStatement().executeUpdate(
                    "DELETE FROM rentals WHERE car_id=" + carId);
            con.createStatement().executeUpdate(
                    "DELETE FROM cars WHERE car_id=" + carId);
            con.commit();
        }
    }

}
