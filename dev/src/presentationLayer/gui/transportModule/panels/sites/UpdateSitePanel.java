package presentationLayer.gui.transportModule.panels;

import presentationLayer.gui.plAbstracts.TransportBasePanel;

import javax.swing.*;
import java.awt.*;

public class UpdateSitePanel extends TransportBasePanel {
    public UpdateSitePanel() {
        super("src/resources/truck_main_page.jpg");
        init();
    }
    private void init() {

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
        JLabel header = new JLabel("Update Site:\n");
        header.setVerticalAlignment(JLabel.TOP);
        contentPanel.add(header);

        //Panel panel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel trucksLabel = new JLabel("Pick Site:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(trucksLabel, constraints);
        JButton addButton2 = new JButton("Sites");
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(addButton2, constraints);

        JLabel baseLabel = new JLabel("New Contact Name:");
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        contentPanel.add(baseLabel, constraints);

        JTextField baseField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(baseField, constraints);

        JLabel maxLabel = new JLabel("New Contact Number:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(maxLabel, constraints);

        JTextField maxField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(maxField, constraints);
    }
}
