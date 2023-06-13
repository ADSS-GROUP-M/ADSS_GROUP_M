package presentationLayer.gui.plUtils;

import javax.swing.*;
import java.awt.*;

public class HorizontalAlignmentExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Horizontal Alignment Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a wrapper panel to hold the components
        JPanel wrapperPanel1 = new JPanel();
        wrapperPanel1.setLayout(new BoxLayout(wrapperPanel1, BoxLayout.X_AXIS));
        JPanel wrapperPanel2 = new JPanel();
        wrapperPanel2.setLayout(new BoxLayout(wrapperPanel2, BoxLayout.X_AXIS));
        JPanel wrapperPanel3 = new JPanel();
        wrapperPanel3.setLayout(new BoxLayout(wrapperPanel3, BoxLayout.X_AXIS));

        // Add some components to the wrapper panel
        JLabel label1 = new JLabel("Label 1");
        JLabel label2 = new JLabel("Label 2");
        JButton button = new JButton("Button");

        wrapperPanel1.add(Box.createHorizontalGlue()); // Add glue to the left
        wrapperPanel1.add(label1);
        wrapperPanel1.add(Box.createHorizontalGlue()); // Add glue to the right

        wrapperPanel2.add(Box.createHorizontalGlue());
        wrapperPanel2.add(label2);
        wrapperPanel2.add(Box.createHorizontalGlue()); // Add glue to the right

        wrapperPanel3.add(Box.createHorizontalGlue());
        wrapperPanel3.add(button);
        wrapperPanel3.add(Box.createHorizontalGlue()); // Add glue to the right

        panel.add(wrapperPanel1);
        panel.add(Box.createVerticalStrut(10)); // Add some space between the components
        panel.add(wrapperPanel2);
        panel.add(Box.createVerticalStrut(10));
        panel.add(wrapperPanel3);

        frame.add(panel);
        frame.setVisible(true);
    }
}

