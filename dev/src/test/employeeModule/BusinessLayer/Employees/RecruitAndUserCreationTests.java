package employeeModule.BusinessLayer.Employees;

import BusinessLayer.employeeModule.User;
import ServiceLayer.employeeModule.Services.employeeObjects.SEmployee;
import utils.Response;
import ServiceLayer.employeeModule.Services.EmployeesService;
import ServiceLayer.employeeModule.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

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
        userService = UserService.getInstance();
        empService = EmployeesService.getInstance();
        userService.loadData(); // Loads the HR Manager user: "admin123" "123", clears the data in each test
        empService.loadData();
        admin = userService.getUser(adminUsername).data(User.class);
        if(userService.getUser(username2).success() == false)
            userService.createUser(admin.getUsername(), username2, password2);
        if(empService.getEmployee(username2).success() == false)
            empService.recruitEmployee(admin.getUsername(),"Moshe Biton", "1", username2,"Hapoalim 12 230", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "More details about Moshe");
        user = userService.getUser(username2).data(User.class);
    }

    @Test
    public void recruit_newEmployee() {
        try {
            Response ans = empService.recruitEmployee(admin.getUsername(),"Max T","2", "555","Hapoalim 12 231", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me");
            assertTrue(ans.success(), ans.message());
            ans = empService.recruitEmployee(admin.getUsername(),"ab T","2", "555","Hapoalim 12 211", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me");
            assertFalse(ans.success(),ans.message()); // existing employee with same ID
            ans = empService.recruitEmployee(username2,"ab T","2", "575","Hapoalim 12 211", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me");
            assertFalse(ans.success(),ans.message()); //unauthorized personnel
        } catch (Exception ignore) { ignore.printStackTrace(); fail("failed");}
    }

    @Test
    public void create_user() {
        try {
            Response response = userService.createUser(adminUsername, "989", password);
            assertTrue(response.success(), response.message());
            response = userService.createUser(adminUsername, username2, password);
            assertFalse(response.success(), response.message()); // existing user
            response = userService.createUser(username2, "985", password);
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
            Response ans = userService.createUser(adminUsername, employeeId, password);
            assertFalse(ans.success() == false);
            ans = empService.recruitEmployee(admin.getUsername(),employeeName,"2", employeeId,"Hapoalim 12 221", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me");
            assertFalse(ans.success() == false);
            try{
            User us = userService.getUser(employeeId).data(User.class);
            assertTrue( us != null);
            Response as = empService.getEmployee(employeeId);
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