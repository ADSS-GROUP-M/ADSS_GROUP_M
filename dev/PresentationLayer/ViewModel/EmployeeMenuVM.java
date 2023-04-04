package dev.PresentationLayer.ViewModel;

import dev.PresentationLayer.Model.BackendController;
import dev.ServiceLayer.Objects.SEmployee;
import dev.ServiceLayer.Objects.SShift;

import java.time.LocalDate;
import java.util.List;

public class EmployeeMenuVM {
    private BackendController backendController;

    public EmployeeMenuVM() {
        backendController = BackendController.getInstance();
    }

    public String requestShift(String branchId, String shiftTime, LocalDate shiftDate, String role) {
        return backendController.requestShift(branchId, shiftTime, shiftDate, role);
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
