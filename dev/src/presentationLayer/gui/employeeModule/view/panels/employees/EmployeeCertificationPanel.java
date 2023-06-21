package presentationLayer.gui.employeeModule.view.panels.employees;


import businessLayer.employeeModule.Role;
import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.model.ObservableEmployee;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyList;
import presentationLayer.gui.plUtils.PrettyTextField;
import serviceLayer.employeeModule.Objects.SEmployee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EmployeeCertificationPanel extends AbstractTransportModulePanel {
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    PrettyTextField employeeIdField;
    JList<String> roleField;
    JButton certifyButton, uncertifyButton;
    JTextArea rolesTextArea;

    public EmployeeCertificationPanel(EmployeesControl control) {
        super(control);
        init();
    }

    private void init() {
        contentPanel.setSize(scrollPane.getSize());
        Dimension textFieldSize = new Dimension(200,30);

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel employeeIdLabel = new JLabel("Employee ID:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(employeeIdLabel, constraints);

        employeeIdField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(employeeIdField.getComponent(), constraints);

        // Find Employee Button
        JButton findEmployeeRolesButton = new JButton("Find Employee Roles");
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        contentPanel.add(findEmployeeRolesButton, constraints);

//        ModelObserver o = this;
        findEmployeeRolesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                findEmployeeRolesButtonClicked();
            }
        });

        JLabel roleLabel = new JLabel("Role:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(roleLabel, constraints);

        DefaultListModel<String> validRoles = new DefaultListModel<>();
        for(Role r : Role.values()) {
            validRoles.addElement(r.toString());
        }
        roleField = new JList<>(validRoles);
        roleField.setEnabled(false);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(roleField, constraints);

        //Certify button
        certifyButton = new JButton("Certify");
        certifyButton.setEnabled(false);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        contentPanel.add(certifyButton, constraints);

        ModelObserver o2 = this;
        certifyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                certifyButtonClicked();
            }
        });

        //Uncertify button
        uncertifyButton = new JButton("Uncertify");
        uncertifyButton.setEnabled(false);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        contentPanel.add(uncertifyButton, constraints);

//        ModelObserver o2 = this;
        uncertifyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                uncertifyButtonClicked();
            }
        });

        JLabel rolesLabel = new JLabel("Employee Roles:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPanel.add(rolesLabel, constraints);

        rolesTextArea = new JTextArea("");
        rolesTextArea.setFont(new Font("Arial", Font.BOLD, 20));
        rolesTextArea.setForeground(Colors.getForegroundColor());
        rolesTextArea.setEditable(false);
        constraints.anchor = GridBagConstraints.SOUTHEAST;
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.gridy = 4;
        contentPanel.add(rolesTextArea, constraints);
    }

    private void findEmployeeRolesButtonClicked(){
//        ObservableEmployee employee =  new ObservableEmployee();
//        employee.subscribe(this);
//        observers.forEach(observer -> observer.add(this, employee));
        Object result = ((EmployeesControl)control).findEmployee(employeeIdField.getText());
        if (result instanceof String) {
            JOptionPane.showMessageDialog(null, result);
            rolesTextArea.setText("");
            roleField.setEnabled(false);
            certifyButton.setEnabled(false);
            uncertifyButton.setEnabled(false);
        } else if (result instanceof SEmployee employee) {
            rolesTextArea.setText(String.join("\n",employee.getRoles()));
            roleField.setEnabled(true);
            certifyButton.setEnabled(true);
            uncertifyButton.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null,"An error has occurred when retrieving the roles of the employee with the given id.");
            rolesTextArea.setText("");
            roleField.setEnabled(false);
            certifyButton.setEnabled(false);
            uncertifyButton.setEnabled(false);
        }
    }

    private void certifyButtonClicked(){
//        ObservableEmployee employee =  new ObservableEmployee();
//        employee.subscribe(this);
//        observers.forEach(observer -> observer.add(this, employee));
        if (roleField.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select an authorization.");
        } else {
            String result = ((EmployeesControl) control).certifyEmployee(employeeIdField.getText(), roleField.getSelectedValue().toString());
            JOptionPane.showMessageDialog(null, result);
            findEmployeeRolesButtonClicked(); // Refreshes the employee roles list once updated
        }
    }

    private void uncertifyButtonClicked(){
//        ObservableEmployee employee =  new ObservableEmployee();
//        employee.subscribe(this);
//        observers.forEach(observer -> observer.add(this, employee));
        if (roleField.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select an authorization.");
        } else {
            String result = ((EmployeesControl) control).uncertifyEmployee(employeeIdField.getText(), roleField.getSelectedValue().toString());
            JOptionPane.showMessageDialog(null, result);
            findEmployeeRolesButtonClicked(); // Refreshes the employee roles list once updated
        }
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
