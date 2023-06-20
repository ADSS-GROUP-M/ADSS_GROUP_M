package presentationLayer.gui.transportModule.view.panels.sites;

import domainObjects.transportModule.Site;
import domainObjects.transportModule.Truck;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.transportModule.control.SitesControl;
import presentationLayer.gui.transportModule.model.ObservableSite;
import presentationLayer.gui.transportModule.model.ObservableTruck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddSitePanel extends AbstractTransportModulePanel {
    private JLabel idLabel;
    private PrettyTextField idField;
    private JLabel addressLabel;
    private PrettyTextField addressField;
    private JLabel zoneLabel;
    private PrettyTextField zoneField;
    private JLabel phoneLabel;
    private PrettyTextField maxField;
    private JLabel nameLabel;
    private PrettyTextField nameField;
    private JLabel typeLabel;
    private JPanel typePanel;
    private JRadioButton logisticCenter;
    private JRadioButton branch;
    private JRadioButton supplier;

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

        idLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(idLabel, constraints);

        idField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(idField.getComponent(), constraints);

        addressLabel = new JLabel("Address:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(addressLabel, constraints);

        addressField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(addressField.getComponent(), constraints);

        zoneLabel = new JLabel("Transport Zone:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(zoneLabel, constraints);

        zoneField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(zoneField.getComponent(), constraints);

        phoneLabel = new JLabel("Contact Phone:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPanel.add(phoneLabel, constraints);

        maxField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(maxField.getComponent(), constraints);

        nameLabel = new JLabel("Contact Name:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPanel.add(nameLabel, constraints);

        nameField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(nameField.getComponent(), constraints);

        typeLabel = new JLabel("Site Type:");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(typeLabel, constraints);

        typePanel = new JPanel();
        logisticCenter = new JRadioButton("Logistic Center");
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        branch = new JRadioButton("Branch");
        supplier = new JRadioButton("Supplier");
        typePanel.add(logisticCenter);
        typePanel.add(branch);
        typePanel.add(supplier);
        contentPanel.add(typePanel, constraints);
        ButtonGroup group = new ButtonGroup();
        group.add(logisticCenter);
        group.add(branch);
        group.add(supplier);

        //Submit button
        JButton submitButton = new JButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        contentPanel.add(submitButton, constraints);

        ModelObserver o = this;
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonClicked();
            }
        });
    }

    private void buttonClicked(){
        ObservableSite site =  new ObservableSite();
        site.subscribe(this);
        site.name = idField.getText();
        site.address = addressField.getText();
        site.transportZone = zoneField.getText();
        site.phoneNumber = maxField.getText();
        site.contactName = nameField.getText();
        if(branch.isSelected())
        {
            site.siteType = Site.SiteType.BRANCH;
        }
        else if(logisticCenter.isSelected())
        {
            site.siteType = Site.SiteType.LOGISTICAL_CENTER;
        }
        else
        {
            site.siteType = Site.SiteType.SUPPLIER;
        }
        observers.forEach(observer -> observer.add(this, site));
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);

        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        panel.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {
    }
}
