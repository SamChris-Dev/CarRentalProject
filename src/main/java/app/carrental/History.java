package app.carrental;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.PreparedStatement;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;



public class History implements Initializable {

    @FXML private TableView<HistoryRecord> historyTable;
    @FXML private TableColumn<HistoryRecord, Integer> historyIdCol;
    @FXML private TableColumn<HistoryRecord, Integer> rentalIdCol;
    @FXML private TableColumn<HistoryRecord, String> clientCol;
    @FXML private TableColumn<HistoryRecord, Integer> carIdCol;
    @FXML private TableColumn<HistoryRecord, Integer> daysCol;
    @FXML private TableColumn<HistoryRecord, Double> totalCol;
    @FXML private TableColumn<HistoryRecord, Date> dateCol;
    @FXML private Button backToMainMenuThree;

    private final ObservableList<HistoryRecord> records = FXCollections.observableArrayList();
    private static final String FILE = "history.txt";

    @FXML
    private void userBackToMainMenuThree(ActionEvent event) throws IOException{
        Main m = new Main();
        m.changeScene("mainmenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        historyIdCol.setCellValueFactory(
                new PropertyValueFactory<>("historyId"));
        rentalIdCol.setCellValueFactory(
                new PropertyValueFactory<>("rentalId"));
        clientCol.setCellValueFactory(
                new PropertyValueFactory<>("clientName"));
        carIdCol.setCellValueFactory(
                new PropertyValueFactory<>("carId"));
        daysCol.setCellValueFactory(
                new PropertyValueFactory<>("days"));
        totalCol.setCellValueFactory(
                new PropertyValueFactory<>("totalPrice"));
        dateCol.setCellValueFactory(
                new PropertyValueFactory<>("returnDate"));


        loadHistoryFromDB();
        historyTable.setItems(records);
    }

    private void loadHistoryFromDB() {

        records.clear();

        String sql = "SELECT * FROM rental_history ORDER BY return_date DESC";

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {
                records.add(new HistoryRecord(
                        rs.getInt("history_id"),
                        rs.getInt("rental_id"),
                        rs.getString("client_name"),
                        rs.getInt("car_id"),
                        rs.getInt("days"),
                        rs.getDouble("total_price"),
                        rs.getDate("return_date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void loadHistory() {

        File f = new File(FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length != 7) continue;

                records.add(new HistoryRecord(
                        Integer.parseInt(d[0]),
                        d[1], d[2],
                        d[3],
                        Integer.parseInt(d[4]),
                        Double.parseDouble(d[5]),
                        d[6]
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


}
