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
}
