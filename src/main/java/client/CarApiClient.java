package client;

import dto.Car;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import java.io.OutputStream;


public class CarApiClient {

    public static List<Car> fetchAvailableCars() throws Exception {

        URL url = new URL(ApiClientConfig.BASE_URL + "/cars");
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );

        Gson gson = new Gson();
        List<Car> cars = gson.fromJson(
                reader,
                new TypeToken<List<Car>>() {}.getType()
        );

        reader.close();
        return cars;
    }


    public static void addCar(Car car) throws Exception {
        URL url = new URL(ApiClientConfig.BASE_URL + "/addcar");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String json = new Gson().toJson(car);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.getBytes());
        }

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed to add car on server");
        }
        connection.disconnect();
    }

}
