package presentationLayer.gui.transportModule.panels;

import presentationLayer.gui.plAbstracts.Panel;
import presentationLayer.gui.plAbstracts.ScrollablePanel;

import javax.swing.*;
import java.awt.*;

public class AddSitePanel extends ScrollablePanel {

    public AddSitePanel() {
        super("src/resources/truck_main_page.jpg");
        init();
    }

    private void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setSize(scrollPane.getSize());
        JLabel header = new JLabel("Enter Site details:\n");
        header.setVerticalAlignment(JLabel.TOP);
        panel.add(header);


        //Panel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel idLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(idLabel, constraints);

        JTextField idField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(idField, constraints);

        JLabel addressLabel = new JLabel("Address:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(addressLabel, constraints);

        JTextField addressField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(addressField, constraints);

        JLabel zoneLabel = new JLabel("Transport Zone");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(zoneLabel, constraints);

        JTextField zoneField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(zoneField, constraints);

        JLabel phoneLabel = new JLabel("Contact Phone");
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(phoneLabel, constraints);

        JTextField maxField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(maxField, constraints);

        JLabel nameLabel = new JLabel("Contact Name");
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(nameLabel, constraints);

        JTextField nameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, constraints);


        JLabel typeLabel = new JLabel("Site Type:");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(typeLabel, constraints);


        JPanel typePanel = new JPanel();
        JRadioButton logisticCenter = new JRadioButton("Logistic Center");
        constraints.gridx = 1;
        constraints.gridy = 6;
        //constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        JRadioButton branch = new JRadioButton("Branch");
        JRadioButton supplier = new JRadioButton("Supplier");
        typePanel.add(logisticCenter);
        typePanel.add(branch);
        typePanel.add(supplier);


        panel.add(typePanel, constraints);
    }

}
