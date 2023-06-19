package presentationLayer.gui.transportModule.view.panels.sites;

import domainObjects.transportModule.Truck;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.plUtils.SearchableString;
import presentationLayer.gui.transportModule.control.SitesControl;
import presentationLayer.gui.transportModule.model.ObservableSite;
import presentationLayer.gui.transportModule.model.ObservableTruck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpdateSitePanel extends AbstractTransportModulePanel {
    private JLabel sitesLabel;
    private SearchBox selectedSite;
    private JLabel contactNameLabel;
    private PrettyTextField contactNameField;
    private JLabel contactNumberLabel;
    private PrettyTextField contactNumberField;
    private JButton submitButton;

    public UpdateSitePanel(SitesControl control) {
        super(control);
        init();
    }
    private void init() {

        ObservableList emptySiteList = new ObservableList<>();
        control.getAll(this,emptySiteList);
        ObservableList<Searchable> sitesList = emptySiteList;
        Collections.sort(sitesList, new Comparator<Searchable>() {
            @Override
            public int compare(Searchable o1, Searchable o2) {
                return o1.getShortDescription().compareTo(o2.getShortDescription());
            }
        });

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
        Dimension textFieldSize = new Dimension(200,30);

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        sitesLabel = new JLabel("Pick Site:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(sitesLabel, constraints);

        selectedSite = new SearchBox(sitesList,"Select Site",textFieldSize, this);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(selectedSite.getComponent(), constraints);

        contactNameLabel = new JLabel("Contact Name:");
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        contentPanel.add(contactNameLabel, constraints);

        contactNameField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(contactNameField.getComponent(), constraints);
        contactNameField.setText(((ObservableSite)selectedSite.getSelected()).getShortDescription());

        contactNumberLabel = new JLabel("Contact Number:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(contactNumberLabel, constraints);

        contactNumberField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(contactNumberField.getComponent(), constraints);

        //Submit button
        submitButton = new JButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 5;
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

        ((JComboBox)selectedSite.getComponent()).addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ObservableSite s = (ObservableSite) e.getItem();

            }
        });
    }

    private void buttonClicked(){
        ObservableSite site =  new ObservableSite();
        site.subscribe(this);
        site.contactName = contactNameField.getText();
        site.phoneNumber = contactNumberField.getText();
        observers.forEach(observer -> observer.add(this, site));
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
