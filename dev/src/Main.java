import businessLayer.transportModule.bingApi.BingAPI;
import businessLayer.transportModule.bingApi.LocationByQueryResponse;
import businessLayer.transportModule.bingApi.Point;
import businessLayer.transportModule.bingApi.Resource;
import dataAccessLayer.DalFactory;
import objects.transportObjects.Site;
import org.junit.jupiter.api.Test;
import presentationLayer.employeeModule.View.MenuManager;
import presentationLayer.transportModule.UiData;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import utils.transportUtils.TransportException;

import java.io.IOException;
import java.util.Arrays;

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

        try{
            LocationByQueryResponse obj = BingAPI.locationByQuery("HaShalom 15, beer sheva");
            System.out.println();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getPoint(){


        String address = ;

        LocationByQueryResponse queryResponse;
        try {
            queryResponse = BingAPI.locationByQuery(address);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        Resource[] resources = queryResponse.resourceSets()[0].resources();
        if (resources.length != 1) {
            throw new RuntimeException("Could not find site or found multiple sites");
        }
        System.out.println(resources[0].name());
    }
}
