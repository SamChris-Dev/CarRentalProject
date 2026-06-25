package server.dao;

import dto.Rental;
import server.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RentalDao {

    public List<Rental> getRentedCars() throws Exception {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT r.rental_id, r.client_name, r.phone, r.car_id, c.model, r.days, r.total_price " +
                     "FROM rentals r JOIN cars c ON r.car_id = c.car_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rentals.add(new Rental(
                        rs.getInt("rental_id"),
                        rs.getString("client_name"),
                        rs.getString("phone"),
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        null, 0, null,
                        rs.getInt("days"),
                        rs.getDouble("total_price")
                ));
            }
        }
        return rentals;
    }

    public void insertRental(Connection con, int carId, String clientName, String phone, int days, double totalPrice) throws Exception {
        String sql = "INSERT INTO rentals (rental_id, car_id, client_name, phone, days, total_price) " +
                     "VALUES (rent_seq.NEXTVAL, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setString(2, clientName);
            ps.setString(3, phone);
            ps.setInt(4, days);
            ps.setDouble(5, totalPrice);
            ps.executeUpdate();
        }
    }

    public Rental getRental(Connection con, int rentalId) throws Exception {
        String sql = "SELECT client_name, car_id, days, total_price FROM rentals WHERE rental_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rentalId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new Rental(
                            rentalId,
                            rs.getString("client_name"),
                            null, // phone not needed for return transfer
                            rs.getInt("car_id"),
                            null, null, 0, null,
                            rs.getInt("days"),
                            rs.getDouble("total_price")
                    );
                }
            }
        }
        return null;
    }

    public void deleteRental(Connection con, int rentalId) throws Exception {
        String sql = "DELETE FROM rentals WHERE rental_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rentalId);
            ps.executeUpdate();
        }
    }
}
