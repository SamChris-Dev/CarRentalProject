package client;

import com.google.gson.Gson;
import dto.RentRequest;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RentalApiClient {

    private static final String BASE_URL =
            "http://localhost:8080";

    public static void rentCar(RentRequest req) throws Exception {

        URL url = new URL(BASE_URL + "/rent");
        HttpURLConnection con =
                (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        String json = new Gson().toJson(req);

        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes());
        }

        if (con.getResponseCode() != 200) {
            throw new RuntimeException("Rent failed");
        }
    }
}