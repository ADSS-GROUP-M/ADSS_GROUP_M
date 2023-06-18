package presentationLayer.gui.plAbstracts.interfaces;

public interface ObservableUIElement extends UIElement {

    void subscribe(UIElementObserver observer);
    void unsubscribe(UIElementObserver observer);
}
