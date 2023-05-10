package businessLayer.transportModule.bingApi;

import businessLayer.transportModule.SitesController;
import javafx.util.Pair;
import utils.JsonUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BingAPI {

    public static long counter = 0;
    
    private static final String key = "ApCimQpMhBbPbxxutJUbXvOeQZ7zrJh-ryeMWVyI7a0iyZ_aDIJdxqSUlED1D7l-";
    private static final String COUNTRY_SUFFIX = ", United States";

    public BingAPI() {
    }

    public LocationByQueryResponse locationByQuery(String address) throws IOException{

        String urlPrefix = "http://dev.virtualearth.net/REST/v1/Locations?q=";
        String urlSuffix = "&key=" + key;
        address = address.concat(COUNTRY_SUFFIX).replace(" ", "%20");

        String json = sendRequest(urlPrefix + address + urlSuffix);
        return JsonUtils.deserialize(json, LocationByQueryResponse.class);
    }

    public void calculateRoute(List<Point> route){


    }

    @Deprecated
    public DistanceMatrixResponse distanceMatrix(List<Pair<Point,Point>> list) throws IOException{
        String urlPrefix = "http://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?";
        String urlSuffix = "&travelMode=driving&key=" + key;
        StringBuilder originsString = new StringBuilder("origins=");
        StringBuilder destinationsString = new StringBuilder("&destinations=");
        for(var pair : list){
            originsString.append(pair.getKey().latitude()).append(",").append(pair.getKey().longitude()).append(";");
            destinationsString.append(pair.getValue().latitude()).append(",").append(pair.getValue().longitude()).append(";");
        }
        originsString.deleteCharAt(originsString.length()-1);
        destinationsString.deleteCharAt(destinationsString.length()-1);

        String url = urlPrefix + originsString + destinationsString + urlSuffix;
        String json = sendRequest(url);
        return JsonUtils.deserialize(json, DistanceMatrixResponse.class);
    }

    private String sendRequest(String _url) throws IOException {
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

        counter++;

        return informationString.toString();

//        return "";
    }
}
