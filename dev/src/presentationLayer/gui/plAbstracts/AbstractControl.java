package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.ObservableObject;
import presentationLayer.gui.plAbstracts.interfaces.UIElementObserver;

public abstract class AbstractControl implements UIElementObserver {

    public abstract ObservableObject getModel();

}
