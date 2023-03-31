package dev.BusinessLayer.Employees;

import dev.Utils.DateUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class Shift {

    private ShiftTime shiftTime;
    private LocalDate date;
    private HashMap<Role,Integer> neededRoles;
    private Branch branch;
    private boolean isApproved; // approved by HR manager
    private HashMap<Employee,List<Role>> employees;
    private int cancelCardApplies;

    private static HashMap<LocalDate,HashMap<Branch,HashMap<ShiftTime,Shift>>> shifts;

    public enum ShiftTime{
        MORNING(),
        EVENING();
    }
    
    private Shift(ShiftTime st, LocalDate date, Branch br)
    {
        this.shiftTime = st;
        this.date = date;
        neededRoles = new HashMap<>();
        neededRoles.put(Role.CASHIER,1);
        neededRoles.put(Role.GENERAL_WORKER,1);
        neededRoles.put(Role.SHIFT_MANAGER,1);
        neededRoles.put(Role.STOREKEEPER,1);
        this.branch = br;
        this.isApproved = false;
        this.employees = new HashMap<>();
        cancelCardApplies = 0;
    }

    public static Shift getInstance(ShiftTime st, LocalDate date, Branch br){
        if(shifts == null)
            shifts = new HashMap<>();
        if(!shifts.containsKey(date))
            shifts.put(date, new HashMap<>());
        if(!shifts.get(date).containsKey(br))
            shifts.get(date).put(br, new HashMap<>());
        if(!shifts.get(date).get(br).containsKey(st))
            shifts.get(date).get(br).put(st, new Shift(st,date,br));

        return shifts.get(date).get(br).get(st);
    }

    public boolean checkLegality(){ // are all constraints of the shift are met?
        for(Role r : neededRoles.keySet()){
            int workersNeededForRole = neededRoles.get(r);
            for(Employee e : this.employees.keySet()){
                if(this.employees.get(e).contains(r))
                    workersNeededForRole --;
            }
            if(workersNeededForRole>0)
                return false; // not enough workers for every role needed in the shift
        }
        return true;
    }

    public void approve(){
        this.isApproved = true;
    }

    public void disapprove(){
        this.isApproved = false;
    }

    public boolean getIsApproved(){
        return isApproved;
    }

    public boolean registerEmployee(ShiftTime st, LocalDate d, Branch br, Employee e, List<Role> roles){
        LocalDate[] week = DateUtils.getWeekDates(d);
        Shift s = getInstance(st, d, br);
        s.employees.put(e, roles);
        if(!e.checkLegality(week[0],week[1])){
            s.employees.remove(e);
            return false;
        }
        return true;
    }

    public boolean isEmployeeWorking(Employee emp){
        for(Employee e : this.employees.keySet()){
            if(e == emp)
                return true;
        }
        return false;
    }

    public List<Role> getRoles(Employee employee) {
        return this.employees.get(employee);
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setNeededRoles(HashMap<Role,Integer> map){
        this.neededRoles = map;
    }

    public HashMap<Role,Integer> getNeededRoles(){
        return this.neededRoles;
    }

    public String toString(){
        String desc = "";
        String roles="UNFINISHED";
        String isVerDesc = "";
        String employeesList = "";
        String legalityWarning = "";
        if(!this.checkLegality())
            legalityWarning = "WARNING! shift doesn't meet constraints!";
        
        desc+="Date: "+this.date.toString() + "/nBranch: " + this.branch.toString()
         +"/nShift Time: "+ this.shiftTime.toString() +"/nRoles: " + roles +
          "/nIs verified by manager: " + isVerDesc +"/nEmployees: "+ employeesList + "\n" + legalityWarning;

        return desc;
    }
    public void useCancelCard(){
        this.cancelCardApplies++;
        // Save the cancelled product
    }
    public int getTotalCancelCardApplications(){
        return this.cancelCardApplies;
    }
    public Employee getShiftManager(){
        for(Employee e: this.employees.keySet()){
            for(Role r : this.employees.get(e)){
                if(r == Role.SHIFT_MANAGER){
                    return e;
                }
            }
        }
        return null;
    }



}
