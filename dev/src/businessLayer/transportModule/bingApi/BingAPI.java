package businessLayer.transportModule.bingApi;

import javafx.util.Pair;
import utils.JsonUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.Map;
import java.util.Scanner;

public class BingAPI {
    
    private static final String key = "Ap_rzOWHxjXVKTzD5kXxfWSj_9LN2Vpr7LSFWv53tgAbvU9iBWl4SqqhUaASGTUE";
    private static final String COUNTRY_SUFFIX = ", United States";

    public static LocationByQueryResponse locationByQuery(String address) throws IOException{

        String urlPrefix = "http://dev.virtualearth.net/REST/v1/Locations?q=";
        String urlSuffix = "&key=" + key;
        address = address.concat(COUNTRY_SUFFIX).replace(" ", "%20");

        String json = sendRequest(urlPrefix + address + urlSuffix);
        return JsonUtils.deserialize(json, LocationByQueryResponse.class);
    }

    public static DistanceMatrixResponse distanceMatrix(Map<Point,Point> map) throws IOException{
        String urlPrefix = "http://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?";
        String urlSuffix = "&travelMode=driving&key=" + key;
        StringBuilder originsString = new StringBuilder("origins=");
        StringBuilder destinationsString = new StringBuilder("&destinations=");
        for(var pair : map.entrySet()){
            originsString.append(pair.getKey().latitude()).append(",").append(pair.getKey().longitude()).append(";");
            destinationsString.append(pair.getValue().latitude()).append(",").append(pair.getValue().longitude()).append(";");
        }

        String url = urlPrefix + originsString + destinationsString + urlSuffix;
        String json = sendRequest(url);
        return JsonUtils.deserialize(json, DistanceMatrixResponse.class);
    }

    private static String sendRequest(String _url) throws IOException {
        URL url = new URL(_url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        //Check if connect is made
        int responseCode = conn.getResponseCode();

        StringBuilder informationString = new StringBuilder();
        // 200 OK
        if (responseCode != 200) {
            throw new IOException("HttpResponseCode: " + responseCode);
        }
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            informationString.append(scanner.nextLine());
        }
        //Close the scanner
        scanner.close();

        return informationString.toString();
    }
}
