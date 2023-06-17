package presentationLayer.gui.plAbstracts.interfaces;

public interface Searchable {
    boolean isMatch(String query);
    String getShortDescription();
    String getLongDescription();
}
