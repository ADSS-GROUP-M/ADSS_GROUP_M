package dev.Employees.ServiceLayer;

import dev.Employees.BusinessLayer.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EmployeesService {
    private static EmployeesService instance;
    private static UserService userService;
    private EmployeesController employeeController;

    private EmployeesService() {
        userService = UserService.getInstance();
        employeeController = EmployeesController.getInstance();
    }

    public EmployeesService getInstance() {
        if (instance == null)
            instance = new EmployeesService();
        return instance;
    }

    public Response<String> recruitEmployee(String username, String firstName, String lastName, int id, int bankNumber, int branchNumber, List<EmploymentConditions> employmentConditions, LocalDate employmentDate) {
        Response<User> userResponse = userService.getUser(username);
        if (userResponse.errorOccurred())
            return Response.createErrorResponse(userResponse.getErrorMessage());
        User user = userResponse.getReturnValue();
        try {
            String[] userDetails = user.recruitEmployeeAndNewUser(firstName + " " + lastName, id, bankNumber, branchNumber, employmentConditions, employmentDate);
            String response = "Employee registered. User details for the new employee: \n username: " + userDetails[0] + " password: " + userDetails[1];
            return new Response<>(response);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<boolean> registerShift(String username, int empId, String branchName, String shiftTime, LocalDate shiftDate, String role) {
        Response<User> userResponse = userService.getUser(username);
        if (userResponse.errorOccurred())
            return Response.createErrorResponse(userResponse.getErrorMessage());
        User user = userResponse.getReturnValue();
        try {
            user.signUpEmployeeToShift(empId, shiftTime, shiftDate, branchName, role);
            return new Response(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }
}
