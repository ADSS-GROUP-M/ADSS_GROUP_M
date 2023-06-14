package presentationLayer.gui.transportModule;

import presentationLayer.gui.plAbstracts.ScrollablePanel;

import javax.swing.*;

public class PanelExample extends ScrollablePanel {

    public PanelExample() {
        super("src/resources/truck_main_page.jpg");
        init();
    }

    public PanelExample(String fileName) {
        super(fileName);
        init();
    }

    private void init() {
        JLabel header = new JLabel("Welcome to the Transport Module!");
        header.setVerticalAlignment(JLabel.TOP);
        panel.add(header);
    }
}
