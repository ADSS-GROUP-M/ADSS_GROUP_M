package employeeModule.BusinessLayer.Employees;

import utils.Response;
import employeeModule.ServiceLayer.Services.EmployeesService;
import employeeModule.ServiceLayer.Services.UserService;
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
        admin = userService.getUser(adminUsername).data();
        if(userService.getUser(username2).success() == false)
            userService.createUser(admin.getUsername(), username2, password2);
        if(empService.getEmployee(username2).success() == false)
            empService.recruitEmployee(admin.getUsername(),"Moshe Biton", "1", username2,"Hapoalim 12 230", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "More details about Moshe");
        user = userService.getUser(username2).data();
    }

    @Test
    public void recruit_newEmployee() {
        try {
            Response ans = empService.recruitEmployee(admin.getUsername(),"Max T","2", "555","Hapoalim 12 231", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me");
            assertTrue(ans.message() == null, ans.message());
            ans = empService.recruitEmployee(admin.getUsername(),"ab T","2", "555","Hapoalim 12 211", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me");
            assertFalse(ans.message() == null, ans.message()); // existing employee with same ID
            ans = empService.recruitEmployee(username2,"ab T","2", "575","Hapoalim 12 211", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "about me");
            assertFalse(ans.message() == null, ans.message()); //unauthorized personnel
        } catch (Exception ignore) { ignore.printStackTrace(); fail("failed");}
    }

    @Test
    public void create_user() {
        try {
            String error = userService.createUser(adminUsername, "989", password).message();
            assertTrue(error == null);
            error = userService.createUser(adminUsername, username2, password).message();
            assertFalse(error  == null); // existing user
            error = userService.createUser(username2, "985", password).message();
            assertFalse(error == null);//unauthorized user
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
            User us = userService.getUser(employeeId).data();
            assertTrue( us != null);
            Response as = empService.getEmployee(employeeId);
            assertTrue(as.data()!=null, as.message());
            assertTrue(as.data().getFullName().equals(employeeName));
            } catch(Exception e){
                fail(e.getMessage());
            }

        } catch (Exception ignore) {
            fail("failed.");
        }
    }
   
}