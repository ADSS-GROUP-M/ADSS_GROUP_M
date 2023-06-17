package presentationLayer.gui.plAbstracts;

import static presentationLayer.gui.plAbstracts.ObservableUIElement.*;

public interface UIElementObserver {
    void update(ObservableUIElement observable, UIElementEvent event);
}
