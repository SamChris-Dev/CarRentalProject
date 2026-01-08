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

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.Optional;

public class AvailableCars implements Initializable {

    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, Integer> idCol;
    @FXML private TableColumn<Car, String> modelCol;
    @FXML private TableColumn<Car, String> fuelCol;
    @FXML private TableColumn<Car, Double> priceCol;
    @FXML private TableColumn<Car, String> accCol;
    @FXML private Button backToMainMenu;
    @FXML private Button rentButton;

    private final ObservableList<Car> cars = FXCollections.observableArrayList();
    private static final String FILE = "cars.txt";
    private static final String RENTAL_FILE = "rentals.txt";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        fuelCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        accCol.setCellValueFactory(new PropertyValueFactory<>("accessories"));

        loadCars();
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
        int rentalId = generateRentalId();


        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(RENTAL_FILE, true))) {

            bw.write(
                    rentalId + "," +
                            name.getText().trim() + "," +
                            phone.getText().trim() + "," +
                            selected.getCarId() + "," +
                            selected.getModel() + "," +
                            selected.getFuelType() + "," +
                            selected.getPricePerDay() + "," +
                            selected.getAccessories() + "," +
                            d + "," +
                            total
            );
            bw.newLine();

        } catch (IOException e) { e.printStackTrace(); }

        cars.remove(selected);
        saveCars();
    }

    private void loadCars() {
        cars.clear();
        File file = new File(FILE);
        if (!file.exists()) createSampleData();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length != 5) continue;
                cars.add(new Car(
                        Integer.parseInt(d[0]),
                        d[1],
                        d[2],
                        Double.parseDouble(d[3]),
                        d[4]
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCars() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Car c : cars) {
                bw.write(c.getCarId() + "," + c.getModel() + "," +
                        c.getFuelType() + "," + c.getPricePerDay() + "," +
                        c.getAccessories());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSampleData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            bw.write("1,Toyota Corolla,Petrol,45.0,AC|Bluetooth");
            bw.newLine();
            bw.write("2,Honda Civic,Diesel,55.0,GPS|AC");
            bw.newLine();
            bw.write("3,Tesla Model 3,Electric,90.0,Autopilot|AC");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int generateRentalId() {

        File file = new File(RENTAL_FILE);


        if (!file.exists() || file.length() == 0) {
            return 1;
        }

        int maxId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {


                if (line.trim().isEmpty()) continue;

                String[] d = line.split(",");


                if (d.length != 10) continue;

                try {
                    int id = Integer.parseInt(d[0].trim());
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException ignored) {

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxId + 1;
    }



    @FXML
    private void userBackToMainMenu(ActionEvent event) throws IOException{
        Main m = new Main();
        m.changeScene("mainmenu.fxml");
    }




}
