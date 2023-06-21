package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;

import java.util.Set;

public abstract class AbstractObservableModel implements ObservableModel, Searchable {

    protected Set<ModelObserver> observers;
    public boolean errorOccurred;
    public String errorMessage;
    public String message;

    protected AbstractObservableModel() {
        observers = new java.util.HashSet<>();
    }

    @Override
    public boolean errorOccurred() {
        return errorOccurred;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMessage() {
        return message;
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

    @Override
    public boolean isMatch(String query) {
        return getLongDescription() != null
                && getShortDescription() != null
                && (getLongDescription().contains(query.trim()) || getShortDescription().contains(query.trim()));
    }

    @Override
    public String toString() {
        return getLongDescription();
    }
}
