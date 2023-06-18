package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.UIElementObserver;

import java.util.List;

public abstract class AbstractControl implements UIElementObserver {

    public abstract ObservableModel getModel(ObservableModel lookupObject);
    public abstract ObservableModel getEmptyModel();
    public abstract List<ObservableModel> getAllModels();
}
