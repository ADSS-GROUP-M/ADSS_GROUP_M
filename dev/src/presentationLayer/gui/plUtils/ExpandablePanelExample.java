package presentationLayer.gui.plUtils;

    import javax.swing.*;
import java.awt.*;

    public class ExpandablePanelExample {
        public static void main(String[] args) {
            JFrame frame = new JFrame("Expandable Panel Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Set BoxLayout with vertical orientation

            // Add components to the panel
            for (int i = 0; i < 150; i++) {
                JLabel label = new JLabel("Label " + (i + 1));
                panel.add(label);
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            frame.add(scrollPane);
            frame.setVisible(true);
        }
    }

