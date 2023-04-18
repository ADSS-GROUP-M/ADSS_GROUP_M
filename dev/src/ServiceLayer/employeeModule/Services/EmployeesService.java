package ServiceLayer.employeeModule.Services;

import BusinessLayer.employeeModule.Authorization;
import BusinessLayer.employeeModule.Employee;
import BusinessLayer.employeeModule.Role;
import BusinessLayer.employeeModule.Shift;
import BusinessLayer.employeeModule.Controllers.EmployeesController;
import BusinessLayer.employeeModule.Controllers.ShiftsController;
import utils.Response;
import Objects.employeeObjects.SEmployee;
import Objects.employeeObjects.SShift;
import Objects.employeeObjects.SShiftType;
import BusinessLayer.employeeModule.Shift.ShiftType;

import java.time.LocalDate;
import java.time.LocalTime;
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
            employeesController.certifyEmployee("111", Role.ShiftManager);
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

    public Response recruitEmployee(String actorUsername, String fullName, String branchId, String employeeId, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.recruitEmployee(branchId, fullName, employeeId, bankDetails, hourlyRate, employmentDate, employmentConditions, details);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response addEmployeeToBranch(String actorUsername, String employeeId, String branchId){
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.addEmployeeToBranch(branchId, employeeId);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response certifyEmployee(String actorUsername, String employeeId, String role) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.certifyEmployee(employeeId, Role.valueOf(role));
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response uncertifyEmployee(String actorUsername, String employeeId, String role) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.uncertifyEmployee(employeeId, Role.valueOf(role));
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response requestShift(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role) {
        try {
            Employee employee = employeesController.getEmployee(branchId, actorUsername);
            shiftsController.requestShift(employee, branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role));
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
       }
    }

    public Response cancelShiftRequest(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role) {
        try {
            Employee employee = employeesController.getEmployee(branchId, actorUsername);
            shiftsController.cancelShiftRequest(employee, branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role));
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response createWeekShifts(String actorUsername, String branchId, LocalDate weekStart) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            shiftsController.createWeekShifts(branchId, weekStart);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response deleteShift(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            shiftsController.deleteShift(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()));
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response setShiftNeededAmount(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role, int amount) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            shiftsController.setShiftNeededAmount(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role), amount);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response getWeekShifts(String actorUsername, String branchId, LocalDate weekStart) {
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
            return new Response(true,result);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response setShiftEmployees(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role, List<String> employeeIds) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            List<Employee> employees = employeesController.getEmployees(branchId, employeeIds);
            shiftsController.setShiftEmployees(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role), employees);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response createBranch(String actorUsername, String branchId) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.createBranch(branchId);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response getEmployeeShifts(String actorUsername) {
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
            return new Response(true,result);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response getEmployee(String actorUsername) {
        try {
            Employee employee = employeesController.getEmployee(actorUsername);
            return new Response(true,new SEmployee(employee));
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response approveShift(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            shiftsController.approveShift(branchId,shiftDate, ShiftType.valueOf(shiftType.toString()));
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response applyCancelCard(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String productId) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.ShiftManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            shiftsController.applyCancelCard(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()),actorUsername, productId);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response updateEmployeeSalary(String actorUsername, String employeeId, double hourlySalaryRate, double salaryBonus) {
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.updateEmployeeSalary(employeeId, hourlySalaryRate, salaryBonus);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response updateEmployeeBankDetails(String actorUsername, String employeeId, String bankDetails){
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.updateEmployeeBankDetails(employeeId, bankDetails);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response updateEmployeeEmploymentConditions(String actorUsername, String employeeId, String employmentConditions){
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.updateEmployeeEmploymentConditions(employeeId, employmentConditions);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response updateEmployeeDetails(String actorUsername, String employeeId, String details){
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.updateEmployeeDetails(employeeId, details);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response reportShiftActivity(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String activity) {
        // TODO: Possibly need to check the actor authorization
        try {
            shiftsController.reportShiftActivity(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), actorUsername, activity);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response updateBranchWorkingHours(String actorUsername , String branchId, LocalTime morningStart, LocalTime morningEnd, LocalTime eveningStart, LocalTime eveningEnd){
        Response authResponse = userService.isAuthorized(actorUsername, Authorization.HRManager);
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false);
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false);
        try {
            employeesController.updateBranchWorkingHours(branchId, morningStart, morningEnd, eveningStart, eveningEnd);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }
}
