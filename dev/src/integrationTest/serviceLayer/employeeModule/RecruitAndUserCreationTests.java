package serviceLayer.employeeModule;

import businessLayer.employeeModule.Branch;
import businessLayer.employeeModule.Controllers.UserController;
import businessLayer.employeeModule.User;
import businessLayer.transportModule.SitesController;
import dataAccessLayer.DalFactory;
import exceptions.EmployeeException;
import exceptions.TransportException;
import objects.transportObjects.Site;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentationLayer.DataGenerator;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Objects.SEmployee;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import utils.Response;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;


public class RecruitAndUserCreationTests {
    private UserService userService;
    private EmployeesService empService;
    private User admin;
    private User user;
    private String adminUsername = "admin123";
    private String password = "123";
    private String username2 = "111";
    private String password2 = "1234";

    @BeforeEach
    public void setUp() throws Exception {
        DalFactory.clearTestDB();
        ServiceFactory serviceFactory = new ServiceFactory(TESTING_DB_NAME);
        userService = serviceFactory.userService();
        empService = serviceFactory.employeesService();

        // Loads the HR Manager user: "admin123" "123", clears the data in each test
        initUserData(userService, serviceFactory.businessFactory().userController());
        // Loads the branches data
        initBranchData(serviceFactory.businessFactory().sitesController());

        admin = Response.fromJson(userService.getUser(adminUsername)).data(User.class);
        if(Response.fromJson(userService.getUser(username2)).success() == false)
            userService.createUser(admin.getUsername(), username2, password2);
        if(Response.fromJson(empService.getEmployee(username2)).success() == false)
            empService.recruitEmployee(admin.getUsername(), Branch.HEADQUARTERS_ID,"Moshe Biton", username2,"Hapoalim 12 230", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "More details about Moshe");
        user = Response.fromJson(userService.getUser(username2)).data(User.class);
    }

    private void initUserData(UserService us, UserController uc) {
        us.initializeManagers();
    }

    private void initBranchData(SitesController sc) {
        Site site1 = new Site("1", "14441 s inglewood ave, hawthorne, ca 90250", "zone1", "111-111-1111", "John Smith", Site.SiteType.BRANCH, 0, 0);
        Site site2 = new Site("2", "19503 s normandie ave, torrance, ca 90501", "zone1", "222-222-2222", "Jane Doe", Site.SiteType.BRANCH, 0, 0);
        Site site3 = new Site("3", "22015 hawthorne blvd, torrance, ca 90503", "zone1", "333-333-3333", "Bob Johnson", Site.SiteType.BRANCH, 0, 0);
        Site site4 = new Site("4", "2100 n long beach blvd, compton, ca 90221", "zone2", "444-444-4444", "Samantha Lee", Site.SiteType.BRANCH, 0, 0);
        Site site5 = new Site("5", "19340 hawthorne blvd, torrance, ca 90503", "zone2", "555-555-5555", "Mike Brown", Site.SiteType.BRANCH, 0, 0);
        Site site6 = new Site("6", "4651 firestone blvd, south gate, ca 90280", "zone2", "666-666-6666", "Emily Wilson", Site.SiteType.BRANCH, 0, 0);
        Site site7 = new Site("7", "1301 n victory pl, burbank, ca 91502", "zone3", "777-777-7777", "Tom Kim", Site.SiteType.BRANCH, 0, 0);
        Site site8 = new Site("8", "6433 fallbrook ave, west hills, ca 91307","zone3", "888-888-8888", "Amanda Garcia", Site.SiteType.BRANCH, 0, 0);
        Site site9 = new Site("9", "8333 van nuys blvd, panorama city, ca 91402","zone4", "123-456-7890" ,"David Kim", Site.SiteType.BRANCH, 0, 0);

        List<Site> sites = new LinkedList<>(){{
            add(site1);
            add(site2);
            add(site3);
            add(site4);
            add(site5);
            add(site6);
            add(site7);
            add(site8);
            add(site9);
        }};

        try {
            sc.addAllSitesFirstTimeSystemLoad(sites);
        } catch (TransportException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    public void recruit_newEmployee() {
        try {
            Response ans = Response.fromJson(empService.recruitEmployee(admin.getUsername(),"2", "Max T", "555","Hapoalim 12 231", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me"));
            assertTrue(ans.success(), ans.message());
            ans = Response.fromJson(empService.recruitEmployee(admin.getUsername(), "2", "ab T", "555","Hapoalim 12 211", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me"));
            assertFalse(ans.success(),ans.message()); // existing employee with same ID
            ans = Response.fromJson(empService.recruitEmployee(username2, "2", "ab T", "575","Hapoalim 12 211", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me"));
            assertFalse(ans.success(),ans.message()); //unauthorized personnel
        } catch (Exception ignore) { ignore.printStackTrace(); fail("failed");}
    }

    @Test
    public void create_user() {
        try {
            Response response =  Response.fromJson(userService.createUser(adminUsername, "989", password));
            assertTrue(response.success(), response.message());
            response =  Response.fromJson(userService.createUser(adminUsername, username2, password));
            assertFalse(response.success(), response.message()); // existing user
            response =  Response.fromJson(userService.createUser(username2, "985", password));
            assertFalse(response.success(), response.message());//unauthorized user
        } catch (Exception ignore) {
            fail("failed.");
        }
    }

    @Test
    public void checkUserToEmployeeLinkage() {
        try {
            String employeeName = "Alex Turner";
            String employeeId = "989";
            Response ans = Response.fromJson(userService.createUser(adminUsername, employeeId, password));
            assertTrue(ans.success());
            ans = Response.fromJson(empService.recruitEmployee(admin.getUsername(), "2", employeeName, employeeId,"Hapoalim 12 221", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me"));
            assertTrue(ans.success());
            try{
            User us = Response.fromJson(userService.getUser(employeeId)).data(User.class);
            assertNotNull(us);
            Response as = Response.fromJson(empService.getEmployee(employeeId));
            assertTrue(as.success(), as.message());
            assertTrue(as.<SEmployee>data(SEmployee.class).getFullName().equals(employeeName));
            } catch(Exception e){
                fail(e.getMessage());
            }

        } catch (Exception ignore) {
            fail("failed.");
        }
    }
   
}