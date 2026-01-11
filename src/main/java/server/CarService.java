package server;

import app.carrental.DBConnection;
import app.carrental.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarService {

    public static List<Car> getAvailableCars() throws Exception {

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



    public static void rentCar(int carId, String clientName, String phone, int days) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {
            double pricePerDay = 0.0;
            String priceSql = "SELECT price_per_day FROM cars WHERE car_id = ?";
            try (PreparedStatement psPrice = con.prepareStatement(priceSql)) {
                psPrice.setInt(1, carId);
                try (ResultSet rs = psPrice.executeQuery()) {
                    if (rs.next()) {
                        pricePerDay = rs.getDouble("price_per_day");
                    }
                }
            }

            double totalPrice = pricePerDay * days;

            String insertRentalSql = "INSERT INTO rentals (rental_id, car_id, client_name, phone, days, total_price) " +
                    "VALUES (rent_seq.NEXTVAL, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(insertRentalSql);
            ps.setInt(1, carId);
            ps.setString(2, clientName);
            ps.setString(3, phone);
            ps.setInt(4, days);
            ps.setDouble(5, totalPrice);
            ps.executeUpdate();
            ps.close();

            String updateCarSql = "UPDATE cars SET status='RENTED' WHERE car_id=?";
            PreparedStatement ps2 = con.prepareStatement(updateCarSql);
            ps2.setInt(1, carId);
            ps2.executeUpdate();
            ps2.close();


            con.commit();

        } catch (Exception e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) con.close();
        }
    }


    public static void addCar(Car car) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO cars (car_id, model, fuel_type, price_per_day, accessories, status) " +
                "VALUES (car_seq.NEXTVAL, ?, ?, ?, ?, 'AVAILABLE')";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, car.getModel());
            ps.setString(2, car.getFuelType());
            ps.setDouble(3, car.getPricePerDay());
            ps.setString(4, car.getAccessories());
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }
}