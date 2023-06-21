package presentationLayer.gui.plAbstracts.interfaces;

public interface Searchable {

    boolean isMatchExactly(String query);
    boolean isMatch(String query);
    String getShortDescription();
    String getLongDescription();
}
