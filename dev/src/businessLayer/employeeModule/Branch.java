package businessLayer.employeeModule;

import exceptions.EmployeeException;

import java.time.LocalTime;
import java.util.Objects;

public class Branch {
    private String name;
    private LocalTime morningStart;
    private LocalTime morningEnd;
    private LocalTime eveningStart;
    private LocalTime eveningEnd;

    public static final String HEADQUARTERS_ID = "branch1";

    public Branch(String name) {
        this();
        this.name = name;
    }

    public Branch() {
        // Default values of branch working hours
        morningStart = LocalTime.of(8,0);
        morningEnd = LocalTime.of(16,0);
        eveningStart = LocalTime.of(16,0);
        eveningEnd = LocalTime.of(22,0);
    }


    public Branch(String name, LocalTime morningShiftStart,LocalTime morningShiftFinish, LocalTime eveningShiftStart, LocalTime eveningShiftFinish){
        this.morningStart = morningShiftStart;
        this.morningEnd = morningShiftFinish;
        this.eveningStart = eveningShiftStart;
        this.eveningEnd = eveningShiftFinish;
        this.name = name;
    }

    public LocalTime getMorningStart() {
        return this.morningStart;
    }

    public LocalTime getMorningEnd() {
        return morningEnd;
    }

    public LocalTime getEveningStart() {
        return eveningStart;
    }

    public LocalTime getEveningEnd() {
        return eveningEnd;
    }

    public void setWorkingHours(LocalTime mFrom, LocalTime mUntil,LocalTime eFrom, LocalTime eUntil){
        this.morningStart = mFrom;
        this.morningEnd = mUntil;
        this.eveningStart = eFrom;
        this.eveningEnd = eUntil;
    }

    public Shift.ShiftType getShiftType(LocalTime localTime) throws EmployeeException {
        if (localTime.equals(morningStart) || (localTime.isAfter(morningStart) && localTime.isBefore(morningEnd)) || localTime.equals(morningEnd))
            return Shift.ShiftType.Morning;
        else if (localTime.equals(eveningStart) || (localTime.isAfter(eveningStart) && localTime.isBefore(eveningEnd)) || localTime.equals(eveningEnd))
            return Shift.ShiftType.Evening;
        throw new EmployeeException("The given LocalTime isn't included in any shift of the day.");
    }

    public String name() {
        return name;
    }

    public static Branch getLookupObject(String address) {
        return new Branch(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Branch branch = (Branch) o;
        return name == branch.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
