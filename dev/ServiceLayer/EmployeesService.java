package dev.ServiceLayer;

import dev.BusinessLayer.Employees.EmployeesController;
import dev.BusinessLayer.Employees.EmploymentConditions;
import dev.BusinessLayer.Employees.User;

import java.time.LocalDate;
import java.util.List;

public class EmployeesService {
    private static EmployeesService instance;
    private static UserService userService;
    private EmployeesController employeesController;

    private EmployeesService() {
        userService = UserService.getInstance();
        employeesController = EmployeesController.getInstance();
    }

    public static EmployeesService getInstance() {
        if (instance == null)
            instance = new EmployeesService();
        return instance;
    }

    public Response<String> recruitEmployee(String username, String firstName, String lastName, String id, int bankNumber, int branchNumber, List<EmploymentConditions> employmentConditions, LocalDate employmentDate) {
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

    public Response<Boolean> registerShift(String username, String empId, String branchName, String shiftTime, LocalDate shiftDate, String role) {
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
