package server;

import app.carrental.DBConnection;
import dto.RentRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RentalService {

    public static void rentCar(RentRequest req) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {
            // Insert rental
            String rentalSql =
                    "INSERT INTO rentals(client_name, phone, car_id, days) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(rentalSql);
            ps.setString(1, req.getClientName());
            ps.setString(2, req.getPhone());
            ps.setInt(3, req.getCarId());
            ps.setInt(4, req.getDays());
            ps.executeUpdate();

            // Update car status
            String carSql =
                    "UPDATE cars SET status='RENTED' WHERE car_id=?";

            PreparedStatement ps2 = con.prepareStatement(carSql);
            ps2.setInt(1, req.getCarId());
            ps2.executeUpdate();

            con.commit();

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }
}