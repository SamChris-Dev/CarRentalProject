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

        loadRentedCars();
        rentalTable.setItems(rentals);

        rentalTable.getSelectionModel().selectedItemProperty().addListener(
                (o, oldV, newV) -> returnBtn.setDisable(newV == null));
    }

    private void loadRentedCars() {

        rentals.clear();

        String sql = "SELECT * FROM rentals";

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
                        null,  // model not needed here
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

    /*private void handleReturn() {

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
    }*/



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
