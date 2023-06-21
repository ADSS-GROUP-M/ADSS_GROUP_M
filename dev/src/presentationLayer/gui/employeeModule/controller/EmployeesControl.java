package presentationLayer.gui.employeeModule.controller;

import exceptions.ErrorOccurredException;
import presentationLayer.gui.employeeModule.model.ObservableEmployee;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import serviceLayer.employeeModule.Objects.SEmployee;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import utils.Response;

import java.time.LocalDate;

public class EmployeesControl extends AbstractControl {

    private final EmployeesService employeesService;

    public EmployeesControl(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @Override
    public void add(ObservableUIElement observable, ObservableModel model) {
        ObservableEmployee employeeModel = (ObservableEmployee) model;
        employeeModel.notifyObservers();
    }

    public String certifyEmployee(String employeeId, String role) {
        String json = employeesService.certifyEmployee(UserService.HR_MANAGER_USERNAME,employeeId,role);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Certified the employee successfully.";
    }

    public String uncertifyEmployee(String employeeId, String role) {
        String json = employeesService.uncertifyEmployee(UserService.HR_MANAGER_USERNAME,employeeId,role);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Uncertified the employee successfully.";
    }

    public String recruitEmployee(String branchId, String employeeName, String employeeId, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String employeeDetails) {
        String json = employeesService.recruitEmployee(UserService.HR_MANAGER_USERNAME, branchId, employeeName, employeeId,bankDetails, hourlyRate, employmentDate, employmentConditions, employeeDetails);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Recruited the employee successfully.";
    }

    public Object findEmployee(String employeeId) {
        String json = employeesService.getEmployee(employeeId);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
            return response.data(SEmployee.class);
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
    }

    public String updateEmployeeEmploymentConditions(String employeeId, String employmentConditions) {
        String json = employeesService.updateEmployeeEmploymentConditions(UserService.HR_MANAGER_USERNAME,employeeId,employmentConditions);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Updated employee's employment conditions successfully.";
    }

    public String updateEmployeeSalary(String employeeId, double hourlySalaryRate, double salaryBonus) {
        String json = employeesService.updateEmployeeSalary(UserService.HR_MANAGER_USERNAME,employeeId,hourlySalaryRate, salaryBonus);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Updated employee's salary successfully.";
    }

    public String updateEmployeeBankDetails(String employeeId, String bankDetails) {
        String json = employeesService.updateEmployeeBankDetails(UserService.HR_MANAGER_USERNAME,employeeId,bankDetails);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Updated employee's bank details successfully.";
    }

    public String updateEmployeeDetails(String employeeId, String employeeDetails) {
        String json = employeesService.updateEmployeeDetails(UserService.HR_MANAGER_USERNAME,employeeId,employeeDetails);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Updated employee's details successfully.";
    }
}
