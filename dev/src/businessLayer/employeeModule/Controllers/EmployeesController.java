package businessLayer.employeeModule.Controllers;

import businessLayer.employeeModule.Branch;
import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import businessLayer.employeeModule.Shift.ShiftType;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.employeeModule.BranchesDAO;
import dataAccessLayer.employeeModule.EmployeeDAO;
import javafx.util.Pair;
import utils.employeeUtils.EmployeeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Branch getBranch(String branchId) throws EmployeeException {
        try {
            return this.branchesDAO.select(branchId);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public Employee getEmployee(String employeeId) throws EmployeeException {
        try {
            return this.employeeDAO.select(employeeId);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public Employee getBranchEmployee(String branchId, String employeeId) throws EmployeeException {
        try {
            return employeeDAO.select(branchesDAO.selectBranchEmployee(branchId, employeeId).getValue());
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public List<Employee> getBranchEmployees(String branchId, List<String> employeeIds) throws EmployeeException {
        try {
            List<Pair<String, String>> branchEmployees = branchesDAO.selectBranchEmployees(branchId, employeeIds);
            List<Employee> result = new ArrayList<>();
            for (Pair<String,String> branchEmployee : branchEmployees) {
                result.add(employeeDAO.select(branchEmployee.getValue()));
            }
            return result;
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void createBranch(String branchId) throws EmployeeException {
        Branch branch = new Branch(branchId);
        try {
            branchesDAO.insert(branch);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void recruitEmployee(String branchId, String fullName, String employeeId, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) throws EmployeeException {
        try{
            Employee employee = new Employee(fullName, employeeId,bankDetails,hourlyRate, employmentDate, employmentConditions, details);
            employeeDAO.insert(employee);
            branchesDAO.insertEmployee(branchId, employeeId);
            this.certifyEmployee(employee.getId(), Role.GeneralWorker);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }
    
    public void addEmployeeToBranch(String branchId, String employeeId) throws EmployeeException {
        try {
            this.branchesDAO.insertEmployee(branchId, employeeId);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void updateEmployeeSalary(String employeeId, double hourlySalaryRate, double salaryBonus) throws EmployeeException {
        Employee employee = getEmployee(employeeId);
        employee.setHourlySalaryRate(hourlySalaryRate);
        employee.setSalaryBonus(salaryBonus);
        try {
            employeeDAO.update(employee);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void updateEmployeeBankDetails(String employeeId, String bankDetails) throws EmployeeException {
        Employee employee = getEmployee(employeeId);
        employee.setBankDetails(bankDetails);
        try {
            employeeDAO.update(employee);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void updateEmployeeEmploymentConditions(String employeeId, String employmentConditions) throws EmployeeException {
        Employee employee = getEmployee(employeeId);
        employee.setEmploymentConditions(employmentConditions);
        try {
            employeeDAO.update(employee);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void updateEmployeeDetails(String employeeId, String details) throws EmployeeException {
        Employee employee = getEmployee(employeeId);
        employee.setDetails(details);
        try {
            employeeDAO.update(employee);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void certifyEmployee(String employeeId, Role role) throws EmployeeException {
        Employee employee = getEmployee(employeeId);
        employee.addRole(role);
        try {
            employeeDAO.update(employee);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void uncertifyEmployee(String employeeId, Role role) throws EmployeeException {
        Employee employee = getEmployee(employeeId);
        employee.removeRole(role);
        try {
            employeeDAO.update(employee);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void updateBranchWorkingHours(String branchId, LocalTime morningStart, LocalTime morningEnd, LocalTime eveningStart, LocalTime eveningEnd) throws EmployeeException {
        try{
            Branch branch = branchesDAO.select(branchId);
            branch.setWorkingHours(morningStart, morningEnd, eveningStart, eveningEnd);
            branchesDAO.update(branch);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public List<Employee> getAvailableDrivers(LocalDateTime dateTime) throws EmployeeException {
        List<Employee> availableDrivers = new ArrayList<>();
        List<Branch> branches;
        try {
            branches = branchesDAO.selectAll();
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }

        for (Branch branch : branches) {
            try{
                ShiftType shiftType = branch.getShiftType(dateTime.toLocalTime()); // Could throw an exception if the given time is not in any shift
                Map<Role, List<Employee>> shiftWorkers = shiftsController.getShift(branch.name(), dateTime.toLocalDate(), shiftType).getShiftWorkers();
                if (shiftWorkers.containsKey(Role.Driver)) {
                    availableDrivers.addAll(shiftWorkers.get(Role.Driver));
                }
            }catch (EmployeeException ignored){}
        }

        if (availableDrivers.isEmpty()) {
            throw new EmployeeException("Could not find any available driver at the specified dateTime.");
        }
        return availableDrivers;
    }

    public void checkStoreKeeperAvailability(LocalDate date, String branchAddress) throws EmployeeException {
        Branch branch = getBranch(branchAddress); // finds a branch by its name / branchId.
        Shift morningShift = shiftsController.getShift(branch.name(), date, ShiftType.Morning);
        Shift eveningShift = shiftsController.getShift(branch.name(), date, ShiftType.Morning);
        if (!morningShift.isStorekeeperWorking() || !eveningShift.isStorekeeperWorking()) {
            throw new EmployeeException("There is no available storekeeper at the given date in this branch.");
        }
    }
}