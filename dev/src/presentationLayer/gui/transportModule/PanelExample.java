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
        Dimension d = MainWindow.screenSize;
        contentPanel.setMinimumSize(new Dimension((int)(d.width*0.2), (int)(d.height*0.5)));
        panel.add(contentPanel,gbc);
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension preferredSize = new Dimension((int) (panel.getWidth() * 0.8), panel.getHeight());
        contentPanel.setPreferredSize(preferredSize);
        contentPanel.validate();
        panel.validate();
    }
}
