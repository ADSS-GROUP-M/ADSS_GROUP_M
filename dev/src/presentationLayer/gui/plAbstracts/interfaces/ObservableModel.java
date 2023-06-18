package presentationLayer.gui.plAbstracts.interfaces;

public interface ObservableModel {

    void subscribe(ModelObserver observer);
    void unsubscribe(ModelObserver observer);
    void notifyObservers();
    ObservableModel getUpdate();
}
