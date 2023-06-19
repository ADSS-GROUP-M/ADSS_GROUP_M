package presentationLayer.gui.plAbstracts.interfaces;

import presentationLayer.gui.plUtils.ObservableList;

/**
 * Observer interface for UIElements. <br/>
 * This interface contains empty default methods for all the methods.<br/>
 * This is done so that the observer can choose which events to catch.
 *
 * @see ObservableUIElement
 */
public interface UIElementObserver {
    default void add(ObservableUIElement observable, ObservableModel model){}
    default void remove(ObservableUIElement observable, ObservableModel model) {}
    default void update(ObservableUIElement observable, ObservableModel model){}
    default void get(ObservableUIElement observable, ObservableModel model){}
    default void getAll(ObservableUIElement observable, ObservableList<ObservableModel> models){}
}
