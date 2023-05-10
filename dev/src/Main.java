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
import serviceLayer.transportModule.ResourceManagementService;
import utils.transportUtils.TransportException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

@SuppressWarnings("NewClassNamingConvention")
public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the Transport-Employees CLI");
        new MenuManager().run();
    }
    @Test
    public void generate(){
        generateData();
    }

    public static void generateData(){
        deleteData();
        ServiceFactory factory = new ServiceFactory();
        UserService userService = factory.userService();
        EmployeesService employeesService = factory.employeesService();
        userService.createData();
        employeesService.createData();
        UiData.generateAndAddData();
    }

    public static void deleteData(){
        DalFactory.clearDB("SuperLiDB.db");
    }

    public static void generateSites(ResourceManagementService rms){
        Site site1 = new Site("branch1", "14441 s inglewood ave, hawthorne, ca 90250", "zone1", "111-111-1111", "John Smith", Site.SiteType.BRANCH, 0, 0);
        Site site2 = new Site("branch2", "19503 s normandie ave, torrance, ca 90501", "zone1", "222-222-2222", "Jane Doe", Site.SiteType.BRANCH, 0, 0);
        Site site3 = new Site("branch3", "22015 hawthorne blvd, torrance, ca 90503", "zone1", "333-333-3333", "Bob Johnson", Site.SiteType.BRANCH, 0, 0);
        Site site4 = new Site("branch4", "2100 n long beach blvd, compton, ca 90221", "zone2", "444-444-4444", "Samantha Lee", Site.SiteType.BRANCH, 0, 0);
        Site site5 = new Site("branch5", "19340 hawthorne blvd, torrance, ca 90503", "zone2", "555-555-5555", "Mike Brown", Site.SiteType.BRANCH, 0, 0);
        Site site6 = new Site("branch6", "4651 firestone blvd, south gate, ca 90280", "zone2", "666-666-6666", "Emily Wilson", Site.SiteType.BRANCH, 0, 0);
        Site site7 = new Site("branch7", "1301 n victory pl, burbank, ca 91502", "zone3", "777-777-7777", "Tom Kim", Site.SiteType.BRANCH, 0, 0);
        Site site8 = new Site("branch8", "6433 fallbrook ave, west hills, ca 91307","zone3", "888-888-8888", "Amanda Garcia", Site.SiteType.BRANCH, 0, 0);
        Site site9 = new Site("branch9", "8333 van nuys blvd, panorama city, ca 91402", "123-456-7890","zone4" ,"David Kim", Site.SiteType.BRANCH, 0, 0);
        Site site10 = new Site("supplier1", "8500 washington blvd, pico rivera, ca 90660", "zone4", "456-789-0123", "William Davis", Site.SiteType.SUPPLIER, 0, 0);
        Site site11 = new Site("supplier2", "20226 avalon blvd, carson, ca 90746", "zone3", "999-999-9999", "Steve Chen", Site.SiteType.SUPPLIER, 0, 0);
        Site site12 = new Site("supplier3", "9001 apollo way, downey, ca 90242", "zone4", "345-678-9012", "Andrew Chen", Site.SiteType.SUPPLIER, 0, 0);
        Site site13 = new Site("supplier4", "2770 e carson st, lakewood, ca 90712", "zone5", "123-456-7890", "Andrew Chen", Site.SiteType.SUPPLIER, 0,0);
        Site site14 = new Site("supplier5", "14501 lakewood blvd, paramount, ca 90723", "zone4", "234-567-8901", "Jessica Park", Site.SiteType.SUPPLIER, 0, 0);
        Site site15 = new Site("logistical1", "3705 e south st, long beach, ca 90805", "zone5", "123-456-7890", "Jessica Park", Site.SiteType.LOGISTICAL_CENTER, 0,0);

        rms.addSite(site1.toJson());
        rms.addSite(site2.toJson());
        rms.addSite(site3.toJson());
        rms.addSite(site4.toJson());
        rms.addSite(site5.toJson());
        rms.addSite(site6.toJson());
        rms.addSite(site7.toJson());
        rms.addSite(site8.toJson());
        rms.addSite(site9.toJson());
        rms.addSite(site10.toJson());
        rms.addSite(site11.toJson());
        rms.addSite(site12.toJson());
        rms.addSite(site13.toJson());
        rms.addSite(site14.toJson());
        rms.addSite(site15.toJson());
    }

    public static void initializeBranch(Site site, LocalDate date){
        // initialize everything so that there will be an available storekeeper and driver and everything
    }
}
