package client;

import app.carrental.Car;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CarApiClient {

    private static final String BASE_URL = "http://localhost:8080";

    public static List<Car> fetchAvailableCars() throws Exception {

        URL url = new URL(BASE_URL + "/cars");
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
}
