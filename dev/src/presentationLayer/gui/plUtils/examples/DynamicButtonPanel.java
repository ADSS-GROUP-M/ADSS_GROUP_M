package presentationLayer.gui.plUtils.examples;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DynamicButtonPanel extends JPanel {
    private JLabel label;
    private JButton button;

    public DynamicButtonPanel() {
        setLayout(new FlowLayout());

        label = new JLabel("Hover over me");
        button = new JButton("Click Me");
        button.setVisible(false);

        add(label);
        add(button);

        label.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setVisible(true);
            }

            public void mouseExited(MouseEvent e) {
                button.setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dynamic Button Label Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DynamicButtonPanel panel = new DynamicButtonPanel();
        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}


