package server;

import static spark.Spark.*;
import com.google.gson.Gson;

public class CarRentalServer {

    public static void main(String[] args) {

        port(8080);

        get("/cars", (req, res) -> {
            try {
                res.type("application/json");
                return new Gson().toJson(CarService.getAvailableCars());
            } catch (Exception e) {
                e.printStackTrace(); // This will print the real error in your IntelliJ console
                res.status(500);
                return "Internal Error: " + e.getMessage();
            }
        });
    }
}
