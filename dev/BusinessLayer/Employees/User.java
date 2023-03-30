package dev.BusinessLayer.Employees;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class User {// this class manages authorization before accessing employee controller

    private String username;
    private String password;
    private Employee linkedEmployee;
    private boolean isHrManager;
    boolean loggedIn;
    private EmployeesController empController;
    private UserController usController;

    public User(UserController uc, String name, String pass, Employee emp, boolean hr){
        this.username = name;
        this.password = pass;
        this.linkedEmployee = emp;
        this.isHrManager = hr;
        this.loggedIn = false;
        empController  = EmployeesController.getInstance();
        usController = uc;
    }

    public boolean login(String password) {
        if (checkPassword(password))
            loggedIn = true;
        return loggedIn;
    }

    public void logout() throws Exception {
        if (!loggedIn)
            throw new Exception("The user is already logged out.");
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    protected boolean checkPassword(String password){
        return this.password.equals(password);
    }

    protected EmployeesController getEmployeeController(){
        return empController;
    }

    protected void linkUserToEmployee(String username, String empId) throws Exception{
        if(!this.isHrManager)
            throw new Exception("Only HR manager is authorized to do this");
        User u = this.usController.getUser(username);
        if(u.linkedEmployee != null)
            throw new Exception("User is already owned by another employee");
        u.linkToEmployee(empId);
    }

    protected void linkToEmployee(String id) throws Exception{
        empController.getEmployee(id);
    }

    public String getUsername() {
		return this.username;
	}

	protected boolean getIsHrManager(){
		return this.isHrManager;
	}

    public String[] recruitEmployeeAndNewUser(String name, String id, int bankNumber, int branchNumber,  List<String> employmentConditions, LocalDate employmentDate) throws Exception { // returns User details for new employee
        if(this.getIsHrManager()){
            BankDetails bd = new BankDetails(bankNumber, branchNumber);
           this.empController.recruitEmployee(name, id, bd, employmentConditions, employmentDate);
           Employee e = this.empController.getEmployee(id);
           String[] userDetails = new String[2];
           userDetails[0] = id;
           userDetails[1] = Integer.toString((int)(Math.random()*100000000));
           if(userDetails[1].length() > 4)
                userDetails[1] =  userDetails[1].substring(0, 4);
           this.usController.createUser(userDetails[0],userDetails[1], e);
            return userDetails;
        } 
        else
            throw new Exception("This user isn't authorized to do this.");
    }

    protected void recruitEmployeeWithoutUser(String name, String id, BankDetails bd, List<String> ec, LocalDate employmentDate) throws Exception{ // returns User details for new employee
        if(this.getIsHrManager()){
           this.empController.recruitEmployee(name, id, bd,ec, employmentDate);
        } 
        else
            throw new Exception("This user isn't authorized to do this.");
    }

    protected void fireEmployee(String empId) throws Exception{
        if(this.getIsHrManager()){
            this.empController.fireEmployee(empId);
        }
        else
            throw new Exception("This user isn't authorized to do this.");
    }

    protected String getSystemTime(){
        LocalDate d = LocalDate.now();
        Integer[] in = new Integer[]{LocalTime.now().getHour(),LocalTime.now().getMinute()};
        String desc = "";
        desc = d.getYear() + "." + d.getMonth() + "." +d.getDayOfWeek() +" "+in[0]+":"+in[1];
        return desc;
    }

//    protected void setSystemTime(int year, int month, int day, int hour ,int minute)throws Exception{
//        if(this.isHrManager)
//            Date.setSystemTime(year, month, day, hour, minute);
//        else
//            throw new Exception("This user is not authorized to do this");
//    }

    public void signUpEmployeeToShift(String empId, String shiftTime, LocalDate shiftDate, String branchName, String role) throws Exception{ // employee to shift
        if(this.linkedEmployee == null  || !this.linkedEmployee.getId().equals(empId))
            throw new Exception("You are not authorized to do it. You cant schedule shifts of other employees if you are not HR manager.");
        else
            this.empController.signUpEmployeeToShift(empId, shiftTime, shiftDate, branchName, role);

    }

    protected void setUnavailabilityToWork(String empId, int year, int month, int day) throws Exception{
        if(this.linkedEmployee == null && !this.isHrManager)
            throw new Exception("You are not authorized to do it. You cant schedule shifts of other employees if you are not HR manager.");
        if(this.isHrManager || this.linkedEmployee.getId().equals(empId))
            this.empController.setUnavailableDate(empId, year, month, day);
        else
            throw new Exception("You are unauthorized to schedule other employee's shifts, unless you are HR manager.");
    }

    protected void setEmployeeSalary(String empId, int x) throws Exception{
        if(this.linkedEmployee == null && !this.isHrManager)
            throw new Exception("You are not authorized to do it.");
        if(this.isHrManager){
            this.empController.setEmployeeSalary(empId,x);
        }else
            throw new Exception("you are unauthorized to do so.");
    }

    protected String employeeDescription(String empId) throws Exception{
        if(this.linkedEmployee == null && !this.isHrManager)
            throw new Exception("You are not authorized to view this information.");
        if(this.isHrManager || this.linkedEmployee.getId().equals(empId))
            return this.empController.getEmployeeDescription(empId) +"/nIs Manager: "+this.isHrManager;
        else
            throw new Exception("Unauthorized to access this information");
    }

    protected String shiftDescription(int year, int month, int day, String branch, String shiftTime) throws Exception{
        return this.empController.getShiftDescription(LocalDate.of(year,month,day), branch, shiftTime);

    }

    protected void applyCancelCardNow() throws Exception{
        if(this.linkedEmployee != null && this.isCurrentShiftManager())
            this.empController.applyCancelCardNow(this.linkedEmployee.getId());
        else
            throw new Exception("You are not authorized to do so because you are not shift manager");
    }

    protected boolean isCurrentShiftManager() throws Exception{
        if(this.linkedEmployee!=null)
            return this.empController.isCurrentShiftManager(linkedEmployee);
        else
            return false;
    }
    protected void setSalary(String empId, int salary) throws Exception{
        if(this.isHrManager)
            this.empController.setEmployeeSalary(empId, salary);
        else
            throw new Exception("Unauthorized to do so.");
    }
}
	
