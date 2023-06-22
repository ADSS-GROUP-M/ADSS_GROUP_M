package presentationLayer.gui.employeeModule.view.panels;

import presentationLayer.gui.plAbstracts.ScrollablePanel;

import javax.swing.*;
import java.awt.*;

public class StoreManagerPanel extends ScrollablePanel {

    public StoreManagerPanel() {
        super();
        init();
    }

    public void init() {
        JLabel header = new JLabel("Welcome to the Store Manager Page!");
        header.setVerticalAlignment(JLabel.TOP);
        panel.add(header);
    }
}