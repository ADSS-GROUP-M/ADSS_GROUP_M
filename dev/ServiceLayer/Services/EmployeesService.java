package dev.ServiceLayer.Services;

import dev.BusinessLayer.Employees.*;
import dev.BusinessLayer.Employees.Controllers.EmployeesController;
import dev.BusinessLayer.Employees.Controllers.ShiftsController;
import dev.ServiceLayer.Objects.Response;
import dev.ServiceLayer.Objects.SEmployee;
import dev.ServiceLayer.Objects.SShift;
import dev.ServiceLayer.Objects.SShiftType;
import dev.BusinessLayer.Employees.Shift.ShiftType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeesService {
    private static EmployeesService instance;
    private static UserService userService;
    private EmployeesController employeesController;
    private ShiftsController shiftsController;

    private EmployeesService() {
        userService = UserService.getInstance();
        employeesController = EmployeesController.getInstance();
        shiftsController = ShiftsController.getInstance();
    }

    public static EmployeesService getInstance() {
        if (instance == null)
            instance = new EmployeesService();
        return instance;
    }

    /**
     * This method loads the initial employee data from the system, during the initial load of the system.
     */
    public void loadData() {
        resetData();
        try {
            // Initializing Branches
            for(int i = 1; i <= 9; i++) {
                String branchId = Integer.toString(i);
                employeesController.createBranch(branchId);
            }
            // TODO: Add initial employees data
            employeesController.recruitEmployee("1","Moshe Biton", "111","Hapoalim 12 230", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "More details about Moshe");
            employeesController.certifyEmployee("111",Role.ShiftManager);
            employeesController.certifyEmployee("111",Role.Storekeeper);
            userService.createUser("admin123","111","1234");
        } catch (Exception ignore) {}
    }

    /**
     * This method resets the employee data from the system.
     */
    public void resetData() {
        employeesController.resetData();
        shiftsController.resetData();
    }

    public Response<Boolean> recruitEmployee(String actorUsername, String fullName, String branchId, String employeeId, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) {
        Response<Boolean> authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.errorOccurred())
            return Response.createErrorResponse(authResponse.getErrorMessage());
        try {
            employeesController.recruitEmployee(branchId, fullName, employeeId, bankDetails, hourlyRate, employmentDate, employmentConditions, details);
            return new Response<>(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> certifyEmployee(String actorUsername, String employeeId, String role) {
        Response<Boolean> authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.errorOccurred())
            return Response.createErrorResponse(authResponse.getErrorMessage());
        try {
            employeesController.certifyEmployee(employeeId, Role.valueOf(role));
            return new Response<>(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> requestShift(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role) {
        try {
            Employee employee = employeesController.getEmployee(branchId, actorUsername);
            shiftsController.requestShift(employee, branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role));
            return new Response<>(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> createWeekShifts(String actorUsername, String branchId, LocalDate weekStart) {
        Response<Boolean> authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.errorOccurred())
            return Response.createErrorResponse(authResponse.getErrorMessage());
        try {
            shiftsController.createWeekShifts(branchId, weekStart);
            return new Response<>(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> setShiftNeededAmount(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role, int amount) {
        Response<Boolean> authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.errorOccurred())
            return Response.createErrorResponse(authResponse.getErrorMessage());
        try {
            shiftsController.setShiftNeededAmount(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role), amount);
            return new Response<>(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<List<SShift[]>> getWeekShifts(String actorUsername, String branchId, LocalDate weekStart) {
        // Possibly check the actor user authorization
        try {
            List<Shift[]> weekShifts = shiftsController.getWeekShifts(branchId, weekStart);
            List<SShift[]> result = new ArrayList<>();
            for(Shift[] shifts : weekShifts) {
                SShift[] serviceShifts = new SShift[shifts.length];
                for (int i = 0; i < shifts.length; i++)
                    if (shifts[i] == null)
                        serviceShifts[i] = null;
                    else
                        serviceShifts[i] = new SShift(shifts[i]);
                result.add(serviceShifts);
            }
            return new Response<>(result);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> setShiftEmployees(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role, List<String> employeeIds) {
        Response<Boolean> authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.errorOccurred())
            return Response.createErrorResponse(authResponse.getErrorMessage());
        try {
            List<Employee> employees = employeesController.getEmployees(branchId, employeeIds);
            shiftsController.setShiftEmployees(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role), employees);
            return new Response<>(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> createBranch(String actorUsername, String branchId) {
        Response<Boolean> authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.errorOccurred())
            return Response.createErrorResponse(authResponse.getErrorMessage());
        try {
            employeesController.createBranch(branchId);
            return new Response<>(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<List<SShift[]>> getEmployeeShifts(String actorUsername) {
        try {
            Employee employee = employeesController.getEmployee(actorUsername);
            List<Shift[]> employeeShifts = shiftsController.getEmployeeShifts(employee);
            List<SShift[]> result = new ArrayList<>();
            for(Shift[] shifts : employeeShifts) {
                SShift[] serviceShifts = new SShift[shifts.length];
                for (int i = 0; i < shifts.length; i++)
                    if (shifts[i] == null)
                        serviceShifts[i] = null;
                    else
                        serviceShifts[i] = new SShift(shifts[i]);
                result.add(serviceShifts);
            }
            return new Response<>(result);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<SEmployee> getEmployee(String actorUsername) {
        try {
            Employee employee = employeesController.getEmployee(actorUsername);
            return new Response<>(new SEmployee(employee));
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> approveShift(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType) {
        Response<Boolean> authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.errorOccurred())
            return Response.createErrorResponse(authResponse.getErrorMessage());
        try {
            shiftsController.approveShift(branchId,shiftDate, ShiftType.valueOf(shiftType.toString()));
            return new Response<>(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> applyCancelCard(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String productId) {
        Response<Boolean> authResponse = userService.isAuthorized(actorUsername, Authorization.ShiftManager);
        if (authResponse.errorOccurred())
            return Response.createErrorResponse(authResponse.getErrorMessage());
        try {
            shiftsController.applyCancelCard(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()),actorUsername, productId);
            return new Response<>(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }
}
