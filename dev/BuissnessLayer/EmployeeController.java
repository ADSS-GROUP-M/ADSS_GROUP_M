package dev.BuissnessLayer;

import java.util.*;

import dev.BuissnessLayer.Shift.ShiftTime;

public class EmployeeController{

    private List<Employee> workingEmployees;
    private List<Employee> firedEmployees;
    private static EmployeeController instance;

    private EmployeeController(){
        workingEmployees = new LinkedList<Employee>();
        firedEmployees = new LinkedList<Employee>();
    }

    public static EmployeeController getInstance(User requestFrom)
    {
        if(instance == null)
            instance = new EmployeeController();
        return instance;
    }

    public void recruitEmployee(String name, int id, BankDetails bd, int salary, List<EmploymentConditions> ec, Date employmentDate) {
            Employee emp = new Employee(name,id,bd,salary,ec, employmentDate);
            this.workingEmployees.add(emp);
            return;
        
    }

    public void fireEmployee(int empId) throws Exception{

        for(Employee e: workingEmployees){
                if(e.getId() == empId){
                    firedEmployees.add(e);
                    workingEmployees.remove(e);
                    return;
                }
        }
        throw new Exception("Employee is not found or already fired");    
    }

    public void signUpEmployeeToShift(int empId,int year, int month, int day, String branchName, String shiftTime, String role)throws Exception{// in a branch
        Date da = this.getDate(year, month, day);
        Branch br = this.stringToBranch(branchName);
        ShiftTime st = this.stringToShiftTime(shiftTime);
        Employee emp = this.getEmployee(empId);
        if(emp.getIsFired())
            throw new Exception("cant do this, this employee is fired!");
        Role rl = this.stringToRole(role);
        List<Role> rls = new LinkedList<Role>();
        rls.add(rl);
        Shift shift = this.getShift(st, da, br);
        shift.registerEmployee(da, st, br, emp, rls);
    }

    public void setRequiredRolesInAShift(int year, int month, int day, String branchName, String shiftTime, String roles) throws Exception{ // roles format example: "1 cashier,4 general worker"
        Date da = this.getDate(year, month, day);
        Branch br = this.stringToBranch(branchName);
        ShiftTime st = this.stringToShiftTime(shiftTime);
        Shift s = this.getShift(st, da, br);
        String[] splitted = roles.split(",", -1);
        HashMap<Role,Integer> rolesMap = new HashMap<Role,Integer>();
        for(String str: splitted){
            String[] numToRole = str.split(" ", 2);
            Role curRole = this.stringToRole(numToRole[1]);
            Integer num = Integer.parseInt(numToRole[0]);
            rolesMap.put(curRole, num);
        }
        s.setNeededRoles(rolesMap);
    }

    public String getShiftDescription(int year, int month, int day, String branchName, String shiftTime) throws Exception{
        Date da = this.getDate(year, month, day);
        Branch br = this.stringToBranch(branchName);
        ShiftTime st = this.stringToShiftTime(shiftTime);
        Shift s = this.getShift(st, da, br);

        String desc = "";
        desc+=s.toString();
        return desc;
    }

    public void setUnavailableDate(int empId, int year, int month, int day) throws Exception{
        Employee emp = this.getEmployee(empId);
        if(emp.getIsFired())
            throw new Exception("employee is fired, cant do this");
        Date d = this.getDate(year, month, day);
        emp.addUnavailableDate(d);  
    }

    public void setAvailableDate(int empId, int year, int month, int day) throws Exception{
        Employee emp = this.getEmployee(empId);
        if(emp.getIsFired())
            throw new Exception("employee is fired, cant do this");
        Date d = this.getDate(year, month, day);
        emp.setAvailableDate(d);  
    }

    public void applyCancelCardNow(int applicationByEmpId) throws Exception{
        Employee e = this.getEmployee(applicationByEmpId);
        Date d = Date.getCurrentDate();
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

    public void setEmployeeSalary(int empId, int x) throws Exception{
        Employee e = this.getEmployee(empId);
        e.setSalary(x);
    }

    public String getEmployeeDescription(int empId) throws Exception{
        Employee e = this.getEmployee(empId);
        return e.toString();
    }

    public Employee getEmployee(int id) throws Exception{
        for(Employee e: this.workingEmployees){
            if(e.getId() == id)
                return e;
        }
        for(Employee e: this.firedEmployees){
            if(e.getId() == id)
                return e;
        }
        throw new Exception("Employee not found");
    
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

    private Date getDate(int year, int month, int day) throws Exception{
       Date d = Date.getInstance(year, month, day);
       if(d == null)
          throw new Exception("invalid date!");
        return d;
    }
    private Shift getShift(ShiftTime st, Date da, Branch br) throws Exception{
        Shift s = Shift.getInstance(st, da, br);
        if(s==null)
            throw new Exception("invalid shift");
        return s;
    }

    
}