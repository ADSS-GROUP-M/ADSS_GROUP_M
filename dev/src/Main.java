import businessLayer.transportModule.bingApi.BingAPI;
import businessLayer.transportModule.bingApi.LocationByQueryResponse;
import dataAccessLayer.DalFactory;
import org.junit.jupiter.api.Test;
import presentationLayer.employeeModule.View.MenuManager;
import presentationLayer.transportModule.UiData;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;

import java.io.IOException;

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
            LocationByQueryResponse obj = BingAPI.locationByQuery("Hanarkis 53, tel mond");
            System.out.println();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
