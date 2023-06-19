package presentationLayer.gui.transportModule.view.panels.sites;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.plUtils.SearchableString;
import presentationLayer.gui.transportModule.control.SitesControl;
import presentationLayer.gui.transportModule.model.ObservableSite;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpdateSitePanel extends AbstractTransportModulePanel {
    public UpdateSitePanel(SitesControl control) {
        super(control);
        init();
    }
    private void init() {


        ObservableList emptySiteList = new ObservableList<>();
        control.getAll(this,emptySiteList);
        ObservableList<Searchable> sites = emptySiteList;
        Collections.sort(sites, new Comparator<Searchable>() {
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

        JLabel sitesLabel = new JLabel("Pick Site:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(sitesLabel, constraints);




        SearchBox searchBox = new SearchBox(sites,"Select Site",textFieldSize, this);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(searchBox.getComponent(), constraints);

        JLabel baseLabel = new JLabel("Contact Name:");
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        contentPanel.add(baseLabel, constraints);

        PrettyTextField baseField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(baseField.getComponent(), constraints);

        JLabel maxLabel = new JLabel("Contact Number:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(maxLabel, constraints);

        PrettyTextField maxField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(maxField.getComponent(), constraints);
    }

    

    @Override
    public void notify(ObservableModel observable) {

    }
}
