package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableObject;

import static domainObjects.transportModule.Site.*;

public class ObservableSite extends AbstractObservableObject {
    public String name;
    public String address;
    public String transportZone;
    public String phoneNumber;
    public String contactName;
    public SiteType siteType;
    public double latitude;
    public double longitude;

    @Override
    public ObservableSite getUpdate() {
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
