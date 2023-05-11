package dataAccessLayer.employeeModule.records;

import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;

import java.time.LocalDate;

public record ShiftRequest(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType, String employeeId, Role role) { }