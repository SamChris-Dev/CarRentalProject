package app.carrental;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.PreparedStatement;



import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RentedCars implements Initializable {

    @FXML private TableView<Rental> rentalTable;
    @FXML private TableColumn<Rental, Integer> idCol;
    @FXML private TableColumn<Rental, String> nameCol;
    @FXML private TableColumn<Rental, String> carCol;
    @FXML private TableColumn<Rental, Integer> daysCol;
    @FXML private TableColumn<Rental, Double> priceCol;
    @FXML private Button returnBtn;
    @FXML private Button backToMainMenuTwo;

    private final ObservableList<Rental> rentals = FXCollections.observableArrayList();



    @FXML
    private void userBackToMainMenuTwo(ActionEvent event) throws IOException{
        Main m = new Main();
        m.changeScene("mainmenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        idCol.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        carCol.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadRentedCars();
        rentalTable.setItems(rentals);

        rentalTable.getSelectionModel().selectedItemProperty().addListener(
                (o, oldV, newV) -> returnBtn.setDisable(newV == null));
    }

    private void loadRentedCars() {

        rentals.clear();

        String sql = "SELECT r.rental_id,\n" +
                "       r.client_name,\n" +
                "       r.phone,\n" +
                "       r.car_id,\n" +
                "       c.model,\n" +
                "       r.days,\n" +
                "       r.total_price\n" +
                "FROM rentals r\n" +
                "JOIN cars c ON r.car_id = c.car_id\n";

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {
                rentals.add(new Rental(
                        rs.getInt("rental_id"),
                        rs.getString("client_name"),
                        rs.getString("phone"),
                        rs.getInt("car_id"),
                        rs.getString("Model"),
                        null,
                        0,
                        null,
                        rs.getInt("days"),
                        rs.getDouble("total_price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @FXML
    private void handleReturn() {
        Rental r = rentalTable.getSelectionModel().getSelectedItem();
        if (r == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Return this car?");
        if (confirm.showAndWait().get() != ButtonType.OK) return;

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            // 1. Insert into history
            PreparedStatement ps1 = con.prepareStatement(
                    "INSERT INTO rental_history VALUES (history_seq.NEXTVAL,?,?,?,?,?,SYSDATE)"
            );
            ps1.setInt(1, r.getRentalId());
            ps1.setString(2, r.getClientName());
            ps1.setInt(3, r.getCarId());
            ps1.setInt(4, r.getDays());
            ps1.setDouble(5, r.getTotalPrice());

            // 2. Delete rental
            PreparedStatement ps2 = con.prepareStatement(
                    "DELETE FROM rentals WHERE rental_id = ?"
            );
            ps2.setInt(1, r.getRentalId());

            // 3. Update car status
            PreparedStatement ps3 = con.prepareStatement(
                    "UPDATE cars SET status = 'AVAILABLE' WHERE car_id = ?"
            );
            ps3.setInt(1, r.getCarId());

            ps1.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();

            con.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
