package presentationLayer.gui.plUtils.examples;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverButtonExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Hover Button Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Create a button with a hover effect
        JButton button = createHoverButton("Hover Button");
        frame.add(button);

        frame.pack();
        frame.setVisible(true);
    }

    private static JButton createHoverButton(String name) {
        JButton button = new JButton(name);
        button.setPreferredSize(new Dimension(150, 20));
        button.addActionListener(e -> {
            // Handle button click event
            System.out.println("Button clicked");
        });
        button.setBorder(new EmptyBorder(5, 5, 5, 5));
        button.setBackground(Color.WHITE);
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(200, 200, 200));
                g2.fillRoundRect(15, 0, c.getWidth() - 15, c.getHeight() + 5, 5, 5);
                g2.fillOval(0, c.getHeight() / 4, c.getHeight() / 2, c.getHeight() / 2);
                super.paint(g2, c);
            }

            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(200, 200, 200, 128));
                g2.fillRoundRect(15, 0, b.getWidth() - 15, b.getHeight() + 5, 5, 5);
                g2.fillOval(0, b.getHeight() / 4, b.getHeight() / 2, b.getHeight() / 2);
                super.paintButtonPressed(g, b);
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }
}

