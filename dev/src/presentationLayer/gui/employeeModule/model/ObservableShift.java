package presentationLayer.gui.employeeModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import serviceLayer.employeeModule.Objects.SEmployee;
import serviceLayer.employeeModule.Objects.SShiftType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static domainObjects.transportModule.Truck.CoolingCapacity;

public final class ObservableShift extends AbstractObservableModel {

    public LocalDate shiftDate;
    public SShiftType shiftType;
    public Map<String, List<ObservableEmployee>> shiftRequests;
    public Map<String, List<ObservableEmployee>> shiftWorkers;
    public boolean isApproved;
    public String response;

    @Override
    public ObservableModel getUpdate() {
        return null;
    }

    @Override
    public boolean errorOccurred() {
        return false;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public boolean isMatchExactly(String query) {
        return false;
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
