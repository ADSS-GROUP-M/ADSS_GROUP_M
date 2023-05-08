import businessLayer.transportModule.bingRestApi.LocationByQueryResponse;
import dataAccessLayer.DalFactory;
import org.junit.jupiter.api.Test;
import presentationLayer.employeeModule.View.MenuManager;
import presentationLayer.transportModule.UiData;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import utils.JsonUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@SuppressWarnings("NewClassNamingConvention")
public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the Transport-Employees CLI");
        new MenuManager().run();
    }
    @Test
    public void generateData(){
        deleteData();
        ServiceFactory factory = new ServiceFactory();
        UserService userService = factory.userService();
        EmployeesService employeesService = factory.employeesService();
        userService.createData();
        employeesService.createData();
        UiData.generateAndAddData();
    }

    @Test
    public void deleteData(){
        DalFactory.clearDB("SuperLiDB.db");
    }

    @Test
    public void api_connection(){

        try {

            String key = "Ap_rzOWHxjXVKTzD5kXxfWSj_9LN2Vpr7LSFWv53tgAbvU9iBWl4SqqhUaASGTUE";
            URL url10 = new URL("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=" +
                    "{" +
                        "lat0,long0;" +
                        "lat1,lon1;" +
                        "latM,lonM" +
                    "}" +
                    "&destinations=" +
                    "{" +
                        "lat0,lon0;" +
                        "lat1,lon1;" +
                        "latN,longN" +
                    "}" +
                    "&travelMode={travelMode}" +
                    "&startTime={startTime}" +
                    "&timeUnit={timeUnit}" +
                    "&key={BingMapsKey}");

            String urlPrefix = "http://dev.virtualearth.net/REST/v1/Locations?q=";
            String urlSuffix = "&key=" + key;
            String address = "Hadaat 6, beer sheva, israel";
            address = address.replace(" ", "%20");
            String url = urlPrefix + address + urlSuffix;
            URL url2 = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url2.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();

                LocationByQueryResponse jsonObj = JsonUtils.deserialize(informationString.toString(), LocationByQueryResponse.class);
                System.out.println();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
