package presentationLayer.gui.transportModule.view.panels.transports;

import javafx.util.Pair;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.*;
import presentationLayer.gui.transportModule.control.*;
import presentationLayer.gui.transportModule.model.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import static presentationLayer.gui.plUtils.SearchBox.DescriptionType.*;

public class AddTransportPanel extends AbstractTransportModulePanel {
    private final SitesControl sitesControl;
    private final DriversControl driversControl;
    private final TrucksControl trucksControl;
    private final ItemListsControl itemListsControl;
    private PrettyList destinationsList;

    private List<Pair<String,Integer>> destinations_itemLists;
    private PrettyTextField year;
    private PrettyTextField month;
    private PrettyTextField day;
    private PrettyTextField hour;
    private PrettyTextField minute;
    private PrettyTextField weightField;
    private SearchBox driverSearchBox;
    private SearchBox truckSearchBox;
    private PrettyTextField itemListField;
    private ObservableList<Searchable> sitesList;
    private ObservableList<Searchable> driversList;
    private ObservableList<Searchable> trucksList;
    private ObservableList<Searchable> itemLists;

    public AddTransportPanel(TransportsControl control,
                             SitesControl sitesControl,
                             DriversControl driversControl,
                             TrucksControl trucksControl,
                             ItemListsControl itemListsControl){
        super(control);
        this.sitesControl = sitesControl;
        this.driversControl = driversControl;
        this.trucksControl = trucksControl;
        this.itemListsControl = itemListsControl;
        destinations_itemLists = new LinkedList<>();
        init();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void init() {
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        //Add the sites list to the screen
        ObservableList emptySiteList = new ObservableList<>();
        sitesControl.getAll(this,emptySiteList);
        sitesList = emptySiteList;

        ObservableList emptyDriversList = new ObservableList<>();
        driversControl.getAll(this,emptyDriversList);
        driversList = emptyDriversList;

        ObservableList emptyTruckList = new ObservableList<>();
        trucksControl.getAll(this,emptyTruckList);
        trucksList = emptyTruckList;

        ObservableList emptyItemList = new ObservableList<>();
        itemListsControl.getAll(this,emptyItemList);
        itemLists = emptyItemList;



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
        year = new PrettyTextField(new Dimension(75,30), "YYYY");
        year.setHorizontalAlignment(JTextField.CENTER);
        year.setMaximumCharacters(4);

        JLabel slash1 = new JLabel("/");
        slash1.setPreferredSize(new Dimension(10,30));
        slash1.setHorizontalAlignment(JLabel.CENTER);
        slash1.setFont(Fonts.textBoxFont.deriveFont(20f));

        month = new PrettyTextField(new Dimension(35,30), "MM");
        month.setHorizontalAlignment(JTextField.CENTER);
        month.setMaximumCharacters(2);

        JLabel slash2 = new JLabel("/");
        slash2.setHorizontalAlignment(JLabel.CENTER);
        slash2.setPreferredSize(new Dimension(10,30));
        slash2.setFont(Fonts.textBoxFont.deriveFont(20f));

        day = new PrettyTextField(new Dimension(35,30), "DD");
        day.setHorizontalAlignment(JTextField.CENTER);
        day.setMaximumCharacters(2);

        JLabel dateTimeSeparator = new JLabel("");
        dateTimeSeparator.setHorizontalAlignment(JLabel.CENTER);
        dateTimeSeparator.setPreferredSize(new Dimension(15,30));
        dateTimeSeparator.setFont(Fonts.textBoxFont.deriveFont(20f));

        hour = new PrettyTextField(new Dimension(35,30), "HH");
        hour.setHorizontalAlignment(JTextField.CENTER);
        hour.setMaximumCharacters(2);

        JLabel colon = new JLabel(":");
        colon.setHorizontalAlignment(JLabel.CENTER);
        colon.setPreferredSize(new Dimension(10,30));
        colon.setFont(Fonts.textBoxFont.deriveFont(20f));

        minute = new PrettyTextField(new Dimension(35,30), "MM");
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

        weightField = new PrettyTextField(textFieldSize);
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

        driverSearchBox = new SearchBox(driversList,"Select Driver",textFieldSize,LONG, panel);
        constraints.gridx = 3;
        constraints.gridy = 0;
        simpleComponentsPanel.add(driverSearchBox.getComponent(), constraints);

        JLabel trucksLabel = new JLabel("Truck:");
        constraints.gridx = 2;
        constraints.gridy = 1;
        simpleComponentsPanel.add(trucksLabel, constraints);

        truckSearchBox = new SearchBox(trucksList,"Select Truck",textFieldSize,LONG, panel);
        constraints.gridx = 3;
        constraints.gridy = 1;
        simpleComponentsPanel.add(truckSearchBox.getComponent(), constraints);

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
        SearchBox destinations = new SearchBox(sitesList,"Select Destination",boxFieldSize, LONG, panel);
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

        itemListField = new PrettyTextField(new Dimension(100,30));
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(10,10,0,50);
        complexComponentsPanel.add(itemListField.getComponent(), constraints);

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

        this.destinationsList = new PrettyList(new ObservableList<>(),panel);
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(this.destinationsList.getComponent(),constraints);


        //Submit button
        JButton submitButton = new JButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 6;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(submitButton, constraints);



        addItemButton.addMouseListener(new MouseAdapter() {

            DefaultListModel<Searchable> model = new DefaultListModel<>();
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedDestination = destinations.getSelected();
                String selectedItemList = itemListField.getText();

                if((selectedDestination == null || selectedDestination.trim().equals("") || selectedDestination.trim().equals("Select Destination") || selectedDestination.trim().equals("No results found"))
                        || (selectedItemList == null || selectedItemList.trim().equals(""))){
                    return;
                }

                int itemListId;
                try{
                    itemListId = Integer.parseInt(selectedItemList);
                } catch(NumberFormatException ex){
                    throw new IllegalArgumentException("item list id must be a number");
                }

                for(Searchable s1 : itemLists) {
                    ObservableItemList itemList = (ObservableItemList) s1;
                    if (itemList.id == itemListId){
                        for(Searchable s2 : sitesList){
                            if(s2.getLongDescription().equals(selectedDestination)){
                                ObservableSite site = (ObservableSite) s2;
                                if (destinations_itemLists.stream().anyMatch(p -> p.getKey().equals(site.name))){
                                    throw new IllegalArgumentException("destination already added");
                                }

                                model.addElement(new SearchableString("List id: %d | %s".formatted(itemListId,s2.getLongDescription())));
                                destinationsList.getList().setModel(model);
                                destinations_itemLists.add(new Pair<>(site.name, itemListId));
                                destinations.reset();
                                itemListField.setText("");
                                return;
                            }
                        }
                    }
                }
                throw new IllegalArgumentException("item list not found");
            }
        });


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

        transport.id = -1;
        try{
            transport.weight = Integer.parseInt(weightField.getText());
        } catch(NumberFormatException e){
            throw new IllegalArgumentException("weight must be a number");
        }
        transport.driverId = ((ObservableDriver) driversList.stream()
                    .filter(s -> s.getLongDescription().equals(driverSearchBox.getSelected()))
                    .findFirst().get()).id;
        transport.truckId = ((ObservableTruck) trucksList.stream()
                .filter(s -> s.getLongDescription().equals(truckSearchBox.getSelected()))
                .findFirst().get()).id;
        transport.route = destinations_itemLists.stream()
                        .map(Pair::getKey)
                        .toList();
        transport.destinations_itemListIds = new HashMap<>(){{
            for(Pair<String, Integer> pair : destinations_itemLists){
                put(pair.getKey(), pair.getValue());
            }
        }};

        try{
            int yearInt = Integer.parseInt(year.getText());
            int monthInt = Integer.parseInt(month.getText());
            int dayInt = Integer.parseInt(day.getText());
            int hourInt = Integer.parseInt(hour.getText());
            int minuteInt = Integer.parseInt(minute.getText());
            transport.departureTime =  LocalDateTime.of(yearInt, monthInt, dayInt, hourInt, minuteInt);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid date format");
        }

        System.out.println();
        observers.forEach(observer -> observer.add(this, transport));
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);
        destinationsList.componentResized(new Dimension((int) (contentPanelSize.width*0.8), (int) (-150 + 2 *contentPanelSize.height/3.0)));

        panel.revalidate();
    }

    

    @Override
    public void notify(ObservableModel observable) {

    }
}
