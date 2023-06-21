package presentationLayer.gui.transportModule.view.panels.trucks;

import domainObjects.transportModule.Truck;
import domainObjects.transportModule.Truck.CoolingCapacity;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.plUtils.SearchableString;
import presentationLayer.gui.transportModule.control.TrucksControl;
import presentationLayer.gui.transportModule.model.ObservableSite;
import presentationLayer.gui.transportModule.model.ObservableTruck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpdateTruckPanel extends AbstractTransportModulePanel {
    private JLabel trucksLabel;
    private SearchBox selectedTruck;
    private JLabel baseLabel;
    private PrettyTextField baseField;
    private JLabel maxLabel;
    private PrettyTextField maxField;
    private JLabel coolingLabel;
    private JPanel coolingPanel;
    private JRadioButton none;
    private JRadioButton cold;
    private JRadioButton frozen;
    private ObservableList emptyTruckList;
    private ObservableList<Searchable> trucksList;
    private ButtonGroup group;

    public UpdateTruckPanel(TrucksControl control) {
        super(control);
        init();
    }
    private void init() {

        emptyTruckList = new ObservableList<>();
        control.getAll(this, emptyTruckList);
        trucksList = emptyTruckList;
        Collections.sort(trucksList, new Comparator<Searchable>() {
            @Override
            public int compare(Searchable o1, Searchable o2) {
                return o1.getShortDescription().compareTo(o2.getShortDescription());
            }
        });

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //title
        trucksLabel = new JLabel("Pick Truck:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(trucksLabel, constraints);

        Dimension textFieldSize = new Dimension(220,30);

        //button
        selectedTruck = new SearchBox(trucksList,"Select Truck",textFieldSize, panel);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(selectedTruck.getComponent(), constraints);

        //title
        baseLabel = new JLabel("Base Weight:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(baseLabel, constraints);

        //text box
        baseField = new PrettyTextField(textFieldSize, false);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(baseField.getComponent(), constraints);

        //title
        maxLabel = new JLabel("Max Weight:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(maxLabel, constraints);

        //text box
        maxField = new PrettyTextField(textFieldSize, false);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(maxField.getComponent(), constraints);

        //title
        coolingLabel = new JLabel("Cooling Capacity:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(coolingLabel, constraints);

        coolingPanel = new JPanel();
        none = new JRadioButton("NONE");
        constraints.gridx = 1;
        constraints.gridy = 4;
        //constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        cold = new JRadioButton("COLD");
        frozen = new JRadioButton("FROZEN");
        coolingPanel.add(none);
        coolingPanel.add(cold);
        coolingPanel.add(frozen);
        contentPanel.add(coolingPanel, constraints);

        group = new ButtonGroup();
        group.add(none);
        group.add(cold);
        group.add(frozen);

        //Submit button
        JButton submitButton = new JButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        contentPanel.add(submitButton, constraints);

        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonClicked();
            }
        });

        selectedTruck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() != ItemEvent.SELECTED){
                    return;
                }

                Object o = e.getItem();

                if(o == null || o instanceof String s && (s.equals("") || s.trim().equals("Select Truck") || s.equals("No results found"))){
                    baseField.setText("");
                    maxField.setText("");
                    return;
                }

                String truckId = (String) o;

                for(Searchable t : trucksList){
                    if(t.getShortDescription().equals(truckId)){
                        ObservableTruck truck = (ObservableTruck) t;
                        baseField.setText(String.valueOf(truck.baseWeight));
                        maxField.setText(String.valueOf(truck.maxWeight));
                        setCoolingButton(truck.coolingCapacity);
                        return;
                    }
                }
                baseField.setText("");
                maxField.setText("");
            }
        });
    }

    private void setCoolingButton(CoolingCapacity cc){
        none.setSelected(false);
        cold.setSelected(false);
        frozen.setSelected(false);
        if(cc == null) return;
        switch(cc){
            case NONE -> none.setSelected(true);
            case COLD -> cold.setSelected(true);
            case FROZEN -> frozen.setSelected(true);
        }
    }

    private void buttonClicked(){
        for(Searchable t : trucksList){
            if(t.getShortDescription().equals(selectedTruck.getSelected())){
                ObservableTruck truck = (ObservableTruck) t;
                truck.subscribe(this);
                truck.baseWeight = Integer.parseInt(baseField.getText());
                truck.maxWeight = Integer.parseInt(maxField.getText());
                if(group.isSelected(none.getModel())){
                    truck.coolingCapacity = CoolingCapacity.valueOf("NONE");
                } else if (group.isSelected(cold.getModel())) {
                    truck.coolingCapacity = CoolingCapacity.valueOf("COLD");
                }
                else{
                    truck.coolingCapacity = CoolingCapacity.valueOf("FROZEN");
                }
                observers.forEach(observer -> observer.update(this, truck));
                return;
            }
        }
    }

    @Override
    protected void clearFields(){
        selectedTruck.reset();
        baseField.setText("");
        maxField.setText("");
        setCoolingButton(null);
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

