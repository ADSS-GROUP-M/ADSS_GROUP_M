package presentationLayer.gui.transportModule.view.panels.trucks;

import presentationLayer.gui.plAbstracts.TransportBasePanel;

import javax.swing.*;
import java.awt.*;


public class UpdateTruckPanel extends TransportBasePanel {
    public UpdateTruckPanel() {
        super();
        init();
    }
    private void init() {

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
        JLabel header = new JLabel("Update Truck:\n");
        header.setVerticalAlignment(JLabel.TOP);

        //Panel panel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(header, constraints);

        JLabel trucksLabel = new JLabel("Pick Truck:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(trucksLabel, constraints);
        JButton addButton2 = new JButton("Trucks");
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(addButton2, constraints);

        JLabel baseLabel = new JLabel("New Base Weight:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        contentPanel.add(baseLabel, constraints);

        JTextField baseField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(baseField, constraints);

        JLabel maxLabel = new JLabel("New Max Weight:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(maxLabel, constraints);

        JTextField maxField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(maxField, constraints);


        JLabel coolingLabel = new JLabel("New Cooling Capacity:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(coolingLabel, constraints);

        JPanel coolingPanel = new JPanel();
        JRadioButton none = new JRadioButton("NONE");
        constraints.gridx = 1;
        constraints.gridy = 4;
        //constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        JRadioButton cold = new JRadioButton("COLD");
        JRadioButton frozen = new JRadioButton("FROZEN");
        coolingPanel.add(none);
        coolingPanel.add(cold);
        coolingPanel.add(frozen);

        contentPanel.add(coolingPanel, constraints);
    }

    @Override
    public Object getUpdate(UIElementEvent event) {
        return null;
    }
}
