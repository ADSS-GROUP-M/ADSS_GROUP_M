package presentationLayer.gui.transportModule.view.panels.trucks;


import domainObjects.transportModule.Truck;
import presentationLayer.gui.plAbstracts.Searchable;
import presentationLayer.gui.plAbstracts.TransportBasePanel;
import serviceLayer.ServiceFactory;
import serviceLayer.transportModule.ResourceManagementService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ViewTrucksPanel extends TransportBasePanel {
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JScrollPane listPanel;

    public ViewTrucksPanel() {
        super();
        init();

    }
    private void init() {

        // Create the list model
        Searchable s1 = new Searchable() {
            final String description = "ItEm NuMbEr 1, beri cool. Case-insensitive Searchable example";
            @Override
            public boolean isMatch(String query) {
                return description.toLowerCase().contains(query.toLowerCase());
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return null;
            }

        };

        Searchable s2 = new Searchable() {
            final String description = "Item number 2, muy suave. case-sensitive Searchable example";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return null;
            }
        };

        Searchable s3 = new Searchable() {
            final String description = "Item number 3, un poco loco";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return null;
            }
        };

        Searchable s4 = new Searchable() {
            final String description = "Item number 4, el gato es en la mesa";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return null;
            }
        };
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        listModel = new DefaultListModel<>();

        // Create the JList
        list = new JList<>(listModel);
        listPanel = new JScrollPane(list);
        JPanel innerPanel = new JPanel();
        innerPanel.add(listPanel);


        addItem(s1.getShortDescription());
        addItem(s2.getShortDescription());
        addItem(s3.getShortDescription());
        addItem(s4.getShortDescription());



        // Create the remove button
        JButton removeButton = new JButton("Remove");
        removeButton.setPreferredSize(new Dimension(100, 30));
        innerPanel.add(removeButton,constraints);
        contentPanel.add(innerPanel,constraints);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showConfirmationDialog();
            }
        });



         //Set up the confirmation dialog on window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showConfirmationDialog();
            }
        });

    }

    private void removeSelectedItems(int selectedIndex) {
            listModel.remove(selectedIndex);

    }

    public void addItem(String item) {
        listModel.addElement(item);
    }

    private void showConfirmationDialog() {
        int[] selectedIndices = list.getSelectedIndices();
        if (selectedIndices.length > 0) {
            int choice = JOptionPane.showConfirmDialog(getComponent(), "Are you sure you want to remove the selected item?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                removeSelectedItems(selectedIndices[0]);
            }
        }
    }


    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension contentPreferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight()*0.6));
        contentPanel.setPreferredSize(new Dimension(contentPreferredSize.width, contentPreferredSize.height+250));
        Dimension preferredSize = new Dimension((int) (scrollPane.getWidth()*0.6), (int) (scrollPane.getHeight()*0.8));
        listPanel.setPreferredSize(preferredSize);
        listPanel.revalidate();
        scrollPane.revalidate();
    }
}
