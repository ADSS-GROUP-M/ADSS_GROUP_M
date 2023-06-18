package presentationLayer.gui.transportModule.view.panels.transports;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableObject;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.Fonts;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.plUtils.SearchableString;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddTransportPanel extends AbstractTransportModulePanel {
    public AddTransportPanel() {
        super();
        init();
    }

    private void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
//        JLabel header = new JLabel("Enter transport details:\n");
//        header.setVerticalAlignment(JLabel.TOP);
//        contentPanel.add(header);
        Dimension textFieldSize = new Dimension(200,30);

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel dateLabel = new JLabel("Departure Date:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(dateLabel, constraints);

        // date text fields
        PrettyTextField year = new PrettyTextField(new Dimension(75,30), "YYYY");
        year.setHorizontalAlignment(JTextField.CENTER);
        year.setMaximumCharacters(4);

        JLabel slash1 = new JLabel("/");
        slash1.setPreferredSize(new Dimension(10,30));
        slash1.setHorizontalAlignment(JLabel.CENTER);
        slash1.setFont(Fonts.textBoxFont.deriveFont(20f));

        PrettyTextField month = new PrettyTextField(new Dimension(35,30), "MM");
        month.setHorizontalAlignment(JTextField.CENTER);
        month.setMaximumCharacters(2);

        JLabel slash2 = new JLabel("/");
        slash2.setHorizontalAlignment(JLabel.CENTER);
        slash2.setPreferredSize(new Dimension(10,30));
        slash2.setFont(Fonts.textBoxFont.deriveFont(20f));

        PrettyTextField day = new PrettyTextField(new Dimension(35,30), "DD");
        day.setHorizontalAlignment(JTextField.CENTER);
        day.setMaximumCharacters(2);

        JPanel datePanel = new JPanel();
        datePanel.add(year.getComponent());
        datePanel.add(slash1);
        datePanel.add(month.getComponent());
        datePanel.add(slash2);
        datePanel.add(day.getComponent());
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(datePanel, constraints);

        // time text fields
        JLabel timeLabel = new JLabel("Departure Time:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(timeLabel, constraints);

        PrettyTextField hour = new PrettyTextField(new Dimension(35,30), "HH");
        hour.setHorizontalAlignment(JTextField.CENTER);
        hour.setMaximumCharacters(2);

        JLabel colon = new JLabel(":");
        colon.setHorizontalAlignment(JLabel.CENTER);
        colon.setPreferredSize(new Dimension(10,30));
        colon.setFont(Fonts.textBoxFont.deriveFont(20f));

        PrettyTextField minute = new PrettyTextField(new Dimension(35,30), "MM");
        minute.setHorizontalAlignment(JTextField.CENTER);
        minute.setMaximumCharacters(2);

        JPanel timePanel = new JPanel();
        timePanel.add(hour.getComponent());
        timePanel.add(colon);
        timePanel.add(minute.getComponent());
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(timePanel, constraints);

        JLabel driversLabel = new JLabel("Pick Driver:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(driversLabel, constraints);



        //TODO: remove this example code and replace with real data


        List<Searchable> searchableList = List.of(new SearchableString("item1"), new SearchableString("item2"), new SearchableString("item3"));
        SearchBox drivers = new SearchBox(searchableList,"Select Driver",textFieldSize, this);
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

        SearchBox trucks = new SearchBox(searchableList,"Select Truck",textFieldSize, this);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(trucks.getComponent(), constraints);

        JLabel weightLabel = new JLabel("Truck Weight:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPanel.add(weightLabel, constraints);

        PrettyTextField weightField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(weightField.getComponent(), constraints);
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension preferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight()*0.9));
        contentPanel.setPreferredSize(preferredSize);
//        panel.setPreferredSize(new Dimension(preferredSize.width, preferredSize.height*2));
        scrollPane.revalidate();
    }

    

    @Override
    public void notify(ObservableObject observable) {

    }
}
