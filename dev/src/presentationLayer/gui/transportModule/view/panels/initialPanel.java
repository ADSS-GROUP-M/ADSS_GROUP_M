package presentationLayer.gui.transportModule.view.panels;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;

import javax.swing.*;
import java.awt.*;

public class initialPanel extends AbstractTransportModulePanel {

    public initialPanel(){
        super(null);
    }

    @Override
    protected void clearFields() {
        // do nothing
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        panel.revalidate();
    }
}
