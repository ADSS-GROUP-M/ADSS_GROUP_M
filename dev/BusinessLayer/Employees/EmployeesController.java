package dev.BusinessLayer.Employees;

import java.time.LocalDate;
import java.util.*;

import dev.BusinessLayer.Employees.Shift.ShiftTime;

public class EmployeesController {

    private Map<String,Employee> employees;
    private static EmployeesController instance;

    private EmployeesController(){
        employees = new HashMap<>();
    }

    public static EmployeesController getInstance() {
        if(instance == null)
            instance = new EmployeesController();
        return instance;
    }

    public void recruitEmployee(String name, String id, BankDetails bd, List<EmploymentConditions> ec, LocalDate employmentDate) {
            Employee emp = new Employee(name,id,bd,ec, employmentDate);
            this.employees.put(id, emp);
    }

    public void fireEmployee(String empId) throws Exception {
        Employee e = this.getEmployee(empId);
        e.setIsFired(true);
    }

    public void signUpEmployeeToShift(String empId, String shiftTimeString, LocalDate shiftDate, String branchName, String role)throws Exception{// in a branch
        Branch branch = this.stringToBranch(branchName);
        ShiftTime shiftTime = stringToShiftTime(shiftTimeString);
        Employee employee = this.getEmployee(empId);
        if(employee.getIsFired())
            throw new Exception("cant do this, this employee is fired!");
        Role r = this.stringToRole(role);
        List<Role> roles = new ArrayList<>();
        roles.add(r);
        Shift shift = this.getShift(shiftTime, shiftDate, branch);
        if(!shift.registerEmployee(shiftTime, shiftDate, branch, employee, roles))
            throw new Exception("Failed registering to shift, due to illegal scheduling");
    }

    public void setRequiredRolesInAShift(LocalDate shiftDate, String branchName, String shiftTime, String roles) throws Exception{ // roles format example: "1 cashier,4 general worker"
        Branch br = this.stringToBranch(branchName);
        ShiftTime st = this.stringToShiftTime(shiftTime);
        Shift s = this.getShift(st, shiftDate, br);
        String[] splitted = roles.split(",", -1);
        HashMap<Role,Integer> rolesMap = new HashMap<>();
        for(String str: splitted){
            String[] numToRole = str.split(" ", 2);
            Role curRole = this.stringToRole(numToRole[1]);
            Integer num = Integer.parseInt(numToRole[0]);
            rolesMap.put(curRole, num);
        }
        s.setNeededRoles(rolesMap);
    }

    public String getShiftDescription(LocalDate shiftDate, String branchName, String shiftTime) throws Exception{
        Branch br = this.stringToBranch(branchName);
        ShiftTime st = this.stringToShiftTime(shiftTime);
        Shift s = this.getShift(st, shiftDate, br);

        String desc = "";
        desc+=s.toString();
        return desc;
    }

    public void setUnavailableDate(String empId, int year, int month, int day) throws Exception{
        Employee emp = this.getEmployee(empId);
        if(emp.getIsFired())
            throw new Exception("employee is fired, cant do this");
        LocalDate d = this.getDate(year, month, day);
        emp.addUnavailableDate(d);  
    }

    public void setAvailableDate(String empId, int year, int month, int day) throws Exception{
        Employee emp = this.getEmployee(empId);
        if(emp.getIsFired())
            throw new Exception("employee is fired, cant do this");
        LocalDate d = this.getDate(year, month, day);
        emp.setAvailableDate(d);  
    }

    public void applyCancelCardNow(String applicationByEmpId) throws Exception{
        Employee e = this.getEmployee(applicationByEmpId);
        LocalDate d = LocalDate.now();
        for(Branch b: Branch.getAllBranches()){
            Integer[] mor = b.getWorkingHours(ShiftTime.MORNING);
            Integer[] eve = b.getWorkingHours(ShiftTime.EVENING);
            Integer[] cur = Date.getCurrentTime();
            ShiftTime st = null;
            if(cur[0]>=mor[0] && cur[0]<mor[1])
                st = ShiftTime.MORNING;
            else if(cur[0]>=eve[0] && cur[0]<eve[1])
                st = ShiftTime.EVENING;
            if(st!=null){
                Shift s = Shift.getInstance(st, d, b);
                if(s.isEmployeeWorking(e)){
                    s.useCancelCard();
                    return;
                }
            }
        }
        throw new Exception("This user is not currently working in any branch");
    }
    public boolean isCurrentShiftManager(Employee e)throws Exception{
        Shift s = Shift.getInstance(null, null, null);
        throw new Exception("unfinished functionality");
    }

    public void setEmployeeSalary(String empId, int x) throws Exception{
        Employee e = this.getEmployee(empId);
        e.setSalary(x);
    }

    public String getEmployeeDescription(String empId) throws Exception{
        Employee e = this.getEmployee(empId);
        return e.toString();
    }

    public Employee getEmployee(String id) throws Exception{
        if (!employees.containsKey(id))
            throw new Exception("The given employee ID wasn't found in the system.");
        return employees.get(id);
    }

    private Branch stringToBranch(String br) throws Exception{
        Branch b = Branch.valueOf(br);
        if(b==null)
            throw new Exception("invalid branch!");
        return b;
    }

    private Role stringToRole(String rl) throws Exception{
        Role r = Role.valueOf(rl);
        if(r==null)
            throw new Exception("invalid role!");
        return r;
    }

    private ShiftTime stringToShiftTime(String s) throws Exception{
        ShiftTime st = ShiftTime.valueOf(s);
        if(st == null)
            throw new Exception("invalid shift time!");
        return st;
    }

    private LocalDate getDate(int year, int month, int day) throws Exception{
       LocalDate d = Date.getInstance(LocalDate.of(year,month,day));
       if(d == null)
          throw new Exception("invalid date!");
        return d;
    }
    private Shift getShift(ShiftTime st, LocalDate da, Branch br) throws Exception{
        Shift s = Shift.getInstance(st, da, br);
        if(s==null)
            throw new Exception("invalid shift");
        return s;
    }

    
}