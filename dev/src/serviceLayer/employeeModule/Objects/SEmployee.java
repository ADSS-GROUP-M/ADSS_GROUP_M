package serviceLayer.employeeModule.Objects;

import businessLayer.employeeModule.Employee;
import utils.JsonUtils;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class SEmployee {
    private final String fullName;
    private final String id;
    private final LocalDate employmentDate;
    private final String employmentConditions;
    private final Set<String> roles;
    private final String details;
    private final double expectedSalary;
    private final double hourlySalaryRate;
    private final double salaryBonus;
    private final String bankDetails;

    public SEmployee(Employee employee) {
        this.fullName = employee.getName();
        this.id = employee.getId();
        this.employmentDate = employee.getEmploymentDate();
        this.employmentConditions = employee.getEmploymentConditions();
        this.roles = employee.getRoles().stream().map(Object::toString).collect(Collectors.toSet());
        this.details = employee.getDetails();
        this.expectedSalary = employee.calculateSalary();
        this.hourlySalaryRate = employee.getHourlySalaryRate();
        this.salaryBonus = employee.getSalaryBonus();
        this.bankDetails = employee.getBankDetails();
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public String getEmploymentConditions() {
        return employmentConditions;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getDetails() {
        return details;
    }

    public double getExpectedSalary() {
        return expectedSalary;
    }

    public double getHourlySalaryRate() {
        return hourlySalaryRate;
    }

    public double getSalaryBonus() {
        return salaryBonus;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }
}
