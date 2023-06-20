package presentationLayer.gui.employeeModule.view.panels.employees;


import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.model.ObservableEmployee;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ModelObserver;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RecruitEmployeePanel extends AbstractTransportModulePanel {
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;

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

        PrettyTextField nameField = new PrettyTextField(textFieldSize);
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

        PrettyTextField branchIdField = new PrettyTextField(textFieldSize);
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

        PrettyTextField employeeIdField = new PrettyTextField(textFieldSize);
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

        PrettyTextField bankNumberField = new PrettyTextField(textFieldSize);
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

        PrettyTextField bankBranchField = new PrettyTextField(textFieldSize);
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

        PrettyTextField salaryRateField = new PrettyTextField(textFieldSize);
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
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        panel.revalidate();
    }

//    private void init2() {
//
//        // TODO: Swap all this example implementation
//        contentPanel.setLayout(new GridBagLayout());
//        GridBagConstraints constraints = new GridBagConstraints();
//
////        list.addMouseListener(new MouseAdapter() {
////            public void mouseClicked(MouseEvent e) {
////                if (e.getClickCount() == 2) { // Double-click event
////                    int selectedIndex = list.getSelectedIndex();
////                    if (selectedIndex != -1) {
////                        String selectedItem = listModel.getElementAt(selectedIndex);
////
////                        openNewWindow(selectedItem);
////                    }
////                }
////            }
////        });
//
//        JPanel innerPanel = new JPanel();
//        innerPanel.setBackground(new Color(0,0,0,0));
//
//        JLabel lbl_employee_name = new JLabel("Employee Name:");
//        lbl_employee_name.setBounds(20, 20, 150, 20);
//        innerPanel.add(lbl_employee_name);
//
//        nameField = new JTextField();
//        nameField.setBounds(180, 20, 200, 20);
//        innerPanel.add(nameField);
//        nameField.setColumns(10);
//
//        JLabel lbl_branch_id = new JLabel("Branch ID:");
//        lbl_branch_id.setBounds(20, 50, 150, 20);
//        innerPanel.add(lbl_branch_id);
//
//        branchIdField = new JTextField();
//        branchIdField.setBounds(180, 50, 200, 20);
//        innerPanel.add(branchIdField);
//        branchIdField.setColumns(10);
//
//        JLabel lbl_employee_id = new JLabel("Employee ID:");
//        lbl_employee_id.setBounds(20, 80, 150, 20);
//        innerPanel.add(lbl_employee_id);
//
//        employeeIdField = new JTextField();
//        employeeIdField.setBounds(180, 80, 200, 20);
//        innerPanel.add(employeeIdField);
//        employeeIdField.setColumns(10);
//
//        JLabel lbl_bank_number = new JLabel("Bank Number:");
//        lbl_bank_number.setBounds(20, 110, 150, 20);
//        innerPanel.add(lbl_bank_number);
//
//        bankNumberField = new JTextField();
//        bankNumberField.setBounds(180, 110, 200, 20);
//        innerPanel.add(bankNumberField);
//        bankNumberField.setColumns(10);
//
//        JLabel lbl_bank_branch = new JLabel("Bank Branch Number:");
//        lbl_bank_branch.setBounds(20, 140, 150, 20);
//        innerPanel.add(lbl_bank_branch);
//
//        bankBranchNumberField = new JTextField();
//        bankBranchNumberField.setBounds(180, 140, 200, 20);
//        innerPanel.add(bankBranchNumberField);
//        bankBranchNumberField.setColumns(10);
//
//        JLabel lbl_salary_rate = new JLabel("Hourly Salary Rate:");
//        lbl_salary_rate.setBounds(20, 170, 150, 20);
//        innerPanel.add(lbl_salary_rate);
//
//        hourlySalaryField = new JTextField();
//        hourlySalaryField.setBounds(180, 170, 200, 20);
//        innerPanel.add(hourlySalaryField);
//        hourlySalaryField.setColumns(10);
//
//        JLabel lbl_employment_date = new JLabel("Employment Date:");
//        lbl_employment_date.setBounds(20, 200, 150, 20);
//        innerPanel.add(lbl_employment_date);
//
//        employmentDateField = new JTextField();
//        employmentDateField.setBounds(180, 200, 200, 20);
//        innerPanel.add(employmentDateField);
//        employmentDateField.setColumns(10);
//
//        JLabel lbl_employment_conditions = new JLabel("Employment Conditions:");
//        lbl_employment_conditions.setBounds(20, 230, 150, 20);
//        innerPanel.add(lbl_employment_conditions);
//
//        employmentConditions = new JTextArea();
//        employmentConditions.setBounds(180, 230, 200, 80);
//        innerPanel.add(employmentConditions);
//
//        JLabel lbl_employee_details = new JLabel("Other Employee Details:");
//        lbl_employee_details.setBounds(20, 320, 150, 20);
//        innerPanel.add(lbl_employee_details);
//
//        employeeDetails = new JTextArea();
//        employeeDetails.setBounds(180, 320, 200, 80);
//        innerPanel.add(employeeDetails);
//
//        JButton btn_submit = new JButton("Submit");
//        btn_submit.addActionListener(e -> submitForm());
//        btn_submit.setBounds(180, 420, 100, 30);
//        innerPanel.add(btn_submit);
////
////        addItem(s1.getShortDescription());
////        addItem(s2.getShortDescription());
////        addItem(s3.getShortDescription());
////        addItem(s4.getShortDescription());
//
//        // Create the remove button
//        contentPanel.add(innerPanel,constraints);
//      /*  removeButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                showConfirmationDialog();
//            }
//        });*/
//
//
//
//         //Set up the confirmation dialog on window close
////        addWindowListener(new WindowAdapter() {
////            @Override
////            public void windowClosing(WindowEvent e) {
////                showConfirmationDialog();
////            }
////        });
//    }
//
//    private void submitForm() {
//        String name = nameField.getText();
//        String branchId = branchIdField.getText();
//        String employeeId = employeeIdField.getText();
//        String bankNumber = bankNumberField.getText();
//        String bankBranchNumber = bankBranchNumberField.getText();
//        String hourlySalary = hourlySalaryField.getText();
//        String employmentDate = employmentDateField.getText();
//        String conditions = employmentConditions.getText();
//        String details = employeeDetails.getText();
//
//        // TODO: Process the form data (e.g., save to database, perform calculations, etc.)
//
//        // Display a message indicating successful submission
//        JOptionPane.showMessageDialog(newOpenWindow, "Form submitted successfully!");
//    }

//    private void showConfirmationDialog() {
//        int[] selectedIndices = list.getSelectedIndices();
//        if (selectedIndices.length > 0) {
//            int choice = JOptionPane.showConfirmDialog(newOpenPanel, "Are you sure you want to remove the selected item?", "Confirmation", JOptionPane.YES_NO_OPTION);
//            if (choice == JOptionPane.YES_OPTION) {
//                // TODO: Configure the item removal here
//                newOpenWindow.dispose();
//            }
//        }
//    }
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

//    @Override
//    public void componentResized(Dimension newSize) {
//        super.componentResized(newSize);
//        Dimension contentPreferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight() * 0.6));
//        contentPanel.setPreferredSize(new Dimension(contentPreferredSize.width, contentPreferredSize.height + 250));
//        Dimension preferredSize = new Dimension((int) (scrollPane.getWidth() * 0.6), (int) (scrollPane.getHeight() * 0.8));
//        listPanel.setPreferredSize(preferredSize);
//        listPanel.revalidate();
//        scrollPane.revalidate();
//    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
