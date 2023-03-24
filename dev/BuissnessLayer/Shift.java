package dev.BuissnessLayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Shift {

    private ShiftTime shiftTime;
    private Date date;
    private HashMap<Role,Integer> neededRoles;
    private Branch branch;
    private boolean isApproved; // approved by HR manager
    private HashMap<Employee,List<Role>> employees;

    private static HashMap<Date,HashMap<Branch,HashMap<ShiftTime,Shift>>> shifts;

    public enum ShiftTime{
        MORNING(),
        EVENING();
    }
    
    private Shift(ShiftTime st, Date date, Branch br)
    {
        this.shiftTime = st;
        this.date = date;
        neededRoles = new HashMap<Role,Integer>();
        neededRoles.put(Role.CASHIER,1);
        neededRoles.put(Role.GENERAL_WORKER,1);
        neededRoles.put(Role.SHIFT_MANAGER,1);
        neededRoles.put(Role.STOREKEEPER,1);
        this.branch = br;
        this.isApproved = false;
        this.employees = new HashMap<Employee,List<Role>>();
    }

    public static Shift getInstance(ShiftTime st, Date date, Branch br){
        if(shifts == null)
            shifts = new HashMap<Date,HashMap<Branch,HashMap<ShiftTime,Shift>>>();
        if(!shifts.containsKey(date))
            shifts.put(date, new HashMap<Branch,HashMap<ShiftTime,Shift>>());
        if(!shifts.get(date).containsKey(br))
            shifts.get(date).put(br, new HashMap<ShiftTime,Shift>());
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

    public void registerEmployee(Date d , ShiftTime st, Branch br, Employee e, List<Role> roles){
        Shift s = getInstance(st, d, br);
        s.employees.put(e, roles);
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

    public Object getBranch() {
        return this.branch;
    }

}
