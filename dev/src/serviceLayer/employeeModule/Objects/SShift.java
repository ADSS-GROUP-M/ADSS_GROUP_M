package serviceLayer.employeeModule.Objects;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SShift {
    private final LocalDate shiftDate;
    private final SShiftType shiftType;
    private final Map<String, Integer> neededRoles;
    private final Map<String, List<SEmployee>> shiftRequests;
    private final Map<String, List<SEmployee>> shiftWorkers;
    private final List<String> cancelCardApplies;
    private final List<String> shiftActivities;
    private final boolean isApproved;

    public SShift(Shift shift) {
        this.shiftDate = shift.getShiftDate();
        this.shiftType = SShiftType.valueOf(shift.getShiftType().toString());
        this.neededRoles = new HashMap<>();
        for(Map.Entry<Role, Integer> entry : shift.getNeededRoles().entrySet()) {
            neededRoles.put(entry.getKey().toString(),entry.getValue());
        }
        this.shiftRequests = new HashMap<>();
        for(Map.Entry<Role, List<Employee>> entry : shift.getShiftRequests().entrySet()) {
            this.shiftRequests.put(entry.getKey().toString(),entry.getValue().stream().map(SEmployee::new).collect(Collectors.toList()));
        }
        this.shiftWorkers = new HashMap<>();
        for(Map.Entry<Role, List<Employee>> entry : shift.getShiftWorkers().entrySet()) {
            this.shiftWorkers.put(entry.getKey().toString(),entry.getValue().stream().map(SEmployee::new).collect(Collectors.toList()));
        }
        this.cancelCardApplies = new ArrayList<>(shift.getShiftCancels());
        this.shiftActivities = new ArrayList<>(shift.getShiftActivities());
        this.isApproved = shift.getIsApproved();
    }

    public Integer getNeededInRole(String role) {
        return this.neededRoles.get(role);
    }

    public Map<String, Integer> getNeededRoles() {
        return this.neededRoles;
    }

    public List<SEmployee> getShiftRequestsEmployees(String role){
        return this.shiftRequests.get(role);
    }

    public List<SEmployee> getShiftWorkersEmployees(String role){
        return this.shiftWorkers.get(role);
    }

    public LocalDate getShiftDate(){
        return this.shiftDate;
    }
    
    public SShiftType getShiftType(){
        return this.shiftType;
    }

    public List<String> getCancelCardApplies() {
        return this.cancelCardApplies;
    }

    public List<String> getShiftActivities() {
        return this.shiftActivities;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public String workersString() {
        StringBuilder result = new StringBuilder(shiftDate.toString() + " " + shiftType.toString() + ":\n");
        if (!isApproved) {
            return result + "Not approved yet.";
        }
        for(Map.Entry<String, List<SEmployee>> roleWorkers : shiftWorkers.entrySet()) {
            result.append(roleWorkers.getKey()).append(": ").append(roleWorkers.getValue().stream().map(SEmployee::getId).collect(Collectors.joining(","))).append("\n");
        }
        return result.toString();
    }

    public String requestsString() {
        StringBuilder result = new StringBuilder(shiftDate.toString() + " " + shiftType.toString() + ":\n");
        for(Map.Entry<String, List<SEmployee>> roleRequests : shiftRequests.entrySet()) {
            result.append(roleRequests.getKey()).append(": ").append(roleRequests.getValue().stream().map(SEmployee::getId).collect(Collectors.joining(","))).append("\n");
        }
        return result.toString();
    }

    public String toString() {
        StringBuilder result = new StringBuilder(shiftDate.toString() + " " + shiftType.toString() + ":\n");
        result.append("Needed Roles:\n");
        for(Map.Entry<String, Integer> neededInRole : neededRoles.entrySet()) {
            result.append(neededInRole.getKey()).append(": ").append(neededInRole.getValue()).append("\n");
        }
        result.append("Requests:\n");
        for(Map.Entry<String, List<SEmployee>> roleRequests : shiftRequests.entrySet()) {
            result.append(roleRequests.getKey()).append(": ").append(roleRequests.getValue().stream().map(SEmployee::getId).collect(Collectors.joining(","))).append("\n");
        }
        result.append("Workers:\n");
        for(Map.Entry<String, List<SEmployee>> roleWorkers : shiftWorkers.entrySet()) {
            result.append(roleWorkers.getKey()).append(": ").append(roleWorkers.getValue().stream().map(SEmployee::getId).collect(Collectors.joining(","))).append("\n");
        }
        result.append("Cancel Card Applies:\n");
        for(String cardCancellation : cancelCardApplies) {
            result.append(cardCancellation).append("\n");
        }
        result.append("Shift Activities:\n");
        for(String shiftActivity : cancelCardApplies) {
            result.append(shiftActivity).append("\n");
        }
        result.append("isApproved: ").append(isApproved).append("\n");
        return result.toString();
    }
}
