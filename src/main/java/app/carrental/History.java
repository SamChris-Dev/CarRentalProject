package app.carrental;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import dto.HistoryRecord;
import client.HistoryApiClient;
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

    @FXML
    private void userBackToMainMenuThree(ActionEvent event) throws IOException{
        Main m = new Main();
        m.changeScene("mainmenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        historyIdCol.setCellValueFactory(new PropertyValueFactory<>("historyId"));
        rentalIdCol.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));


        dateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        loadHistoryFromDB();
        historyTable.setItems(records);
    }

    private void loadHistoryFromDB() {
        try {
            records.setAll(HistoryApiClient.fetchHistory());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load history").show();
        }
    }
}