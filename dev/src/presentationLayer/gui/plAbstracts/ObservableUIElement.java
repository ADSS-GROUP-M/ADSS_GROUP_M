package presentationLayer.gui.plAbstracts;

public interface ObservableUIElement extends UIElement{

    enum UIElementEvent {
        ADD,
        REMOVE,
        UPDATE,
        GET,
        GET_ALL
    }

    void subscribe(UIElementObserver observer, UIElementEvent event);
    void unsubscribe(UIElementObserver observer, UIElementEvent event);
    void notifyObservers(UIElementEvent event);
    Object getUpdate(UIElementEvent event);
}
