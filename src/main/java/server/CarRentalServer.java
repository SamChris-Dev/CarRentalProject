package server;

import static spark.Spark.*;
import com.google.gson.Gson;
import dto.RentRequest;
import dto.Car;

public class CarRentalServer {

    public static void main(String[] args) {

        port(8080);

        get("/cars", (req, res) -> {
            try {
                res.type("application/json");
                return new Gson().toJson(CarService.getAvailableCars());
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Internal Error: " + e.getMessage();
            }
        });

        post("/rent", (req, res) -> {
            try {
                Gson gson = new Gson();
                RentRequest rentRequest = gson.fromJson(req.body(), RentRequest.class);

                CarService.rentCar(
                        rentRequest.getCarId(),
                        rentRequest.getClientName(),
                        rentRequest.getPhone(),
                        rentRequest.getDays()
                );

                res.status(200);
                return "Car rented successfully";

            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Rent Failed: " + e.getMessage();
            }
        });


        post("/addcar", (req, res) -> {
            try {
                Gson gson = new Gson();

                Car newCar = gson.fromJson(req.body(), Car.class);

                CarService.addCar(newCar);

                res.status(200);
                return "Car added successfully";
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Error adding car: " + e.getMessage();
            }
        });

        get("/rentals", (req, res) -> {
            try {
                res.type("application/json");
                return new Gson().toJson(CarService.getRentedCars());
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Internal Error: " + e.getMessage();
            }
        });

        post("/return/:rentalId", (req, res) -> {
            try {
                int rentalId = Integer.parseInt(req.params(":rentalId"));
                CarService.returnCar(rentalId);
                res.status(200);
                return "Car returned successfully";
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Error returning car: " + e.getMessage();
            }
        });

        get("/history", (req, res) -> {
            try {
                res.type("application/json");
                return new Gson().toJson(CarService.getHistory());
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "Internal Error: " + e.getMessage();
            }
        });
    }
}