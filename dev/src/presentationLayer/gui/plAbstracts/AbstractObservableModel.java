package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;

import java.util.Set;

public abstract class AbstractObservableModel implements ObservableModel, Searchable {

    protected Set<ModelObserver> observers;

    protected AbstractObservableModel() {
        observers = new java.util.HashSet<>();
    }

    @Override
    public void subscribe(ModelObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(ModelObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ModelObserver observer : observers) {
            observer.notify(this);
        }
    }
}
