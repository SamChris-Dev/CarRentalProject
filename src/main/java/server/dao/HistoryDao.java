package server.dao;

import dto.HistoryRecord;
import server.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HistoryDao {

    public List<HistoryRecord> getHistory() throws Exception {
        List<HistoryRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM rental_history ORDER BY action_date DESC";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                records.add(new HistoryRecord(
                        rs.getInt("history_id"),
                        rs.getInt("rental_id"),
                        rs.getString("client_name"),
                        rs.getInt("car_id"),
                        rs.getInt("days"),
                        rs.getDouble("total_price"),
                        rs.getDate("action_date")
                ));
            }
        }
        return records;
    }

    public void insertHistory(Connection con, int rentalId, String clientName, int carId, int days, double totalPrice) throws Exception {
        String sql = "INSERT INTO rental_history (history_id, rental_id, client_name, car_id, days, total_price, action_date) " +
                     "VALUES (history_seq.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rentalId);
            ps.setString(2, clientName);
            ps.setInt(3, carId);
            ps.setInt(4, days);
            ps.setDouble(5, totalPrice);
            ps.executeUpdate();
        }
    }
}
