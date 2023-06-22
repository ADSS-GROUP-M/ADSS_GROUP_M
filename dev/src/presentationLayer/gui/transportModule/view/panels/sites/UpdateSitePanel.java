package presentationLayer.gui.transportModule.view.panels.sites;

import presentationLayer.gui.plAbstracts.AbstractModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyButton;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.transportModule.control.SitesControl;
import presentationLayer.gui.transportModule.model.ObservableSite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;

public class UpdateSitePanel extends AbstractModulePanel {
    private JLabel sitesLabel;
    private SearchBox selectedSite;
    private JLabel contactNameLabel;
    private PrettyTextField contactNameField;
    private JLabel contactNumberLabel;
    private PrettyTextField contactNumberField;
    private PrettyButton submitButton;
    private ObservableList emptySiteList;
    private ObservableList<Searchable> sitesList;

    public UpdateSitePanel(SitesControl control) {
        super(control);
        init();
    }
    private void init() {

        emptySiteList = new ObservableList<>();
        control.getAll(this, emptySiteList);
        sitesList = emptySiteList;

        Collections.sort(sitesList, new Comparator<Searchable>() {
            @Override
            public int compare(Searchable o1, Searchable o2) {
                return o1.getShortDescription().compareTo(o2.getShortDescription());
            }
        });

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        sitesLabel = new JLabel("Pick Site:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(sitesLabel, constraints);

        Dimension textFieldSize = new Dimension(200,30);

        selectedSite = new SearchBox(sitesList,"Select Site",textFieldSize, panel);
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

        contactNameField = new PrettyTextField(textFieldSize,false);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(contactNameField.getComponent(), constraints);

        contactNumberLabel = new JLabel("Contact Number:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(contactNumberLabel, constraints);

        contactNumberField = new PrettyTextField(textFieldSize,false);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(contactNumberField.getComponent(), constraints);

        //Submit button
        submitButton = new PrettyButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(submitButton, constraints);

        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonClicked();
            }
        });

        selectedSite.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() != ItemEvent.SELECTED){
                    return;
                }

                Object o = e.getItem();

                if(o == null || o instanceof String s && (s.equals("") || s.trim().equals("Select Site") || s.equals("No results found"))){
                    contactNameField.setText("");
                    contactNumberField.setText("");
                    return;
                }

                String siteName = (String) o;

                for(Searchable s : sitesList){
                    if(s.getShortDescription().equals(siteName)){
                        ObservableSite site = (ObservableSite) s;
                        contactNameField.setText(site.contactName);
                        contactNumberField.setText(site.phoneNumber);
                        return;
                    }
                }
                contactNameField.setText("");
                contactNumberField.setText("");
            }
        });
    }

    @Override
    protected void clearFields() {
        selectedSite.setSelected("Select Site");
        contactNameField.setText("");
        contactNumberField.setText("");
    }

    private void buttonClicked(){
        for(Searchable s : sitesList){
            if(s.getShortDescription().equals(selectedSite.getSelected())){
                ObservableSite site = (ObservableSite) s;
                site.subscribe(this);
                site.contactName = contactNameField.getText();
                site.phoneNumber = contactNumberField.getText();
                observers.forEach(observer -> observer.update(this, site));
            }
        }
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);

        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        panel.revalidate();
    }

}


