package presentationLayer.gui.transportModule.view.panels.transports;

import domainObjects.transportModule.Truck;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.*;
import presentationLayer.gui.transportModule.control.*;
import presentationLayer.gui.transportModule.model.ObservableTransport;
import presentationLayer.gui.transportModule.model.ObservableTruck;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static presentationLayer.gui.plUtils.SearchBox.*;
import static presentationLayer.gui.plUtils.SearchBox.DescriptionType.*;

public class AddTransportPanel extends AbstractTransportModulePanel {
    private final SitesControl sitesControl;
    private final DriversControl driversControl;
    private final TrucksControl trucksControl;
    private PrettyList sitesList;
    private PrettyList driversList;
    private PrettyList trucksList;
    public AddTransportPanel(TransportsControl control,
                             SitesControl sitesControl,
                             DriversControl driversControl,
                             TrucksControl trucksControl){
        super(control);
        this.sitesControl = sitesControl;
        this.driversControl = driversControl;
        this.trucksControl = trucksControl;
        init();
    }

    private void init() {
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        //Add the sites list to the screen
        ObservableList emptySiteList = new ObservableList<>();
        sitesControl.getAll(this,emptySiteList);
        ObservableList<Searchable> sites = emptySiteList;


        // ====================== SIMPLE COMPONENTS =============================

        JPanel simpleComponentsPanel = new JPanel();
        simpleComponentsPanel.setLayout(new GridBagLayout());
        simpleComponentsPanel.setBackground(new Color(0,0,0,0));

        Dimension textFieldSize = new Dimension(200,30);

        JLabel dateLabel = new JLabel("Departure Date And Time:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0,0,0,10);
        constraints.anchor = GridBagConstraints.WEST;
        simpleComponentsPanel.add(dateLabel, constraints);

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

        JLabel dateTimeSeparator = new JLabel("");
        dateTimeSeparator.setHorizontalAlignment(JLabel.CENTER);
        dateTimeSeparator.setPreferredSize(new Dimension(15,30));
        dateTimeSeparator.setFont(Fonts.textBoxFont.deriveFont(20f));

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

        JPanel dateTimePanel = new JPanel();
        dateTimePanel.setBackground(new Color(0,0,0,0));
        dateTimePanel.add(year.getComponent());
        dateTimePanel.add(slash1);
        dateTimePanel.add(month.getComponent());
        dateTimePanel.add(slash2);
        dateTimePanel.add(day.getComponent());
        dateTimePanel.add(dateTimeSeparator);
        dateTimePanel.add(hour.getComponent());
        dateTimePanel.add(colon);
        dateTimePanel.add(minute.getComponent());

        constraints.gridx = 1;
        constraints.insets = new Insets(0,0,0,60);
        simpleComponentsPanel.add(dateTimePanel, constraints);

        JLabel weightLabel = new JLabel("Truck Weight:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        simpleComponentsPanel.add(weightLabel, constraints);

        PrettyTextField weightField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(0,5,0,0);
        simpleComponentsPanel.add(weightField.getComponent(), constraints);

        JLabel driversLabel = new JLabel("Driver:");
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0,0,0,10);
        simpleComponentsPanel.add(driversLabel, constraints);

        //TODO: remove this example code and replace with real data

        List<Searchable> searchableList = List.of(new SearchableString("item1"), new SearchableString("item2"), new SearchableString("item3"));
        SearchBox drivers = new SearchBox(searchableList,"Select Driver",textFieldSize, panel);
        constraints.gridx = 3;
        constraints.gridy = 0;
        simpleComponentsPanel.add(drivers.getComponent(), constraints);

        JLabel trucksLabel = new JLabel("Truck:");
        constraints.gridx = 2;
        constraints.gridy = 1;
        simpleComponentsPanel.add(trucksLabel, constraints);

        SearchBox trucks = new SearchBox(searchableList,"Select Truck",textFieldSize, panel);
        constraints.gridx = 3;
        constraints.gridy = 1;
        simpleComponentsPanel.add(trucks.getComponent(), constraints);

        // ================================= END OF SIMPLE COMPONENTS =================================


        constraints = new GridBagConstraints();

        JPanel complexComponentsPanel = new JPanel();
        complexComponentsPanel.setLayout(new GridBagLayout());
        complexComponentsPanel.setBackground(new Color(0,0,0,0));

        JLabel destinationsLabel = new JLabel("Destination:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0,0,0,0);
        constraints.anchor = GridBagConstraints.WEST;

        complexComponentsPanel.add(destinationsLabel, constraints);

        Dimension boxFieldSize = new Dimension(700,30);
        SearchBox destinations = new SearchBox(sites,"Select Destination",boxFieldSize, LONG, panel);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 6;
        constraints.insets = new Insets(0,10,0,0);
        complexComponentsPanel.add(destinations.getComponent(), constraints);

        constraints.gridwidth = 1;
        JLabel itemListLabel = new JLabel("Item List:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(10,0,0,0);
        constraints.anchor = GridBagConstraints.WEST;
        complexComponentsPanel.add(itemListLabel, constraints);

        PrettyTextField itemList = new PrettyTextField(new Dimension(100,30));
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(10,10,0,50);
        complexComponentsPanel.add(itemList.getComponent(), constraints);

        constraints.insets = new Insets(10,10,0,0);
        JButton addItemButton = new JButton("Add");
        constraints.gridx = 2;
        constraints.gridy = 1;
        complexComponentsPanel.add(addItemButton, constraints);

        JButton removeItemButton = new JButton("Remove");
        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.insets = new Insets(10,10,0,100);
        complexComponentsPanel.add(removeItemButton, constraints);

        constraints.insets = new Insets(10,10,0,0);
        JButton moveUpButton = new JButton("Move Up");
        constraints.gridx = 4;
        constraints.gridy = 1;
        complexComponentsPanel.add(moveUpButton, constraints);

        JButton moveDownButton = new JButton("Move Down");
        constraints.gridx = 5;
        constraints.gridy = 1;
        complexComponentsPanel.add(moveDownButton, constraints);

        // ================================= END OF COMPLEX COMPONENTS =================================
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(25, 0, 25, 0);
        contentPanel.add(simpleComponentsPanel,constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 50, 0);
        contentPanel.add(complexComponentsPanel,constraints);

        sitesList = new PrettyList(sites,panel);
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(sitesList.getComponent(),constraints);


        //Submit button
        JButton submitButton = new JButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
//        simpleComponentsPanel.add(submitButton, constraints);


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
        ObservableTransport transport =  new ObservableTransport();
        transport.subscribe(this);
//        transport.id = ;
        observers.forEach(observer -> observer.add(this, transport));
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);
        sitesList.componentResized(new Dimension((int) (contentPanelSize.width*0.8), (int) (-75 + 2 *contentPanelSize.height/3.0)));

        panel.revalidate();
    }

    

    @Override
    public void notify(ObservableModel observable) {

    }
}
