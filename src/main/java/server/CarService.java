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

    public static void rentCar(
            int carId,
            String customerName,
            int days
    ) throws Exception {

        Connection con = DBConnection.getConnection();

        // 1. Insert rental
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO rentals (rental_id, car_id, customer_name, days) " +
                        "VALUES (rent_seq.NEXTVAL, ?, ?, ?)"
        );
        ps.setInt(1, carId);
        ps.setString(2, customerName);
        ps.setInt(3, days);
        ps.executeUpdate();

        // 2. Update car status
        PreparedStatement ps2 = con.prepareStatement(
                "UPDATE cars SET status='RENTED' WHERE car_id=?"
        );
        ps2.setInt(1, carId);
        ps2.executeUpdate();

        // 3. Insert history
        PreparedStatement ps3 = con.prepareStatement(
                "INSERT INTO rental_history " +
                        "(history_id, car_id, customer_name, days, action_date) " +
                        "VALUES (history_seq.NEXTVAL, ?, ?, ?, SYSDATE)"
        );
        ps3.setInt(1, carId);
        ps3.setString(2, customerName);
        ps3.setInt(3, days);
        ps3.executeUpdate();

        con.close();
    }

}
