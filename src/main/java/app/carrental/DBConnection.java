package app.carrental;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521";
    private static final String USER = "C##car_rental";
    private static final String PASSWORD = "car123";

    public static Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
