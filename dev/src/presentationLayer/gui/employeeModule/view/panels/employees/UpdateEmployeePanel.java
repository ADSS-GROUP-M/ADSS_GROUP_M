package presentationLayer.gui.employeeModule.view.panels.employees;


import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.model.ObservableEmployee;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.Fonts;
import presentationLayer.gui.plUtils.PrettyTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UpdateEmployeePanel extends AbstractTransportModulePanel {
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JScrollPane listPanel;
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;

    public UpdateEmployeePanel(EmployeesControl control) {
        super(control);
        init();
    }

    private void init() {
        contentPanel.setSize(scrollPane.getSize());
//        JLabel header = new JLabel("Enter employee details:\n");
//        header.setVerticalAlignment(JLabel.TOP);
//        contentPanel.add(header);
        Dimension textFieldSize = new Dimension(200,30);

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel employeeIdLabel = new JLabel("Employee ID:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(employeeIdLabel, constraints);

        PrettyTextField employeeIdField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(employeeIdField.getComponent(), constraints);

        // Find Employee Button
        JButton findEmployeeButton = new JButton("Find Employee");
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        contentPanel.add(findEmployeeButton, constraints);

        ModelObserver o = this;
        findEmployeeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                findEmployeeButtonClicked();
            }
        });

        JLabel branchIdLabel = new JLabel("Branch Id:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(branchIdLabel, constraints);

        PrettyTextField branchIdField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        branchIdField.getComponent().setEnabled(false);
        contentPanel.add(branchIdField.getComponent(), constraints);

        JLabel nameLabel = new JLabel("Employee Name:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(nameLabel, constraints);

        PrettyTextField nameField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        nameField.getComponent().setEnabled(false);
        contentPanel.add(nameField.getComponent(), constraints);

        JLabel bankNumberLabel = new JLabel("Bank Number:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPanel.add(bankNumberLabel, constraints);

        PrettyTextField bankNumberField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        bankNumberField.getComponent().setEnabled(false);
        contentPanel.add(bankNumberField.getComponent(), constraints);

        JLabel bankBranchLabel = new JLabel("Bank Branch:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPanel.add(bankBranchLabel, constraints);

        PrettyTextField bankBranchField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        bankBranchField.getComponent().setEnabled(false);
        contentPanel.add(bankBranchField.getComponent(), constraints);

        JLabel salaryRateLabel = new JLabel("Hourly Salary Rate:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 6;
        contentPanel.add(salaryRateLabel, constraints);

        PrettyTextField salaryRateField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        salaryRateField.getComponent().setEnabled(false);
        contentPanel.add(salaryRateField.getComponent(), constraints);

        JLabel dateLabel = new JLabel("Employment Date:");
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(dateLabel, constraints);

        // date text fields
        PrettyTextField year = new PrettyTextField(new Dimension(75,30), "YYYY");
        year.setHorizontalAlignment(JTextField.CENTER);
        year.setMaximumCharacters(4);

        JLabel slash1 = new JLabel("/");
        slash1.setPreferredSize(new Dimension(10,30));
        slash1.setHorizontalAlignment(JLabel.CENTER);
        slash1.setFont(Fonts.textBoxFont.deriveFont(20f));

        PrettyTextField month = new PrettyTextField(new Dimension(35,30), "MM");
        month.setHorizontalAlignment(JTextField.CENTER);
        month.setMaximumCharacters(2);

        JLabel slash2 = new JLabel("/");
        slash2.setHorizontalAlignment(JLabel.CENTER);
        slash2.setPreferredSize(new Dimension(10,30));
        slash2.setFont(Fonts.textBoxFont.deriveFont(20f));

        PrettyTextField day = new PrettyTextField(new Dimension(35,30), "DD");
        day.setHorizontalAlignment(JTextField.CENTER);
        day.setMaximumCharacters(2);

        JPanel datePanel = new JPanel();
        datePanel.add(year.getComponent());
        datePanel.add(slash1);
        datePanel.add(month.getComponent());
        datePanel.add(slash2);
        datePanel.add(day.getComponent());
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        year.getComponent().setEnabled(false);
        month.getComponent().setEnabled(false);
        day.getComponent().setEnabled(false);
        contentPanel.add(datePanel, constraints);

        JLabel employmentConditionsLabel = new JLabel("Employment Conditions:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 8;
        contentPanel.add(employmentConditionsLabel, constraints);

        PrettyTextField employmentConditionsField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 8;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        employmentConditionsField.getComponent().setEnabled(false);
        contentPanel.add(employmentConditionsField.getComponent(), constraints);

        JLabel employeeDetailsLabel = new JLabel("Employee Details:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 9;
        contentPanel.add(employeeDetailsLabel, constraints);

        PrettyTextField employeeDetailsField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 9;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        employeeDetailsField.getComponent().setEnabled(false);
        contentPanel.add(employeeDetailsField.getComponent(), constraints);

        //Submit button
        JButton submitButton = new JButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        submitButton.setEnabled(false);
        contentPanel.add(submitButton, constraints);

        ModelObserver o2 = this;
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                submitButtonClicked();
            }
        });
    }

    private void submitButtonClicked(){
        ObservableEmployee employee =  new ObservableEmployee();
        employee.subscribe(this);
        observers.forEach(observer -> observer.add(this, employee));
    }

    private void findEmployeeButtonClicked(){
        ObservableEmployee employee =  new ObservableEmployee();
        employee.subscribe(this);
        //TODO: Update the find employee button logic here - need to enable back all the other text fields and submit button.
        observers.forEach(observer -> observer.add(this, employee));
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
