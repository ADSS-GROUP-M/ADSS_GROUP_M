package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.interfaces.Searchable;

public class SearchableString implements Searchable {

    private String string;

    public SearchableString(String string) {
        this.string = string;
    }

    @Override
    public boolean isMatchExactly(String query) {
        return string.equals(query);
    }

    @Override
    public boolean isMatch(String query) {
        return string.contains(query);
    }

    @Override
    public String getShortDescription() {
        return string;
    }

    @Override
    public String getLongDescription() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }
}
