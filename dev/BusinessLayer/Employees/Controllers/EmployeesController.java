package dev.BusinessLayer.Employees.Controllers;

import java.time.LocalDate;
import java.util.*;

import dev.BusinessLayer.Employees.Branch;
import dev.BusinessLayer.Employees.Employee;
import dev.BusinessLayer.Employees.Role;

public class EmployeesController {
    private Map<String, Branch> branches;
    private Map<Branch,Map<String, Employee>> employees;
    private static EmployeesController instance;
    private ShiftsController shiftsController;

    private EmployeesController(){
        this.branches = new HashMap<>();
        this.employees = new HashMap<>();
        this.shiftsController = ShiftsController.getInstance();
    }

    public static EmployeesController getInstance() {
        if(instance == null)
            instance = new EmployeesController();
        return instance;
    }

    public void resetData() {
        this.branches.clear();
        this.employees.clear();
    }

    public Branch getBranch(String branchId) throws Exception {
        if (!this.branches.containsKey(branchId))
            throw new Exception("The branch " + branchId + " was not found.");
        return this.branches.get(branchId);
    }

    public Employee getEmployee(String employeeId) throws Exception {
        for (Map<String, Employee> branch : employees.values()) {
            if (branch.containsKey(employeeId))
                return branch.get(employeeId);
        }
        throw new Exception("The employee " + employeeId + " was not found in the system.");
    }

    public Employee getEmployee(String branchId, String employeeId) throws Exception {
        Branch branch = getBranch(branchId);
        if (!this.employees.containsKey(branch))
            throw new Exception("The branch " + branchId + " was not found.");
        Map<String, Employee> branchEmployees = this.employees.get(branch);
        if (!branchEmployees.containsKey(employeeId))
            throw new Exception("The employee " + employeeId + " was not found in the branch.");
        return branchEmployees.get(employeeId);
    }

    public List<Employee> getEmployees(String branchId, List<String> employeeIds) throws Exception {
        List<Employee> result = new ArrayList<>();
        for (String employeeId : employeeIds) {
            Employee employee = getEmployee(branchId, employeeId);
            result.add(employee);
        }
        return result;
    }

    public void createBranch(String branchId) throws Exception {
        if (this.branches.containsKey(branchId))
            throw new Exception("The branch " + branchId + " already exists in the system.");
        Branch branch = new Branch();
        this.branches.put(branchId, branch);
        this.employees.put(branch, new HashMap<>());
        shiftsController.createBranch(branchId);
    }

    public void recruitEmployee(String branchId, String fullName, String employeeId, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) throws Exception {
        Branch branch = getBranch(branchId);
        if(this.employees.get(branch).containsKey(employeeId))
            throw new Exception("This employee id already exists in the system.");
        Employee employee = new Employee(fullName, employeeId, hourlyRate, bankDetails, employmentDate, employmentConditions, details);
        this.employees.get(branch).put(employeeId, employee);
        this.certifyEmployee(employee.getId(), Role.GeneralWorker);
    }

    public void updateEmployeeSalary(String employeeId, double hourlySalaryRate, double salaryBonus) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.setHourlySalaryRate(hourlySalaryRate);
        employee.setSalaryBonus(salaryBonus);
    }

    public void updateEmployeeBankDetails(String employeeId, String bankDetails) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.setBankDetails(bankDetails);
    }

    public void updateEmployeeEmploymentConditions(String employeeId, String employmentConditions) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.setEmploymentConditions(employmentConditions);
    }

    public void updateEmployeeDetails(String employeeId, String details) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.setDetails(details);
    }

    public void certifyEmployee(String employeeId, Role role) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.addRole(role);
    }

    public void uncertifyEmployee(String employeeId, Role role) throws Exception {
        Employee employee = getEmployee(employeeId);
        employee.removeRole(role);
    }

    //public void fireEmployee(String empId) throws Exception {
    //    Employee e = this.getEmployee(empId);
    //    //e.setIsFired(true);
    //}

    //public void signUpEmployeeToShift(String empId, String shiftTimeString, LocalDate shiftDate, String branchName, String role)throws Exception{// in a branch
    //    Branch branch = this.stringToBranch(branchName);
    //    ShiftTime shiftTime = stringToShiftTime(shiftTimeString);
    //    Employee employee = this.getEmployee(empId);
    //    if(employee.getIsFired())
    //        throw new Exception("cant do this, this employee is fired!");
    //    Role r = this.stringToRole(role);
    //    List<Role> roles = new ArrayList<>();
    //    roles.add(r);
    //    Shift shift = this.getShift(shiftTime, shiftDate, branch);
    //    if(!shift.registerEmployee(shiftTime, shiftDate, branch, employee, roles))
    //        throw new Exception("Failed registering to shift, due to illegal scheduling");
    //}
//
    //public void setRequiredRolesInAShift(LocalDate shiftDate, String branchName, String shiftTime, String roles) throws Exception{ // roles format example: "1 cashier,4 general worker"
    //    Branch br = this.stringToBranch(branchName);
    //    ShiftTime st = this.stringToShiftTime(shiftTime);
    //    Shift s = this.getShift(st, shiftDate, br);
    //    String[] splitted = roles.split(",", -1);
    //    HashMap<Role,Integer> rolesMap = new HashMap<>();
    //    for(String str: splitted){
    //        String[] numToRole = str.split(" ", 2);
    //        Role curRole = this.stringToRole(numToRole[1]);
    //        Integer num = Integer.parseInt(numToRole[0]);
    //        rolesMap.put(curRole, num);
    //    }
    //    s.setNeededRoles(rolesMap);
    //}
//
    //public String getShiftDescription(LocalDate shiftDate, String branchName, String shiftTime) throws Exception{
    //    Branch br = this.stringToBranch(branchName);
    //    ShiftTime st = this.stringToShiftTime(shiftTime);
    //    Shift s = this.getShift(st, shiftDate, br);
//
    //    String desc = "";
    //    desc+=s.toString();
    //    return desc;
    //}
//
    //public void setUnavailableDate(String empId, int year, int month, int day) throws Exception{
    //    Employee emp = this.getEmployee(empId);
    //    if(emp.getIsFired())
    //        throw new Exception("employee is fired, cant do this");
    //    LocalDate d = this.getDate(year, month, day);
    //    emp.addUnavailableDate(d);
    //}
//
    //public void setAvailableDate(String empId, int year, int month, int day) throws Exception{
    //    Employee emp = this.getEmployee(empId);
    //    if(emp.getIsFired())
    //        throw new Exception("employee is fired, cant do this");
    //    LocalDate d = this.getDate(year, month, day);
    //    emp.setAvailableDate(d);
    //}
//
    //public void applyCancelCardNow(String applicationByEmpId) throws Exception{
    //    Employee e = this.getEmployee(applicationByEmpId);
    //    LocalDate d = LocalDate.now();
    //    for(Branch b: Branch.getAllBranches()){
    //        Integer[] mor = b.getWorkingHours(ShiftTime.MORNING);
    //        Integer[] eve = b.getWorkingHours(ShiftTime.EVENING);
    //        Integer[] cur = new Integer[]{LocalTime.now().getHour(), LocalTime.now().getMinute()};
    //        ShiftTime st = null;
    //        if(cur[0]>=mor[0] && cur[0]<mor[1])
    //            st = ShiftTime.MORNING;
    //        else if(cur[0]>=eve[0] && cur[0]<eve[1])
    //            st = ShiftTime.EVENING;
    //        if(st!=null){
    //            Shift s = Shift.getInstance(st, d, b);
    //            if(s.isEmployeeWorking(e)){
    //                s.useCancelCard();
    //                return;
    //            }
    //        }
    //    }
    //    throw new Exception("This user is not currently working in any branch");
    //}
    //public boolean isCurrentShiftManager(Employee e)throws Exception{
    //    Shift s = Shift.getInstance(null, null, null);
    //    throw new Exception("unfinished functionality");
    //}
//
    //public void setEmployeeSalary(String empId, int x) throws Exception{
    //    Employee e = this.getEmployee(empId);
    //    e.setSalary(x);
    //}
//
    //public String getEmployeeDescription(String empId) throws Exception{
    //    Employee e = this.getEmployee(empId);
    //    return e.toString();
    //}
//
    //public Employee getEmployee(String id) throws Exception{
    //    if (!employees.containsKey(id))
    //        throw new Exception("The given employee ID wasn't found in the system.");
    //    return employees.get(id);
    //}
//
    //private Branch stringToBranch(String br) throws Exception{
    //    Branch b = Branch.valueOf(br);
    //    if(b==null)
    //        throw new Exception("invalid branch!");
    //    return b;
    //}
}