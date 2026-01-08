package app.carrental;

public class HistoryRecord {

    private int rentalId;
    private String clientName;
    private String phone;
    private String carModel;
    private int days;
    private double totalPrice;
    private String returnedAt;

    public HistoryRecord(int rentalId, String clientName, String phone,
                         String carModel, int days,
                         double totalPrice, String returnedAt) {
        this.rentalId = rentalId;
        this.clientName = clientName;
        this.phone = phone;
        this.carModel = carModel;
        this.days = days;
        this.totalPrice = totalPrice;
        this.returnedAt = returnedAt;
    }

    public int getRentalId() { return rentalId; }
    public String getClientName() { return clientName; }
    public String getCarModel() { return carModel; }
    public int getDays() { return days; }
    public double getTotalPrice() { return totalPrice; }
    public String getReturnedAt() { return returnedAt; }
}
