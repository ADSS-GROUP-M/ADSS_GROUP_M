package businessLayer.employeeModule;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Employee {
    private String name;
    private String id;
    private String bankDetails;
    private double hourlySalaryRate;

    private double monthlyHours;
    private double salaryBonus;
    private LocalDate employmentDate;
    private String employmentConditions;
    private String details;
    private Set<Role> roles;

    public Employee(String name, String id,String bankDetails, double hourlySalaryRate, LocalDate employmentDate, String employmentConditions, String details){
       this.name = name;
       this.id = id;
       this.bankDetails = bankDetails;
       this.hourlySalaryRate = hourlySalaryRate;
       this.monthlyHours = 0;
       this.salaryBonus = 0;
       //calculateSalary();
       this.employmentDate = employmentDate;
       this.employmentConditions = employmentConditions;
       this.details = details;
       this.roles = new HashSet<>();
    }


    public String getId() {
        return this.id;
    }


    public String getName() {
        return this.name;
    }


    public LocalDate getEmploymentDate() {
        return this.employmentDate;
    }

    public String getEmploymentConditions() {
        return employmentConditions;
    }


    public void setEmploymentConditions(String employmentConditions) {
        this.employmentConditions = employmentConditions;
    }


    public String getDetails() {
        return details;
    }


    public String getBankDetails() {
        return this.bankDetails;
    }


    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void addRole(Role role) {
        roles.add(role);
    }


    public void removeRole(Role role) throws Exception {
        if (!roles.contains(role))
            throw new Exception("The given role: `" + role + "` is not one of the employee roles.");
        roles.remove(role);
    }


    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setHourlySalaryRate(double newRate){
       this.hourlySalaryRate = newRate;
    }


    public double getHourlySalaryRate() {
        return this.hourlySalaryRate;
    }


    public void setSalaryBonus(double salaryBonus) {
        this.salaryBonus = salaryBonus;
    }


    public double getSalaryBonus() {
        return this.salaryBonus;
    }


    public void addMonthlyHours(double hours) {
        this.monthlyHours += hours;
    }


    public void resetMonthlyHours() {
        this.monthlyHours = 0;
    }
    public double getMonthlyHours() {
        return this.monthlyHours;
    }


    public double calculateSalary() {
        return hourlySalaryRate * monthlyHours + salaryBonus;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMonthlyHours(double monthlyHours) {
        this.monthlyHours = monthlyHours;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public static Employee getLookupObject(String id) {
        return new Employee(null,id,null,0,null,null,null);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}