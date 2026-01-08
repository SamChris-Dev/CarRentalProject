package app.carrental;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;


public class MainMenu {
    @FXML
    private Button logout;
    @FXML
    private Button availableCars;
    @FXML
    private Button rentedCars;
    @FXML
    private Button historyBtn;


    public void userAvailableCars(ActionEvent event) throws IOException{
        Main m = new Main();
        m.changeScene("availableCars.fxml");

    }

    public void userHistory(ActionEvent event) throws IOException{
        Main m = new Main();
        m.changeScene("history.fxml");

    }

    public void userRentedCars(ActionEvent event) throws IOException{
        Main m = new Main();
        m.changeScene("rentedCars.fxml");
    }


    public void userLogOut(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("login.fxml");

    }

}
