package PresentationLayer.employeeModule.ViewModel;

import PresentationLayer.employeeModule.Model.BackendController;
import ServiceLayer.employeeModule.Services.employeeObjects.SEmployee;
import ServiceLayer.employeeModule.Services.employeeObjects.SShift;

import java.time.LocalDate;
import java.util.List;

public class EmployeeMenuVM {
    private BackendController backendController;

    public EmployeeMenuVM() {
        backendController = BackendController.getInstance();
    }

    public String requestShift(String branchId, String shiftType, LocalDate shiftDate, String role) {
        return backendController.requestShift(branchId, shiftType, shiftDate, role);
    }

    public String cancelShiftRequest(String branchId, String shiftType, LocalDate shiftDate, String role) {
        return backendController.cancelShiftRequest(branchId, shiftType, shiftDate, role);
    }

    public String reportShiftActivity(String branchId, LocalDate shiftDate, String shiftType, String activity) {
        return backendController.reportShiftActivity(branchId, shiftDate, shiftType, activity);
    }

    public String applyCancelCard(String branchId, LocalDate shiftDate, String shiftType, String productId) {
        return backendController.applyCancelCard(branchId, shiftDate, shiftType, productId);
    }

    public String logout() {
        return backendController.logout();
    }

    public String getNextWeekShifts(String branchId) {
        try {
            List<SShift[]> shifts = backendController.getNextWeekShifts(branchId);
            // Return week schedule for Employee
            String result = "";
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts)
                    if (shift != null) {
                        result += shift.workersString() + "\n";
                        found = true;
                    }
            }
            if (!found)
                result = "There aren't any approved shifts in the next week in branch " + branchId + ".";
            return result;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getWeekShifts(String branchId, LocalDate weekStart) {
        try {
            List<SShift[]> shifts = backendController.getWeekShifts(branchId, weekStart);
            // Return week schedule for Employee
            String result = "";
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts)
                    if (shift != null) {
                        result += shift.workersString() + "\n";
                        found = true;
                    }
            }
            if (!found)
                result = "There aren't any approved shifts in the specified week in branch " + branchId + ".";
            return result;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getMyShifts() {
        try {
            List<SShift[]> shifts = backendController.getMyShifts();
            // Return all shifts for Employee
            String result = "";
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts)
                    if (shift != null && shift.isApproved()) {
                        result += shift.workersString() + "\n";
                        found = true;
                    }
            }
            if (!found)
                result = "There aren't any approved shifts planned for you.";
            return result;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getAllEmployeeDetails() {
        try {
            SEmployee employee = backendController.getEmployee();
            return employee.toString() + "\n";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
