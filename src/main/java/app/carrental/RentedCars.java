package app.carrental;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import dto.Rental;
import client.RentalApiClient;
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
        try {
            rentals.setAll(RentalApiClient.fetchRentedCars());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load rentals").show();
        }
    }





    @FXML
    private void handleReturn() {
        Rental r = rentalTable.getSelectionModel().getSelectedItem();
        if (r == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Return this car?");
        if (confirm.showAndWait().get() != ButtonType.OK) return;

        try {
            RentalApiClient.returnCar(r.getRentalId());
            rentals.remove(r);
            new Alert(Alert.AlertType.INFORMATION, "Car returned successfully!").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to return car").show();
        }
    }




}
