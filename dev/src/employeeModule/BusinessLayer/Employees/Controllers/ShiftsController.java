package employeeModule.BusinessLayer.Employees.Controllers;

import employeeModule.BusinessLayer.Employees.Employee;
import employeeModule.BusinessLayer.Employees.Role;
import employeeModule.BusinessLayer.Employees.Shift;
import employeeModule.BusinessLayer.Employees.Shift.ShiftType;
import employeeModule.employeeUtils.DateUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.next;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ShiftsController {
    private static ShiftsController instance;
    private Map<String, Map<LocalDate, Map<ShiftType,Shift>>> shifts; // branchID to <Date to <ShiftTime to Shift>>

    private ShiftsController() {
        this.shifts = new HashMap<>();
    }

    public static ShiftsController getInstance() {
        if (instance == null)
            instance = new ShiftsController();
        return instance;
    }

    public void resetData() {
        this.shifts.clear();
    }

    protected void createBranch(String branchId) throws Exception {
        if(this.shifts.containsKey(branchId))
            throw new Exception("The branch " + branchId + " already exists in the system.");
        this.shifts.put(branchId, new HashMap<>());
    }

    public Shift getShift(String branchId, LocalDate shiftDate, ShiftType shiftType) throws Exception {
        Map<LocalDate, Map<ShiftType,Shift>> shiftBranch = getShiftBranch(branchId);
        Map<ShiftType, Shift> shiftDay = getShiftDay(shiftBranch, shiftDate);
        if (!shiftDay.containsKey(shiftType))
            throw new Exception("The shift " + shiftDate + shiftType + " does not exist in the branch " + branchId + ".");
        return shiftDay.get(shiftType);
    }

    public boolean shiftExists(String branchId, LocalDate shiftDate, ShiftType shiftType) {
        if (!shifts.containsKey(branchId))
            return false;
        Map<LocalDate, Map<ShiftType,Shift>> shiftBranch = shifts.get(branchId);
        if (!shiftBranch.containsKey(shiftDate))
            return false;
        Map<ShiftType, Shift> shiftDay = shiftBranch.get(shiftDate);
        if (!shiftDay.containsKey(shiftType))
            return false;
        return true;
    }

    private Map<LocalDate, Map<ShiftType,Shift>> getShiftBranch(String branchId) throws Exception {
        if (!this.shifts.containsKey(branchId))
            throw new Exception("The branch " + branchId + " does not exist in the system.");
        return this.shifts.get(branchId);
    }

    private Map<ShiftType,Shift> getShiftDay(Map<LocalDate, Map<ShiftType, Shift>> shiftBranch, LocalDate shiftDate) throws Exception {
        if (!shiftBranch.containsKey(shiftDate))
            throw new Exception("The shift date " + shiftDate + " does not exist in this branch.");
        return shiftBranch.get(shiftDate);
    }

    public Shift createShift(String branchId, LocalDate shiftDate, ShiftType shiftType) throws Exception {
        Map<LocalDate, Map<ShiftType,Shift>> shiftBranch = getShiftBranch(branchId);
        if (!shiftBranch.containsKey(shiftDate))
            shiftBranch.put(shiftDate, new HashMap<>());
        Map<ShiftType, Shift> shiftDay = shiftBranch.get(shiftDate);
        if (shiftDay.containsKey(shiftType))
            throw new Exception("This shift already exists in the system.");
        Shift shift = new Shift(shiftDate, shiftType);
        shiftDay.put(shiftType, shift);
        return shift;
    }

    public void createWeekShifts(String branchId, LocalDate weekStart) throws Exception {
        // Get dates until the end of the week
        List<LocalDate> dates = weekStart.datesUntil(weekStart.with(next(DayOfWeek.SUNDAY))).toList();
        // Check if any shift in the week already exists
        for (LocalDate date : dates) {
            if (shiftExists(branchId, date, ShiftType.Morning) || shiftExists(branchId, date, ShiftType.Evening))
                throw new Exception("Invalid week start date, there are already existing shifts during this week, please edit them or delete them to start over.");
        }
        // Create the week shifts
        for (LocalDate date : dates) {
            createShift(branchId, date, ShiftType.Morning);
            createShift(branchId, date, ShiftType.Evening);
        }
    }

    public void deleteShift(String branchId, LocalDate shiftDate, ShiftType shiftType) throws Exception {
        Shift shift = getShift(branchId, shiftDate, shiftType);
        getShiftDay(getShiftBranch(branchId),shiftDate).remove(shiftType,shift);
    }

    public List<Shift[]> getWeekShifts(String branchId, LocalDate weekStart) {
        // Get dates until the end of the week
        List<LocalDate> dates = weekStart.datesUntil(weekStart.with(next(DayOfWeek.SUNDAY))).toList();
        // Add the corresponding shifts to a list
        List<Shift[]> result = new ArrayList<>();
        for (LocalDate date : dates) {
            Shift[] dayShifts = new Shift[2];
            try {
                Shift morningShift = getShift(branchId, date, ShiftType.Morning);
                dayShifts[0] = morningShift;
            } catch (Exception ignore) {}
            try {
                Shift eveningShift = getShift(branchId, date, ShiftType.Evening);
                dayShifts[1] = eveningShift;
            } catch (Exception ignore) {}
            result.add(dayShifts);
        }
        return result;
    }

    public void setShiftNeededAmount(String branchId, LocalDate shiftDate, ShiftType shiftType, Role role, int amount) throws Exception {
        Shift shift = getShift(branchId, shiftDate, shiftType);
        shift.setNeededRole(role, amount);
    }

    public void requestShift(Employee employee, String branchId, LocalDate shiftDate, ShiftType shiftType, Role role) throws Exception {
        Shift shift = getShift(branchId, shiftDate, shiftType);
        if (!employee.getRoles().contains(role))
            throw new Exception("Invalid shift request, the employee is not certified for this role: " + role + ".");
        if (shift.isEmployeeWorking(employee))
            throw new Exception("Invalid shift request, the employee is already working in this shift.");
        if (shift.isEmployeeRequesting(employee))
            throw new Exception("Invalid shift request, the employee has already requested to work in this shift.");
        if (employeeRequestedInDate(employee, shiftDate))
            throw new Exception("Invalid shift request, the employee has already requested to work in this day.");
        if (numEmployeeShiftRequestsInWeek(employee, shift.getShiftDate()) >= 6 && !employeeRequestedInDate(employee,shift.getShiftDate()))
            throw new Exception("Invalid shift request, the employee has already requested to work in 6 days of this week.");
        shift.addShiftRequest(role, employee);
    }

    public void cancelShiftRequest(Employee employee, String branchId, LocalDate shiftDate, ShiftType shiftType, Role role) throws Exception {
        Shift shift = getShift(branchId, shiftDate, shiftType);
        if (!shift.isEmployeeRequestingForRole(employee, role))
            throw new Exception("Invalid shift request, the employee hasn't requested to work in this shift and this role.");
        shift.removeShiftRequest(role, employee);
    }

    private List<Shift> employeeShiftRequestsInWeek(Employee employee, LocalDate dayInWeek) {
        List<Shift> morningShifts = getEmployeeRequests(employee).stream().map(s->s[0]).toList();
        List<Shift> eveningShifts = getEmployeeRequests(employee).stream().map(s->s[1]).toList();
        List<Shift> shifts = Stream.concat(morningShifts.stream(),eveningShifts.stream()).toList();
        return shifts.stream().filter(s-> s != null && DateUtils.getWeekNumber(s.getShiftDate()) == DateUtils.getWeekNumber(dayInWeek)).toList();
    }

    private int numEmployeeShiftRequestsInWeek(Employee employee, LocalDate dayInWeek) {
        return employeeShiftRequestsInWeek(employee, dayInWeek).size();
    }

    private boolean employeeRequestedInDate(Employee employee, LocalDate dayInWeek) {
        return !getEmployeeRequests(employee).stream().filter(s-> s[0].getShiftDate().equals(dayInWeek)).toList().isEmpty();
    }

    public void setShiftEmployees(String branchId, LocalDate shiftDate, ShiftType shiftType, Role role, List<Employee> employees) throws Exception {
        Shift shift = getShift(branchId, shiftDate, shiftType);
        for (Employee employee : employees)
            if (!employee.getRoles().contains(role))
                throw new Exception("Invalid shift request, the employee " + employee.getId() + " is not certified for this role: " + role + ".");
        for (Employee employee : employees)
            shift.addShiftWorker(role, employee);
    }

    public List<Shift[]> getEmployeeShifts(Employee employee) {
        List<Shift[]> result = new ArrayList<>();
        for (Map<LocalDate, Map<ShiftType,Shift>> branchShifts : shifts.values()) {
            for (Map<ShiftType, Shift> dayShifts : branchShifts.values()) {
                Shift[] shifts = new Shift[2];
                if (dayShifts.containsKey(ShiftType.Morning)) {
                    Shift morningShift = dayShifts.get(ShiftType.Morning);
                    if (morningShift.isEmployeeWorking(employee))
                        shifts[0] = morningShift;
                }
                if (dayShifts.containsKey(ShiftType.Evening)) {
                    Shift eveningShift = dayShifts.get(ShiftType.Evening);
                    if (eveningShift.isEmployeeWorking(employee))
                        shifts[1] = eveningShift;
                }
                if (shifts[0] != null || shifts[1] != null)
                    result.add(shifts);
            }
        }
        return result;
    }

    public List<Shift[]> getEmployeeRequests(Employee employee) {
        List<Shift[]> result = new ArrayList<>();
        for (Map<LocalDate, Map<ShiftType,Shift>> branchShifts : shifts.values()) {
            for (Map<ShiftType, Shift> dayShifts : branchShifts.values()) {
                Shift[] shifts = new Shift[2];
                if (dayShifts.containsKey(ShiftType.Morning)) {
                    Shift morningShift = dayShifts.get(ShiftType.Morning);
                    if (morningShift.isEmployeeRequesting(employee))
                        shifts[0] = morningShift;
                }
                if (dayShifts.containsKey(ShiftType.Evening)) {
                    Shift eveningShift = dayShifts.get(ShiftType.Evening);
                    if (eveningShift.isEmployeeRequesting(employee))
                        shifts[1] = eveningShift;
                }
                if (shifts[0] != null || shifts[1] != null)
                    result.add(shifts);
            }
        }
        return result;
    }

    public void approveShift(String branchId, LocalDate shiftDate, ShiftType shiftType) throws Exception {
        Shift shift = getShift(branchId, shiftDate, shiftType);
        shift.approve();
    }

    public void applyCancelCard(String branchId, LocalDate shiftDate, ShiftType shiftType, String employeeId, String productId) throws Exception {
        Shift shift = getShift(branchId, shiftDate, shiftType);
        shift.useCancelCard(employeeId, productId);
    }

    public void reportShiftActivity(String branchId, LocalDate shiftDate, ShiftType shiftType, String reportingEmployeeId, String activity) throws Exception {
        Shift shift = getShift(branchId, shiftDate, shiftType);
        shift.reportActivity(reportingEmployeeId, activity);
    }
}
