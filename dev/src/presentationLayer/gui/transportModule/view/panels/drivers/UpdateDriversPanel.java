package presentationLayer.gui.transportModule.view.panels.drivers;

import domainObjects.transportModule.Driver;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyButton;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.plUtils.SearchableString;
import presentationLayer.gui.transportModule.control.DriversControl;
import presentationLayer.gui.transportModule.model.ObservableDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UpdateDriversPanel extends AbstractTransportModulePanel {

    private ObservableList emptyDriverList;
    private ObservableList<Searchable> driversList;
    private JLabel driversLabel;
    private SearchBox selectedDrivers;
    private JLabel licencesLabel;
    private SearchBox selectedLicence;
    private PrettyButton submitButton;

    public UpdateDriversPanel(DriversControl control) {
        super(control);
        init();
    }

    private void init() {

        emptyDriverList = new ObservableList<>();
        control.getAll(this, emptyDriverList);
        driversList = emptyDriverList;
        Collections.sort(driversList, new Comparator<Searchable>() {
            @Override
            public int compare(Searchable o1, Searchable o2) {
                return o1.getShortDescription().compareTo(o2.getShortDescription());
            }
        });

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        Dimension textFieldSize = new Dimension(200,30);

        //title
        driversLabel = new JLabel("Pick Driver:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(driversLabel, constraints);

        //button
        selectedDrivers = new SearchBox(driversList,"Select Driver",textFieldSize, panel);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(selectedDrivers.getComponent(), constraints);

        //title
        licencesLabel = new JLabel("Pick Licence:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(licencesLabel, constraints);

        //button
        java.util.List<Searchable> LicencesSearchableList = new ArrayList<>();
        for (Driver.LicenseType type : Driver.LicenseType.values()) {
            LicencesSearchableList.add(new SearchableString(type.toString()));
        }

        selectedLicence = new SearchBox(LicencesSearchableList,"Select Licence",textFieldSize, panel);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(selectedLicence.getComponent(), constraints);

        //Submit button
        submitButton = new PrettyButton("Submit");
        constraints.gridx = 1;
        constraints.gridy = 3;
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

        selectedDrivers.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }

                Object o = e.getItem();

                if (o == null || o instanceof String s && (s.equals("") || s.trim().equals("Select Driver") || s.equals("No results found"))) {
                    selectedLicence.reset();
                    return;
                }

                String driverId = (String) o;

                for (Searchable d : driversList) {
                    if (d.getShortDescription().equals(driverId)) {
                        ObservableDriver driver = (ObservableDriver) d;
                        selectedLicence.setSelected(driver.licenseType.toString());
                        return;
                    }
                }
                selectedLicence.reset();
            }
        });
    }

    private void buttonClicked(){
        for(Searchable d : driversList){
            if(d.getShortDescription().equals(selectedDrivers.getSelected())){
                ObservableDriver driver = (ObservableDriver) d;
                driver.licenseType = Driver.LicenseType.valueOf(selectedLicence.getSelected());
                driver.subscribe(this);

                observers.forEach(observer -> observer.update(this, driver));
                return;
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

    @Override
    protected void clearFields(){
        selectedDrivers.reset();
        selectedLicence.reset();
    }
}
