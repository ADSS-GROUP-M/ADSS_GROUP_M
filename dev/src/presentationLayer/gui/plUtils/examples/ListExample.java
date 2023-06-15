package presentationLayer.gui.plUtils.examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListExample extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JButton removeButton;

    public ListExample() {
        // Set up the JFrame
        setTitle("List Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the list model
        listModel = new DefaultListModel<>();

        // Create the JList
        list = new JList<>(listModel);
        add(new JScrollPane(list), BorderLayout.CENTER);

        // Create the remove button
        removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedItems();
            }
        });
        add(removeButton, BorderLayout.SOUTH);
    }

    private void removeSelectedItems() {
        int[] selectedIndices = list.getSelectedIndices();
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            listModel.remove(selectedIndices[i]);
        }
    }

    public void addItem(String item) {
        listModel.addElement(item);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ListExample example = new ListExample();
                example.setSize(300, 200);
                example.setVisible(true);
                example.addItem("Item 1");
                example.addItem("Item 2");
                example.addItem("Item 3");
            }
        });
    }
}
