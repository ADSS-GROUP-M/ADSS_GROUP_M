package presentationLayer.gui.employeeModule.model;

import businessLayer.employeeModule.Authorization;
import presentationLayer.gui.plAbstracts.AbstractObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;

import java.time.LocalDate;
import java.util.Set;

public final class ObservableUser extends AbstractObservableModel {

    public String username;
    public String password;
    public Set<Authorization> authorizations;
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
