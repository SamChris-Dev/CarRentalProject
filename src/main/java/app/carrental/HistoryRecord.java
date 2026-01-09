package app.carrental;

import java.util.Date;

public class HistoryRecord {

    private int historyId;
    private int rentalId;
    private String clientName;
    private int carId;
    private int days;
    private double totalPrice;
    private Date returnDate;

    public HistoryRecord(int historyId, int rentalId, String clientName,
                         int carId, int days, double totalPrice, Date returnDate) {
        this.historyId = historyId;
        this.rentalId = rentalId;
        this.clientName = clientName;
        this.carId = carId;
        this.days = days;
        this.totalPrice = totalPrice;
        this.returnDate = returnDate;
    }

    public int getHistoryId() { return historyId; }
    public int getRentalId() { return rentalId; }
    public String getClientName() { return clientName; }
    public int getCarId() { return carId; }
    public int getDays() { return days; }
    public double getTotalPrice() { return totalPrice; }
    public Date getReturnDate() { return returnDate; }

}
