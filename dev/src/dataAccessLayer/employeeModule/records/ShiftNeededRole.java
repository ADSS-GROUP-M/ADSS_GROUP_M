package dataAccessLayer.employeeModule.records;

import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;

import java.time.LocalDate;

public record ShiftNeededRole(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType, Role role, Integer amount) {

    public static ShiftNeededRole getLookupObject(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType, Role role) {
        return new ShiftNeededRole(branchId,shiftDate, shiftType, role, 0);
    }
}

