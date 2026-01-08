package app.carrental;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;



public class History implements Initializable {

    @FXML private TableView<HistoryRecord> historyTable;
    @FXML private TableColumn<HistoryRecord, Integer> idCol;
    @FXML private TableColumn<HistoryRecord, String> nameCol;
    @FXML private TableColumn<HistoryRecord, String> carCol;
    @FXML private TableColumn<HistoryRecord, Integer> daysCol;
    @FXML private TableColumn<HistoryRecord, Double> priceCol;
    @FXML private TableColumn<HistoryRecord, String> dateCol;
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

        idCol.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        carCol.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("returnedAt"));

        loadHistory();
        historyTable.setItems(records);
    }

    private void loadHistory() {

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
    }


}
