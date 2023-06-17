package presentationLayer.gui.transportModule.view.panels.trucks;

import presentationLayer.gui.plAbstracts.TransportBasePanel;

import javax.swing.*;
import java.awt.*;

public class AddTruckPanel extends TransportBasePanel {

    public AddTruckPanel() {
        super();
        init();
    }

    private void init() {

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
        JLabel header = new JLabel("Enter Truck Details:\n");
        header.setVerticalAlignment(JLabel.TOP);
        contentPanel.add(header);

        //Panel panel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel idLabel = new JLabel("License Plate:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(idLabel, constraints);

        JTextField idField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(idField, constraints);

        JLabel modelLabel = new JLabel("Model:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(modelLabel, constraints);

        JTextField modelField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(modelField, constraints);

        JLabel baseLabel = new JLabel("Base Weight:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(baseLabel, constraints);

        JTextField baseField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(baseField, constraints);

        JLabel maxLabel = new JLabel("Max Weight:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPanel.add(maxLabel, constraints);

        JTextField maxField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(maxField, constraints);

        JLabel coolingLabel = new JLabel("Cooling Capacity:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(coolingLabel, constraints);

        JPanel coolingPanel = new JPanel();
        JRadioButton none = new JRadioButton("NONE");
             constraints.gridx = 1;
        constraints.gridy = 5;
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
    public Object getUpdate() {
        return null;
    }
}
