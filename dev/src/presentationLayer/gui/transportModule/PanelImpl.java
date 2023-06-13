package presentationLayer.gui.transportModule;

import presentationLayer.gui.plAbstracts.Panel;

import javax.swing.*;
import java.awt.*;

public class PanelImpl extends Panel {

    private JScrollPane scrollPane;

    public PanelImpl() {
        super("src/resources/truck_main_page.jpg");
        init();
    }

    public PanelImpl(String fileName) {
        super(fileName);
        init();
    }

    public void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setSize((int)(d.getWidth()*0.8), d.height);
        JLabel header = new JLabel("Welcome to the Transport Module!");
        header.setVerticalAlignment(JLabel.TOP);
        panel.add(header);
        panel.setAutoscrolls(true);


        scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setSize((int)(d.getWidth()*0.8), d.height);
        scrollPane.setLocation((int)(d.getWidth()*0.2), 0);
    }

    @Override
    public Component getComponent() {
        return scrollPane;
    }
}
