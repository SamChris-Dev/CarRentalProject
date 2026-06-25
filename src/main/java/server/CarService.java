package server;

import dto.Car;
import dto.Rental;
import dto.HistoryRecord;
import server.dao.CarDao;
import server.dao.HistoryDao;
import server.dao.RentalDao;

import java.sql.Connection;
import java.util.List;

public class CarService {

    private static final CarDao carDao = new CarDao();
    private static final RentalDao rentalDao = new RentalDao();
    private static final HistoryDao historyDao = new HistoryDao();

    public static List<Car> getAvailableCars() throws Exception {
        return carDao.getAvailableCars();
    }

    public static void rentCar(int carId, String clientName, String phone, int days) throws Exception {
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {
            double pricePerDay = carDao.getPricePerDay(con, carId);
            double totalPrice = pricePerDay * days;

            rentalDao.insertRental(con, carId, clientName, phone, days, totalPrice);
            carDao.updateCarStatus(con, carId, "RENTED");

            con.commit();
        } catch (Exception e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) con.close();
        }
    }

    public static void addCar(Car car) throws Exception {
        carDao.addCar(car);
    }

    public static List<Rental> getRentedCars() throws Exception {
        return rentalDao.getRentedCars();
    }

    public static void returnCar(int rentalId) throws Exception {
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);
        try {
            Rental rental = rentalDao.getRental(con, rentalId);
            if (rental != null) {
                historyDao.insertHistory(
                        con, rentalId, rental.getClientName(), rental.getCarId(),
                        rental.getDays(), rental.getTotalPrice()
                );
                rentalDao.deleteRental(con, rentalId);
                carDao.updateCarStatus(con, rental.getCarId(), "AVAILABLE");

                con.commit();
            }
        } catch(Exception e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) con.close();
        }
    }

    public static List<HistoryRecord> getHistory() throws Exception {
        return historyDao.getHistory();
    }
}