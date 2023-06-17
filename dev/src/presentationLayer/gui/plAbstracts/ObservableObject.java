package presentationLayer.gui.plAbstracts;

public interface ObservableObject<T> {

    void subscribe(ObjectObserver<T> observer);
    void unsubscribe(ObjectObserver<T> observer);
    void notifyObservers();
    T getUpdate();
}
