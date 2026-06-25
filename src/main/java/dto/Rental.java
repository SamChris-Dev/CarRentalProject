package dto;

public class Rental {
    private int rentalId;
    private String clientName;
    private String phone;
    private int carId;
    private String carModel;
    private String fuelType;
    private double pricePerDay;
    private String accessories;
    private int days;
    private double totalPrice;

    public Rental(int rentalId, String clientName, String phone,
                  int carId, String carModel,
                  String fuelType, double pricePerDay,
                  String accessories, int days, double totalPrice) {

        this.rentalId = rentalId;
        this.clientName = clientName;
        this.phone = phone;
        this.carId = carId;
        this.carModel = carModel;
        this.fuelType = fuelType;
        this.pricePerDay = pricePerDay;
        this.accessories = accessories;
        this.days = days;
        this.totalPrice = totalPrice;
    }

    public int getRentalId() { return rentalId; }
    public String getClientName() { return clientName; }
    public String getPhone() { return phone; }
    public int getCarId() { return carId; }
    public String getCarModel() { return carModel; }
    public String getFuelType() { return fuelType; }
    public double getPricePerDay() { return pricePerDay; }
    public String getAccessories() { return accessories; }
    public int getDays() { return days; }
    public double getTotalPrice() { return totalPrice; }

}

