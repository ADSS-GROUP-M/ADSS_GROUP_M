package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableModel;

import java.util.HashMap;

public class ObservableItemList extends AbstractObservableModel {

    public int id;
    public HashMap<String, Integer> load;
    public HashMap<String, Integer> unload;

    @Override
    public ObservableItemList getUpdate() {
        return null;
    }

    @Override
    public boolean isMatchExactly(String query) {
        return String.valueOf(id).equals(query.trim());
    }

    @Override
    public boolean isMatch(String query) {
        return String.valueOf(id).contains(query.trim()) || super.isMatch(query);
    }

    @Override
    public String getShortDescription() {
        return String.valueOf(id);
    }

    @Override
    public String getLongDescription() {
        throw new UnsupportedOperationException("Item List does not have a long description");
    }
}
