package app.carrental;

public class Car {

    private int carId;
    private String model;
    private String fuelType;
    private double pricePerDay;
    private String accessories;


    public Car(int carId, String model, String fuelType, double pricePerDay, String accessories) {
        this.carId = carId;
        this.model = model;
        this.fuelType = fuelType;
        this.pricePerDay = pricePerDay;
        this.accessories = accessories;

    }

    public int getCarId() { return carId; }
    public String getModel() { return model; }
    public String getFuelType() { return fuelType; }
    public double getPricePerDay() { return pricePerDay; }
    public String getAccessories() { return accessories; }


}
