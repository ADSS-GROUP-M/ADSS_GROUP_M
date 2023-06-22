package presentationLayer.gui.transportModule.view.panels.transports;

import javafx.util.Pair;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.*;
import presentationLayer.gui.transportModule.control.*;
import presentationLayer.gui.transportModule.model.*;
import presentationLayer.gui.plAbstracts.interfaces.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static presentationLayer.gui.plUtils.SearchBox.DescriptionType.LONG;

public class UpdateTransportPanel extends AbstractTransportModulePanel {
    private final ObservableTransport toEdit;
    private final SitesControl sitesControl;

    private final DriversControl driversControl;
    private final TrucksControl trucksControl;
    private final ItemListsControl itemListsControl;
    private final Function<Panel, Void> setActivePanel;
    private PrettyList destinationsList;
    private List<Pair<String,Integer>> destinations_itemLists;
    private PrettyTextField year;
    private PrettyTextField month;
    private PrettyTextField day;
    private PrettyTextField hour;
    private PrettyTextField minute;
    private PrettyTextField weightField;
    private SearchBox driverSearchBox;
    private PrettyTextField itemListField;
    private ObservableList<Searchable> sitesList;
    private ObservableList<Searchable> driversList;
    private ObservableList<Searchable> trucksList;
    private ObservableList<Searchable> itemLists;
    private DefaultListModel<Searchable> model;
    private JPanel complexComponentsPanel;
    private SearchBox destinations;
    private JPanel simpleComponentsPanel;
    private SearchBox trucksSearchBox;

    public UpdateTransportPanel(ObservableTransport toEdit,
                                TransportsControl control,
                                SitesControl sitesControl,
                                DriversControl driversControl,
                                TrucksControl trucksControl,
                                ItemListsControl itemListsControl,
                                Function<Panel, Void> setActivePanel){
        super(control);
        this.toEdit = toEdit;
        this.sitesControl = sitesControl;
        this.driversControl = driversControl;
        this.trucksControl = trucksControl;
        this.itemListsControl = itemListsControl;
        this.setActivePanel = setActivePanel;
        destinations_itemLists = new LinkedList<>();
        model = new DefaultListModel<>();
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

        simpleComponentsPanel = new JPanel();
        simpleComponentsPanel.setLayout(new GridBagLayout());
        simpleComponentsPanel.setBackground(new Color(0,0,0,0));

        Dimension textFieldSize = new Dimension(200,30);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;

        JLabel dateLabel = new JLabel("Departure Date And Time:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0,0,0,10);
//        constraints.anchor = GridBagConstraints.WEST;
        simpleComponentsPanel.add(dateLabel, constraints);

        // date text fields
        year = new PrettyTextField(new Dimension(75,30), "YYYY",false);
        year.setHorizontalAlignment(JTextField.CENTER);
        year.setMaximumCharacters(4);

        JLabel slash1 = new JLabel("/");
        slash1.setPreferredSize(new Dimension(10,30));
        slash1.setHorizontalAlignment(JLabel.CENTER);
        slash1.setFont(Fonts.textBoxFont.deriveFont(20f));

        month = new PrettyTextField(new Dimension(35,30), "MM",false);
        month.setHorizontalAlignment(JTextField.CENTER);
        month.setMaximumCharacters(2);

        JLabel slash2 = new JLabel("/");
        slash2.setHorizontalAlignment(JLabel.CENTER);
        slash2.setPreferredSize(new Dimension(10,30));
        slash2.setFont(Fonts.textBoxFont.deriveFont(20f));

        day = new PrettyTextField(new Dimension(35,30), "DD",false);
        day.setHorizontalAlignment(JTextField.CENTER);
        day.setMaximumCharacters(2);

        JLabel dateTimeSeparator = new JLabel("");
        dateTimeSeparator.setHorizontalAlignment(JLabel.CENTER);
        dateTimeSeparator.setPreferredSize(new Dimension(15,30));
        dateTimeSeparator.setFont(Fonts.textBoxFont.deriveFont(20f));

        hour = new PrettyTextField(new Dimension(35,30), "HH",false);
        hour.setHorizontalAlignment(JTextField.CENTER);
        hour.setMaximumCharacters(2);

        JLabel colon = new JLabel(":");
        colon.setHorizontalAlignment(JLabel.CENTER);
        colon.setPreferredSize(new Dimension(10,30));
        colon.setFont(Fonts.textBoxFont.deriveFont(20f));

        minute = new PrettyTextField(new Dimension(35,30), "MM",false);
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
        constraints.insets = new Insets(0,0,10,60);
        simpleComponentsPanel.add(dateTimePanel, constraints);

        JLabel weightLabel = new JLabel("Truck Weight:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        simpleComponentsPanel.add(weightLabel, constraints);

        weightField = new PrettyTextField(textFieldSize,false);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(0,5,10,0);
        simpleComponentsPanel.add(weightField.getComponent(), constraints);

        JLabel driversLabel = new JLabel("Driver:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(0,0,10,10);
        simpleComponentsPanel.add(driversLabel, constraints);

        driverSearchBox = new SearchBox(driversList,"Select Driver",textFieldSize,LONG,false, panel);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.insets = new Insets(0,5,10,0);
        simpleComponentsPanel.add(driverSearchBox.getComponent(), constraints);

        JLabel trucksLabel = new JLabel("Truck:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.insets = new Insets(0,0,0,5);
        simpleComponentsPanel.add(trucksLabel, constraints);

        trucksSearchBox = new SearchBox(trucksList, "Select Truck", textFieldSize, LONG,false, panel);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.insets = new Insets(0,5,0,0);
        simpleComponentsPanel.add(trucksSearchBox.getComponent(), constraints);

        // ================================= END OF SIMPLE COMPONENTS =================================


        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        complexComponentsPanel = new JPanel();
        complexComponentsPanel.setLayout(new GridBagLayout());
        complexComponentsPanel.setBackground(new Color(0,0,0,0));

        JLabel destinationsLabel = new JLabel("Destination:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0,0,0,0);

        complexComponentsPanel.add(destinationsLabel, constraints);

        Dimension boxFieldSize = new Dimension(300,30);
        destinations = new SearchBox(sitesList,"Select Destination",boxFieldSize, LONG, panel);
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
        complexComponentsPanel.add(itemListLabel, constraints);

        itemListField = new PrettyTextField(new Dimension(100,30));
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(10,10,0,50);
        complexComponentsPanel.add(itemListField.getComponent(), constraints);

        constraints.insets = new Insets(10,10,0,0);
        PrettyButton addItemButton = new PrettyButton("Add");
        constraints.gridx = 2;
        constraints.gridy = 1;
        complexComponentsPanel.add(addItemButton, constraints);

        PrettyButton removeItemButton = new PrettyButton("Remove");
        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.insets = new Insets(10,10,0,100);
        complexComponentsPanel.add(removeItemButton, constraints);

        constraints.insets = new Insets(10,10,0,0);
        PrettyButton moveUpButton = new PrettyButton("Move Up");
        constraints.gridx = 4;
        constraints.gridy = 1;
        complexComponentsPanel.add(moveUpButton, constraints);

        PrettyButton moveDownButton = new PrettyButton("Move Down");
        constraints.gridx = 5;
        constraints.gridy = 1;
        complexComponentsPanel.add(moveDownButton, constraints);

        // ================================= END OF COMPLEX COMPONENTS =================================
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weighty = 1;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(25, 0, 25, 0);
        contentPanel.add(simpleComponentsPanel,constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 15, 0);
        constraints.weighty = 2;
        contentPanel.add(complexComponentsPanel,constraints);

        destinationsList = new PrettyList(new ObservableList<>(),panel);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weighty = 6;
        contentPanel.add(destinationsList.getComponent(),constraints);


        //Submit button
        PrettyButton submitButton = new PrettyButton("Update Transport");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(submitButton, constraints);



        addItemButton.addMouseListener(new MouseAdapter() {
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
                    JOptionPane.showMessageDialog(null, "Item list id must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for(Searchable s1 : itemLists) {
                    ObservableItemList itemList = (ObservableItemList) s1;
                    if (itemList.id == itemListId){
                        for(Searchable s2 : sitesList){
                            if(s2.getLongDescription().equals(selectedDestination)){
                                ObservableSite site = (ObservableSite) s2;
                                if (destinations_itemLists.stream().anyMatch(p -> p.getKey().equals(site.name))){
                                    JOptionPane.showMessageDialog(null, "Destination already added", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
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
                JOptionPane.showMessageDialog(null, "Item list not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonClicked();
            }
        });

        removeItemButton.addActionListener(e -> {
            int index = destinationsList.getList().getSelectedIndex();
            if(index == -1){
                return;
            }
            destinations_itemLists.remove(index);
            model.remove(index);
            destinationsList.getList().setModel(model);
        });

        moveUpButton.addActionListener(e -> {
            int index = destinationsList.getList().getSelectedIndex();
            if(index == -1 || index == 0){
                return;
            }
            Pair<String, Integer> pair = destinations_itemLists.get(index);
            destinations_itemLists.remove(index);
            destinations_itemLists.add(index-1, pair);
            Searchable removed = model.remove(index);
            model.add(index-1, removed);
            destinationsList.getList().setModel(model);
            destinationsList.getList().setSelectedIndex(index-1);
        });

        moveDownButton.addActionListener(e -> {
            int index = destinationsList.getList().getSelectedIndex();
            if(index == -1 || index == destinations_itemLists.size()-1){
                return;
            }
            Pair<String, Integer> pair = destinations_itemLists.get(index);
            destinations_itemLists.remove(index);
            destinations_itemLists.add(index+1, pair);
            Searchable removed = model.remove(index);
            model.add(index+1, removed);
            destinationsList.getList().setModel(model);
            destinationsList.getList().setSelectedIndex(index+1);
        });

        loadDataToFields();

    }

    private void loadDataToFields() {

        year.setText(String.valueOf(toEdit.departureTime.getYear()));
        month.setText(String.valueOf(toEdit.departureTime.getMonthValue()));
        day.setText(String.valueOf(toEdit.departureTime.getDayOfMonth()));
        hour.setText(String.valueOf(toEdit.departureTime.getHour()));
        minute.setText(String.valueOf(toEdit.departureTime.getMinute()));
        weightField.setText(String.valueOf(toEdit.weight));
        driverSearchBox.setSelected(driversList.stream().filter(s -> s.isMatchExactly(toEdit.driverId)).findFirst().get().getLongDescription());
        trucksSearchBox.setSelected(trucksList.stream().filter(s -> s.isMatchExactly(toEdit.truckId)).findFirst().get().getLongDescription());
        destinations_itemLists = toEdit.route.stream().map(s -> new Pair<>(s,toEdit.destinations_itemListIds.get(s))).collect(Collectors.toList());
        destinationsList.getList().setModel(new DefaultListModel<>());
        destinations_itemLists.forEach(p -> model.addElement(new SearchableString("List id: %d | %s".formatted(p.getValue(), sitesList.stream().filter(s->s.isMatchExactly(p.getKey())).findFirst().get().getLongDescription()))));
        destinationsList.getList().setModel(model);
    }

    private void buttonClicked(){
        ObservableTransport transport =  new ObservableTransport();
        transport.subscribe(this);

        transport.id = toEdit.id;
        try{
            transport.weight = Integer.parseInt(weightField.getText());
        } catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this.getComponent(), "Weight must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        transport.driverId = ((ObservableDriver) driversList.stream()
                .filter(s -> s.getLongDescription().equals(driverSearchBox.getSelected()))
                .findFirst().get()).id;
        transport.truckId = ((ObservableTruck) trucksList.stream()
                .filter(s -> s.getLongDescription().equals(trucksSearchBox.getSelected()))
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
        } catch (NumberFormatException | DateTimeException e){
            JOptionPane.showMessageDialog(this.getComponent(), "Invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        observers.forEach(observer -> observer.update(this, transport));
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);

        if(newSize.width < 900){
            newSize.width = 900;
        }

        if(newSize.height < 600){
            newSize.height = 600;
        }

        Dimension contentPanelSize = new Dimension((int) (newSize.width * 0.8), (int) (newSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);
        destinationsList.componentResized(new Dimension((int) (contentPanelSize.width*0.8), (int) (contentPanelSize.height*0.3)));

        panel.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {
        super.notify(observable);
        if(observable.errorOccurred() == false){
            setActivePanel.apply(new ViewTransportsPanel(setActivePanel,
                    (TransportsControl) control,sitesControl, driversControl, trucksControl, itemListsControl));
        }
    }

    @Override
    protected void clearFields() {
        year.setText("");
        month.setText("");
        day.setText("");
        hour.setText("");
        minute.setText("");
        weightField.setText("");
        destinationsList.getList().setModel(new DefaultListModel<>());
        destinations_itemLists.clear();
        model.clear();
        driverSearchBox.reset();
        trucksSearchBox.reset();
    }
}
