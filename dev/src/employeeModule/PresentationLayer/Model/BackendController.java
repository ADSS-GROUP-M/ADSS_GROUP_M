package employeeModule.PresentationLayer.Model;

import employeeModule.ServiceLayer.Objects.SEmployee;
import employeeModule.ServiceLayer.Objects.SShift;
import employeeModule.ServiceLayer.Objects.SShiftType;
import employeeModule.ServiceLayer.Services.EmployeesService;
import employeeModule.ServiceLayer.Objects.Response;
import employeeModule.ServiceLayer.Services.UserService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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


    public String certifyEmployee(String employeeId, String role) {
        Response<Boolean> response = employeesService.certifyEmployee(loggedUsername, employeeId, role);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Certified employee successfully.";
    }

    public String uncertifyEmployee(String employeeId, String role) {
        Response<Boolean> response = employeesService.uncertifyEmployee(loggedUsername, employeeId, role);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Uncertified employee successfully.";
    }

    public String approveShift(String branchId, LocalDate shiftDate, String shiftType) {
        try {
            Response<Boolean> response = employeesService.approveShift(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType));
            if (response.errorOccurred())
                return response.getErrorMessage();
            else
                return "Approved shift successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }

    public String addEmployeeToBranch(String employeeId, String branchId) {
        Response<Boolean> response = employeesService.addEmployeeToBranch(loggedUsername, employeeId, branchId);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Added employee to branch successfully.";
    }

    public String deleteShift(String branchId, LocalDate shiftDate, String shiftType) {
        try {
            Response<Boolean> response = employeesService.deleteShift(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType));
            if (response.errorOccurred())
                return response.getErrorMessage();
            else
                return "Deleted shift successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }

    public String createBranch(String branchId) {
        Response<Boolean> response = employeesService.createBranch(loggedUsername, branchId);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Created the branch successfully.";
    }

    public String updateBranchWorkingHours(String branchId, LocalTime morningStart, LocalTime morningEnd, LocalTime eveningStart, LocalTime eveningEnd) {
        Response<Boolean> response = employeesService.updateBranchWorkingHours(loggedUsername, branchId, morningStart, morningEnd, eveningStart, eveningEnd);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Updated the branch's working hours successfully.";
    }

    public String updateEmployeeSalary(String employeeId, double hourlySalaryRate, double salaryBonus) {
        Response<Boolean> response = employeesService.updateEmployeeSalary(loggedUsername, employeeId, hourlySalaryRate, salaryBonus);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Updated the employee's salary successfully.";
    }

    public String updateEmployeeBankDetails(String employeeId, String bankDetails) {
        Response<Boolean> response = employeesService.updateEmployeeBankDetails(loggedUsername, employeeId, bankDetails);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Updated the employee's bank details successfully.";
    }

    public String updateEmployeeEmploymentConditions(String employeeId, String employmentConditions) {
        Response<Boolean> response = employeesService.updateEmployeeEmploymentConditions(loggedUsername, employeeId, employmentConditions);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Updated the employee's employment conditions successfully.";
    }

    public String updateEmployeeDetails(String employeeId, String details) {
        Response<Boolean> response = employeesService.updateEmployeeDetails(loggedUsername, employeeId, details);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Updated the employee's details successfully.";
    }

    public String authorizeUser(String username, String authorization) {
        Response<Boolean> response = userService.authorizeUser(loggedUsername, username, authorization);
        if (response.errorOccurred())
            return response.getErrorMessage();
        else
            return "Authorized the user successfully.";
    }

    public String cancelShiftRequest(String branchId, String shiftType, LocalDate shiftDate, String role) {
        try {
            Response<Boolean> response = employeesService.cancelShiftRequest(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), role);
            if (response.errorOccurred())
                return response.getErrorMessage();
            else
                return "Cancelled the shift request successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }

    public String reportShiftActivity(String branchId, LocalDate shiftDate, String shiftType, String activity) {
        try {
            Response<Boolean> response = employeesService.reportShiftActivity(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), activity);
            if (response.errorOccurred())
                return response.getErrorMessage();
            else
                return "Reported the shift activity successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }

    public String applyCancelCard(String branchId, LocalDate shiftDate, String shiftType, String productId) {
        try {
            Response<Boolean> response = employeesService.applyCancelCard(loggedUsername, branchId, shiftDate, SShiftType.valueOf(shiftType), productId);
            if (response.errorOccurred())
                return response.getErrorMessage();
            else
                return "Cancelled the product successfully.";
        } catch (IllegalArgumentException e) {
            return "Invalid ShiftType value, expected `Morning` or `Evening`.";
        }
    }
}
