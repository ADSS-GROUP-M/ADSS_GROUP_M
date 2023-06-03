package businessLayer.transportModule.bingApi;

import com.sun.tools.javac.Main;
import exceptions.TransportException;
import utils.FileUtils;
import utils.JsonUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class BingAPI {
    private static final String BING_API_KEY_FILE_NAME = "BingAPI_Key.txt";
    private final String key;

    public BingAPI() {
        String _key;
        try {
            _key = FileUtils.getResource(BING_API_KEY_FILE_NAME);
        } catch (IOException e) {
            System.out.printf("Bing API key not found, please make sure that the file %s exists in the jar root directory%n", BING_API_KEY_FILE_NAME);
            _key = "";
        }
        key = _key;
    }

    public LocationByQueryResponse locationByQuery(String address) throws TransportException {

        String urlPrefix = "http://dev.virtualearth.net/REST/v1/Locations?q=";
        String urlSuffix = "&key=" + key;
        address = address.replace(" ", "%20");

        String json = sendRequest(urlPrefix + address + urlSuffix);
        return JsonUtils.deserialize(json, LocationByQueryResponse.class);
    }

    public DistanceMatrixResponse distanceMatrix(List<Point> list) throws TransportException {
        String urlPrefix = "http://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?";
        String urlSuffix = "&travelMode=driving&key=" + key;
        StringBuilder originsString = new StringBuilder("origins=");
        StringBuilder destinationsString = new StringBuilder("&destinations=");

        // formatted as: origins=lat1,long1;lat2,long2;...&destinations=lat1,long1;lat2,long2;...
        for(var point : list){
            originsString.append(point.latitude()).append(",").append(point.longitude()).append(";");
            destinationsString.append(point.latitude()).append(",").append(point.longitude()).append(";");
        }

        // remove last ';' from each string
        originsString.deleteCharAt(originsString.length()-1);
        destinationsString.deleteCharAt(destinationsString.length()-1);

        String url = urlPrefix + originsString + destinationsString + urlSuffix;
        String json = sendRequest(url);
        return JsonUtils.deserialize(json, DistanceMatrixResponse.class);
    }

    private String sendRequest(String _url) throws TransportException {

        HttpURLConnection conn = null;

        try{
            URL url = new URL(_url);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();

            StringBuilder informationString = new StringBuilder();
            // 200 OK
            if (responseCode != 200) {
                throw new TransportException("Connection to Bing Maps API failed with error code "+responseCode);
            }
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            //Close the scanner
            scanner.close();

            return informationString.toString();
        } catch (IOException e) {
            StringBuilder message = new StringBuilder();
            try {
                message.append("Connection to Bing Maps API failed, ")
                        .append(e.getClass().getName())
                        .append("\n")
                        .append("please make sure that you can access http://dev.virtualearth.net/");
                if (conn != null) {
                    int responseCode = conn.getResponseCode();
                    message.append("\n")
                            .append("Error code: ")
                            .append(responseCode);
                }
            } catch (IOException ignored) {}
            throw new TransportException(message.toString(), e);
        }
    }
}
