package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableObject;
import presentationLayer.gui.plAbstracts.interfaces.ObservableObject;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObservableTransport extends AbstractObservableObject {

    public int id;
    public List<String> route;
    public Map<String,Integer> destinations_itemListIds;
    public String driverId;
    public String truckId;
    public LocalDateTime departureTime;
    public int weight;
    public Set<String> destinationsSet;
    public Map<String, LocalTime> estimatedArrivalTimes;
    public boolean manualOverride;

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
