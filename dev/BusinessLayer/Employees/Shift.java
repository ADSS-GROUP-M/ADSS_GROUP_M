package dev.BusinessLayer.Employees;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shift {
    private LocalDate shiftDate;
    private ShiftType shiftType;
    private boolean isApproved; // approved by HR manager
    private Map<Role, Integer> neededRoles;
    private Map<Role, List<Employee>> shiftRequests;
    private Map<Role, List<Employee>> shiftWorkers;
    private List<String> cancelCardApplies;

    public enum ShiftType {
        Morning,
        Evening;
    }
    
    public Shift(LocalDate shiftDate, ShiftType shiftType) {
        this.shiftType = shiftType;
        this.shiftDate = shiftDate;
        this.neededRoles = new HashMap<>();
        this.neededRoles.put(Role.Cashier,1);
        this.neededRoles.put(Role.GeneralWorker,1);
        this.neededRoles.put(Role.ShiftManager,1);
        this.neededRoles.put(Role.Storekeeper,1);
        this.isApproved = false;
        this.shiftRequests = new HashMap<>();
        this.shiftWorkers = new HashMap<>();
        this.cancelCardApplies = new ArrayList<>();
    }

    public boolean checkLegality() { // are all constraints of the shift are met?
        for(Role role : neededRoles.keySet()){
            if (!shiftWorkers.containsKey(role) || shiftWorkers.get(role).size() < neededRoles.get(role))
                return false;
        }
        return true;
    }

    public void approve() throws Exception {
        if (this.isApproved)
            throw new Exception("This shift is already approved.");
        this.isApproved = true;
        if (!this.checkLegality())
            throw new Exception("Shift approved, but notice that the shift constraints are not met!");
    }

    public void disapprove(){
        this.isApproved = false;
    }

    public boolean getIsApproved(){
        return isApproved;
    }

    //public boolean registerEmployee(ShiftTime st, LocalDate d, Branch br, Employee e, List<Role> roles){
    //    LocalDate[] week = DateUtils.getWeekDates(d);
    //    Shift s = getInstance(st, d, br);
    //    s.employees.put(e, roles);
    //    if(!e.checkLegality(week[0],week[1])){
    //        s.employees.remove(e);
    //        return false;
    //    }
    //    return true;
    //}

    public boolean isEmployeeWorking(Employee employee){
        for(List<Employee> employeesByRole : this.shiftWorkers.values()){
            if(employeesByRole.contains(employee))
                return true;
        }
        return false;
    }

    public boolean isEmployeeRequesting(Employee employee){
        for(List<Employee> employeesByRole : this.shiftRequests.values()){
            if(employeesByRole.contains(employee))
                return true;
        }
        return false;
    }

    public boolean isEmployeeRequestingForRole(Employee employee, Role role) {
        if (!this.shiftRequests.containsKey(role))
            return false;
        return this.shiftRequests.get(role).contains(employee);
    }

    //public List<Role> getRoles(Employee employee) {
    //    return this.employees.get(employee);
    //}

    public List<Employee> getRoleEmployees(Role role) {
        if (!shiftWorkers.containsKey(role))
            return new ArrayList<>();
        return this.shiftWorkers.get(role);
    }

    public void setNeededRoles(HashMap<Role,Integer> map){
        this.neededRoles = map;
    }

    public void setNeededRole(Role role, Integer amount){
        this.neededRoles.put(role, amount);
    }

    public Map<Role,Integer> getNeededRoles(){
        return this.neededRoles;
    }

    public void addShiftRequest(Role role, Employee employee) throws Exception {
        if (!this.neededRoles.containsKey(role))
            throw new Exception("Invalid shift request, the role " + role + " is not needed in the shift.");
        if (!this.shiftRequests.containsKey(role))
            this.shiftRequests.put(role, new ArrayList<>());
        this.shiftRequests.get(role).add(employee);
    }

    public void addShiftWorker(Role role, Employee employee) throws Exception {
        if (!this.neededRoles.containsKey(role))
            throw new Exception("Invalid shift employee addition, the role " + role + " is not needed in the shift.");
        if (!isEmployeeRequestingForRole(employee, role))
            throw new Exception("Invalid shift employee addition, the employee did not request for this shift in this role.");
        if (!this.shiftWorkers.containsKey(role))
            this.shiftWorkers.put(role, new ArrayList<>());
        this.shiftWorkers.get(role).add(employee);
    }

    public LocalDate getShiftDate() {
        return this.shiftDate;
    }

    public ShiftType getShiftType() {
        return this.shiftType;
    }

    public Map<Role, List<Employee>> getShiftWorkers() {
        return this.shiftWorkers;
    }

    public Map<Role, List<Employee>> getShiftRequests() {
        return this.shiftRequests;
    }

    public String toString(){
        String desc = "";
        String roles="UNFINISHED";
        String isVerDesc = "";
        String employeesList = "";
        String legalityWarning = "";
        //if(!this.checkLegality())
        //    legalityWarning = "WARNING! shift doesn't meet constraints!";
        
        desc+="Shift Date: "+this.shiftDate.toString()+"\nShift Time: "+ this.shiftType.toString() +"\nRoles: " + roles +
          "\nIs verified by Manager: " + isVerDesc +"\nEmployees: "+ employeesList + "\n" + legalityWarning;

        return desc;
    }

    public void useCancelCard(String cancellingEmployeeId, String productId){
        // TODO: Save each card apply in an object, to make it easier at the next steps when we need to save it in the database
        this.cancelCardApplies.add(cancellingEmployeeId + " " + productId + " " + LocalDateTime.now());
        // Save the cancelled product
    }

    public List<String> getCancelCardApplications(){
        return this.cancelCardApplies;
    }

    public Employee getShiftManager(){
        if (shiftWorkers.containsKey(Role.ShiftManager) && !shiftWorkers.get(Role.ShiftManager).isEmpty())
            return shiftWorkers.get(Role.ShiftManager).get(0);
        //for(Employee e: this.employees.keySet()){
        //    for(Role r : this.employees.get(e)){
        //        if(r == Role.ShiftManager){
        //            return e;
        //        }
        //    }
        //}
        return null;
    }
}
