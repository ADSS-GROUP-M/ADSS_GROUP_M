package presentationLayer.gui.transportModule.panels;

import presentationLayer.gui.plAbstracts.TransportBasePanel;

import javax.swing.*;
import java.awt.*;

public class AddTransportPanel extends TransportBasePanel {
    public AddTransportPanel() {
        super("src/resources/truck_main_page.jpg");
        init();
    }

    private void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
            JLabel header = new JLabel("Enter transport details:\n");

        header.setVerticalAlignment(JLabel.TOP);
        contentPanel.add(header);


        //Panel panel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel dateLabel = new JLabel("Departure Date:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(dateLabel, constraints);

        JTextField dateField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(dateField, constraints);

        JLabel timeLabel = new JLabel("Departure Time:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(timeLabel, constraints);

        JTextField timeField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(timeField, constraints);

        JLabel driversLabel = new JLabel("Pick Driver:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(driversLabel, constraints);
        JButton addButton = new JButton("Add Driver");
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(addButton, constraints);


        JLabel trucksLabel = new JLabel("Pick Truck:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(trucksLabel, constraints);
        JButton addButton2 = new JButton("Add Truck");
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(addButton2, constraints);

        JLabel weightLabel = new JLabel("Truck Weight:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPanel.add(weightLabel, constraints);

        JTextField weightField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(weightField, constraints);



    }

}
