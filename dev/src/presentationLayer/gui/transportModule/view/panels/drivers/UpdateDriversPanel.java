package presentationLayer.gui.transportModule.view.panels.drivers;

import domainObjects.transportModule.Driver;
import presentationLayer.gui.plAbstracts.Searchable;
import presentationLayer.gui.plAbstracts.TransportBasePanel;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.plUtils.SearchableString;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateDriversPanel extends TransportBasePanel {

    public UpdateDriversPanel() {
        super();
        init();
    }

    private void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        Dimension textFieldSize = new Dimension(200,30);

        //title
        JLabel driversLabel = new JLabel("Pick Driver:    ");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(driversLabel, constraints);

        //button
        java.util.List<Searchable> driversSearchableList = List.of(new SearchableString("item1"), new SearchableString("item2"), new SearchableString("item3"));

        SearchBox drivers = new SearchBox(driversSearchableList,"Select Driver",textFieldSize, this);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(drivers.getComponent(), constraints);

        //title
        JLabel licencesLabel = new JLabel("Pick Licence:    ");
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

        SearchBox Licences = new SearchBox(LicencesSearchableList,"Select Licence",textFieldSize, this);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(Licences.getComponent(), constraints);
    }
}
