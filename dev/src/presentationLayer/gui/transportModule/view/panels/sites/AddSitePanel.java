package presentationLayer.gui.transportModule.view.panels.sites;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.transportModule.control.SitesControl;

import javax.swing.*;
import java.awt.*;

public class AddSitePanel extends AbstractTransportModulePanel {
    public AddSitePanel(SitesControl control) {
        super(control);
        init();
    }
    private void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
        Dimension textFieldSize = new Dimension(200,30);

        //Panel panel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel idLabel = new JLabel("Name:");
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

        JLabel addressLabel = new JLabel("Address:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(addressLabel, constraints);

        PrettyTextField addressField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(addressField.getComponent(), constraints);

        JLabel zoneLabel = new JLabel("Transport Zone:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(zoneLabel, constraints);

        PrettyTextField zoneField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(zoneField.getComponent(), constraints);

        JLabel phoneLabel = new JLabel("Contact Phone:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPanel.add(phoneLabel, constraints);

        PrettyTextField maxField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(maxField.getComponent(), constraints);

        JLabel nameLabel = new JLabel("Contact Name:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPanel.add(nameLabel, constraints);

        PrettyTextField nameField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(nameField.getComponent(), constraints);


        JLabel typeLabel = new JLabel("Site Type:");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(typeLabel, constraints);


        JPanel typePanel = new JPanel();
        JRadioButton logisticCenter = new JRadioButton("Logistic Center");
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        JRadioButton branch = new JRadioButton("Branch");
        JRadioButton supplier = new JRadioButton("Supplier");
        typePanel.add(logisticCenter);
        typePanel.add(branch);
        typePanel.add(supplier);
        contentPanel.add(typePanel, constraints);
        ButtonGroup group = new ButtonGroup();
        group.add(logisticCenter);
        group.add(branch);
        group.add(supplier);
    }

    

    @Override
    public void notify(ObservableModel observable) {

    }
}
