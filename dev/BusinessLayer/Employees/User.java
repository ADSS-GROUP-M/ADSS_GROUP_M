package dev.BusinessLayer.Employees;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {// This class represents a user in the system and manages its authorization

    private String username;
    private String password;
    private boolean loggedIn;
    private Set<Authorization> authorizations;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.loggedIn = false;
        this.authorizations = new HashSet<>();
    }

    public User(String username, String password, Authorization auth){
        this(username, password);
        this.authorizations.add(auth);
    }

    public boolean login(String password) throws Exception {
        if (loggedIn)
            throw new Exception("The user is already logged in to the system.");
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

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }

    public String getUsername() {
		return this.username;
	}

	public boolean isAuthorized(Authorization auth){
		return this.authorizations.contains(auth);
	}

    public List<Authorization> getAuthorizations() {
        return new ArrayList<>(this.authorizations); // Returns a copy of the user's authorization, as a list
    }

    public void authorize(Authorization auth) {
        this.authorizations.add(auth);
    }

    //public String[] recruitEmployeeAndNewUser(String name, String id, int bankNumber, int branchNumber,  List<String> employmentConditions, LocalDate employmentDate) throws Exception { // returns User details for new employee
    //    if(this.getIsHrManager()){
    //        BankDetails bd = new BankDetails(bankNumber, branchNumber);
    //       this.empController.recruitEmployee(name, id, bd, employmentConditions, employmentDate);
    //       Employee e = this.empController.getEmployee(id);
    //       String[] userDetails = new String[2];
    //       userDetails[0] = id;
    //       userDetails[1] = Integer.toString((int)(Math.random()*100000000));
    //       if(userDetails[1].length() > 4)
    //            userDetails[1] =  userDetails[1].substring(0, 4);
    //       this.usController.createUser(userDetails[0],userDetails[1], e);
    //        return userDetails;
    //    }
    //    else
    //        throw new Exception("This user isn't authorized to do this.");
    //}

    //protected void recruitEmployeeWithoutUser(String name, String id, BankDetails bd, List<String> ec, LocalDate employmentDate) throws Exception{ // returns User details for new employee
    //    if(this.getIsHrManager()){
    //       this.empController.recruitEmployee(name, id, bd,ec, employmentDate);
    //    }
    //    else
    //        throw new Exception("This user isn't authorized to do this.");
    //}

    //protected void fireEmployee(String empId) throws Exception{
    //    if(this.getIsHrManager()){
    //        this.empController.fireEmployee(empId);
    //    }
    //    else
    //        throw new Exception("This user isn't authorized to do this.");
    //}

    //protected String getSystemTime(){
    //    LocalDate d = LocalDate.now();
    //    Integer[] in = new Integer[]{LocalTime.now().getHour(),LocalTime.now().getMinute()};
    //    String desc = "";
    //    desc = d.getYear() + "." + d.getMonth() + "." +d.getDayOfWeek() +" "+in[0]+":"+in[1];
    //    return desc;
    //}

    //protected void setSystemTime(int year, int month, int day, int hour ,int minute)throws Exception{
    //    if(this.isHrManager)
    //        Date.setSystemTime(year, month, day, hour, minute);
    //    else
    //        throw new Exception("This user is not authorized to do this");
    //}

    //public void signUpEmployeeToShift(String empId, String shiftTime, LocalDate shiftDate, String branchName, String role) throws Exception{ // employee to shift
    //    if(this.linkedEmployee == null  || !this.linkedEmployee.getId().equals(empId))
    //        throw new Exception("You are not authorized to do it. You cant schedule shifts of other employees if you are not HR manager.");
    //    else
    //        this.empController.signUpEmployeeToShift(empId, shiftTime, shiftDate, branchName, role);
    //}

    //protected void setUnavailabilityToWork(String empId, int year, int month, int day) throws Exception{
    //    if(this.linkedEmployee == null && !this.isHrManager)
    //        throw new Exception("You are not authorized to do it. You cant schedule shifts of other employees if you are not HR manager.");
    //    if(this.isHrManager || this.linkedEmployee.getId().equals(empId))
    //        this.empController.setUnavailableDate(empId, year, month, day);
    //    else
    //        throw new Exception("You are unauthorized to schedule other employee's shifts, unless you are HR manager.");
    //}

    //protected void setEmployeeSalary(String empId, int x) throws Exception{
    //    if(this.linkedEmployee == null && !this.isHrManager)
    //        throw new Exception("You are not authorized to do it.");
    //    if(this.isHrManager){
    //        this.empController.setEmployeeSalary(empId,x);
    //    }else
    //        throw new Exception("you are unauthorized to do so.");
    //}

    //protected String employeeDescription(String empId) throws Exception{
    //    if(this.linkedEmployee == null && !this.isHrManager)
    //        throw new Exception("You are not authorized to view this information.");
    //    if(this.isHrManager || this.linkedEmployee.getId().equals(empId))
    //        return this.empController.getEmployeeDescription(empId) +"/nIs Manager: "+this.isHrManager;
    //    else
    //        throw new Exception("Unauthorized to access this information");
    //}

    //protected String shiftDescription(int year, int month, int day, String branch, String shiftTime) throws Exception{
    //    return this.empController.getShiftDescription(LocalDate.of(year,month,day), branch, shiftTime);
    //}

    //protected void applyCancelCardNow() throws Exception{
    //    if(this.linkedEmployee != null && this.isCurrentShiftManager())
    //        this.empController.applyCancelCardNow(this.linkedEmployee.getId());
    //    else
    //        throw new Exception("You are not authorized to do so because you are not shift manager");
    //}

    //protected boolean isCurrentShiftManager() throws Exception{
    //    if(this.linkedEmployee!=null)
    //        return this.empController.isCurrentShiftManager(linkedEmployee);
    //    else
    //        return false;
    //}

    //protected void setSalary(String empId, int salary) throws Exception{
    //    if(this.isHrManager)
    //        this.empController.setEmployeeSalary(empId, salary);
    //    else
    //        throw new Exception("Unauthorized to do so.");
    //}
}
	
