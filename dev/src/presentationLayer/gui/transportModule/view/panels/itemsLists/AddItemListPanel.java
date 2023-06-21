package presentationLayer.gui.transportModule.view.panels.itemsLists;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.PrettyScrollBar;
import presentationLayer.gui.transportModule.control.ItemListsControl;
import presentationLayer.gui.transportModule.model.ObservableItemList;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AddItemListPanel extends AbstractTransportModulePanel {

    private JTextArea unloadingField;
    private JTextArea loadingField;

    public AddItemListPanel(ItemListsControl control) {
        super(control);
        init();
    }
    private void init() {


        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel header = new JLabel("Enter Item List details:\n");
        header.setFont(new Font("Serif", Font.PLAIN, 30));
        header.setForeground(Colors.getForegroundColor());
        header.setVerticalAlignment(JLabel.TOP);
        header.setPreferredSize(new Dimension(300, 100));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridwidth =8;
        contentPanel.add(header, constraints);


        JLabel loadingLabel = new JLabel("Item loading list:");
        loadingLabel.setPreferredSize(new Dimension(200, 100));
        loadingLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        constraints.gridx = 0;
        constraints.gridy = 0;

        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 0, 5);
        contentPanel.add(loadingLabel, constraints);


        loadingField = new JTextArea(50,20);
        loadingField.setLineWrap(true);
        loadingField.setWrapStyleWord(true);
        loadingField.setEditable(true);
        loadingField.setFont(new Font("Serif", Font.PLAIN, 25));
        JScrollPane areaScrollPane = new JScrollPane(loadingField);
        areaScrollPane.setVerticalScrollBar(new PrettyScrollBar(100));
        areaScrollPane.getVerticalScrollBar().setUnitIncrement(30);

        areaScrollPane.setPreferredSize(new Dimension(700, 200));

        constraints.gridx = 0;
        constraints.gridy = 1;
        //constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(areaScrollPane, constraints);
        JLabel unloadingLabel = new JLabel("Item unloading list:");
        unloadingLabel.setPreferredSize(new Dimension(200, 100));
        unloadingLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 0, 5);
        contentPanel.add(unloadingLabel, constraints);

        unloadingField = new JTextArea(50,20);
        unloadingField.setLineWrap(true);
        unloadingField.setWrapStyleWord(true);
        unloadingField.setEditable(true);
        unloadingField.setFont(new Font("Serif", Font.PLAIN, 25));
        JScrollPane areaScrollPane2 = new JScrollPane(unloadingField);
        areaScrollPane2.setVerticalScrollBar(new PrettyScrollBar(100));
        areaScrollPane2.getVerticalScrollBar().setUnitIncrement(30);
        areaScrollPane2.setPreferredSize(new Dimension(700, 200));

        constraints.gridx = 0;
        constraints.gridy = 3;
        //constraints.fill = GridBagConstraints.HORIZONTAL;

        contentPanel.add(areaScrollPane2, constraints);


        JButton submitButton = new JButton("Submit");
        //submitButton.setFont(new Font("Serif", Font.PLAIN, 25));
        submitButton.setPreferredSize(new Dimension(100, 30));
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 0,10, 0);
        contentPanel.add(submitButton, constraints);


        submitButton.addActionListener(e -> {
            ObservableItemList model = new ObservableItemList();
            model.subscribe(this);
            model.id = -1;

            Map<String,Integer> loading = parseList(loadingField.getText());
            Map<String,Integer> unloading = parseList(unloadingField.getText());
            model.load = loading;
            model.unload = unloading;

            if(loading == null || unloading == null || (loading.isEmpty() && unloading.isEmpty())) {
                return;
            }

            control.add(this,model);
        });
        
    }

    private Map<String, Integer> parseList(String s) {

        Map<String,Integer> items = new HashMap<>();

        if(s == null || s.equals("")) {
            return items;
        }

        String[] lines = s.split("\n");
        for(String line : lines) {
            String[] itemQuantityArray = line.split(" ");
            StringBuilder item = new StringBuilder();
            for(int i = 0; i < itemQuantityArray.length-1; i++){
                item.append(itemQuantityArray[i]);
                if(i != itemQuantityArray.length-2) {
                    item.append(" ");
                }
            }
            int quantity;
            try{
                quantity = Integer.parseInt(itemQuantityArray[itemQuantityArray.length-1]);
                if(quantity <= 0) {
                    throw new NumberFormatException();
                }
            }catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid quantity in line: " + line, "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            if(items.containsKey(item.toString())) {
                JOptionPane.showMessageDialog(null, "Duplicate item in line: " + line, "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            items.put(item.toString(),quantity);
        }
        return items;
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);

        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        panel.revalidate();
    }

    @Override
    protected void clearFields() {
        unloadingField.setText("");
        loadingField.setText("");
    }
}
