package presentationLayer.cli.employeeModule.ViewModel;

import presentationLayer.cli.employeeModule.Model.BackendController;
import serviceLayer.employeeModule.Objects.SEmployee;
import serviceLayer.employeeModule.Objects.SShift;

import java.time.LocalDate;
import java.util.List;

public class EmployeeMenuVM {
    private final BackendController backendController;

    public EmployeeMenuVM(BackendController backendController) {
        this.backendController = backendController;
    }

    public String requestShift(String branchId, String shiftType, LocalDate shiftDate, String role) {
        return backendController.requestShift(branchId, shiftType, shiftDate, role);
    }

    public String cancelShiftRequest(String branchId, LocalDate shiftDate, String shiftType, String role) {
        return backendController.cancelShiftRequest(branchId, shiftDate, shiftType, role);
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
            StringBuilder result = new StringBuilder();
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts) {
                    if (shift != null) {
                        result.append(shift.workersString()).append("\n");
                        found = true;
                    }
                }
            }
            if (!found) {
                result = new StringBuilder("There aren't any approved shifts in the next week in branch " + branchId + ".");
            }
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getWeekShifts(String branchId, LocalDate weekStart) {
        try {
            List<SShift[]> shifts = backendController.getWeekShifts(branchId, weekStart);
            // Return week schedule for Employee
            StringBuilder result = new StringBuilder();
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts) {
                    if (shift != null) {
                        result.append(shift.workersString()).append("\n");
                        found = true;
                    }
                }
            }
            if (!found) {
                result = new StringBuilder("There aren't any approved shifts in the specified week in branch " + branchId + ".");
            }
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getMyShifts() {
        try {
            List<SShift[]> shifts = backendController.getMyShifts();
            // Return all shifts for Employee
            StringBuilder result = new StringBuilder();
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts) {
                    if (shift != null && shift.isApproved()) {
                        result.append(shift.workersString()).append("\n");
                        found = true;
                    }
                }
            }
            if (!found) {
                result = new StringBuilder("There aren't any approved shifts planned for you.");
            }
            return result.toString();
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
