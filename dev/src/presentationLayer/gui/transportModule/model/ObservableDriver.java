package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;

import static domainObjects.transportModule.Driver.LicenseType;

public class ObservableDriver extends AbstractObservableModel {
    public String id;
    public String name;
    public LicenseType licenseType;
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
        return String.format("Id: %s | Name: %s | Licence: %s",
                id, name, licenseType);
    }
}
