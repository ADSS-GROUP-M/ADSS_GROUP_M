package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.ObjectObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableObject;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;

import java.util.Set;

public abstract class AbstractObservableObject implements ObservableObject, Searchable {

    protected Set<ObjectObserver> observers;

    protected AbstractObservableObject() {
        observers = new java.util.HashSet<>();
    }

    @Override
    public void subscribe(ObjectObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(ObjectObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ObjectObserver observer : observers) {
            observer.notify(this);
        }
    }
}
