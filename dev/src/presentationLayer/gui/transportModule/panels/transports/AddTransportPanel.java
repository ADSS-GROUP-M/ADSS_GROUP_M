package presentationLayer.gui.transportModule.panels.transports;

import presentationLayer.gui.plAbstracts.Searchable;
import presentationLayer.gui.plAbstracts.TransportBasePanel;
import presentationLayer.gui.plUtils.SearchBox;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class AddTransportPanel extends TransportBasePanel {
    public AddTransportPanel() {
        super("src/resources/truck_main_page.jpg");
        init();
    }

    private void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
            JLabel header = new JLabel("Enter transport details:\n");

        header.setVerticalAlignment(JLabel.TOP);
        contentPanel.add(header);


        //Panel panel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel dateLabel = new JLabel("Departure Date:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(dateLabel, constraints);

        JTextField dateField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(dateField, constraints);

        JLabel timeLabel = new JLabel("Departure Time:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(timeLabel, constraints);

        JTextField timeField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(timeField, constraints);

        JLabel driversLabel = new JLabel("Pick Driver:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(driversLabel, constraints);


        class SearchableString implements Searchable {

            private String string;

            public SearchableString(String string) {
                this.string = string;
            }

            @Override
            public boolean isMatch(String query) {
                return string.contains(query);
            }

            @Override
            public String getShortDescription() {
                return string;
            }

            @Override
            public String getLongDescription() {
                return null;
            }
        }

        List<Searchable> searchableStrings = List.of(new SearchableString("driver1"), new SearchableString("driver2"), new SearchableString("driver3"));
        SearchBox drivers = new SearchBox(searchableStrings,"Select Driver",new Dimension(200,30), repaintListener);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(drivers.getComponent(), constraints);


        JLabel trucksLabel = new JLabel("Pick Truck:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(trucksLabel, constraints);
        JButton addButton2 = new JButton("Trucks");
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(addButton2, constraints);

        JLabel weightLabel = new JLabel("Truck Weight:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPanel.add(weightLabel, constraints);

        JTextField weightField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(weightField, constraints);
    }

}
