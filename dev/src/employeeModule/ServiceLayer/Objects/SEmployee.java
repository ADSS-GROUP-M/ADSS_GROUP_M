package employeeModule.ServiceLayer.Objects;

import dev.BusinessLayer.Employees.Employee;
import dev.Utils.JsonUtils;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class SEmployee {
    private String fullName;
    private String id;
    private LocalDate employmentDate;
    private String employmentConditions;
    private Set<String> roles;
    private String details;
    private double expectedSalary;

    public SEmployee(Employee employee) {
        this.fullName = employee.getName();
        this.id = employee.getId();
        this.employmentDate = employee.getEmploymentDate();
        this.employmentConditions = employee.getEmploymentConditions();
        this.roles = employee.getRoles().stream().map(Object::toString).collect(Collectors.toSet());
        this.details = employee.getDetails();
        this.expectedSalary = employee.calculateSalary();
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

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }
}
