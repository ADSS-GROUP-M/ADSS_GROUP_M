package presentationLayer.gui.employeeModule.view.panels.employees;


import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.Fonts;
import presentationLayer.gui.plUtils.PrettyTextField;
import serviceLayer.employeeModule.Objects.SEmployee;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EmployeeDetailsPanel extends AbstractTransportModulePanel {
    JTextArea nameField, employeeIdField, bankNumberField, bankBranchField, salaryRateField, salaryBonusField, employmentConditionsField, employeeDetailsField;
    PrettyTextField year, month, day;
    String employeeId;

    public EmployeeDetailsPanel(String employeeId, EmployeesControl control) {
        super(control);
        this.employeeId = employeeId;
        init();
    }

    private void init() {
        contentPanel.setSize(scrollPane.getSize());

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel nameLabel = new JLabel("Employee Name:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(nameLabel, constraints);

        nameField = new JTextArea("");
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(nameField, constraints);

        JLabel employeeIdLabel = new JLabel("Employee ID:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(employeeIdLabel, constraints);

        employeeIdField = new JTextArea("");
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(employeeIdField, constraints);

        JLabel bankNumberLabel = new JLabel("Bank Number:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPanel.add(bankNumberLabel, constraints);

        bankNumberField = new JTextArea("");
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(bankNumberField, constraints);

        JLabel bankBranchLabel = new JLabel("Bank Branch:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPanel.add(bankBranchLabel, constraints);

        bankBranchField = new JTextArea("");
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(bankBranchField, constraints);

        JLabel salaryRateLabel = new JLabel("Hourly Salary Rate:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPanel.add(salaryRateLabel, constraints);

        salaryRateField = new JTextArea("");
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(salaryRateField, constraints);

        JLabel salaryBonusLabel = new JLabel("Salary Bonus:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 6;
        contentPanel.add(salaryBonusLabel, constraints);

        salaryBonusField = new JTextArea("");
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(salaryBonusField, constraints);

        JLabel dateLabel = new JLabel("Employment Date:");
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(dateLabel, constraints);

        // date text fields
        year = new PrettyTextField(new Dimension(75,30), "YYYY");
        year.getComponent().setEnabled(false);
        year.setHorizontalAlignment(JTextField.CENTER);
        year.setMaximumCharacters(4);

        JLabel slash1 = new JLabel("/");
        slash1.setPreferredSize(new Dimension(10,30));
        slash1.setHorizontalAlignment(JLabel.CENTER);
        slash1.setFont(Fonts.textBoxFont.deriveFont(20f));

        month = new PrettyTextField(new Dimension(35,30), "MM");
        month.getComponent().setEnabled(false);
        month.setHorizontalAlignment(JTextField.CENTER);
        month.setMaximumCharacters(2);

        JLabel slash2 = new JLabel("/");
        slash2.setHorizontalAlignment(JLabel.CENTER);
        slash2.setPreferredSize(new Dimension(10,30));
        slash2.setFont(Fonts.textBoxFont.deriveFont(20f));

        day = new PrettyTextField(new Dimension(35,30), "DD");
        day.getComponent().setEnabled(false);
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

        employmentConditionsField = new JTextArea("");
        constraints.gridx = 1;
        constraints.gridy = 8;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(employmentConditionsField, constraints);

        JLabel employeeDetailsLabel = new JLabel("Employee Details:");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 9;
        contentPanel.add(employeeDetailsLabel, constraints);

        employeeDetailsField = new JTextArea("");
        constraints.gridx = 1;
        constraints.gridy = 9;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(employeeDetailsField, constraints);

        fillDetails();
    }

    private void fillDetails(){
//        ObservableEmployee employee =  new ObservableEmployee();
//        employee.subscribe(this);
//        observers.forEach(observer -> observer.add(this, employee));
        Object result = ((EmployeesControl) control).findEmployee(employeeId);
        if (result instanceof String) {
            JOptionPane.showMessageDialog(null, result);
        }
        else if (result instanceof SEmployee) {
            employeeIdField.setText(employeeId);

            String bankDetails = ((SEmployee) result).getBankDetails();
            String bankNumber = bankDetails.substring(0, bankDetails.lastIndexOf(' '));
            String bankBranch = bankDetails.substring(bankDetails.lastIndexOf(' ') + 1);
            bankNumberField.setText(bankNumber);
            bankBranchField.setText(bankBranch);

            nameField.setText(((SEmployee) result).getFullName());
            salaryRateField.setText(String.valueOf(((SEmployee) result).getHourlySalaryRate()));
            salaryBonusField.setText(String.valueOf(((SEmployee) result).getSalaryBonus()));

            LocalDate employmentDate = ((SEmployee) result).getEmploymentDate();
            year.setText(String.valueOf(employmentDate.getYear()));
            month.setText(String.valueOf(employmentDate.getMonth().getValue()));
            day.setText(String.valueOf(employmentDate.getDayOfMonth()));
            employmentConditionsField.setText(((SEmployee) result).getEmploymentConditions());
            employeeDetailsField.setText(((SEmployee) result).getDetails());
        }
        else {
            JOptionPane.showMessageDialog(null, "An unexpected result has returned from the employee service.");
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
