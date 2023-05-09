import businessLayer.transportModule.SitesDistancesController;
import businessLayer.transportModule.bingApi.BingAPI;
import businessLayer.transportModule.bingApi.LocationByQueryResponse;
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


//        String address = ;
//
//        LocationByQueryResponse queryResponse;
//        try {
//            queryResponse = BingAPI.locationByQuery(address);
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//        Resource[] resources = queryResponse.resourceSets()[0].resources();
//        if (resources.length != 1) {
//            throw new RuntimeException("Could not find site or found multiple sites");
//        }
//        System.out.println(resources[0].name());
    }

    @Test
    public void matrix(){
        SitesDistancesController controller = new SitesDistancesController();
        Site site1 = new Site("zone1", "14441 s inglewood ave, hawthorne, ca 90250", "111-111-1111", "John Smith", Site.SiteType.BRANCH, 0, 0);
        Site site2 = new Site("zone1", "19503 s normandie ave, torrance, ca 90501", "222-222-2222", "Jane Doe", Site.SiteType.BRANCH, 0, 0);
        Site site3 = new Site("zone1", "22015 hawthorne blvd, torrance, ca 90503", "333-333-3333", "Bob Johnson", Site.SiteType.BRANCH, 0, 0);
        Site site4 = new Site("zone2", "2100 n long beach blvd, compton, ca 90221", "444-444-4444", "Samantha Lee", Site.SiteType.BRANCH, 0,0);

        try {
            site1 = new Site (site1,controller.getCoordinates(site1).latitude(),controller.getCoordinates(site1).longitude());
            site2 = new Site (site2,controller.getCoordinates(site2).latitude(),controller.getCoordinates(site2).longitude());
            site3 = new Site (site3,controller.getCoordinates(site3).latitude(),controller.getCoordinates(site3).longitude());
            site4 = new Site (site4,controller.getCoordinates(site4).latitude(),controller.getCoordinates(site4).longitude());
            controller.createDistanceObjects(site1,Arrays.asList(site2, site3, site4));
        } catch (TransportException e) {
            throw new RuntimeException(e);
        }

    }

}
