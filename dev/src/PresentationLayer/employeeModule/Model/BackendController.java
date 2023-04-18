package PresentationLayer.employeeModule.Model;

import com.google.gson.reflect.TypeToken;
import ServiceLayer.employeeModule.Objects.SEmployee;
import ServiceLayer.employeeModule.Objects.SShift;
import ServiceLayer.employeeModule.Objects.SShiftType;
import ServiceLayer.employeeModule.Services.EmployeesService;
import utils.Response;
import ServiceLayer.employeeModule.Services.UserService;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.next;

public class BackendController {

    public static final Type STRING_lIST_TYPE = new TypeToken<List<String>>(){}.getType();
    private static final Type LIST_SSHIFT_ARRAY_TYPE = new TypeToken<List<SShift[]>>(){}.getType();

    private static BackendController instance;
    private UserService userService;
    private EmployeesService employeesService;
    private String loggedUsername;

    public BackendController() {
        userService = UserService.getInstance();
        employeesService = EmployeesService.getInstance();
        loggedUsername = null;

        // Data Initialization
        userService.loadData();
        employeesService.loadData();
    }

    public static BackendController getInstance() {
        if (instance == null)
            instance = new BackendController();
        return instance;
    }

    public boolean isLoggedIn() {
        return loggedUsername != null;
    }

    public String login(String username, String password) {
        Response response = userService.login(username, password);
        if (response.success() == false)
            return response.message();
        else if (response.success()) {
            loggedUsername = username;
            return "Login successful.";
        }
        else
            return "Login failed.";
    }

    public String logout() {
        Response response = userService.logout(loggedUsername);
        if (response.success() == false)
            return response.message();
        else if (response.success()) {
            loggedUsername = null;
            return "Logged out successfully.";
        }
        else
            return "Logout failed.";
    }

    public String recruitEmployee(String fullName, String branchId, String id, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) {
        Response response = employeesService.recruitEmployee(loggedUsername, fullName, branchId, id, bankDetails, hourlyRate, employmentDate, employmentConditions, details);
        if (response.success() == false)
            return response.message();
        else
            return null;
    }

    public String createUser(String username, String password) {
        Response response = userService.createUser(loggedUsername, username, password);
        if (response.success() == false)
            return response.message();
        else if (response.success()) {
            return "Created user successfully. Username: " + username + " Password: " + password + ".";
        }
        else
            return "An error has occurred while creating the user: " + response.message();
    }

    public String requestShift(String branchId, String shiftTime, LocalDate shiftDate, String role) {
        try {
            Response response = employeesService.requestShift(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftTime), role);
            if (response.success() == false)
                return response.message();
            else {
                boolean succeeded = response.success();
                if (succeeded)
                    return "Requested the shift registration successfully.";
                else
                    return "Could not register to the shift.";
            }
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        } catch (Exception e) {
            return "An error has occurred while requesting the shift: " + e.getMessage();
        }
    }

    public List<String> getUserAuthorizations() {
        Response response = userService.getUserAuthorizations(loggedUsername);
        if (response.success() == false)
            return null;
        else{
            return response.data(STRING_lIST_TYPE);
        }
    }

    public String createWeekShifts(String branchId, LocalDate weekStart) {
        Response response = employeesService.createWeekShifts(loggedUsername, branchId, weekStart);
        if (response.success() == false)
            return response.message();
        else {
            boolean succeeded = response.success();
            if (succeeded)
                return "Created week shifts successfully.";
            else
                return "Could not create the week shifts.";
        }
    }

    public String createNextWeekShifts(String branchId) {
        LocalDate nextSunday = LocalDate.now().with(next(DayOfWeek.SUNDAY));
        return createWeekShifts(branchId, nextSunday);
    }

    public String setShiftNeededAmount(String branchId, LocalDate shiftDate, String shiftType, String role, int amount) {
        try {
            Response response = employeesService.setShiftNeededAmount(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), role, amount);
            if (response.success() == false)
                return response.message();
            else {
                boolean succeeded = response.success();
                if (succeeded)
                    return "Updated the shift's needed amount of " + role + " successfully.";
                else
                    return "Could not set the shift needed amounts.";
            }
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType was given, expected `Morning` or `Evening`.";
        } catch (Exception e) {
            return "An error has occurred while setting the shift needed amounts: " + e.getMessage();
        }
    }

    public String setShiftEmployees(String branchId, LocalDate shiftDate, String shiftType, String role, List<String> employeeIds) {
        try {
            Response response = employeesService.setShiftEmployees(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), role, employeeIds);
            if (response.success() == false)
                return response.message();
            else {
                boolean succeeded = response.success();
                if (succeeded)
                    return "Updated the shift's employees in " + role + " successfully.";
                else
                    return "Could not set the shift employees.";
            }
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType was given, expected `Morning` or `Evening`.";
        } catch (Exception e) {
            return "An error has occurred while setting the shift employees: " + e.getMessage();
        }
    }

    public List<SShift[]> getWeekShifts(String branchId, LocalDate weekStart) throws Exception {
        Response response = employeesService.getWeekShifts(loggedUsername, branchId, weekStart);
        if (response.success() == false)
            throw new Exception(response.message());
        else {
            List<SShift[]> result = response.data(LIST_SSHIFT_ARRAY_TYPE);
            return result;
        }
    }

    public List<SShift[]> getNextWeekShifts(String branchId) throws Exception {
        LocalDate nextSunday = LocalDate.now().with(next(DayOfWeek.SUNDAY));
        return getWeekShifts(branchId, nextSunday);
    }

    public List<SShift[]> getMyShifts() throws Exception {
        Response response = employeesService.getEmployeeShifts(loggedUsername);
        if (response.success() == false)
            throw new Exception(response.message());
        else {
            List<SShift[]> result = response.data(LIST_SSHIFT_ARRAY_TYPE);
            return result;
        }
    }

    public SEmployee getEmployee() throws Exception {
        Response response = employeesService.getEmployee(loggedUsername);
        if (response.success() == false)
            throw new Exception(response.message());
        else{
            return response.data(SEmployee.class);
        }
    }


    public String certifyEmployee(String employeeId, String role) {
        Response response = employeesService.certifyEmployee(loggedUsername, employeeId, role);
        if (response.success() == false)
            return response.message();
        else
            return "Certified employee successfully.";
    }

    public String uncertifyEmployee(String employeeId, String role) {
        Response response = employeesService.uncertifyEmployee(loggedUsername, employeeId, role);
        if (response.success() == false)
            return response.message();
        else
            return "Uncertified employee successfully.";
    }

    public String approveShift(String branchId, LocalDate shiftDate, String shiftType) {
        try {
            Response response = employeesService.approveShift(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType));
            if (response.success() == false)
                return response.message();
            else
                return "Approved shift successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }

    public String addEmployeeToBranch(String employeeId, String branchId) {
        Response response = employeesService.addEmployeeToBranch(loggedUsername, employeeId, branchId);
        if (response.success() == false)
            return response.message();
        else
            return "Added employee to branch successfully.";
    }

    public String deleteShift(String branchId, LocalDate shiftDate, String shiftType) {
        try {
            Response response = employeesService.deleteShift(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType));
            if (response.success() == false)
                return response.message();
            else
                return "Deleted shift successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }

    public String createBranch(String branchId) {
        Response response = employeesService.createBranch(loggedUsername, branchId);
        if (response.success() == false)
            return response.message();
        else
            return "Created the branch successfully.";
    }

    public String updateBranchWorkingHours(String branchId, LocalTime morningStart, LocalTime morningEnd, LocalTime eveningStart, LocalTime eveningEnd) {
        Response response = employeesService.updateBranchWorkingHours(loggedUsername, branchId, morningStart, morningEnd, eveningStart, eveningEnd);
        if (response.success() == false)
            return response.message();
        else
            return "Updated the branch's working hours successfully.";
    }

    public String updateEmployeeSalary(String employeeId, double hourlySalaryRate, double salaryBonus) {
        Response response = employeesService.updateEmployeeSalary(loggedUsername, employeeId, hourlySalaryRate, salaryBonus);
        if (response.success() == false)
            return response.message();
        else
            return "Updated the employee's salary successfully.";
    }

    public String updateEmployeeBankDetails(String employeeId, String bankDetails) {
        Response response = employeesService.updateEmployeeBankDetails(loggedUsername, employeeId, bankDetails);
        if (response.success() == false)
            return response.message();
        else
            return "Updated the employee's bank details successfully.";
    }

    public String updateEmployeeEmploymentConditions(String employeeId, String employmentConditions) {
        Response response = employeesService.updateEmployeeEmploymentConditions(loggedUsername, employeeId, employmentConditions);
        if (response.success() == false)
            return response.message();
        else
            return "Updated the employee's employment conditions successfully.";
    }

    public String updateEmployeeDetails(String employeeId, String details) {
        Response response = employeesService.updateEmployeeDetails(loggedUsername, employeeId, details);
        if (response.success() == false)
            return response.message();
        else
            return "Updated the employee's details successfully.";
    }

    public String authorizeUser(String username, String authorization) {
        Response response = userService.authorizeUser(loggedUsername, username, authorization);
        if (response.success() == false)
            return response.message();
        else
            return "Authorized the user successfully.";
    }

    public String cancelShiftRequest(String branchId, String shiftType, LocalDate shiftDate, String role) {
        try {
            Response response = employeesService.cancelShiftRequest(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), role);
            if (response.success() == false)
                return response.message();
            else
                return "Cancelled the shift request successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }

    public String reportShiftActivity(String branchId, LocalDate shiftDate, String shiftType, String activity) {
        try {
            Response response = employeesService.reportShiftActivity(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), activity);
            if (response.success() == false)
                return response.message();
            else
                return "Reported the shift activity successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }

    public String applyCancelCard(String branchId, LocalDate shiftDate, String shiftType, String productId) {
        try {
            Response response = employeesService.applyCancelCard(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), productId);
            if (response.success() == false)
                return response.message();
            else
                return "Cancelled the product successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }
}
