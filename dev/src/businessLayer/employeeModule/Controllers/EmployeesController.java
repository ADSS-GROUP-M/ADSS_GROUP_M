package businessLayer.employeeModule.Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import businessLayer.employeeModule.Branch;
import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import businessLayer.employeeModule.Shift.ShiftType;
import dataAccessLayer.employeeModule.BranchesDAO;
import dataAccessLayer.employeeModule.EmployeeDAO;
import javafx.util.Pair;

public class EmployeesController {
    //private Map<String, Branch> branches; //BranchID to branch
    //private Map<Branch,Map<String, Employee>> employees; // branch to <employeeID to Employee>
    private BranchesDAO branchesDAO;
    private EmployeeDAO employeeDAO;
    private ShiftsController shiftsController;

    public EmployeesController(ShiftsController shiftsController, BranchesDAO branchesDAO, EmployeeDAO employeeDAO){
        this.shiftsController = shiftsController;
        this.branchesDAO = branchesDAO;
        this.employeeDAO = employeeDAO;
    }

    public void resetData() {
        this.branchesDAO.clearTable();
        this.employeeDAO.clearTable();
    }

    public Branch getBranch(String branchId) throws Exception {
        return this.branchesDAO.select(branchId);
    }

    public Employee getEmployee(String employeeId) throws Exception {
        return this.employeeDAO.select(employeeId);
    }

    public Employee getBranchEmployee(String branchId, String employeeId) throws Exception {
        return employeeDAO.select(branchesDAO.selectBranchEmployee(branchId, employeeId).getValue());
    }

    public List<Employee> getBranchEmployees(String branchId, List<String> employeeIds) throws Exception {
        List<Pair<String,String>> branchEmployees = branchesDAO.selectBranchEmployees(branchId, employeeIds);
        List<Employee> result = new ArrayList<>();
        for (Pair<String,String> branchEmployee : branchEmployees) {
            result.add(employeeDAO.select(branchEmployee.getValue()));
        }
        return result;
    }

    public void createBranch(String branchId) throws Exception {
        Branch branch = new Branch(branchId);
        branchesDAO.insert(branch);
    }

    public void recruitEmployee(String branchId, String fullName, String employeeId, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) throws Exception {
        Employee employee = new Employee(fullName, employeeId,bankDetails,hourlyRate, employmentDate, employmentConditions, details);
        employeeDAO.insert(employee);
        branchesDAO.insertEmployee(branchId, employeeId);
        this.certifyEmployee(employee.getId(), Role.GeneralWorker);
    }
    
    public void addEmployeeToBranch(String branchId, String employeeId) throws Exception {
        this.branchesDAO.insertEmployee(branchId, employeeId);
    }

    public void updateEmployeeSalary(String employeeId, double hourlySalaryRate, double salaryBonus) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.setHourlySalaryRate(hourlySalaryRate);
        employee.setSalaryBonus(salaryBonus);
        employeeDAO.update(employee);
    }

    public void updateEmployeeBankDetails(String employeeId, String bankDetails) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.setBankDetails(bankDetails);
        employeeDAO.update(employee);
    }

    public void updateEmployeeEmploymentConditions(String employeeId, String employmentConditions) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.setEmploymentConditions(employmentConditions);
        employeeDAO.update(employee);
    }

    public void updateEmployeeDetails(String employeeId, String details) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.setDetails(details);
        employeeDAO.update(employee);
    }

    public void certifyEmployee(String employeeId, Role role) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.addRole(role);
        employeeDAO.update(employee);
    }

    public void uncertifyEmployee(String employeeId, Role role) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.removeRole(role);
        employeeDAO.update(employee);
    }

    public void updateBranchWorkingHours(String branchId, LocalTime morningStart, LocalTime morningEnd, LocalTime eveningStart, LocalTime eveningEnd) throws Exception {
        Branch branch = branchesDAO.select(branchId);
        branch.setWorkingHours(morningStart, morningEnd, eveningStart, eveningEnd);
        branchesDAO.update(branch);
    }

    public List<Employee> getAvailableDrivers(LocalDateTime dateTime) throws Exception {
        List<Employee> availableDrivers = new ArrayList<>();
        for (Branch branch : branchesDAO.selectAll()) {
            try {
                ShiftType shiftType = branch.getShiftType(dateTime.toLocalTime()); // Could throw an exception if the given time is not in any shift
                Map<Role, List<Employee>> shiftWorkers = shiftsController.getShift(branch.address(), dateTime.toLocalDate(), shiftType).getShiftWorkers();
                if (shiftWorkers.containsKey(Role.Driver))
                    availableDrivers.addAll(shiftWorkers.get(Role.Driver));
            } catch (Exception ignore) {}
        }
        if (availableDrivers.isEmpty())
            throw new Exception("Could not find any available driver at the specified dateTime.");
        return availableDrivers;
    }

    public void checkStoreKeeperAvailability(LocalDate date, String branchAddress) throws Exception {
        Branch branch = getBranch(branchAddress); // finds a branch by its address / branchId.
        Shift morningShift = shiftsController.getShift(branch.address(), date, ShiftType.Morning);
        Shift eveningShift = shiftsController.getShift(branch.address(), date, ShiftType.Morning);
        if (!morningShift.isStorekeeperWorking() || !eveningShift.isStorekeeperWorking()) {
            throw new Exception("There is no available storekeeper at the given date in this branch.");
        }
    }
}