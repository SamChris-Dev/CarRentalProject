package dto;

public class RentRequest {
    private int carId;
    private String clientName;
    private String phone;
    private int days;

    public void setCarId(int carId) { this.carId = carId; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDays(int days) { this.days = days; }

    public int getCarId() { return carId; }
    public String getClientName() { return clientName; }
    public String getPhone() { return phone; }
    public int getDays() { return days; }
}