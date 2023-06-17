package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableObject;

import java.util.HashMap;

public class ObservableItemList extends AbstractObservableObject {

    public int id;
    public HashMap<String, Integer> load;
    public HashMap<String, Integer> unload;

    @Override
    public ObservableItemList getUpdate() {
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
