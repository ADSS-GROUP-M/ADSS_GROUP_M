package presentationLayer.gui.transportModule;

import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.plAbstracts.ScrollablePanel;
import presentationLayer.gui.plUtils.ContentPanel;

import javax.swing.*;
import java.awt.*;

public class PanelExample extends ScrollablePanel {

    private ContentPanel contentPanel;

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
        contentPanel = new ContentPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        contentPanel.add(header);
        panel.add(contentPanel);
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension preferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight()*0.5));
        contentPanel.setPreferredSize(preferredSize);
        scrollPane.revalidate();
    }
}
