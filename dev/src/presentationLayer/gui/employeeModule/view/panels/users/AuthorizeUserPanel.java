package presentationLayer.gui.employeeModule.view.panels.users;


import businessLayer.employeeModule.Authorization;
import presentationLayer.gui.employeeModule.controller.UsersControl;
import presentationLayer.gui.employeeModule.model.ObservableUser;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.PrettyTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizeUserPanel extends AbstractTransportModulePanel {
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    PrettyTextField usernameField;
    JList<String> authorizationField;
    JTextArea authorizationsTextArea;
    JButton authorizeButton;

    public AuthorizeUserPanel(UsersControl control) {
        super(control);
        init();
    }

    private void init() {
        contentPanel.setSize(scrollPane.getSize());
        Dimension textFieldSize = new Dimension(200,30);

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel usernameLabel = new JLabel("Username:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(usernameLabel, constraints);

        usernameField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(usernameField.getComponent(), constraints);

        // Find Employee Button
        JButton findUserAuthorizationsButton = new JButton("Find User Authorizations");
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        contentPanel.add(findUserAuthorizationsButton, constraints);

        findUserAuthorizationsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                findUserAuthorizationsButtonClicked();
            }
        });

        JLabel authorizationLabel = new JLabel("Authorization:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(authorizationLabel, constraints);


        DefaultListModel<String> validAuthorizations = new DefaultListModel<>();
        for(Authorization auth : Authorization.values()) {
            validAuthorizations.addElement(auth.toString());
        }

        authorizationField = new JList<>(validAuthorizations);
        authorizationField.setEnabled(false);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(authorizationField, constraints);

        //Authorize button
        authorizeButton = new JButton("Authorize");
        authorizeButton.setEnabled(false);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(authorizeButton, constraints);

        ModelObserver o = this;
        authorizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                authorizeButtonClicked();
            }
        });

        JLabel authorizationsLabel = new JLabel("User Authorizations:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPanel.add(authorizationsLabel, constraints);

        authorizationsTextArea = new JTextArea("");
        authorizationsTextArea.setFont(new Font("Arial", Font.BOLD, 20));
        authorizationsTextArea.setForeground(Colors.getForegroundColor());
        authorizationsTextArea.setEditable(false);
        constraints.anchor = GridBagConstraints.SOUTHEAST;
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.gridy = 4;
        contentPanel.add(authorizationsTextArea, constraints);
    }

    private void authorizeButtonClicked(){
//        ObservableUser user =  new ObservableUser();
//        user.subscribe(this);
//        observers.forEach(observer -> observer.add(this, user));
        if (authorizationField.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select an authorization.");
        } else {
            String result = ((UsersControl) control).authorizeUser(usernameField.getText(), authorizationField.getSelectedValue());
            JOptionPane.showMessageDialog(null, result);
            findUserAuthorizationsButtonClicked(); // Refreshes the user's authorizations list once updated
        }
    }

    private void findUserAuthorizationsButtonClicked() {
        Object result = ((UsersControl)control).findUserAuthorizations(usernameField.getText());
        if (result instanceof String) {
            JOptionPane.showMessageDialog(null, result);
            authorizationsTextArea.setText("");
            authorizationField.setEnabled(false);
            authorizeButton.setEnabled(false);
        } else if (result instanceof List authorizations) {
            List<String> auths = new ArrayList<>();
            authorizations.forEach(auth -> auths.add(auth.toString()));
            authorizationsTextArea.setText(String.join("\n",auths));
            authorizationField.setEnabled(true);
            authorizeButton.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null,"An error has occurred when retrieving the user authorizations.");
            authorizationsTextArea.setText("");
            authorizationField.setEnabled(false);
            authorizeButton.setEnabled(false);
        }
    }
    
    private void openNewWindow(String selectedItem) {
        newOpenWindow = new JFrame(selectedItem);
        newOpenWindow.setSize(800, 600);
        newOpenWindow.setLocationRelativeTo(contentPanel);
        newOpenWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //newOpenWindow.setLayout(new GridBagLayout());

        newOpenWindow.getContentPane();



        JLabel label = new JLabel("Selected Item: \n" + selectedItem);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Colors.getForegroundColor());
       
        label.setBounds(0,0,400,200);

        //newOpenWindow.add(label);

        //Create the remove button
        //JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Remove");
        removeButton.setPreferredSize(new Dimension(100, 30));
        removeButton.setBounds(550,400,100,30);
        JButton editButton = new JButton("Edit");
        editButton.setPreferredSize(new Dimension(100, 30));
        editButton.setBounds(100,400,100,30);
        newOpenPanel.setLayout(null);
        newOpenPanel.add(label);
        newOpenPanel.add(removeButton);
        newOpenPanel.add(editButton);

//        removeButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                showConfirmationDialog();
//            }
//        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Open the edit window

            }
        });
        //GridBagConstraints constraints = new GridBagConstraints();
        //constraints.fill = GridBagConstraints.WEST;
        //constraints.anchor = GridBagConstraints.PAGE_END;
        //newOpenPanel.add(buttonPanel);
        //buttonPanel.setLocation(0,20);
        newOpenWindow.add(newOpenPanel);

        newOpenWindow.setVisible(true);
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        panel.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
