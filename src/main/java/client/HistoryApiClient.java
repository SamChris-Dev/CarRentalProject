package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.HistoryRecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HistoryApiClient {

    public static List<HistoryRecord> fetchHistory() throws Exception {
        URL url = new URL(ApiClientConfig.BASE_URL + "/history");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Gson gson = new Gson();
        List<HistoryRecord> records = gson.fromJson(
                reader,
                new TypeToken<List<HistoryRecord>>() {}.getType()
        );
        reader.close();
        return records;
    }
}
