package dataAccessLayer.employeeModule.records;

import businessLayer.employeeModule.Shift;

import java.time.LocalDate;

public record ShiftActivity(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType, String activity) { }