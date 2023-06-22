package presentationLayer.gui.transportModule.view.panels;

import presentationLayer.gui.plAbstracts.AbstractModulePanel;

import java.awt.*;

public class initialPanel extends AbstractModulePanel {

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
