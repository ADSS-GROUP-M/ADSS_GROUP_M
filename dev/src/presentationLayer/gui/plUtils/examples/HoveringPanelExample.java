package presentationLayer.gui.plUtils.examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.*;

public class HoveringPanelExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Hovering Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JTextField textField = new JTextField("Click inside me");
        HoveringPopup hoveringPopup = new HoveringPopup();

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                hoveringPopup.show(textField);
            }

            @Override
            public void focusLost(FocusEvent e) {
                hoveringPopup.hide();
            }
        });

        textField.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                hoveringPopup.updateLocation(textField);
            }
        });

        frame.getContentPane().add(textField);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class HoveringPopup {
        private JWindow window;
        private JPanel panel;

        public HoveringPopup() {
            window = new JWindow();
            window.setSize(200, 100);

            panel = new JPanel();
            panel.setBackground(Color.lightGray);

            JLabel label = new JLabel("Hovering Panel Content");
            panel.add(label);

            window.getContentPane().add(panel);
        }

        public void show(JTextField textField) {
            updateLocation(textField);
            window.setVisible(true);
        }

        public void hide() {
            window.setVisible(false);
        }

        public void updateLocation(JTextField textField) {
            Point textFieldLocation = textField.getLocationOnScreen();
            int x = textFieldLocation.x;
            int y = textFieldLocation.y - window.getHeight();
            window.setLocation(x, y);
        }
    }
}
