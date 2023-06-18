package presentationLayer.gui.plAbstracts.interfaces;

/**
 * Observer interface for UIElements. <br/>
 * This interface contains empty default methods for all the methods.<br/>
 * This is done so that the observer can choose which events to catch.
 *
 * @see ObservableUIElement
 */
public interface UIElementObserver {
    default void add(ObservableUIElement observable){}
    default void remove(ObservableUIElement observable) {}
    default void update(ObservableUIElement observable){}
    default void get(ObservableUIElement observable){}
    default void getAll(ObservableUIElement observable){}
}
