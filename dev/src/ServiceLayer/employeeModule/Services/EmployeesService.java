package ServiceLayer.employeeModule.Services;

import BusinessLayer.employeeModule.Authorization;
import BusinessLayer.employeeModule.Employee;
import BusinessLayer.employeeModule.Role;
import BusinessLayer.employeeModule.Shift;
import BusinessLayer.employeeModule.Controllers.EmployeesController;
import BusinessLayer.employeeModule.Controllers.ShiftsController;
import utils.JsonUtils;
import utils.Response;
import ServiceLayer.employeeModule.Objects.SEmployee;
import ServiceLayer.employeeModule.Objects.SShift;
import ServiceLayer.employeeModule.Objects.SShiftType;
import BusinessLayer.employeeModule.Shift.ShiftType;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    //TODO:
    // implement the following methods:
    // ============================================================================= |

    public String getAvailableDrivers(String atDateTime){
        LocalDateTime dateTime = JsonUtils.deserialize(atDateTime,LocalDateTime.class);
        //TODO: implement this
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String checkStoreKeeperAvailability(String dateToCheck, String branchAddress){
        LocalDate date = JsonUtils.deserialize(dateToCheck,LocalDate.class);
        //TODO: implement this
        throw new UnsupportedOperationException("Not implemented yet");
    }

    //<TODO - - - - - - - - - - - - - - - - - -  - - - - - - />

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

    public String recruitEmployee(String actorUsername, String fullName, String branchId, String employeeId, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.recruitEmployee(branchId, fullName, employeeId, bankDetails, hourlyRate, employmentDate, employmentConditions, details);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String addEmployeeToBranch(String actorUsername, String employeeId, String branchId){
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.addEmployeeToBranch(branchId, employeeId);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String certifyEmployee(String actorUsername, String employeeId, String role) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.certifyEmployee(employeeId, Role.valueOf(role));
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String uncertifyEmployee(String actorUsername, String employeeId, String role) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.uncertifyEmployee(employeeId, Role.valueOf(role));
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String requestShift(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role) {
        try {
            Employee employee = employeesController.getEmployee(branchId, actorUsername);
            shiftsController.requestShift(employee, branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role));
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
       }
    }

    public String cancelShiftRequest(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role) {
        try {
            Employee employee = employeesController.getEmployee(branchId, actorUsername);
            shiftsController.cancelShiftRequest(employee, branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role));
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String createWeekShifts(String actorUsername, String branchId, LocalDate weekStart) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            shiftsController.createWeekShifts(branchId, weekStart);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String deleteShift(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            shiftsController.deleteShift(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()));
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setShiftNeededAmount(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role, int amount) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            shiftsController.setShiftNeededAmount(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role), amount);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getWeekShifts(String actorUsername, String branchId, LocalDate weekStart) {
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
            return new Response(true,result).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String setShiftEmployees(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role, List<String> employeeIds) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            List<Employee> employees = employeesController.getEmployees(branchId, employeeIds);
            shiftsController.setShiftEmployees(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role), employees);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String createBranch(String actorUsername, String branchId) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.createBranch(branchId);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getEmployeeShifts(String actorUsername) {
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
            return new Response(true,result).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getEmployee(String actorUsername) {
        try {
            Employee employee = employeesController.getEmployee(actorUsername);
            return new Response(true,new SEmployee(employee)).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String approveShift(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            shiftsController.approveShift(branchId,shiftDate, ShiftType.valueOf(shiftType.toString()));
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String applyCancelCard(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String productId) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.ShiftManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            shiftsController.applyCancelCard(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()),actorUsername, productId);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String updateEmployeeSalary(String actorUsername, String employeeId, double hourlySalaryRate, double salaryBonus) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.updateEmployeeSalary(employeeId, hourlySalaryRate, salaryBonus);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String updateEmployeeBankDetails(String actorUsername, String employeeId, String bankDetails){
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.updateEmployeeBankDetails(employeeId, bankDetails);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String updateEmployeeEmploymentConditions(String actorUsername, String employeeId, String employmentConditions){
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.updateEmployeeEmploymentConditions(employeeId, employmentConditions);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String updateEmployeeDetails(String actorUsername, String employeeId, String details){
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.updateEmployeeDetails(employeeId, details);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String reportShiftActivity(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String activity) {
        // TODO: Possibly need to check the actor authorization
        try {
            shiftsController.reportShiftActivity(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), actorUsername, activity);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String updateBranchWorkingHours(String actorUsername , String branchId, LocalTime morningStart, LocalTime morningEnd, LocalTime eveningStart, LocalTime eveningEnd){
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.updateBranchWorkingHours(branchId, morningStart, morningEnd, eveningStart, eveningEnd);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }
}
