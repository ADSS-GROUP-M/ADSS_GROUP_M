package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.ObservableObject;
import presentationLayer.gui.plAbstracts.interfaces.UIElementObserver;

import java.util.List;

public abstract class AbstractControl implements UIElementObserver {

    public abstract ObservableObject getModel(ObservableObject lookupObject);
    public abstract ObservableObject getEmptyModel();
    public abstract List<ObservableObject> getAllModels();
}
