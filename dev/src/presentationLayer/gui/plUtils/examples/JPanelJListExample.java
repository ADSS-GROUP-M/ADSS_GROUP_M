package presentationLayer.gui.plUtils.examples;

import javax.swing.*;
import java.awt.*;

public class JPanelJListExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Create the JFrame and set basic properties
        JFrame frame = new JFrame("JPanel in JList Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());

        // Create a JList
        JList<JPanel> jList = new JList<>();
        jList.setCellRenderer(new CustomListCellRenderer());

        // Create your JPanel objects
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.YELLOW);
        panel1.setPreferredSize(new Dimension(100, 50));

        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.CYAN);
        panel2.setPreferredSize(new Dimension(100, 50));

        // Add the panels to the list model
        DefaultListModel<JPanel> listModel = new DefaultListModel<>();
        listModel.addElement(panel1);
        listModel.addElement(panel2);

        // Set the list model to your JList
        jList.setModel(listModel);

        // Add the JList to a scroll pane
        JScrollPane scrollPane = new JScrollPane(jList);

        // Add the scroll pane to the frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Show the frame
        frame.setVisible(true);
    }

    static class CustomListCellRenderer extends JPanel implements ListCellRenderer<JPanel> {
        public Component getListCellRendererComponent(JList<? extends JPanel> list, JPanel value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            // Clear the panel and add the custom component
            removeAll();
            add(value);

            // Customize the appearance based on the selection and focus
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setOpaque(true);
            return this;
        }
    }
}

