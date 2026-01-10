package app.carrental;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;

//import java.sql.PreparedStatement;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.Optional;

import client.CarApiClient;
import server.CarRentalServer;

public class AvailableCars implements Initializable {

    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, Integer> idCol;
    @FXML private TableColumn<Car, String> modelCol;
    @FXML private TableColumn<Car, String> fuelCol;
    @FXML private TableColumn<Car, Double> priceCol;
    @FXML private TableColumn<Car, String> accCol;
    @FXML private Button backToMainMenu;
    @FXML private Button rentButton;
    @FXML private Button addButton;

    private final ObservableList<Car> cars = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        fuelCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        accCol.setCellValueFactory(new PropertyValueFactory<>("accessories"));

        //loadCarsFromDB();
        loadCarsFromServer();

        carTable.setItems(cars);

        carTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> rentButton.setDisable(newVal == null)
        );
    }

    @FXML
    private void handleRent() {
        Car selected = carTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Rent Car");

        TextField name = new TextField();
        TextField phone = new TextField();
        TextField days = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Client Name:"), name);
        grid.addRow(1, new Label("Phone:"), phone);
        grid.addRow(2, new Label("Days:"), days);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);



        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;


        if (name.getText().isEmpty() ||
                phone.getText().isEmpty() ||
                !days.getText().matches("\\d+")) {

            new Alert(Alert.AlertType.ERROR,
                    "Please enter valid rental information.")
                    .showAndWait();
            return;
        }

        int d = Integer.parseInt(days.getText());
        double total = d * selected.getPricePerDay();











       /* try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            //  Insert into rentals
            PreparedStatement ps1 = con.prepareStatement(
                    "INSERT INTO rentals VALUES (rental_seq.NEXTVAL,?,?,?,?,?,SYSDATE)"
            );
            ps1.setString(1, name.getText());
            ps1.setString(2, phone.getText());
            ps1.setInt(3, selected.getCarId());
            ps1.setInt(4, d);
            ps1.setDouble(5, total);

            //  Update car status
            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE cars SET status = 'RENTED' WHERE car_id = ?"
            );
            ps2.setInt(1, selected.getCarId());

            ps1.executeUpdate();
            ps2.executeUpdate();

            con.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        cars.remove(selected);

    }



   /* private void loadCarsFromDB() {

        cars.clear();

        String sql = "SELECT * FROM cars WHERE status = 'AVAILABLE'";

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {

            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getString("fuel_type"),
                        rs.getDouble("price_per_day"),
                        rs.getString("accessories")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void loadCarsFromServer() {

        cars.clear();

        try {
            cars.addAll(CarApiClient.fetchAvailableCars());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Cannot connect to server").show();
        }
    }



    @FXML
    private void userBackToMainMenu(ActionEvent event) throws IOException{
        Main m = new Main();
        m.changeScene("mainmenu.fxml");
    }

    @FXML
    private void handleAddCar() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Car");

        TextField model = new TextField();
        TextField fuel = new TextField();
        TextField price = new TextField();
        TextField accessories = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Model:"), model);
        grid.addRow(1, new Label("Fuel Type:"), fuel);
        grid.addRow(2, new Label("Price per day:"), price);
        grid.addRow(3, new Label("Accessories:"), accessories);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        if (dialog.showAndWait().get() != ButtonType.OK) return;

        if (!price.getText().matches("\\d+(\\.\\d+)?")) {
            new Alert(Alert.AlertType.ERROR, "Invalid price").show();
            return;
        }

       /* try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO cars VALUES (car_seq.NEXTVAL,?,?,?,?, 'AVAILABLE')"
            );

            ps.setString(1, model.getText());
            ps.setString(2, fuel.getText());
            ps.setDouble(3, Double.parseDouble(price.getText()));
            ps.setString(4, accessories.getText());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadCarsFromDB();*/
    }



}
