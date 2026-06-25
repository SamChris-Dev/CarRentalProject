package app.carrental;

import client.CarApiClient;
import client.RentalApiClient;
import dto.Car;
import dto.RentRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AvailableCars implements Initializable {

    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, Integer> idCol;
    @FXML private TableColumn<Car, String> modelCol;
    @FXML private TableColumn<Car, String> fuelCol;
    @FXML private TableColumn<Car, Double> priceCol;
    @FXML private TableColumn<Car, String> accCol;

    // These must be declared to match the fx:id in your FXML
    @FXML private Button rentButton;
    @FXML private Button backToMainMenu;
    @FXML private Button addButton;

    private final ObservableList<Car> cars = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        fuelCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        accCol.setCellValueFactory(new PropertyValueFactory<>("accessories"));

        carTable.setItems(cars);
        loadCarsFromServer();

        carTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> rentButton.setDisable(newVal == null)
        );
    }


    @FXML
    private void userBackToMainMenu(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("mainmenu.fxml");
    }


    @FXML
    private void handleAddCar() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Car");
        dialog.setHeaderText("Enter car details:");


        TextField modelField = new TextField();
        TextField fuelField = new TextField();
        TextField priceField = new TextField();
        TextField accField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Model:"), modelField);
        grid.addRow(1, new Label("Fuel Type:"), fuelField);
        grid.addRow(2, new Label("Price/Day:"), priceField);
        grid.addRow(3, new Label("Accessories:"), accField);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);


        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {

                String model = modelField.getText();
                String fuel = fuelField.getText();
                String acc = accField.getText();
                if(model.isEmpty() || fuel.isEmpty() || priceField.getText().isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
                    return;
                }
                double price = Double.parseDouble(priceField.getText());


                Car newCar = new Car(0, model, fuel, price, acc);


                CarApiClient.addCar(newCar);


                loadCarsFromServer();
                new Alert(Alert.AlertType.INFORMATION, "Car added successfully!").show();

            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Price must be a number!").show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to add car: " + e.getMessage()).show();
            }
        }
    }


    @FXML
    private void handleRent() {
        Car selected = carTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Rent Car");
        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField daysField = new TextField();

        GridPane grid = new GridPane();
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Phone:"), phoneField);
        grid.addRow(2, new Label("Days:"), daysField);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                RentRequest req = new RentRequest();
                req.setCarId(selected.getCarId());
                req.setClientName(nameField.getText());
                req.setPhone(phoneField.getText());
                req.setDays(Integer.parseInt(daysField.getText()));

                RentalApiClient.rentCar(req);

                cars.remove(selected);
                new Alert(Alert.AlertType.INFORMATION, "Car rented successfully!").show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to rent: " + e.getMessage()).show();
            }
        }
    }

    private void loadCarsFromServer() {
        try {
            cars.setAll(CarApiClient.fetchAvailableCars());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Server Connection Failed").show();
        }
    }
}