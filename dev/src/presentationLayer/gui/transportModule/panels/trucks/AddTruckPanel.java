package presentationLayer.gui.transportModule.panels.trucks;

import presentationLayer.gui.plAbstracts.Panel;
import presentationLayer.gui.plAbstracts.ScrollablePanel;

import javax.swing.*;
import java.awt.*;

public class AddTruckPanel extends ScrollablePanel {

    public AddTruckPanel() {
        super();
        init();
    }

    private void init() {

        JTextField truckIdField = new JTextField();
        panel.add(truckIdField);

    }
}
