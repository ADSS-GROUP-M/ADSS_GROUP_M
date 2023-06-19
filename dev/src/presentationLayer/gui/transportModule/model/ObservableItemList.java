package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableModel;

import java.util.HashMap;

public class ObservableItemList extends AbstractObservableModel {

    public int id;
    public HashMap<String, Integer> load;
    public HashMap<String, Integer> unload;
    public String response;

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

    }
}
