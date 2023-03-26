package dev.BusinessLayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import dev.BusinessLayer.Shift.ShiftTime;

public enum Branch {
    BRANCH1(),
    BRANCH2(),
    BRANCH3(),
    BRANCH4(),
    BRANCH5(),
    BRANCH6(),
    BRANCH7(),
    BRANCH8(),
    BRANCH9();

    private HashMap<ShiftTime,Integer[]> workingHours;
    private List<Date>  nonWorkingDays;

    private Branch(){
        this.nonWorkingDays = new LinkedList<Date>();
        this.workingHours = new HashMap<ShiftTime, Integer[]>();
        Integer[] w1 = {8,16}, w2 = {16,24};
        this.workingHours.put(ShiftTime.MORNING, w1);
        this.workingHours.put(ShiftTime.EVENING, w2);
    }

    private Branch(int morningShiftStart,int morningShiftFinish, int eveningShiftStart, int eveningShiftFinish){
        this.nonWorkingDays = new LinkedList<Date>();
        this.workingHours = new HashMap<ShiftTime, Integer[]>();
        Integer[] w1 = {morningShiftStart,morningShiftFinish}, w2 = {eveningShiftStart,eveningShiftFinish};
        this.workingHours.put(ShiftTime.MORNING, w1);
        this.workingHours.put(ShiftTime.EVENING, w2);
    }

    public Integer[] getWorkingHours(ShiftTime s){ // returns 2 slot array : {shift starting time, shift end time}
        return this.workingHours.get(s);
    }

    public void addNonWorkingDay(Date d){
        this.nonWorkingDays.add(d);
    }

    public boolean isWorkingDay(Date date){
        for(Date d : this.nonWorkingDays){
            if(d == date)
                return false;
        }
        return true;
    }

    public static List<Branch> getAllBranches(){
        List<Branch> b = new LinkedList<Branch>();
        for( Branch br : Branch.values()){
            b.add(br);
        }
        return b;
    }
}
