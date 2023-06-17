package presentationLayer.gui.transportModule.view.panels.trucks;


import presentationLayer.gui.plAbstracts.TransportBasePanel;

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
    private JButton removeButton;
    public ViewTrucksPanel() {
        super();
        init();
    }

    private void init() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        contentPanel.setSize(scrollPane.getSize());
        JLabel header = new JLabel("Trucks:\n");
        header.setVerticalAlignment(JLabel.TOP);
        contentPanel.add(header);

        contentPanel.setLayout(new BorderLayout());
        // Create the list model
        listModel = new DefaultListModel<>();

        // Create the JList
        list = new JList<>(listModel);
        contentPanel.add(new JScrollPane(list), BorderLayout.CENTER);
        for (int i = 0; i < 15; i++)
            addItem("Element " + i);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Create the remove button
        removeButton = new JButton("Remove");
        removeButton.setPreferredSize(new Dimension(100, 30));
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showConfirmationDialog();
            }
        });
        buttonPanel.add(removeButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set up the confirmation dialog on window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showConfirmationDialog();
            }
        });
    }

    private void removeSelectedItems(int selectedIndex) {
        //int[] selectedIndices = list.getSelectedIndices();
        //for (int i = selectedIndices.length - 1; i >= 0; i--) {
            listModel.remove(selectedIndex);
        //}
    }

    public void addItem(String item) {
        listModel.addElement(item);
    }

    private void showConfirmationDialog() {
        int[] selectedIndices = list.getSelectedIndices();
        if (selectedIndices.length > 0) {
            int choice = JOptionPane.showConfirmDialog(getComponent(), "Are you sure you want to remove the selected item(s)?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                removeSelectedItems(selectedIndices[0]);
            }
        }
    }


    @Override
    public Object getUpdate(UIElementEvent event) {
        return null;
    }
}
