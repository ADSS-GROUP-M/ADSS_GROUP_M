package employeeModule.PresentationLayer.ViewModel;

import employeeModule.PresentationLayer.Model.BackendController;
import employeeModule.ServiceLayer.Objects.SShift;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class HRManagerMenuVM {
    private BackendController backendController;

    public HRManagerMenuVM() {
        backendController = BackendController.getInstance();
    }

    public String recruitEmployee(String fullName, String branchId, String id, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) {
        return backendController.recruitEmployee(fullName, branchId, id, bankDetails, hourlyRate, employmentDate, employmentConditions, details);
    }

    public String createUser(String username, String password) {
        return backendController.createUser(username, password);
    }

    public String logout() {
        return backendController.logout();
    }

    public String getNextWeekShiftRequests(String branchId) {
        try {
            List<SShift[]> shifts = backendController.getNextWeekShifts(branchId);
            // Return next week's shift requests for the HR Manager
            String result = "";
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts)
                    if (shift != null) {
                        result += shift.requestsString() + "\n";
                        found = true;
                    }
            }
            if (!found)
                result = "There are no requests for shifts planned for next week in branch " + branchId + ".";
            return result;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getWeekShiftRequests(String branchId, LocalDate weekStart) {
        try {
            List<SShift[]> shifts = backendController.getWeekShifts(branchId, weekStart);
            // Return the week's shift requests for the HR Manager
            String result = "";
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts)
                    if (shift != null) {
                        result += shift.requestsString() + "\n";
                        found = true;
                    }
            }
            if (!found)
                result = "There are no requests for shifts planned in the specified week in branch " + branchId + ".";
            return result;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getNextWeekShifts(String branchId) {
        try {
            List<SShift[]> shifts = backendController.getNextWeekShifts(branchId);
            // Return week schedule for HR Manager
            String result = "";
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts)
                    if (shift != null) {
                        result += shift.toString() + "\n";
                        found = true;
                    }
            }
            if (!found)
                result = "There are no shifts planned for next week in branch " + branchId + ".";
            return result;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getWeekShifts(String branchId, LocalDate weekStart) {
        try {
            List<SShift[]> shifts = backendController.getWeekShifts(branchId, weekStart);
            // Return week schedule for HR Manager
            String result = "";
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts)
                    if (shift != null) {
                        result += shift.toString() + "\n";
                        found = true;
                    }
            }
            if (!found)
                result = "There are no shifts planned in the specified week in branch " + branchId + ".";
            return result;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String createWeekShifts(String branchId, LocalDate weekStart) {
        return backendController.createWeekShifts(branchId, weekStart);
    }

    public String createNextWeekShifts(String branchId) {
        return backendController.createNextWeekShifts(branchId);
    }

    public String setShiftNeededAmount(String branchId, LocalDate shiftDate, String shiftType, String role, int amount) {
        return backendController.setShiftNeededAmount(branchId, shiftDate, shiftType, role, amount);
    }

    public String setShiftEmployees(String branchId, LocalDate shiftDate, String shiftType, String role, List<String> employeeIds) {
        return backendController.setShiftEmployees(branchId, shiftDate, shiftType, role, employeeIds);
    }

    public String certifyEmployee(String employeeId, String role) {
        return backendController.certifyEmployee(employeeId, role);
    }

    public String uncertifyEmployee(String employeeId, String role) {
        return backendController.uncertifyEmployee(employeeId, role);
    }

    public String approveShift(String branchId, LocalDate shiftDate, String shiftType) {
        return backendController.approveShift(branchId, shiftDate, shiftType);
    }

    public String addEmployeeToBranch(String employeeId, String branchId) {
        return backendController.addEmployeeToBranch(employeeId, branchId);
    }

    public String deleteShift(String branchId, LocalDate shiftDate, String shiftType) {
        return backendController.deleteShift(branchId, shiftDate, shiftType);
    }

    public String createBranch(String branchId) {
        return backendController.createBranch(branchId);
    }

    public String updateBranchWorkingHours(String branchId, LocalTime morningStart, LocalTime morningEnd, LocalTime eveningStart, LocalTime eveningEnd) {
        return backendController.updateBranchWorkingHours(branchId, morningStart, morningEnd, eveningStart, eveningEnd);
    }

    public String updateEmployeeSalary(String employeeId, double hourlySalaryRate, double salaryBonus) {
        return backendController.updateEmployeeSalary(employeeId, hourlySalaryRate, salaryBonus);
    }

    public String updateEmployeeBankDetails(String employeeId, String bankDetails) {
        return backendController.updateEmployeeBankDetails(employeeId, bankDetails);
    }

    public String updateEmployeeEmploymentConditions(String employeeId, String employmentConditions) {
        return backendController.updateEmployeeEmploymentConditions(employeeId, employmentConditions);
    }

    public String updateEmployeeDetails(String employeeId, String details) {
        return backendController.updateEmployeeDetails(employeeId, details);
    }

    public String authorizeUser(String username, String authorization) {
        return backendController.authorizeUser(username, authorization);
    }
}
