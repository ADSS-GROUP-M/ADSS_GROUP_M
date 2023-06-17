package presentationLayer.gui.plUtils.examples;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchableListExample {
    private final JScrollPane pane;
    private final JFrame frame;
    private JList<String> list;
    private JTextField searchField;
    private DefaultListModel<String> originalModel;

    public SearchableListExample() {
        frame = new JFrame("Searchable List Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Create the JList
        String[] items = {"Apple", "Banana", "Cherry", "Durian", "Grape", "Lemon", "Mango", "Orange"};
        originalModel = new DefaultListModel<>();
        for (String item : items) {
            originalModel.addElement(item);
        }
        list = new JList<>(originalModel);

        // Create the JTextField for searching
        searchField = new JTextField(15);
        frame.add(searchField);
        pane = new JScrollPane(list);
        frame.add(pane);
        JTextField searchField2 = new JTextField(15);
        frame.add(searchField2);

        // Add a listener to filter the items based on user input
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterItems();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterItems();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterItems();
            }
        });

        // Add a focus listener to show/hide the list when the search field gains/loses focus
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                showList();
            }

            @Override
            public void focusLost(FocusEvent e) {
                hideList();
            }
        });

        // Add a mouse listener to handle double-click event on the JList
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String selectedItem = list.getModel().getElementAt(index);
                        searchField.setText(selectedItem);
                        transferFocus(); // Transfer focus to another component
                    }
                }
            }
        });

        hideList(); // Hide the list initially

        frame.pack();
        frame.setVisible(true);
    }

    private void filterItems() {
        String searchText = searchField.getText().toLowerCase();
        DefaultListModel<String> filteredModel = new DefaultListModel<>();

        // Iterate over the original items and add matching ones to the filtered model
        for (int i = 0; i < originalModel.getSize(); i++) {
            String item = originalModel.getElementAt(i);
            if (item.toLowerCase().contains(searchText)) {
                filteredModel.addElement(item);
            }
        }

        list.setModel(filteredModel);
    }

    private void showList() {
        pane.setVisible(true);
        frame.revalidate();
    }

    private void hideList() {
        pane.setVisible(false);
        frame.revalidate();
    }

    private void transferFocus() {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.focusNextComponent();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchableListExample());
    }
}
