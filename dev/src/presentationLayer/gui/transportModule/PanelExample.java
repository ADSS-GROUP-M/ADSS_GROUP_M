package presentationLayer.gui.transportModule;

import presentationLayer.gui.plAbstracts.ScrollablePanel;
import presentationLayer.gui.plUtils.ContentPanel;
import presentationLayer.gui.plUtils.TextNote;

import javax.swing.*;
import java.awt.*;

import static presentationLayer.gui.plUtils.ContentPanel.COLOR;

public class PanelExample extends ScrollablePanel {

    private TextNote text;

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
        header.setFont(header.getFont().deriveFont(30f));
        contentPanel = new ContentPanel(COLOR);

        contentPanel.setLayout(new GridBagLayout());
        panel.setLayout(new GridBagLayout());

        text = new TextNote("""
                This is an example of a panel.
                
                
                You can add any component you want to it.
                the blue part is the content panel.""");
        text.setSize(300,600);
        text.setFont(new Font("Arial", Font.PLAIN, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        contentPanel.add(text,gbc);

        gbc.insets = new Insets(50,0,75,0);
        panel.add(header,gbc);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0,0,0,0);
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(contentPanel,gbc);
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension preferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight()*0.6));
        contentPanel.setPreferredSize(preferredSize);
        scrollPane.revalidate();
    }
}
