package presentationLayer.gui.transportModule.view.panels.trucks;

import presentationLayer.gui.plAbstracts.TransportBasePanel;
import presentationLayer.gui.plUtils.PrettyTextField;

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

        Dimension textFieldSize = new Dimension(220,30);

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel idLabel = new JLabel("License Plate:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(idLabel, constraints);

        PrettyTextField idField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(idField.getComponent(), constraints);

        JLabel modelLabel = new JLabel("Model:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(modelLabel, constraints);

        PrettyTextField modelField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(modelField.getComponent(), constraints);

        JLabel baseLabel = new JLabel("Base Weight:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(baseLabel, constraints);

        PrettyTextField baseField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(baseField.getComponent(), constraints);

        JLabel maxLabel = new JLabel("Max Weight:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(maxLabel, constraints);

        PrettyTextField maxField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(maxField.getComponent(), constraints);

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
    public Object getUpdate(UIElementEvent event) {
        return null;
    }
}
