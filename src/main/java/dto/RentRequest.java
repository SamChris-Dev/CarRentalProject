package dto;

public class RentRequest {

    private int carId;
    private String clientName;
    private String phone;
    private int days;

    public int getCarId() {
        return carId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getPhone() {
        return phone;
    }

    public int getDays() {
        return days;
    }
}