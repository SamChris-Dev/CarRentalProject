package client;

import com.google.gson.Gson;
import dto.RentRequest;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import dto.Rental;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RentalApiClient {

    public static void rentCar(RentRequest request) throws Exception {
        URL url = new URL(ApiClientConfig.BASE_URL + "/rent");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        String json = new Gson().toJson(request);

        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes());
        }

        if (con.getResponseCode() != 200) {
            throw new RuntimeException("Rent failed on server");
        }
        con.disconnect();
    }

    public static List<Rental> fetchRentedCars() throws Exception {
        URL url = new URL(ApiClientConfig.BASE_URL + "/rentals");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Gson gson = new Gson();
        List<Rental> rentals = gson.fromJson(
                reader,
                new TypeToken<List<Rental>>() {}.getType()
        );
        reader.close();
        return rentals;
    }

    public static void returnCar(int rentalId) throws Exception {
        URL url = new URL(ApiClientConfig.BASE_URL + "/return/" + rentalId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        if (con.getResponseCode() != 200) {
            throw new RuntimeException("Return failed on server");
        }
        con.disconnect();
    }
}