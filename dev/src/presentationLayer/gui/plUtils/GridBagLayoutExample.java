package presentationLayer.gui.plUtils;

import java.awt.*;
import javax.swing.*;

public class GridBagLayoutExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 0;
//        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(usernameLabel, constraints);

        JTextField usernameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);

        JPasswordField passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("Login");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, constraints);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}

