package dev.BusinessLayer.Employees;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import dev.BusinessLayer.Employees.Shift.ShiftType;

public class Branch {
    private LocalTime morningStart;
    private LocalTime morningEnd;
    private LocalTime eveningStart;
    private LocalTime eveningEnd;

    public Branch() {
        // Default values of branch working hours
        morningStart = LocalTime.of(8,0);
        morningEnd = LocalTime.of(16,0);
        eveningStart = LocalTime.of(16,0);
        eveningEnd = LocalTime.of(22,0);
    }

    public Branch(int morningShiftStart,int morningShiftFinish, int eveningShiftStart, int eveningShiftFinish){
        morningStart = LocalTime.of(morningShiftStart, 0);
        morningEnd = LocalTime.of(morningShiftFinish, 0);
        eveningStart = LocalTime.of(eveningShiftStart, 0);
        eveningEnd = LocalTime.of(eveningShiftFinish, 0);
    }

    public LocalTime getMorningStart() {
        return this.morningStart;
    }

    public LocalTime getMorningEnd() {
        return morningEnd;
    }

    public LocalTime getEveningStart() {
        return eveningStart;
    }

    public LocalTime getEveningEnd() {
        return eveningEnd;
    }
    public void setWorkingHours(LocalTime mFrom, LocalTime mUntil,LocalTime eFrom, LocalTime eUntil){
        this.morningStart = mFrom;
        this.morningEnd = mUntil;
        this.eveningStart = eFrom;
        this.eveningEnd = eUntil;
    }
}
