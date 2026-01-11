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

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM cars WHERE model='TEST CAR'");

            assertTrue(rs.next());

            con.createStatement()
                    .executeUpdate("DELETE FROM cars WHERE model='TEST CAR'");
        }


    }

    @Test
    void testRentCar() throws Exception {

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            String insertCarSql = "INSERT INTO cars (car_id, model, fuel_type, price_per_day, accessories, status) " +
                    "VALUES (car_seq.NEXTVAL, ?, ?, ?, ?, 'AVAILABLE')";

            PreparedStatement psCar = con.prepareStatement(insertCarSql);
            psCar.setString(1, "TEST_RENT_UNIT");
            psCar.setString(2, "Electric");
            psCar.setDouble(3, 100.0);
            psCar.setString(4, "None");
            psCar.executeUpdate();

            int carId = 0;
            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT car_id FROM cars WHERE model='TEST_RENT_UNIT'");
            if (rs.next()) {
                carId = rs.getInt("car_id");
            }

            String insertRentalSql = "INSERT INTO rentals (rental_id, car_id, client_name, phone, days, total_price) " +
                    "VALUES (rent_seq.NEXTVAL, ?, ?, ?, ?, ?)";

            PreparedStatement psRent = con.prepareStatement(insertRentalSql);
            psRent.setInt(1, carId);
            psRent.setString(2, "JUnit Tester");
            psRent.setString(3, "555-9999");
            psRent.setInt(4, 5);
            psRent.setDouble(5, 500.0);
            psRent.executeUpdate();


            PreparedStatement psUpdate = con.prepareStatement(
                    "UPDATE cars SET status='RENTED' WHERE car_id=?"
            );
            psUpdate.setInt(1, carId);
            psUpdate.executeUpdate();

            con.commit();


            rs = con.createStatement()
                    .executeQuery("SELECT status FROM cars WHERE car_id=" + carId);
            assertTrue(rs.next());
            assertEquals("RENTED", rs.getString("status"));


            rs = con.createStatement()
                    .executeQuery("SELECT * FROM rentals WHERE car_id=" + carId);
            assertTrue(rs.next());
            assertEquals("JUnit Tester", rs.getString("client_name"));
            assertEquals(500.0, rs.getDouble("total_price"));


            con.createStatement().executeUpdate(
                    "DELETE FROM rentals WHERE car_id=" + carId);
            con.createStatement().executeUpdate(
                    "DELETE FROM cars WHERE car_id=" + carId);
            con.commit();
        }
    }

}
