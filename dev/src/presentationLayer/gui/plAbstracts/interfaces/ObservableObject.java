package presentationLayer.gui.plAbstracts.interfaces;

public interface ObservableObject {

    void subscribe(ObjectObserver observer);
    void unsubscribe(ObjectObserver observer);
    void notifyObservers();
    ObservableObject getUpdate();
}
