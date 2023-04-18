package employeeModule.BusinessLayer.Employees;
import com.google.gson.reflect.TypeToken;
import utils.Response;
import employeeModule.ServiceLayer.Objects.SEmployee;
import employeeModule.ServiceLayer.Objects.SShift;
import employeeModule.ServiceLayer.Objects.SShiftType;
import employeeModule.ServiceLayer.Services.EmployeesService;
import employeeModule.ServiceLayer.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import employeeModule.employeeUtils.DateUtils;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTests {
    public static final Type LIST_SSHIFT_ARRAY_TYPE = new TypeToken<List<SShift[]>>() {
    }.getType();
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
        userService = UserService.getInstance();
        empService = EmployeesService.getInstance();
        userService.loadData(); // Loads the HR Manager user: "admin123" "123", clears the data in each test
        empService.loadData();
        admin = userService.getUser(adminUsername).data(User.class);
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
        for(int i=0; i<30;i++){
        usernames[i] = usernamer;
        passwords[i] = passworder;
        fullnames[i] = namer;
        branches[i] = brancher;
        bankDetails[i] = bankDetailer;
        hourlyRates[i] = hourlyRater;
        employmentDates[i] = employmentDater;
        employmentConditions[i] = employmentConditioner;
        details[i] = detailer;

        if(userService.getUser(usernames[i]).success() == false)
            userService.createUser(admin.getUsername(), usernames[i], passwords[i]);
        if(empService.getEmployee(usernames[i]).success() == false)
            empService.recruitEmployee(admin.getUsername(),fullnames[i], branches[i], usernames[i],bankDetails[i], hourlyRates[i], employmentDates[i],employmentConditions[i], details[i]);
        users[i] = userService.getUser(usernames[i]).data(User.class);
        users[i].login(passwords[i]);

        counter++;        
        usernamer = Integer.toString(counter);
        passworder = Integer.toString(counter);
      
        if(i>=10)
            brancher = "1";
        if(i>=20)
            brancher = "1";
        Response ans = null;
        if(i<10){
            ans = userService.authorizeUser(adminUsername, usernames[i], Authorization.ShiftManager.name());
            empService.certifyEmployee(adminUsername, usernames[i], Role.ShiftManager.name());
        }
        else if(i>=10 && i<20){
            userService.authorizeUser(adminUsername, usernames[i], Authorization.Cashier.name());
            empService.certifyEmployee(adminUsername, usernames[i], Role.Cashier.name());
        }
        else if(i>=20 && i<=25){
            userService.authorizeUser(adminUsername, usernames[i], Authorization.Storekeeper.name());
            empService.certifyEmployee(adminUsername, usernames[i], Role.Storekeeper.name());
        }
        
    }
        
    }
    // 0-9 shiftmanager, 10-19 cashier, 20-25 storekeeper
    @Test
    public void situation1() { // 
        try {
            
            LocalDate[] week = DateUtils.getWeekDates(LocalDate.of(2023,4, 9));
            Response ans = empService.createWeekShifts(adminUsername, "1", week[0]);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.setShiftNeededAmount(adminUsername, "1", week[0], SShiftType.Morning, Role.Cashier.name(), 2);
            assertFalse(ans.success() == false, ans.message());
            ans =empService.setShiftNeededAmount(adminUsername, "1", week[0], SShiftType.Morning, Role.Storekeeper.name(), 1);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.requestShift(usernames[0],"1" , week[0], SShiftType.Morning, Role.ShiftManager.name());
            assertFalse(ans.success() == false, ans.message());
            ans = empService.requestShift(usernames[10],"1" , week[0], SShiftType.Morning, Role.Cashier.name());
            assertFalse(ans.success() == false, ans.message());
            ans = empService.requestShift(usernames[11],"1" , week[0], SShiftType.Morning, Role.Cashier.name());
            assertFalse(ans.success() == false, ans.message());
            ans = empService.requestShift(usernames[11],"1" , week[0], SShiftType.Morning, Role.Cashier.name()); // done twice intentionally
           // assertFalse(ans.getErrorMessage(), ans.success() == false);
            ans = empService.requestShift(usernames[20],"1" , week[0], SShiftType.Morning, Role.Storekeeper.name());
            assertFalse(ans.success() == false, ans.message());
            ans = empService.requestShift(usernames[21],"1" , week[0], SShiftType.Morning, Role.GeneralWorker.name());
            assertFalse(ans.success() == false, ans.message());
            Response ans2 = empService.getWeekShifts(adminUsername, "1",week[0]);
            boolean foundShift = false;
            SShift theShift = null;
            for(SShift[] shifts : ans2.<List<SShift[]>>data(LIST_SSHIFT_ARRAY_TYPE)){
                for(SShift shift: shifts){
                    if(shift.getShiftDate().isEqual(week[0]) &&  shift.getShiftType() == SShiftType.Morning){
                        theShift = shift;
                        foundShift = true;
                        boolean foundEmployee1 = false, foundEmployee2 = false;
                        for(SEmployee se: shift.getShiftRequestsEmployees(Role.Cashier.name()) ){
                            if(se.getId().equals( empService.getEmployee(usernames[10]).<SEmployee>data(SEmployee.class).getId()))
                                foundEmployee1 = true;
                            if(se.getId().equals( empService.getEmployee(usernames[11]).<SEmployee>data(SEmployee.class).getId()))
                                foundEmployee2 = true;
                        }
                        assertTrue(foundEmployee1 && foundEmployee2);
                        
                        assertTrue( shift.getShiftRequestsEmployees(Role.ShiftManager.name()).get(0).getId().equals(empService.getEmployee(usernames[0]).<SEmployee>data(SEmployee.class).getId()));
                        assertTrue( shift.getShiftRequestsEmployees(Role.Storekeeper.name()).get(0).getId().equals(empService.getEmployee(usernames[20]).<SEmployee>data(SEmployee.class).getId()));
                        assertTrue( shift.getShiftRequestsEmployees(Role.GeneralWorker.name()).get(0).getId().equals(empService.getEmployee(usernames[21]).<SEmployee>data(SEmployee.class).getId()));

                    }
                }
            }
            assertTrue(foundShift);
            ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.Cashier.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.Cashier.name())));
            ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.ShiftManager.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.ShiftManager.name())));
            ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.Storekeeper.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.Storekeeper.name())));
            ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.GeneralWorker.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.GeneralWorker.name())));
            ans = empService.approveShift(adminUsername, "1", week[0], SShiftType.Morning);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.setShiftNeededAmount(adminUsername, "1", week[0], SShiftType.Morning, "Cashier", 3);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.approveShift(adminUsername, "1", week[0], SShiftType.Morning);
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
        Response ans = empService.applyCancelCard(usernames[0], "1", date, SShiftType.Morning, "something");
        assertTrue(ans.success() == false, ans.message());// a shift manager that is not signed up to this shift
        empService.createWeekShifts(adminUsername, "1", DateUtils.getWeekDates(date)[0]);
        ans = empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Cashier.name(), 1);
        assertFalse(ans.success() == false, ans.message());
        ans = empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.ShiftManager.name(), 1);
        assertFalse(ans.success() == false, ans.message());
        ans = empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Storekeeper.name(), 0);
        assertFalse(ans.success() == false, ans.message());
        ans = empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.GeneralWorker.name(), 0);
        assertFalse(ans.success() == false, ans.message());
        empService.requestShift(usernames[0], "1", date, SShiftType.Morning, Role.ShiftManager.name());
        ans = empService.requestShift(usernames[10], "1", date, SShiftType.Morning, Role.Cashier.name());
        assertFalse(ans.success() == false, ans.message());
        List<String> li = new LinkedList<>(), li2 = new LinkedList<>();
        li.add(usernames[0]);
        ans = empService.setShiftEmployees(adminUsername, "1", date, SShiftType.Morning, Role.ShiftManager.name(), li);
        assertFalse(ans.success() == false, ans.message());
        li2.add(usernames[10]);
        ans = empService.setShiftEmployees(adminUsername, "1", date, SShiftType.Morning, Role.Cashier.name(), li2);
        assertFalse(ans.success() == false, ans.message());
        ans = empService.approveShift(adminUsername,"1", date, SShiftType.Morning);
        assertFalse(ans.success() == false, ans.message());
        ans = empService.applyCancelCard(usernames[0], "1", date, SShiftType.Morning,"something");
        assertFalse(ans.success() == false, ans.message());
        ans = empService.applyCancelCard(usernames[10], "1", date, SShiftType.Morning,"something");
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
        ans = userService.createUser(admin.getUsername(), newUsername, "123");
        ans = empService.recruitEmployee(admin.getUsername(),"abc", "2", newUsername,"Nothin 123 11", 30, week[0],"Nothing", "about");
        assertFalse(ans.success() == false, ans.message());
        ans = empService.recruitEmployee(admin.getUsername(),"abc", "1", newUsername,"Nothin 123 11", 30, week[0],"Nothing", "about");
        assertTrue(ans.success() == false, ans.message()); //recruiting same employee to different branch
        ans = empService.addEmployeeToBranch(adminUsername, newUsername, "1");
        ans = userService.login(newUsername, "123");
        ans = empService.requestShift(newUsername,"1" , week[0], SShiftType.Morning, Role.GeneralWorker.name());
        assertTrue(ans.success() == false, ans.message()); // shift doesnt exist, therefore should fail

        ans = empService.createWeekShifts(adminUsername, "1", week[0]);
        ans = empService.createWeekShifts(adminUsername, "2", week[0]);
        for(LocalDate date: week){ // set up needs for shifts
            ans = empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Cashier.name(), 1);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.ShiftManager.name(), 1);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Storekeeper.name(), 0);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.GeneralWorker.name(), 0);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.setShiftNeededAmount(adminUsername, "1", date, SShiftType.Morning, Role.Steward.name(), 1);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.setShiftNeededAmount(adminUsername, "2", date, SShiftType.Morning, Role.ShiftManager.name(), 1);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.setShiftNeededAmount(adminUsername, "2", date, SShiftType.Morning, Role.Storekeeper.name(), 0);
            assertFalse(ans.success() == false, ans.message());
            ans = empService.setShiftNeededAmount(adminUsername, "2", date, SShiftType.Morning, Role.GeneralWorker.name(), 0);
            assertFalse(ans.success() == false, ans.message());
        }// branch1 - manager,cashier,steward branch2 - shiftmanager, cashier
        ans = empService.requestShift(newUsername, "1", week[0], SShiftType.Morning, Role.Steward.name());
        assertTrue(ans.success() == false, ans.message()); // uncertified to be steward
        ans = empService.certifyEmployee(adminUsername, newUsername, Role.Steward.name());
        ans = empService.certifyEmployee(adminUsername, newUsername, Role.Cashier.name());
        ans = empService.requestShift(newUsername, "1", week[0], SShiftType.Morning, Role.Steward.name());
        assertFalse(ans.success() == false, ans.message());

        ans = empService.requestShift(newUsername, "2", week[0], SShiftType.Morning, Role.Steward.name());
        assertTrue(ans.success() == false, ans.message()); // cannot sign up to paraller shifts in different branches

        ans = empService.requestShift(newUsername, "2", week[0], SShiftType.Evening, Role.Steward.name());
        assertTrue(ans.success() == false, ans.message()); // cannot sign up to 2 shifts a day

        ans = empService.requestShift(newUsername, "1", week[1], SShiftType.Morning, Role.Steward.name());
        assertFalse(ans.success() == false, ans.message());
        ans = empService.requestShift(newUsername, "1", week[2], SShiftType.Morning, Role.Steward.name());
        assertFalse(ans.success() == false, ans.message());
        ans = empService.requestShift(newUsername, "1", week[3], SShiftType.Morning, Role.Steward.name());
        assertFalse(ans.success() == false, ans.message());
        ans = empService.requestShift(newUsername, "1", week[4], SShiftType.Morning, Role.Steward.name());
        assertFalse(ans.success() == false, ans.message());
        ans = empService.requestShift(newUsername, "2", week[5], SShiftType.Evening, Role.Cashier.name());
        assertFalse(ans.success() == false, ans.message());

        ans = empService.requestShift(newUsername, "2", week[6], SShiftType.Evening, Role.Cashier.name());
        assertTrue(ans.success() == false, ans.message()); //cannot sign up to 7 shifts a week

        List<String> ids = new LinkedList<>();
        ans = empService.requestShift(usernames[0], "1", week[0], SShiftType.Morning, Role.ShiftManager.name());
        ans = empService.requestShift(usernames[10], "1", week[0], SShiftType.Morning, Role.Cashier.name());
        ans = empService.cancelShiftRequest(usernames[10], "1", week[0], SShiftType.Morning, Role.Cashier.name());
        assertFalse(ans.success() == false, ans.message());
        ids.add(newUsername);
        ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning, Role.Steward.name(),ids);
        assertFalse(ans.success() == false, ans.message());
        ids.remove(newUsername);
        ids.add(usernames[0]);
        ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning, Role.ShiftManager.name(),ids);
        assertFalse(ans.success() == false, ans.message());
        ids.remove(usernames[0]);
        ids.add(usernames[10]);
        ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning, Role.Cashier.name(),ids);
        assertTrue(ans.success() == false, ans.message()); // the request was removed by the usernames[10], so the manager cant set him to the shift
        ans = empService.approveShift(adminUsername, "1",week[0],SShiftType.Morning);
        assertTrue(ans.success() == false, ans.message()); // cashier is missing, approving shift should make a warning.
        Response ans2 = empService.getEmployeeShifts(newUsername);
        Response ans3 = empService.getEmployeeShifts(usernames[0]);
        Response ans4 = empService.getEmployeeShifts(usernames[10]);
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
