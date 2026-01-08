package app.carrental;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stg;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stg = primaryStage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Car Rental System");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));

        double newWidth = pane.prefWidth(-1);
        double newHeight = pane.prefHeight(-1);

        stg.getScene().setRoot(pane);
        stg.setWidth(newWidth);
        stg.setHeight(newHeight);
        stg.sizeToScene();
    }




    public static void main(String[] args) {
        launch(args);
    }
}