package presentationLayer.gui.plAbstracts.interfaces;

import static presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement.*;

public interface UIElementObserver {
    void notify(ObservableUIElement observable, UIElementEvent event);
}
