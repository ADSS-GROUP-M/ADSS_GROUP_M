package employeeModule.BusinessLayer.Employees;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Shift {
    private LocalDate shiftDate;
    private ShiftType shiftType;
    private boolean isApproved; // approved by HR manager
    private Map<Role, Integer> neededRoles;
    private Map<Role, List<Employee>> shiftRequests;
    private Map<Role, List<Employee>> shiftWorkers;
    private List<String> cancelCardApplies;
    private List<String> shiftActivities;

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
        // Checks if any employee is signed to work in two different roles at the same shift. (According to the requirements, he should be able to request it, but it isn't legal as the final shift configuration)
        List<Employee> duplicateEmployees = getDuplicateEmployees();
        return duplicateEmployees.isEmpty();
    }

    private String getLegalityProblems() {
        String result = "";
        for(Role role : neededRoles.keySet()){
            if (!shiftWorkers.containsKey(role) || shiftWorkers.get(role).size() < neededRoles.get(role))
                result += "There are only " + shiftWorkers.get(role).size() + " / " + neededRoles.get(role) + " employees in the role " + role + "\n";
        }
        for(Employee employee : getDuplicateEmployees()) {
            result += "The employee " + employee.getId() + " is assigned to multiple roles in this shift.\n";
        }
        return result;
    }

    private List<Employee> getDuplicateEmployees() {
        Set<Employee> employees = new HashSet<>();
        Set<Employee> duplicateEmployees = new HashSet<>();
        for(List<Employee> roleWorkers : shiftWorkers.values()) {
            for (Employee employee : roleWorkers) {
                if (employees.contains(employee))
                    duplicateEmployees.add(employee);
                else
                    employees.add(employee);
            }
        }
        return duplicateEmployees.stream().toList();
    }

    public void approve() throws Exception {
        if (this.isApproved)
            throw new Exception("This shift is already approved.");
        this.isApproved = true;
        if (!this.checkLegality())
            throw new Exception("Shift approved, but notice that the shift constraints are not met! " + getLegalityProblems());
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
        if(amount > 0)
            this.neededRoles.put(role, amount);
        else if(amount == 0)// the lack of this condition caused bugs while checking legality when a role is set to amount=0
            this.neededRoles.remove(role); 
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

    public void removeShiftRequest(Role role, Employee employee) throws Exception {
        if (!this.shiftRequests.containsKey(role))
            throw new Exception("Invalid shift cancellation request, the given role doesn't have any shift requests in this shift.");
        if (!this.shiftRequests.get(role).contains(employee))
            throw new Exception("Invalid shift cancellation request, the given employee didn't have a request for this role in this shift.");
        this.shiftRequests.get(role).remove(employee);
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

    public void reportActivity(String reportingEmployee, String activity) {
        this.shiftActivities.add(activity); // TODO: Possibly need to save the reported activities in a table in the database at the next milestone.
        // Save the reported activity
    }

    public List<String> getCancelCardApplications(){
        return this.cancelCardApplies;
    }

    public List<String> getShiftActivities(){
        return this.shiftActivities;
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
