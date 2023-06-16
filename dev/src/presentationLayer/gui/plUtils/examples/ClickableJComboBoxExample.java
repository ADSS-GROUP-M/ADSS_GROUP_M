package presentationLayer.gui.plUtils.examples;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickableJComboBoxExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClickableJComboBoxExample::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Clickable JComboBox Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] options = {"Option 1", "Option 2", "Option 3"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setUI(new ClickableComboBoxUI()); // Set custom UI

        frame.getContentPane().add(comboBox);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class ClickableComboBoxUI extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
        return new JButton() {
            @Override
            public int getWidth() {
                return 0; // Hide the arrow button
            }
        };
    }

    @Override
    protected void installListeners() {
        super.installListeners();

        comboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!comboBox.isPopupVisible()) {
                    comboBox.setPopupVisible(true); // Show the popup when clicked
                }
            }
        });
    }
}
