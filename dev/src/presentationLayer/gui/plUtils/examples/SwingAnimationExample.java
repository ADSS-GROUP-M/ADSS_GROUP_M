package presentationLayer.gui.plUtils.examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingAnimationExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Swing Animation Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a custom component
        CustomComponent customComponent = new CustomComponent();
        frame.add(customComponent);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Start the animation
        customComponent.startAnimation();
    }

    static class CustomComponent extends JComponent {
        private int x;
        private int y;

        public CustomComponent() {
            x = 50;
            y = 50;
        }

        public void startAnimation() {
            Timer timer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Update the position of the component
                    x++;
                    y++;

                    // Repaint the component to reflect the updated position
                    repaint();
                }
            });

            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(Color.RED);
            g2.fillRect(x, y, 50, 50);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400, 400);
        }
    }
}

