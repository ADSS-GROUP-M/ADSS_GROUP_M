package presentationLayer.gui.transportModule.view.panels.itemsLists;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.transportModule.control.ItemListsControl;

import javax.swing.*;
import java.awt.*;

public class AddItemListPanel extends AbstractTransportModulePanel {

    public AddItemListPanel(ItemListsControl control) {
        super(control);
        init();
    }
    private void init() {

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
        JLabel header = new JLabel("Enter Item List details:\n");
        header.setVerticalAlignment(JLabel.TOP);
        contentPanel.add(header);


        //Panel panel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel loadingLabel = new JLabel("Item loading list:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(loadingLabel, constraints);


        JTextArea loadingField = new JTextArea(20,20);
        loadingField.setLineWrap(true);
        loadingField.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(loadingField);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(250, 100));

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(areaScrollPane, constraints);
        JLabel unloadingLabel = new JLabel("Item unloading list:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(unloadingLabel, constraints);

        JTextArea unloadingField = new JTextArea(20,20);
        loadingField.setLineWrap(true);
        loadingField.setWrapStyleWord(true);
        JScrollPane areaScrollPane2 = new JScrollPane(unloadingField);
        areaScrollPane2.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane2.setPreferredSize(new Dimension(250, 100));

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        contentPanel.add(areaScrollPane2, constraints);
        
    }

    

    @Override
    public void notify(ObservableModel observable) {

    }
}
