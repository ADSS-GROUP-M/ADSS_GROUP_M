package presentationLayer.gui.transportModule;

import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.plAbstracts.ScrollablePanel;
import presentationLayer.gui.plUtils.ContentPanel;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.RELATIVE;

public class PanelExample extends ScrollablePanel {

    private TextNote text;

    public class TextNote extends JTextArea {
        public TextNote(String text) {
            super(text);
            setBackground(null);
            setOpaque(false);
            setEditable(false);
            setBorder(null);
            setLineWrap(true);
            setWrapStyleWord(true);
            setFocusable(false);
        }
    }

    private static final Color COLOR = new Color(107, 190, 255, 150);
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

        gbc.weighty = 1;
        panel.add(header,gbc);

        gbc.gridy = 1;
        gbc.weighty = 2;
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
