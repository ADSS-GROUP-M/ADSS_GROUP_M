package presentationLayer.gui.employeeModule.view;

import presentationLayer.PresentationFactory;
import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.employeeModule.controller.UsersControl;
import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.plUtils.PrettyButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmployeeLoginView {
    public EmployeeLoginView() {
        JFrame frame = new JFrame("Employee Login");
        // Initialize and center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation((int)(MainWindow.screenSize.width*0.4), (int)(MainWindow.screenSize.height*0.4));

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
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

        PrettyButton loginButton = new PrettyButton("Login");
        loginButton.addActionListener(e -> {
            PresentationFactory factory = new PresentationFactory();
            EmployeesControl employeesControl = factory.employeesControl();
            ShiftsControl shiftsControl = factory.shiftsControl();
            UsersControl usersControl = factory.usersControl();

            Object result = usersControl.login(usernameField.getText(),passwordField.getText());
            if (result instanceof String) {
                JOptionPane.showMessageDialog(null, result);
            } else {
                if ((boolean)result) {
                    MainWindow mainWindow = new EmployeeView(employeesControl, shiftsControl, usernameField.getText());
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "An unexpected result has returned from the user service.");
                }
            }
        });

        // Set the default enter button
        frame.getRootPane().setDefaultButton(loginButton);

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

    public static void main(String[] args) {
        new EmployeeLoginView();
    }
}

