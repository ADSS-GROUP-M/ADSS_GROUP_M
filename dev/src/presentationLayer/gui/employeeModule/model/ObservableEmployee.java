package presentationLayer.gui.employeeModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import serviceLayer.employeeModule.Objects.SShiftType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ObservableEmployee extends AbstractObservableModel {

    public String fullName;
    public String id;
    public LocalDate employmentDate;
    public String employmentConditions;
    public Set<String> roles;
    public String details;
    public double expectedSalary;
    public String response;

    @Override
    public ObservableModel getUpdate() {
        return null;
    }

    @Override
    public boolean isMatch(String query) {
        return false;
    }

    @Override
    public String getShortDescription() {
        return null;
    }

    @Override
    public String getLongDescription() {
        return null;
    }
}
