package app.carrental;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


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
    private static final String RENTAL_FILE = "rentals.txt";
    private static final String CAR_FILE = "cars.txt";
    private static final String HISTORY_FILE = "history.txt";


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

        loadRentals();
        rentalTable.setItems(rentals);

        rentalTable.getSelectionModel().selectedItemProperty().addListener(
                (o, oldV, newV) -> returnBtn.setDisable(newV == null));
    }


    private void loadRentals() {

        rentals.clear();

        File file = new File(RENTAL_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");


                if (d.length != 10) continue;

                rentals.add(new Rental(
                        Integer.parseInt(d[0]),
                        d[1], d[2],
                        Integer.parseInt(d[3]),
                        d[4],
                        d[5],
                        Double.parseDouble(d[6]),
                        d[7],
                        Integer.parseInt(d[8]),
                        Double.parseDouble(d[9])


                ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReturn() {

        Rental selected = rentalTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;


        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Return");
        confirm.setHeaderText("Return selected car?");
        confirm.setContentText(
                "Car: " + selected.getCarModel() +
                        "\nClient: " + selected.getClientName()
        );

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        returnBtn.setDisable(true);

        try {

            loadRentals();


            rentals.removeIf(r ->
                    r.getRentalId() == selected.getRentalId()
            );


            saveRentals();


            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter(CAR_FILE, true))) {

                bw.write(
                        selected.getCarId() + "," +
                                selected.getCarModel() + "," +
                                selected.getFuelType() + "," +
                                selected.getPricePerDay() + "," +
                                selected.getAccessories()
                );
                bw.newLine();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) {
                bw.write(
                        selected.getRentalId() + "," +
                                selected.getClientName() + "," +
                                selected.getPhone() + "," +
                                selected.getCarModel() + "," +
                                selected.getDays() + "," +
                                selected.getTotalPrice() + "," +
                                java.time.LocalDateTime.now()
                );
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }


            loadRentals();
            rentalTable.refresh();

            new Alert(Alert.AlertType.INFORMATION,
                    "Car returned successfully.")
                    .showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Failed to return car.")
                    .showAndWait();
        } finally {
            returnBtn.setDisable(true);
        }
    }



    private void saveRentals() {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(RENTAL_FILE))) {

            for (Rental r : rentals) {
                bw.write(
                        r.getRentalId() + "," +
                                r.getClientName() + "," +
                                r.getPhone() + "," +
                                r.getCarId() + "," +
                                r.getCarModel() + "," +
                                r.getFuelType() + "," +
                                r.getPricePerDay() + "," +
                                r.getAccessories() + "," +
                                r.getDays() + "," +
                                r.getTotalPrice()

                );
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
