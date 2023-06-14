package presentationLayer.gui.plAbstracts;

import java.awt.*;

public interface UIElement {
    Component getComponent();
    void componentResized(Dimension newSize);
}
