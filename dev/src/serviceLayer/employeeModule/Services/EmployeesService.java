package serviceLayer.employeeModule.Services;


import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.Controllers.EmployeesController;
import businessLayer.employeeModule.Controllers.ShiftsController;
import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import businessLayer.employeeModule.Shift.ShiftType;
import objects.transportObjects.Driver;
import objects.transportObjects.Site;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Objects.SEmployee;
import serviceLayer.employeeModule.Objects.SShift;
import serviceLayer.employeeModule.Objects.SShiftType;
import serviceLayer.transportModule.ResourceManagementService;
import utils.JsonUtils;
import utils.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeesService {
    private static UserService userService;
    private ResourceManagementService rms;
    private EmployeesController employeesController;
    private ShiftsController shiftsController;

    public EmployeesService(ServiceFactory serviceFactory, EmployeesController employeesController, ShiftsController shiftsController) {
        rms = serviceFactory.getResourceManagementService();
        userService = serviceFactory.userService();
        this.employeesController = employeesController;
        this.shiftsController = shiftsController;
    }

    /**
     * This method returns a list containing the ids of the drivers in the shift that occurs at the specified date and time.
     * @param atDateTime The needed date and time
     * @return A serialized response containing a list with all the ids of the available drivers, if any exists.
     * Otherwise, if no drivers are available at the specified date and time, returns a matching error response.
     */
    //TODO: Test this method
    public String getAvailableDrivers(String atDateTime) {
        LocalDateTime dateTime = JsonUtils.deserialize(atDateTime,LocalDateTime.class);
        try {
            List<Employee> availableDrivers = employeesController.getAvailableDrivers(dateTime);
            return new Response(true, availableDrivers.stream().map(x->x.getId()).collect(Collectors.toList())).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    /**
     * This method checks if there is a storekeeper at the specified date and branch. (DateTime?)
     * @param dateToCheck The needed date
     * @param branchAddress The needed branch address
     * @return A success response if there is an available storekeeper.
     * Otherwise, returns a matching error response.
     */
    //TODO: Test this method
    public String checkStoreKeeperAvailability(String dateToCheck, String branchAddress) {
        LocalDateTime date = JsonUtils.deserialize(dateToCheck,LocalDateTime.class);
        try {
            employeesController.checkStoreKeeperAvailability(date.toLocalDate(), branchAddress);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    /**
     * This method loads the initial employee data from the system, during the initial load of the system.
     */
    public void createData() {
        resetData();
        try {
            // Initializing Branches - TODO: Should be moved to Transport module
            rms.addSite(new Site("Headquarters","1","123456789","Headquarters", Site.SiteType.BRANCH).toJson());
            for(int i = 2; i <= 9; i++) {
                String branchId = Integer.toString(i);
                rms.addSite(new Site("Zone" + i,branchId, "phone" + i,"contact"+i, Site.SiteType.BRANCH).toJson());
            }
            // TODO: Add initial employees data, after the Transport Sites have already been added
            employeesController.recruitEmployee("1","Moshe Biton", "111","Hapoalim 12 230", 50, LocalDate.of(2023,2,2),"Employment Conditions Test", "More details about Moshe");
            employeesController.certifyEmployee("111", Role.ShiftManager);
            employeesController.certifyEmployee("111",Role.Storekeeper);
            userService.createUser("admin123","111","1234");
        } catch (Exception ignore) {
            Exception x = ignore;
        }
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

    public String certifyDriver(String actorUsername, String employeeId, String driverLicense) {
        Response authResponse = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse.success() == false)
            return new Response(authResponse.message(),false).toJson();
        else if(authResponse.dataToBoolean() == false)
            return new Response("User isn't authorized to do this",false).toJson();
        try {
            employeesController.certifyEmployee(employeeId, Role.Driver);
            Employee driver = employeesController.getEmployee(employeeId);
            rms.addDriver(new Driver(employeeId,driver.getName(), Driver.LicenseType.valueOf(driverLicense)).toJson());
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
            Employee employee = employeesController.getBranchEmployee(branchId, actorUsername);
            shiftsController.requestShift(employee, branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role));
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
       }
    }

    public String cancelShiftRequest(String actorUsername, String branchId, LocalDate shiftDate, SShiftType shiftType, String role) {
        try {
            Employee employee = employeesController.getBranchEmployee(branchId, actorUsername);
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
            List<Employee> employees = employeesController.getBranchEmployees(branchId, employeeIds);
            shiftsController.setShiftEmployees(branchId, shiftDate, ShiftType.valueOf(shiftType.toString()), Role.valueOf(role), employees);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String createBranch(String actorUsername, String branchId) {
        Response authResponse1 = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.TransportManager));
        Response authResponse2 = Response.fromJson(userService.isAuthorized(actorUsername, Authorization.HRManager));
        if (authResponse1.success() == false && authResponse2.success() == false)
            return new Response(authResponse1.message() + ", " + authResponse2.message(),false).toJson();
        else if(authResponse1.dataToBoolean() == false && authResponse2.dataToBoolean() == false)
            return new Response("User isn't authorized to create branches",false).toJson();
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
