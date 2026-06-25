package server.dao;

import dto.Car;
import server.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDao {

    public List<Car> getAvailableCars() throws Exception {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars WHERE status='AVAILABLE'";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getString("fuel_type"),
                        rs.getDouble("price_per_day"),
                        rs.getString("accessories")
                ));
            }
        }
        return cars;
    }

    public void addCar(Car car) throws Exception {
        String sql = "INSERT INTO cars (car_id, model, fuel_type, price_per_day, accessories, status) " +
                "VALUES (car_seq.NEXTVAL, ?, ?, ?, ?, 'AVAILABLE')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, car.getModel());
            ps.setString(2, car.getFuelType());
            ps.setDouble(3, car.getPricePerDay());
            ps.setString(4, car.getAccessories());
            ps.executeUpdate();
        }
    }

    public double getPricePerDay(Connection con, int carId) throws Exception {
        String sql = "SELECT price_per_day FROM cars WHERE car_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("price_per_day");
                }
            }
        }
        return 0.0;
    }

    public void updateCarStatus(Connection con, int carId, String status) throws Exception {
        String sql = "UPDATE cars SET status=? WHERE car_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, carId);
            ps.executeUpdate();
        }
    }
}
