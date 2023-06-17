package presentationLayer.gui.plAbstracts.interfaces;

import java.awt.*;

public interface UIElement {
    Component getComponent();
    void componentResized(Dimension newSize);
}
