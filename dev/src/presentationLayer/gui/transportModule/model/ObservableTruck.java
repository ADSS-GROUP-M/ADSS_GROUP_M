package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableObject;
import presentationLayer.gui.plAbstracts.interfaces.ObservableObject;

import static domainObjects.transportModule.Truck.CoolingCapacity;

public final class ObservableTruck extends AbstractObservableObject {

    public String id;
    public String model;
    public int baseWeight;
    public int maxWeight;
    public CoolingCapacity coolingCapacity;

    @Override
    public ObservableObject getUpdate() {
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
