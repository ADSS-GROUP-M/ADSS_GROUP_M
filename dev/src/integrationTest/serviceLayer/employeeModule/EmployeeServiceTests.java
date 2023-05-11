package serviceLayer.employeeModule;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.Controllers.UserController;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.User;
import businessLayer.transportModule.SitesController;
import com.google.gson.reflect.TypeToken;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import objects.transportObjects.Site;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentationLayer.DataGenerator;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Objects.SEmployee;
import serviceLayer.employeeModule.Objects.SShift;
import serviceLayer.employeeModule.Objects.SShiftType;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import serviceLayer.transportModule.ResourceManagementService;
import utils.Response;
import utils.employeeUtils.DateUtils;
import utils.transportUtils.TransportException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static serviceLayer.employeeModule.Services.UserService.HR_MANAGER_USERNAME;

public class EmployeeServiceTests {
    public static final Type LIST_SSHIFT_ARRAY_TYPE = new TypeToken<List<SShift[]>>() {}.getType();
    private UserService userService;
    private EmployeesService empService;
    private User admin;
    private String adminUsername = "admin123";
    private String password = "123";
    private User[] users = new User[30];
    private String[] usernames = new String[30];
    private String[] passwords = new String[30];
    private String[] fullnames = new String[30];
    private String[] branches = new String[30];
    private String[] bankDetails = new String[30];
    private double[] hourlyRates = new double[30];
    private LocalDate[] employmentDates = new LocalDate[30];
    private String[] employmentConditions = new String[30];
    private String[] details = new String[30];

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
        users = new User[30];
        String usernamer = "0";
        String passworder = "0";
        String namer = "abc";
        String brancher = "1";
        String bankDetailer = "Hapoalim 00 000";
        double hourlyRater = 30;
        LocalDate employmentDater = LocalDate.of(2023, 1, 1);
        String employmentConditioner = "slave";
        String detailer = "nothing";
        int counter = 0;
        for(int i=0; i<30;i++) {
            usernames[i] = usernamer;
            passwords[i] = passworder;
            fullnames[i] = namer;
            branches[i] = brancher;
            bankDetails[i] = bankDetailer;
            hourlyRates[i] = hourlyRater;
            employmentDates[i] = employmentDater;
            employmentConditions[i] = employmentConditioner;
            details[i] = detailer;

            if (Response.fromJson(userService.getUser(usernames[i])).success() == false)
                userService.createUser(admin.getUsername(), usernames[i], passwords[i]);
            if (Response.fromJson(empService.getEmployee(usernames[i])).success() == false)
                empService.recruitEmployee(admin.getUsername(), branches[i], fullnames[i], usernames[i], bankDetails[i], hourlyRates[i], employmentDates[i], employmentConditions[i], details[i]);
            users[i] = Response.fromJson(userService.getUser(usernames[i])).data(User.class);
            users[i].login(passwords[i]);

            counter++;
            usernamer = Integer.toString(counter);
            passworder = Integer.toString(counter);

            if (i >= 10)
                brancher = "1";
            if (i >= 20)
                brancher = "1";
            Response ans = null;
            if (i < 10) {
                Response.fromJson(userService.authorizeUser(adminUsername, usernames[i], Authorization.ShiftManager.name()));
                empService.certifyEmployee(adminUsername, usernames[i], Role.ShiftManager.name());
            } else if (i >= 10 && i < 20) {
                userService.authorizeUser(adminUsername, usernames[i], Authorization.Cashier.name());
                empService.certifyEmployee(adminUsername, usernames[i], Role.Cashier.name());
            } else if (i >= 20 && i <= 25) {
                userService.authorizeUser(adminUsername, usernames[i], Authorization.Storekeeper.name());
                empService.certifyEmployee(adminUsername, usernames[i], Role.Storekeeper.name());
            }

        }
    }

    private void initUserData(UserService us, UserController uc) {
        DataGenerator.initializeUserData(us,uc);
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
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    // 0-9 shiftmanager, 10-19 cashier, 20-25 storekeeper
    @Test
    public void situation1() { // 
        try {
            
            LocalDate[] week = DateUtils.getWeekDates(LocalDate.of(2023,4, 9));
            Response ans = Response.fromJson(empService.createWeekShifts(adminUsername, "1", week[0]));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", week[0], SShiftType.Morning, Role.Cashier.name(), 2));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", week[0], SShiftType.Morning, Role.Storekeeper.name(), 1));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.requestShift(usernames[0],"1" , week[0], SShiftType.Morning, Role.ShiftManager.name()));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.requestShift(usernames[10],"1" , week[0], SShiftType.Morning, Role.Cashier.name()));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.requestShift(usernames[11],"1" , week[0], SShiftType.Morning, Role.Cashier.name()));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.requestShift(usernames[11],"1" , week[0], SShiftType.Morning, Role.Cashier.name())); // done twice intentionally
           // assertFalse(ans.getErrorMessage(), ans.success() == false);
            ans = Response.fromJson(empService.requestShift(usernames[20],"1" , week[0], SShiftType.Morning, Role.Storekeeper.name()));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.requestShift(usernames[21],"1" , week[0], SShiftType.Morning, Role.GeneralWorker.name()));
            assertFalse(ans.success() == false, ans.message());
            Response ans2 = Response.fromJson(empService.getWeekShifts(adminUsername, "1",week[0]));
            boolean foundShift = false;
            SShift theShift = null;
            for(SShift[] shifts : ans2.<List<SShift[]>>data(LIST_SSHIFT_ARRAY_TYPE)){
                for(SShift shift: shifts){
                    if(shift.getShiftDate().isEqual(week[0]) &&  shift.getShiftType() == SShiftType.Morning){
                        theShift = shift;
                        foundShift = true;
                        boolean foundEmployee1 = false, foundEmployee2 = false;
                        for(SEmployee se: shift.getShiftRequestsEmployees(Role.Cashier.name()) ){
                            if(se.getId().equals( Response.fromJson(empService.getEmployee(usernames[10])).<SEmployee>data(SEmployee.class).getId()))
                                foundEmployee1 = true;
                            if(se.getId().equals( Response.fromJson(empService.getEmployee(usernames[11])).<SEmployee>data(SEmployee.class).getId()))
                                foundEmployee2 = true;
                        }
                        assertTrue(foundEmployee1 && foundEmployee2);
                        
                        assertTrue( shift.getShiftRequestsEmployees(Role.ShiftManager.name()).get(0).getId().equals(Response.fromJson(empService.getEmployee(usernames[0])).<SEmployee>data(SEmployee.class).getId()));
                        assertTrue( shift.getShiftRequestsEmployees(Role.Storekeeper.name()).get(0).getId().equals(Response.fromJson(empService.getEmployee(usernames[20])).<SEmployee>data(SEmployee.class).getId()));
                        assertTrue( shift.getShiftRequestsEmployees(Role.GeneralWorker.name()).get(0).getId().equals(Response.fromJson(empService.getEmployee(usernames[21])).<SEmployee>data(SEmployee.class).getId()));

                    }
                }
            }
            assertTrue(foundShift);
            ans = Response.fromJson(empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.Cashier.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.Cashier.name()))));
            ans = Response.fromJson(empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.ShiftManager.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.ShiftManager.name()))));
            ans = Response.fromJson(empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.Storekeeper.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.Storekeeper.name()))));
            ans = Response.fromJson(empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.GeneralWorker.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.GeneralWorker.name()))));
            ans = Response.fromJson(empService.approveShift(adminUsername, "1", week[0], SShiftType.Morning));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", week[0], SShiftType.Morning, "Cashier", 3));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.approveShift(adminUsername, "1", week[0], SShiftType.Morning));
            assertTrue(ans.success() == false, ans.message());
        } catch (Exception ignore) { ignore.printStackTrace(); fail("failed");}
    }
    private List<String> employeesToIds(List<SEmployee> emps){
        List<String> li = new LinkedList<String>();
        for(SEmployee emp : emps){
            li.add(emp.getId());
        }
        return li;
    }
    @Test
    public void situation2(){ // test cancel card, create shift, set shift needed roles, set employees to work and approve.
        LocalDate date = LocalDate.now();
        Response ans = Response.fromJson(empService.applyCancelCard(usernames[0], "1", date, SShiftType.Morning, "something"));
        assertTrue(ans.success() == false, ans.message());// a shift manager that is not signed up to this shift
        empService.createWeekShifts(adminUsername, "1", DateUtils.getWeekDates(date)[0]);
        ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Cashier.name(), 1));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.ShiftManager.name(), 1));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Storekeeper.name(), 0));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.GeneralWorker.name(), 0));
        assertFalse(ans.success() == false, ans.message());
        empService.requestShift(usernames[0], "1", date, SShiftType.Morning, Role.ShiftManager.name());
        ans = Response.fromJson(empService.requestShift(usernames[10], "1", date, SShiftType.Morning, Role.Cashier.name()));
        assertFalse(ans.success() == false, ans.message());
        List<String> li = new LinkedList<>(), li2 = new LinkedList<>();
        li.add(usernames[0]);
        ans = Response.fromJson(empService.setShiftEmployees(adminUsername, "1", date, SShiftType.Morning, Role.ShiftManager.name(), li));
        assertFalse(ans.success() == false, ans.message());
        li2.add(usernames[10]);
        ans = Response.fromJson(empService.setShiftEmployees(adminUsername, "1", date, SShiftType.Morning, Role.Cashier.name(), li2));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.approveShift(adminUsername,"1", date, SShiftType.Morning));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.applyCancelCard(usernames[0], "1", date, SShiftType.Morning,"something"));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.applyCancelCard(usernames[10], "1", date, SShiftType.Morning,"something"));
        assertTrue(ans.success() == false, ans.message()); // uncertified employee
    }
    @Test
    // checking illegal requests: 
    // requesting shifts that dont exist,
    // employee illegal shift requests,
    // deleting a shift an recreate it, 
    // recruiting existing employee,
    
    public void situation3(){
        LocalDate[] dates = DateUtils.getWeekDates(LocalDate.of(2023, 4, 6));
        LocalDate[] week = DateUtils.getConsequtiveDates(dates[0],dates[1]);
        Response ans;
        String newUsername = "777";
        ans = Response.fromJson(userService.createUser(admin.getUsername(), newUsername, "123"));
        ans = Response.fromJson(empService.recruitEmployee(admin.getUsername(), "2", "abc", newUsername,"Nothin 123 11", 30, week[0],"Nothing", "about"));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.recruitEmployee(admin.getUsername(), "1", "abc", newUsername,"Nothin 123 11", 30, week[0],"Nothing", "about"));
        assertTrue(ans.success() == false, ans.message()); //recruiting same employee to different branch
        ans = Response.fromJson(empService.addEmployeeToBranch(adminUsername, newUsername, "1"));
        ans = Response.fromJson(userService.login(newUsername, "123"));
        ans = Response.fromJson(empService.requestShift(newUsername,"1" , week[0], SShiftType.Morning, Role.GeneralWorker.name()));
        assertTrue(ans.success() == false, ans.message()); // shift doesnt exist, therefore should fail

        ans = Response.fromJson(empService.createWeekShifts(adminUsername, "1", week[0]));
        ans = Response.fromJson(empService.createWeekShifts(adminUsername, "2", week[0]));
        for(LocalDate date: week){ // set up needs for shifts
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Cashier.name(), 1));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.ShiftManager.name(), 1));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Storekeeper.name(), 0));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.GeneralWorker.name(), 0));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Steward.name(), 1));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "2", date, SShiftType.Morning, Role.ShiftManager.name(), 1));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "2", date, SShiftType.Morning, Role.Storekeeper.name(), 0));
            assertFalse(ans.success() == false, ans.message());
            ans = Response.fromJson(empService.setShiftNeededAmount(adminUsername, "2", date, SShiftType.Morning, Role.GeneralWorker.name(), 0));
            assertFalse(ans.success() == false, ans.message());
        }// branch1 - manager,cashier,steward branch2 - shiftmanager, cashier
        ans = Response.fromJson(empService.requestShift(newUsername, "1", week[0], SShiftType.Morning, Role.Steward.name()));
        assertTrue(ans.success() == false, ans.message()); // uncertified to be steward
        ans = Response.fromJson(empService.certifyEmployee(adminUsername, newUsername, Role.Steward.name()));
        ans = Response.fromJson(empService.certifyEmployee(adminUsername, newUsername, Role.Cashier.name()));
        ans = Response.fromJson(empService.requestShift(newUsername, "1", week[0], SShiftType.Morning, Role.Steward.name()));
        assertFalse(ans.success() == false, ans.message());

        ans = Response.fromJson(empService.requestShift(newUsername, "2", week[0], SShiftType.Morning, Role.Steward.name()));
        assertTrue(ans.success() == false, ans.message()); // cannot sign up to paraller shifts in different branches

        ans = Response.fromJson(empService.requestShift(newUsername, "2", week[0], SShiftType.Evening, Role.Steward.name()));
        assertTrue(ans.success() == false, ans.message()); // cannot sign up to 2 shifts a day

        ans = Response.fromJson(empService.requestShift(newUsername, "1", week[1], SShiftType.Morning, Role.Steward.name()));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.requestShift(newUsername, "1", week[2], SShiftType.Morning, Role.Steward.name()));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.requestShift(newUsername, "1", week[3], SShiftType.Morning, Role.Steward.name()));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.requestShift(newUsername, "1", week[4], SShiftType.Morning, Role.Steward.name()));
        assertFalse(ans.success() == false, ans.message());
        ans = Response.fromJson(empService.requestShift(newUsername, "2", week[5], SShiftType.Evening, Role.Cashier.name()));
        assertFalse(ans.success() == false, ans.message());

        ans = Response.fromJson(empService.requestShift(newUsername, "2", week[6], SShiftType.Evening, Role.Cashier.name()));
        assertTrue(ans.success() == false, ans.message()); //cannot sign up to 7 shifts a week

        List<String> ids = new LinkedList<>();
        ans = Response.fromJson(empService.requestShift(usernames[0], "1", week[0], SShiftType.Morning, Role.ShiftManager.name()));
        ans = Response.fromJson(empService.requestShift(usernames[10], "1", week[0], SShiftType.Morning, Role.Cashier.name()));
        ans = Response.fromJson(empService.cancelShiftRequest(usernames[10], "1", week[0], SShiftType.Morning, Role.Cashier.name()));
        assertFalse(ans.success() == false, ans.message());
        ids.add(newUsername);
        ans = Response.fromJson(empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning, Role.Steward.name(),ids));
        assertFalse(ans.success() == false, ans.message());
        ids.remove(newUsername);
        ids.add(usernames[0]);
        ans = Response.fromJson(empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning, Role.ShiftManager.name(),ids));
        assertFalse(ans.success() == false, ans.message());
        ids.remove(usernames[0]);
        ids.add(usernames[10]);
        ans = Response.fromJson(empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning, Role.Cashier.name(),ids));
        assertTrue(ans.success() == false, ans.message()); // the request was removed by the usernames[10], so the manager cant set him to the shift
        ans = Response.fromJson(empService.approveShift(adminUsername, "1",week[0],SShiftType.Morning));
        assertTrue(ans.success() == false, ans.message()); // cashier is missing, approving shift should make a warning.
        Response ans2 = Response.fromJson(empService.getEmployeeShifts(newUsername));
        Response ans3 = Response.fromJson(empService.getEmployeeShifts(usernames[0]));
        Response ans4 = Response.fromJson(empService.getEmployeeShifts(usernames[10]));
        boolean flag1=false,flag2=false,flag3=false;
        Type type = new TypeToken<List<SShift[]>>(){}.getType();
        for(SShift[] shi : ans2.<List<SShift[]>>data(type)){
            if(shi[0].getShiftDate().equals(week[0]) && shi[0].getShiftType() == SShiftType.Morning){// branch also needs to be checked, but code doesnt allow
              for(SEmployee sEmployee: shi[0].getShiftWorkersEmployees(Role.Steward.name()) ){
                if(sEmployee.getId().equals(newUsername))
                    flag1 = true;
              }
              for(SEmployee sEmployee: shi[0].getShiftWorkersEmployees(Role.ShiftManager.name()) ){
                if(sEmployee.getId().equals(usernames[0]))
                    flag2 = true;
              }
              try{
              for(SEmployee sEmployee: shi[0].getShiftWorkersEmployees(Role.Cashier.name()) ){
                if(sEmployee.getId().equals(usernames[10]))
                    flag3 = true;
              }
              }catch(Exception ignore){}
            }

        }
        assertTrue(flag1);// checking existance of employees in the approved shift, without the third worker (shouldnt be there)
        assertTrue(flag2);
        assertFalse(flag3);
    }

}
