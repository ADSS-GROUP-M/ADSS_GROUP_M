package dataAccessLayer.dalAssociationClasses.employeeModule;

import businessLayer.employeeModule.Shift;

import java.time.LocalDate;

public record ShiftActivity(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType, String activity) { }