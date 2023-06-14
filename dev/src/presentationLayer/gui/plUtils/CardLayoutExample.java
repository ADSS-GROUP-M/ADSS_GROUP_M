package presentationLayer.gui.plUtils;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CardLayoutExample {
    private JFrame frame;
    private JPanel panel;
    private CardLayout cardLayout;

    public static void main(String[] args) {
        CardLayoutExample example = new CardLayoutExample();
        example.createAndShowGUI();
    }

    public void createAndShowGUI() {
        // Create the main frame
        frame = new JFrame("CardLayout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the cards panel and set its layout to CardLayout
        panel = new JPanel();
        cardLayout = new CardLayout();
        panel.setLayout(cardLayout);

        // Create the individual screens/panels
        JPanel panel1 = createScreen("Screen 1", "Switch to Screen 2");
        JPanel panel2 = createScreen("Screen 2", "Switch to Screen 1");

        // Add the screens/panels to the cards panel
        panel.add(panel1, "screen1");
        panel.add(panel2, "screen2");

        // Create buttons to switch between screens
        JButton switchToScreen1Button = new JButton("Switch to Screen 1");
        switchToScreen1Button.addActionListener(e -> cardLayout.show(panel, "screen1"));

        JButton switchToScreen2Button = new JButton("Switch to Screen 2");
        switchToScreen2Button.addActionListener(e -> cardLayout.show(panel, "screen2"));

        // Add the cards panel and the buttons to the main frame
        frame.getContentPane().add(panel);
        frame.getContentPane().add(switchToScreen1Button, "North");
        frame.getContentPane().add(switchToScreen2Button, "South");

        // Display the main frame
        frame.setPreferredSize(new Dimension(300, 200));
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel createScreen(String screenName, String buttonText) {
        JPanel screen = new JPanel();
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> {
            if (screenName.equals("Screen 1")) {
                cardLayout.show(panel, "screen2");
            } else if (screenName.equals("Screen 2")) {
                cardLayout.show(panel, "screen1");
            }
        });
        screen.add(button);
        return screen;
    }
}


