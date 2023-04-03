package dev.PresentationLayer.Model;

import dev.ServiceLayer.Objects.SEmployee;
import dev.ServiceLayer.Objects.SShift;
import dev.ServiceLayer.Objects.SShiftType;
import dev.ServiceLayer.Services.EmployeesService;
import dev.ServiceLayer.Objects.Response;
import dev.ServiceLayer.Services.UserService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.next;

public class BackendController {

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
        Response<Boolean> response = userService.login(username, password);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else if (response.getReturnValue() == true) {
            loggedUsername = username;
            return "Login successful.";
        }
        else
            return "Login failed.";
    }

    public String logout() {
        Response<Boolean> response = userService.logout(loggedUsername);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else if (response.getReturnValue() == true) {
            loggedUsername = null;
            return "Logged out successfully.";
        }
        else
            return "Logout failed.";
    }

    public String recruitEmployee(String fullName, String branchId, String id, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) {
        Response<Boolean> response = employeesService.recruitEmployee(loggedUsername, fullName, branchId, id, bankDetails, hourlyRate, employmentDate, employmentConditions, details);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return null;
    }

    public String createUser(String username, String password) {
        Response<Boolean> response = userService.createUser(loggedUsername, username, password);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else if (response.getReturnValue() == true) {
            return "Created user successfully. Username: " + username + " Password: " + password + ".";
        }
        else
            return "An error has occurred while creating the user: " + response.getErrorMessage();
    }

    public String requestShift(String branchId, String shiftTime, LocalDate shiftDate, String role) {
        try {
            Response<Boolean> response = employeesService.requestShift(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftTime), role);
            if (response.errorOccurred())
                return response.getErrorMessage();
            else {
                boolean succeeded = response.getReturnValue();
                if (succeeded)
                    return null;
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
        Response<List<String>> response = userService.getUserAuthorizations(loggedUsername);
        if (response.errorOccurred())
            return null;
        else
            return response.getReturnValue();
    }

    public String createWeekShifts(String branchId, LocalDate weekStart) {
        Response<Boolean> response = employeesService.createWeekShifts(loggedUsername, branchId, weekStart);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else {
            boolean succeeded = response.getReturnValue();
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
            Response<Boolean> response = employeesService.setShiftNeededAmount(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), role, amount);
            if (response.errorOccurred())
                return response.getErrorMessage();
            else {
                boolean succeeded = response.getReturnValue();
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
            Response<Boolean> response = employeesService.setShiftEmployees(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), role, employeeIds);
            if (response.errorOccurred())
                return response.getErrorMessage();
            else {
                boolean succeeded = response.getReturnValue();
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
        Response<List<SShift[]>> response = employeesService.getWeekShifts(loggedUsername, branchId, weekStart);
        if (response.errorOccurred())
            throw new Exception(response.getErrorMessage());
        else {
            List<SShift[]> result = response.getReturnValue();
            return result;
        }
    }

    public List<SShift[]> getNextWeekShifts(String branchId) throws Exception {
        LocalDate nextSunday = LocalDate.now().with(next(DayOfWeek.SUNDAY));
        return getWeekShifts(branchId, nextSunday);
    }

    public List<SShift[]> getMyShifts() throws Exception {
        Response<List<SShift[]>> response = employeesService.getEmployeeShifts(loggedUsername);
        if (response.errorOccurred())
            throw new Exception(response.getErrorMessage());
        else {
            List<SShift[]> result = response.getReturnValue();
            return result;
        }
    }

    public SEmployee getEmployee() throws Exception {
        Response<SEmployee> response = employeesService.getEmployee(loggedUsername);
        if (response.errorOccurred())
            throw new Exception(response.getErrorMessage());
        else
            return response.getReturnValue();
    }
}
