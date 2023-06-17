package presentationLayer.gui.plAbstracts;

public interface ObservableUIElement extends UIElement{

    void subscribe(UIElementObserver observer);
    void unsubscribe(UIElementObserver observer);
    void notifyObservers();
    Object getUpdate();
}
