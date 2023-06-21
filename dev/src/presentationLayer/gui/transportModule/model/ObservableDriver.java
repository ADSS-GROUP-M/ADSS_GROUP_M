package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;

import static domainObjects.transportModule.Driver.LicenseType;

public class ObservableDriver extends AbstractObservableModel {
    public String id;
    public String name;
    public LicenseType licenseType;

    @Override
    public ObservableModel getUpdate() {
        return null;
    }

    @Override
    public boolean isMatchExactly(String query) {
        return id.equals(query.trim());
    }

    @Override
    public boolean isMatch(String query) {
        return id.contains(query.trim()) || super.isMatch(query);
    }

    @Override
    public String getShortDescription() {
        return id + " - " + name ;
    }

    @Override
    public String getLongDescription() {
        return String.format("Id: %s | Name: %s | Licence: %s",
                id, name, licenseType);
    }
}
