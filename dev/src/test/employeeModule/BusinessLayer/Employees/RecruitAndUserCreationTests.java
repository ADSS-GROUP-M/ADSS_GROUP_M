package employeeModule.BusinessLayer.Employees;

import businessLayer.employeeModule.User;
import serviceLayer.employeeModule.Objects.SEmployee;
import serviceLayer.transportModule.ServiceFactory;
import utils.Response;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

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
        ServiceFactory serviceFactory = new ServiceFactory(TESTING_DB_NAME);
        userService = serviceFactory.userService();
        empService = serviceFactory.employeesService();
        userService.loadData(); // Loads the HR Manager user: "admin123" "123", clears the data in each test
        empService.loadData();
        admin = Response.fromJson(userService.getUser(adminUsername)).data(User.class);
        if(Response.fromJson(userService.getUser(username2)).success() == false)
            userService.createUser(admin.getUsername(), username2, password2);
        if(Response.fromJson(empService.getEmployee(username2)).success() == false)
            empService.recruitEmployee(admin.getUsername(),"Moshe Biton", "1", username2,"Hapoalim 12 230", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "More details about Moshe");
        user = Response.fromJson(userService.getUser(username2)).data(User.class);
    }

    @Test
    public void recruit_newEmployee() {
        try {
            Response ans = Response.fromJson(empService.recruitEmployee(admin.getUsername(),"Max T","2", "555","Hapoalim 12 231", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me"));
            assertTrue(ans.success(), ans.message());
            ans = Response.fromJson(empService.recruitEmployee(admin.getUsername(),"ab T","2", "555","Hapoalim 12 211", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me"));
            assertFalse(ans.success(),ans.message()); // existing employee with same ID
            ans = Response.fromJson(empService.recruitEmployee(username2,"ab T","2", "575","Hapoalim 12 211", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me"));
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
            assertFalse(ans.success() == false);
            ans = Response.fromJson(empService.recruitEmployee(admin.getUsername(),employeeName,"2", employeeId,"Hapoalim 12 221", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me"));
            assertFalse(ans.success() == false);
            try{
            User us = Response.fromJson(userService.getUser(employeeId)).data(User.class);
            assertTrue( us != null);
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