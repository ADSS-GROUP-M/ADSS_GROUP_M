package dev.BusinessLayer.Employees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import dev.BusinessLayer.Employees.Shift.ShiftType;

public class Branch {
    private HashMap<ShiftType,Integer[]> workingHours;
    private List<LocalDate>  nonWorkingDays;

    public Branch(){
        this.nonWorkingDays = new ArrayList<>();
        this.workingHours = new HashMap<ShiftType, Integer[]>();
        Integer[] w1 = {8,16}, w2 = {16,24};
        this.workingHours.put(ShiftType.Morning, w1);
        this.workingHours.put(ShiftType.Evening, w2);
    }

    public Branch(int morningShiftStart,int morningShiftFinish, int eveningShiftStart, int eveningShiftFinish){
        this.nonWorkingDays = new LinkedList<>();
        this.workingHours = new HashMap<ShiftType, Integer[]>();
        Integer[] w1 = {morningShiftStart,morningShiftFinish}, w2 = {eveningShiftStart,eveningShiftFinish};
        this.workingHours.put(ShiftType.Morning, w1);
        this.workingHours.put(ShiftType.Evening, w2);
    }

    public Integer[] getWorkingHours(ShiftType s){ // returns 2 slot array : {shift starting time, shift end time}
        return this.workingHours.get(s);
    }

    public void addNonWorkingDay(LocalDate d){
        this.nonWorkingDays.add(d);
    }

    public boolean isWorkingDay(LocalDate date){
        for(LocalDate d : this.nonWorkingDays){
            if(d == date)
                return false;
        }
        return true;
    }
}
