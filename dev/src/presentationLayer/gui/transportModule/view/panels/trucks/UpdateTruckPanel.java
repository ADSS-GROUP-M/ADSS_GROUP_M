package presentationLayer.gui.transportModule.view.panels.trucks;

import domainObjects.transportModule.Truck;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.plUtils.SearchBox;
import presentationLayer.gui.plUtils.SearchableString;
import presentationLayer.gui.transportModule.control.TrucksControl;
import presentationLayer.gui.transportModule.model.ObservableTruck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UpdateTruckPanel extends AbstractTransportModulePanel {
    private JLabel trucksLabel;
    private SearchBox trucks;
    private JLabel baseLabel;
    private PrettyTextField baseField;
    private JLabel maxLabel;
    private PrettyTextField maxField;
    private JLabel coolingLabel;
    private JPanel coolingPanel;
    private JRadioButton none;
    private JRadioButton cold;
    private JRadioButton frozen;
    private List<Searchable> trucksSearchableList;
    public UpdateTruckPanel(TrucksControl control) {
        super(control);
//        for(ObservableModel truck : control.getAllModels()){
//            trucksSearchableList.add(truck)
//        }
        init();
    }
    private void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());

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

//        //need to change the list to real items
//        trucksSearchableList = List.of(new SearchableString("item1"), new SearchableString("item2"), new SearchableString("item3"));
        //button
        trucks = new SearchBox(trucksSearchableList,"Select Truck",textFieldSize, this);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(trucks.getComponent(), constraints);

        //title
        baseLabel = new JLabel("Base Weight:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(baseLabel, constraints);

        //text box
        baseField = new PrettyTextField(textFieldSize);
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
        maxField = new PrettyTextField(textFieldSize);
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

        ButtonGroup group = new ButtonGroup();
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

        ModelObserver o = this;
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //buttonClicked();
            }
        });
    }

//    private void buttonClicked(){
//        ObservableTruck truck =  (ObservableTruck) control.getModel(trucks.getComponent());
//        truck.subscribe(this);
//        truck.baseWeight = Integer.parseInt(baseField.getText());
//        truck.maxWeight = Integer.parseInt(maxField.getText());
//        if(none.isSelected())
//        {
//            truck.coolingCapacity = Truck.CoolingCapacity.NONE;
//        }
//        else if(cold.isSelected())
//        {
//            truck.coolingCapacity = Truck.CoolingCapacity.COLD;
//        }
//        else
//        {
//            truck.coolingCapacity = Truck.CoolingCapacity.FROZEN;
//        }
//        observers.forEach(observer -> observer.add(this, truck));
//    }

    @Override
    public void notify(ObservableModel observable) {

    }
}

