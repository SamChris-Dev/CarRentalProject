package app.carrental;

public class TestDB {
    public static void main(String[] args) {
        try {
            DBConnection.getConnection();
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
