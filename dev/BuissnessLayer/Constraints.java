package dev.BuissnessLayer;

import java.util.List;

import dev.BuissnessLayer.Shift.ShiftTime;

public class Constraints {
    //private List<Integer> unavailableWeekDays;
    private List<Role> availableRoles;
    private List<Branch> availableBranches;
    private final int MAX_WORKDAYS_A_WEEK = 6;
    private final int MAX_SHIFTS_A_DAY = 1;
    private Employee employee;

    public Constraints(Employee emp){
        //unavailableWeekDays = new LinkedList<Integer>();
        availableRoles = new LinkedList<Role>();
        availableBranches = Branch.getAllBranches();
        this.employee = emp;
    }

    public boolean checkScheduleLegality(Date from, Date until){
        List<Branch> branches = Branch.getAllBranches();
        int workDaysAWeek = 0;
        int shiftsADay = 0;

        Date current = from;
        while(current.betweenDates(from, until)){
            boolean worksShiftTime = false;
            boolean isWorkingThisDay = false;
            for(Branch b: branches){
                Shift sh1 = Shift.getInstance(ShiftTime.MORNING, current, b);
                if(sh1.isEmployeeWorking(employee)){
                    if(worksShiftTime)
                        return false;//employee scheduled to 2 shifts that happen simultaniously
                    else{
                        worksShiftTime = true;
                        isWorkingThisDay = true;
                        shiftsADay ++;
                        for(Role r: sh1.getRoles(employee)){
                            if(!availableRoles.contains(r))
                                return false; //unavailable role
                        }
                        if(!availableBranches.contains(sh1.getBranch()))
                            return false;// unavailable branch
                    }
                }
            }
            worksShiftTime = false;
            for(Branch b:branches){
                Shift sh2 = Shift.getInstance(ShiftTime.EVENING, current, b);
                if(sh2.isEmployeeWorking(employee)){
                    if(worksShiftTime)
                        return false;//employee scheduled to 2 shifts that happen simultaniously
                    else{
                            worksShiftTime = true;
                            isWorkingThisDay = true;
                            shiftsADay++;
                            for(Role r: sh2.getRoles(employee)){
                                if(!availableRoles.contains(r))
                                    return false; //unavailable role
                            }
                            if(!availableBranches.contains(sh2.getBranch()))
                                return false;// unavailable branch
                    }
                }
            }
            if(isWorkingThisDay)
                workDaysAWeek ++;
            if(shiftsADay>this.MAX_SHIFTS_A_DAY)
                return false;// more shifts a day than possible
            shiftsADay = 0;
            if(current.getWeekday() == 7){
                if(workDaysAWeek>this.MAX_WORKDAYS_A_WEEK)
                    return false; //too many work days a week
                workDaysAWeek = 0;
            }
            current = current.getNextDay();
        }
        if(workDaysAWeek>this.MAX_WORKDAYS_A_WEEK)
            return false; //too many work days a week
    }
}
