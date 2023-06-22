package presentationLayer.gui.employeeModule.view.panels.employees;


import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.model.ObservableEmployee;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.Fonts;
import presentationLayer.gui.plUtils.PrettyButton;
import presentationLayer.gui.plUtils.PrettyTextField;
import serviceLayer.employeeModule.Objects.SEmployee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class UpdateEmployeePanel extends AbstractTransportModulePanel {
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    PrettyTextField employeeIdField, nameField, bankNumberField, bankBranchField, salaryRateField, salaryBonusField, year, month, day, employmentConditionsField, employeeDetailsField;
    PrettyButton submitButton;

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

        employeeIdField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(employeeIdField.getComponent(), constraints);

        // Find Employee Button
        PrettyButton findEmployeeButton = new PrettyButton("Find Employee");
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

        JLabel nameLabel = new JLabel("Employee Name:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(nameLabel, constraints);

        nameField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        nameField.getComponent().setEnabled(false);
        contentPanel.add(nameField.getComponent(), constraints);

        JLabel bankNumberLabel = new JLabel("Bank Number:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(bankNumberLabel, constraints);

        bankNumberField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        bankNumberField.getComponent().setEnabled(false);
        contentPanel.add(bankNumberField.getComponent(), constraints);

        JLabel bankBranchLabel = new JLabel("Bank Branch:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPanel.add(bankBranchLabel, constraints);

        bankBranchField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        bankBranchField.getComponent().setEnabled(false);
        contentPanel.add(bankBranchField.getComponent(), constraints);

        JLabel salaryRateLabel = new JLabel("Hourly Salary Rate:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPanel.add(salaryRateLabel, constraints);

        salaryRateField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        salaryRateField.getComponent().setEnabled(false);
        contentPanel.add(salaryRateField.getComponent(), constraints);

        JLabel salaryBonusLabel = new JLabel("Salary Bonus:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 6;
        contentPanel.add(salaryBonusLabel, constraints);

        salaryBonusField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        salaryBonusField.getComponent().setEnabled(false);
        contentPanel.add(salaryBonusField.getComponent(), constraints);

        JLabel dateLabel = new JLabel("Employment Date:");
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(dateLabel, constraints);

        // date text fields
        year = new PrettyTextField(new Dimension(75,30), "YYYY");
        year.setHorizontalAlignment(JTextField.CENTER);
        year.setMaximumCharacters(4);

        JLabel slash1 = new JLabel("/");
        slash1.setPreferredSize(new Dimension(10,30));
        slash1.setHorizontalAlignment(JLabel.CENTER);
        slash1.setFont(Fonts.textBoxFont.deriveFont(20f));

        month = new PrettyTextField(new Dimension(35,30), "MM");
        month.setHorizontalAlignment(JTextField.CENTER);
        month.setMaximumCharacters(2);

        JLabel slash2 = new JLabel("/");
        slash2.setHorizontalAlignment(JLabel.CENTER);
        slash2.setPreferredSize(new Dimension(10,30));
        slash2.setFont(Fonts.textBoxFont.deriveFont(20f));

        day = new PrettyTextField(new Dimension(35,30), "DD");
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

        employmentConditionsField = new PrettyTextField(textFieldSize);
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

        employeeDetailsField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 9;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        employeeDetailsField.getComponent().setEnabled(false);
        contentPanel.add(employeeDetailsField.getComponent(), constraints);

        //Submit button
        submitButton = new PrettyButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        submitButton.setEnabled(false);
        contentPanel.add(submitButton, constraints);

        ModelObserver o2 = this;
        submitButton.addActionListener(e -> submitButtonClicked());
    }

    @Override
    protected void clearFields() {
        
    }

    private void submitButtonClicked(){
//        ObservableEmployee employee =  new ObservableEmployee();
//        employee.subscribe(this);
//        observers.forEach(observer -> observer.add(this, employee));
        String employeeId = employeeIdField.getText();
        double salaryRate;
        double salaryBonus;

        try{
            salaryRate = Double.parseDouble(salaryRateField.getText());
            salaryBonus = Double.parseDouble(salaryBonusField.getText());
        } catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Please enter a valid salary rate and bonus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String bankDetails = bankNumberField.getText() + " " + bankBranchField.getText();
        String employmentConditions = employmentConditionsField.getText();
        String employeeDetails = employeeDetailsField.getText();

        String salaryResponse = ((EmployeesControl)control).updateEmployeeSalary(employeeId,salaryRate,salaryBonus);
        String bankDetailsResponse = ((EmployeesControl)control).updateEmployeeBankDetails(employeeId,bankDetails);
        String employmentConditionsResponse = ((EmployeesControl)control).updateEmployeeEmploymentConditions(employeeId,employmentConditions);
        String detailsResponse = ((EmployeesControl)control).updateEmployeeDetails(employeeId,employeeDetails);

        String message = "";
        message += (salaryResponse != null ? salaryResponse + "\n" : "");
        message += (bankDetailsResponse != null ? bankDetailsResponse + "\n" : "");
        message += (employmentConditionsResponse != null ? employmentConditionsResponse + "\n" : "");
        message += (detailsResponse != null ? detailsResponse + "\n" : "");

        if (!message.equals("")) {
            JOptionPane.showMessageDialog(null,message);
        }
    }

    private void findEmployeeButtonClicked(){
//        ObservableEmployee employee =  new ObservableEmployee();
//        employee.subscribe(this);
//        observers.forEach(observer -> observer.add(this, employee));
        Object result = ((EmployeesControl)control).findEmployee(employeeIdField.getText());
        if (result instanceof String) {
            JOptionPane.showMessageDialog(null, result);
            bankNumberField.getComponent().setEnabled(false);
            bankBranchField.getComponent().setEnabled(false);
            salaryRateField.getComponent().setEnabled(false);
            salaryBonusField.getComponent().setEnabled(false);
            employmentConditionsField.getComponent().setEnabled(false);
            employeeDetailsField.getComponent().setEnabled(false);
            submitButton.setEnabled(false);
        } else if (result instanceof SEmployee employee) {
            nameField.setText(employee.getFullName());

            String bankDetails = employee.getBankDetails();
            String bankNumber = bankDetails.substring(0,bankDetails.lastIndexOf(' '));
            String bankBranch = bankDetails.substring(bankDetails.lastIndexOf(' ') + 1);
            bankNumberField.setText(bankNumber);
            bankBranchField.setText(bankBranch);

            salaryRateField.setText(String.valueOf(employee.getHourlySalaryRate()));
            salaryBonusField.setText(String.valueOf(employee.getSalaryBonus()));

            LocalDate employmentDate = employee.getEmploymentDate();
            year.setText(String.valueOf(employmentDate.getYear()));
            month.setText(String.valueOf(employmentDate.getMonth().getValue()));
            day.setText(String.valueOf(employmentDate.getDayOfMonth()));
            employmentConditionsField.setText(employee.getEmploymentConditions());
            employeeDetailsField.setText(employee.getDetails());

            bankNumberField.getComponent().setEnabled(true);
            bankBranchField.getComponent().setEnabled(true);
            salaryRateField.getComponent().setEnabled(true);
            salaryBonusField.getComponent().setEnabled(true);
            employmentConditionsField.getComponent().setEnabled(true);
            employeeDetailsField.getComponent().setEnabled(true);
            submitButton.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null,"An error has occurred when retrieving the employee with the given id.");
            bankNumberField.getComponent().setEnabled(false);
            bankBranchField.getComponent().setEnabled(false);
            salaryRateField.getComponent().setEnabled(false);
            salaryBonusField.getComponent().setEnabled(false);
            employmentConditionsField.getComponent().setEnabled(false);
            employeeDetailsField.getComponent().setEnabled(false);
            submitButton.setEnabled(false);
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

    
}
