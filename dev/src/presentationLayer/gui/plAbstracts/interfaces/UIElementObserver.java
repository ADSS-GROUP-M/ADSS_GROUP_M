package presentationLayer.gui.plAbstracts.interfaces;

import java.util.List;

public interface UIElementObserver {
    default void add(ObservableUIElement observable){}
    default void remove(ObservableUIElement observable) {}
    default void update(ObservableUIElement observable){}
    default void get(ObservableUIElement observable){}
    default void getAll(ObservableUIElement observable){}
}
