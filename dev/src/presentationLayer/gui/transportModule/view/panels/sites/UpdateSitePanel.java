package presentationLayer.gui.transportModule.view.panels.sites;

import presentationLayer.gui.plAbstracts.interfaces.ObservableObject;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.plUtils.SearchableString;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UpdateSitePanel extends AbstractTransportModulePanel {
    public UpdateSitePanel() {
        super();
        init();
    }
    private void init() {
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

        List<Searchable> searchableList = List.of(new SearchableString("item1"), new SearchableString("item2"), new SearchableString("item3"));
        SearchBox sites = new SearchBox(searchableList,"Select Site",textFieldSize, this);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(sites.getComponent(), constraints);

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
    public Object getUpdate(UIElementEvent event) {
        return null;
    }

    @Override
    public void notify(ObservableObject observable) {

    }
}
