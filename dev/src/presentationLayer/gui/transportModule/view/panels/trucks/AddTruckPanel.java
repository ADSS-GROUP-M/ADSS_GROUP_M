package presentationLayer.gui.transportModule.view.panels.trucks;

import domainObjects.transportModule.Truck;
import presentationLayer.gui.plAbstracts.AbstractModulePanel;
import presentationLayer.gui.plUtils.PrettyButton;
import presentationLayer.gui.plUtils.PrettyTextField;
import presentationLayer.gui.transportModule.control.TrucksControl;
import presentationLayer.gui.transportModule.model.ObservableTruck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddTruckPanel extends AbstractModulePanel {
    private JLabel idLabel;
    private PrettyTextField idField;
    private JLabel modelLabel;
    private PrettyTextField modelField;
    private JLabel baseLabel;
    private PrettyTextField baseField;
    private JLabel maxLabel;
    private PrettyTextField maxField;
    private JLabel coolingLabel;
    private JPanel coolingPanel;
    private JRadioButton none;
    private JRadioButton cold;
    private JRadioButton frozen;

    public AddTruckPanel(TrucksControl control) {
        super(control);
        init();
    }

    private void init() {

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());

        Dimension textFieldSize = new Dimension(220,30);

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        idLabel = new JLabel("License Plate:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(idLabel, constraints);

        idField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(idField.getComponent(), constraints);

        modelLabel = new JLabel("Model:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(modelLabel, constraints);

        modelField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(modelField.getComponent(), constraints);

        baseLabel = new JLabel("Base Weight:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(baseLabel, constraints);

        baseField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(baseField.getComponent(), constraints);

        maxLabel = new JLabel("Max Weight:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(maxLabel, constraints);

        maxField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(maxField.getComponent(), constraints);

        coolingLabel = new JLabel("Cooling Capacity:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(coolingLabel, constraints);

        coolingPanel = new JPanel();
        none = new JRadioButton("NONE");
             constraints.gridx = 1;
        constraints.gridy = 5;
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
        PrettyButton submitButton = new PrettyButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(submitButton, constraints);

        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonClicked();
            }
        });
    }

    private void buttonClicked(){
        ObservableTruck truck =  new ObservableTruck();
        truck.subscribe(this);
        truck.id = idField.getText();
        truck.model = modelField.getText();
        truck.baseWeight = Integer.parseInt(baseField.getText());
        truck.maxWeight = Integer.parseInt(maxField.getText());
        if(none.isSelected())
        {
            truck.coolingCapacity = Truck.CoolingCapacity.NONE;
        }
        else if(cold.isSelected())
        {
            truck.coolingCapacity = Truck.CoolingCapacity.COLD;
        }
        else
        {
            truck.coolingCapacity = Truck.CoolingCapacity.FROZEN;
        }
        observers.forEach(observer -> observer.add(this, truck));
    }

    private void setCoolingButton(Truck.CoolingCapacity cc){
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

    @Override
    protected void clearFields(){
        idField.setText("");
        modelField.setText("");
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
