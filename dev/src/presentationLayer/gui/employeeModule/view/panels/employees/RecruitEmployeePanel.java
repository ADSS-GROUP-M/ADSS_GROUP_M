package presentationLayer.gui.employeeModule.view.panels.employees;


import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.model.ObservableEmployee;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.*;
import utils.DateUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RecruitEmployeePanel extends AbstractTransportModulePanel {
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    PrettyTextField nameField, branchIdField, employeeIdField, bankNumberField, bankBranchField, salaryRateField, year, month, day, employmentConditionsField, employeeDetailsField;

    public RecruitEmployeePanel(EmployeesControl control) {
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

        JLabel nameLabel = new JLabel("Employee Name:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(nameLabel, constraints);

        nameField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(nameField.getComponent(), constraints);

        JLabel branchIdLabel = new JLabel("Branch Id:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(branchIdLabel, constraints);

        branchIdField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(branchIdField.getComponent(), constraints);

        JLabel employeeIdLabel = new JLabel("Employee ID:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(employeeIdLabel, constraints);

        employeeIdField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(employeeIdField.getComponent(), constraints);

        JLabel bankNumberLabel = new JLabel("Bank Number:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPanel.add(bankNumberLabel, constraints);

        bankNumberField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(bankNumberField.getComponent(), constraints);

        JLabel bankBranchLabel = new JLabel("Bank Branch:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPanel.add(bankBranchLabel, constraints);

        bankBranchField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(bankBranchField.getComponent(), constraints);

        JLabel salaryRateLabel = new JLabel("Hourly Salary Rate:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 6;
        contentPanel.add(salaryRateLabel, constraints);

        salaryRateField = new PrettyTextField(textFieldSize);
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(salaryRateField.getComponent(), constraints);

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
        contentPanel.add(employeeDetailsField.getComponent(), constraints);

        //Submit button
        JButton submitButton = new JButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        contentPanel.add(submitButton, constraints);

        ModelObserver o = this;
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonClicked();
            }
        });
    }

    private void buttonClicked(){
        ObservableEmployee employee =  new ObservableEmployee();
        employee.subscribe(this);
        observers.forEach(observer -> observer.add(this, employee));
        try {
            String branchId = branchIdField.getText();
            String employeeName = nameField.getText();
            String employeeId = employeeIdField.getText();
            String bankDetails = bankNumberField.getText() + " " + bankBranchField.getText();
            double salaryRate = Double.parseDouble(salaryRateField.getText());
            LocalDate employmentDate = DateUtils.parse(day.getText() + "/" + month.getText() + "/" + year.getText());
            String employmentConditions = employmentConditionsField.getText();
            String employeeDetails = employeeDetailsField.getText();
            String result = ((EmployeesControl) control).recruitEmployee(branchId, employeeName, employeeId, bankDetails, salaryRate, employmentDate, employmentConditions, employeeDetails);
            JOptionPane.showMessageDialog(null, result);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid salary rate was given: " + salaryRateField.getText());
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Invalid employment date was given. " + e.getMessage());
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
