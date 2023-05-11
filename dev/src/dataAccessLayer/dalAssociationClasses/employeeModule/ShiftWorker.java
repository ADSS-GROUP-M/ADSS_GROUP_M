package dataAccessLayer.dalAssociationClasses.employeeModule;

import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;

import java.time.LocalDate;

public record ShiftWorker(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType, String employeeId, Role role) {

    public static ShiftWorker getLookupObject(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType, String employeeId) {
        return new ShiftWorker(branchId,shiftDate, shiftType, employeeId, Role.GeneralWorker);
    }
}
