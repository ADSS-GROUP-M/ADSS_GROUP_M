package businessLayer.transportModule.bingRestApi;

import utils.JsonUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class BingRestAPI {


    private static final String key = "Ap_rzOWHxjXVKTzD5kXxfWSj_9LN2Vpr7LSFWv53tgAbvU9iBWl4SqqhUaASGTUE";

    public static LocationByQueryResponse locationByQuery(String address) throws IOException{

        String urlPrefix = "http://dev.virtualearth.net/REST/v1/Locations?q=";
        String urlSuffix = "&key=" + key;
        address = address.replace(" ", "%20");

        URL url = new URL(urlPrefix + address + urlSuffix);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        //Check if connect is made
        int responseCode = conn.getResponseCode();

        // 200 OK
        if (responseCode != 200) {
            throw new IOException("HttpResponseCode: " + responseCode);
        } else {

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            //Close the scanner
            scanner.close();

            return JsonUtils.deserialize(informationString.toString(), LocationByQueryResponse.class);
        }
    }
}
