package dev.ServiceLayer.Objects;

import dev.BusinessLayer.Employees.Employee;
import dev.BusinessLayer.Employees.Role;
import dev.BusinessLayer.Employees.Shift;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SShift {
    private LocalDate shiftDate;
    private SShiftType shiftType;
    private Map<String, List<SEmployee>> shiftRequests;
    private Map<String, List<SEmployee>> shiftWorkers;
    private boolean isApproved;

    public SShift(Shift shift) {
        this.shiftDate = shift.getShiftDate();
        this.shiftType = SShiftType.valueOf(shift.getShiftType().toString());
        this.shiftRequests = new HashMap<>();
        for(Map.Entry<Role, List<Employee>> entry : shift.getShiftRequests().entrySet()) {
            this.shiftRequests.put(entry.getKey().toString(),entry.getValue().stream().map(SEmployee::new).collect(Collectors.toList()));
        }
        this.shiftWorkers = new HashMap<>();
        for(Map.Entry<Role, List<Employee>> entry : shift.getShiftWorkers().entrySet()) {
            this.shiftWorkers.put(entry.getKey().toString(),entry.getValue().stream().map(SEmployee::new).collect(Collectors.toList()));
        }
        this.isApproved = shift.getIsApproved();
    }

    public boolean isApproved() {
        return isApproved;
    }

    public String workersString() {
        String result = shiftDate.toString() + " " + shiftType.toString() + ":\n";
        if (!isApproved)
            return result + "Not approved yet.";
        for(Map.Entry<String, List<SEmployee>> roleWorkers : shiftWorkers.entrySet())
            result += roleWorkers.getKey() + ": " + roleWorkers.getValue().stream().map(SEmployee::getId).collect(Collectors.joining(",")) + "\n";
        return result;
    }

    public String requestsString() {
        String result = shiftDate.toString() + " " + shiftType.toString() + ":\n";
        for(Map.Entry<String, List<SEmployee>> roleRequests : shiftRequests.entrySet())
            result += roleRequests.getKey() + ": " + roleRequests.getValue().stream().map(SEmployee::getId).collect(Collectors.joining(",")) + "\n";
        return result;
    }

    public String toString() {
        String result = shiftDate.toString() + " " + shiftType.toString() + ":\n";
        result += "Requests:\n";
        for(Map.Entry<String, List<SEmployee>> roleRequests : shiftRequests.entrySet())
            result += roleRequests.getKey() + ": " + roleRequests.getValue().stream().map(SEmployee::getId).collect(Collectors.joining(",")) + "\n";
        result += "Workers:\n";
        for(Map.Entry<String, List<SEmployee>> roleWorkers : shiftWorkers.entrySet())
            result += roleWorkers.getKey() + ": " + roleWorkers.getValue().stream().map(SEmployee::getId).collect(Collectors.joining(",")) + "\n";
        result += "isApproved: " + isApproved + "\n";
        return result;
    }
}
