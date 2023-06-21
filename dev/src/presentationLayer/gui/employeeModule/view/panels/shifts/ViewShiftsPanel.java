package presentationLayer.gui.employeeModule.view.panels.shifts;


import businessLayer.employeeModule.Role;
import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.employeeModule.view.HRManagerView;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.Colors;
import serviceLayer.employeeModule.Objects.SEmployee;
import serviceLayer.employeeModule.Objects.SShift;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ViewShiftsPanel extends AbstractTransportModulePanel {
    HRManagerView hrManagerView;
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    JTable calendarTable;
    JLabel monthLabel;
    Calendar calendar;
    JRadioButton morningRadioButton, eveningRadioButton;
    ButtonGroup buttonGroup;

    public ViewShiftsPanel(ShiftsControl control, HRManagerView hrManagerView) {
        super(control);
        this.hrManagerView = hrManagerView;
        init();
    }

    private void init() {
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel headerLabel = new JLabel("Select the shift day to show:");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        contentPanel.add(headerLabel, constraints);

        monthLabel = new JLabel("");
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        contentPanel.add(monthLabel, constraints);

        calendarTable = new JTable();
        calendarTable.setRowHeight(50);
        calendarTable.setDefaultEditor(Object.class, null);
        calendarTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (row == 0 || column == 0) {
                    cell.setBackground(Color.LIGHT_GRAY);
                } else {
                    cell.setBackground(Color.WHITE);
                }

                return cell;
            }
        });

        calendarTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check for double-click event
                if (e.getClickCount() == 2) {
                    // Get the selected cell coordinates
                    int row = calendarTable.getSelectedRow();
                    int col = calendarTable.getSelectedColumn();

                    // Get the day value from the selected cell
                    int day = (int)calendarTable.getValueAt(row, col);
                    if (newOpenWindow != null) {
                        newOpenWindow.dispose();
                        newOpenWindow = null;
                    }
                    openNewWindow(((ShiftsControl)control).getShifts(LocalDate.of(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1, day)));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(calendarTable);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(scrollPane, constraints);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.SOUTH;
        contentPanel.add(buttonPanel, constraints);

        JButton prevButton = new JButton("<<");
        prevButton.addActionListener(e -> {
            displayPreviousMonth();
        });
        buttonPanel.add(prevButton);

        JButton nextButton = new JButton(">>");
        nextButton.addActionListener(e -> {
            displayNextMonth();
        });
        buttonPanel.add(nextButton);

        // Initialize the calendar instance and display the current month's shifts
        calendar = new GregorianCalendar();
        displayMonth(calendar);
    }

    @Override
    protected void clearFields() {
        
    }

    private void displayPreviousMonth() {
        calendar.add(Calendar.MONTH, -1);
        displayMonth(calendar);
    }

    private void displayNextMonth() {
        calendar.add(Calendar.MONTH, 1);
        displayMonth(calendar);
    }

    private void displayMonth(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DateFormatSymbols dfs = new DateFormatSymbols();
        String monthName = dfs.getMonths()[month];

        monthLabel.setText(monthName + " " + year);

        int startDay = calendar.get(Calendar.DAY_OF_WEEK);
        int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int rows = (int) Math.ceil((startDay + numberOfDays - 1) / 7.0);
        int cols = 7;

        DefaultTableModel tableModel = (DefaultTableModel) calendarTable.getModel();
        tableModel.setRowCount(0); // Clear the table's data model

        Object[][] data = new Object[rows][cols];
        String[] columnNames = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        tableModel.setDataVector(data, columnNames); // Set the new data in the table's data model

        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        defaultRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        calendarTable.setDefaultRenderer(Object.class, defaultRenderer);

        int row = 0;
        int col = startDay - 1;

        for (int day = 1; day <= numberOfDays; day++) {
            if (col == 7) {
                col = 0;
                row++;
            }
            tableModel.setValueAt(day, row, col);
            col++;
        }
        calendarTable.getColumnModel().getColumn(0).setPreferredWidth(50); // Adjust width of the first column

        // Simulates a button click on the panel to reload the calendar table
        try {
            Robot robot = new Robot();
            // Get current mouse position
            Point originalMousePos = MouseInfo.getPointerInfo().getLocation();
            // Calculate new click position
            int offsetX = 160;
            int offsetY = 0;
            int clickX = originalMousePos.x + offsetX;
            int clickY = originalMousePos.y + offsetY;

            // Move the mouse cursor to the new position
            robot.mouseMove(clickX, clickY);
            // Simulate left mouse button press
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            // Simulate left mouse button release
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            // Return the mouse cursor to its original position
            robot.mouseMove(originalMousePos.x, originalMousePos.y);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationDialog(SShift shift) {
        int choice = JOptionPane.showConfirmDialog(newOpenPanel, "Are you sure you want to remove the selected shift?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            String errorMessage = ((ShiftsControl)control).deleteShift(shift);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(null,errorMessage);
            }
            newOpenWindow.dispose();
        }
    }

    private void openNewWindow(Object dayShifts) {
        newOpenWindow = new JFrame();
        newOpenWindow.setSize(900, 650);
        newOpenWindow.setLocationRelativeTo(contentPanel);
        newOpenWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        newOpenWindow.getContentPane();
        newOpenPanel.setLayout(null);

        if (dayShifts instanceof SShift[] shifts){
            GridLayout g = new GridLayout(0,2);
            g.setHgap(-300);
            JPanel morningShiftPanel = new JPanel(g);
            morningShiftPanel.setBackground(new Color(255,255,255));

            JPanel eveningShiftPanel = new JPanel(g);
            eveningShiftPanel.setBackground(new Color(255,255,255));

            JScrollPane morningShiftScrollPane = new JScrollPane(morningShiftPanel);
            morningShiftScrollPane.setBounds(50, 50, 800, 200);
            morningShiftScrollPane.getVerticalScrollBar().setUnitIncrement(20);
            morningShiftScrollPane.getVerticalScrollBar().setBlockIncrement(100);
            JScrollPane eveningShiftScrollPane = new JScrollPane(eveningShiftPanel);
            eveningShiftScrollPane.setBounds(50, 300, 800, 200);
            eveningShiftScrollPane.getVerticalScrollBar().setUnitIncrement(20);
            eveningShiftScrollPane.getVerticalScrollBar().setBlockIncrement(100);

            JLabel morningShiftLabel = new JLabel("Morning Shift:");
            JLabel eveningShiftLabel = new JLabel("Evening Shift:");
            morningShiftLabel.setFont(new Font("Arial", Font.BOLD, 20));
            morningShiftLabel.setForeground(Colors.getForegroundColor());
            morningShiftLabel.setBounds(50, 50, 800, 50);

            morningRadioButton = new JRadioButton("Morning");
            if (shifts[0] != null) {
                // Shift date and type
                JLabel shiftDateTypeLabel = new JLabel("ShiftDate,Type:");
                shiftDateTypeLabel.setFont(new Font("Arial", Font.BOLD, 20));
                shiftDateTypeLabel.setForeground(Colors.getForegroundColor());
                morningShiftPanel.add(shiftDateTypeLabel);

                JLabel shiftDateTypeDescription = new JLabel(shifts[0].getShiftDate().toString() + " " + shifts[0].getShiftType());
                shiftDateTypeDescription.setFont(new Font("Arial", Font.BOLD, 20));
                shiftDateTypeDescription.setForeground(Colors.getForegroundColor());
                morningShiftPanel.add(shiftDateTypeDescription);

                // Needed Roles
                JLabel neededRolesLabel = new JLabel("Needed Roles:");
                neededRolesLabel.setFont(new Font("Arial", Font.BOLD, 20));
                neededRolesLabel.setForeground(Colors.getForegroundColor());
                morningShiftPanel.add(neededRolesLabel);

                DefaultListModel<String> neededRolesListModel = new DefaultListModel<>();
                for(Map.Entry<String,Integer> neededRole : shifts[0].getNeededRoles().entrySet()) {
                    neededRolesListModel.addElement(neededRole.getKey() + ": " + neededRole.getValue().toString());
                }
                JList<String> neededRolesList = new JList<String>(neededRolesListModel);
                morningShiftPanel.add(neededRolesList);

                // Shift Requests
                JLabel requestsLabel = new JLabel("Requests:");
                requestsLabel.setFont(new Font("Arial", Font.BOLD, 20));
                requestsLabel.setForeground(Colors.getForegroundColor());
                morningShiftPanel.add(requestsLabel);

                JPanel requestsPanel = new JPanel(new GridLayout(0,2));
                for (Role role : Role.values()) {
                    JLabel roleLabel = new JLabel(role.toString());
                    DefaultListModel<String> requestsInRoleListModel = new DefaultListModel<>();
                    if (shifts[0].getShiftRequestsEmployees(role.toString()) != null) {
                        requestsPanel.add(roleLabel);
                        for (SEmployee employee : shifts[0].getShiftRequestsEmployees(role.toString())) {
                            requestsInRoleListModel.addElement(employee.getId());
                        }
                        JList<String> neededInRole = new JList<String>(requestsInRoleListModel);
                        requestsPanel.add(neededInRole);
                    }
                }
                morningShiftPanel.add(requestsPanel);

                // Shift Workers
                JLabel workersLabel = new JLabel("Workers:");
                workersLabel.setFont(new Font("Arial", Font.BOLD, 20));
                workersLabel.setForeground(Colors.getForegroundColor());
                morningShiftPanel.add(workersLabel);

                JPanel workersPanel = new JPanel(new GridLayout(0,2));
                for (Role role : Role.values()) {
                    JLabel roleLabel = new JLabel(role.toString());
                    DefaultListModel<String> requestsInRoleListModel = new DefaultListModel<>();
                    if (shifts[0].getShiftWorkersEmployees(role.toString()) != null) {
                        workersPanel.add(roleLabel);
                        for (SEmployee employee : shifts[0].getShiftWorkersEmployees(role.toString())) {
                            requestsInRoleListModel.addElement(employee.getId());
                        }
                        JList<String> neededInRole = new JList<String>(requestsInRoleListModel);
                        workersPanel.add(neededInRole);
                    }
                }
                morningShiftPanel.add(workersPanel);

                // Cancel Card Applies
                JLabel cancelCardAppliesLabel = new JLabel("Cancel Card Applies:");
                cancelCardAppliesLabel.setFont(new Font("Arial", Font.BOLD, 20));
                cancelCardAppliesLabel.setForeground(Colors.getForegroundColor());
                morningShiftPanel.add(cancelCardAppliesLabel);

                DefaultListModel<String> cancelCardAppliesListModel = new DefaultListModel<>();
                for(String cancelCartApply : shifts[0].getCancelCardApplies()) {
                    cancelCardAppliesListModel.addElement(cancelCartApply);
                }
                JList<String> cancelCartAppliesList = new JList<String>(cancelCardAppliesListModel);
                morningShiftPanel.add(cancelCartAppliesList);

                // Shift Activities
                JLabel shiftActivitiesLabel = new JLabel("Shift Activities:");
                shiftActivitiesLabel.setFont(new Font("Arial", Font.BOLD, 20));
                shiftActivitiesLabel.setForeground(Colors.getForegroundColor());
                morningShiftPanel.add(shiftActivitiesLabel);

                DefaultListModel<String> shiftActivitiesListModel = new DefaultListModel<>();
                for(String shiftActivity : shifts[0].getShiftActivities()) {
                    shiftActivitiesListModel.addElement(shiftActivity);
                }
                JList<String> shiftActivitiesList = new JList<String>(shiftActivitiesListModel);
                morningShiftPanel.add(shiftActivitiesList);

                // Is Approved
                JLabel approvedLabel = new JLabel("Approved:");
                approvedLabel.setFont(new Font("Arial", Font.BOLD, 20));
                approvedLabel.setForeground(Colors.getForegroundColor());
                morningShiftPanel.add(approvedLabel);

                JLabel approvedDescription = new JLabel(String.valueOf(shifts[0].isApproved()));
                approvedDescription.setFont(new Font("Arial", Font.BOLD, 20));
                approvedDescription.setForeground(Colors.getForegroundColor());
                morningShiftPanel.add(approvedDescription);
            }
            if (shifts[1] != null) {
                // Shift date and type
                JLabel shiftDateTypeLabel = new JLabel("ShiftDate,Type:");
                shiftDateTypeLabel.setFont(new Font("Arial", Font.BOLD, 20));
                shiftDateTypeLabel.setForeground(Colors.getForegroundColor());
                eveningShiftPanel.add(shiftDateTypeLabel);

                JLabel shiftDateTypeDescription = new JLabel(shifts[1].getShiftDate().toString() + " " + shifts[1].getShiftType());
                shiftDateTypeDescription.setFont(new Font("Arial", Font.BOLD, 20));
                shiftDateTypeDescription.setForeground(Colors.getForegroundColor());
                eveningShiftPanel.add(shiftDateTypeDescription);

                // Needed Roles
                JLabel neededRolesLabel = new JLabel("Needed Roles:");
                neededRolesLabel.setFont(new Font("Arial", Font.BOLD, 20));
                neededRolesLabel.setForeground(Colors.getForegroundColor());
                eveningShiftPanel.add(neededRolesLabel);

                DefaultListModel<String> neededRolesListModel = new DefaultListModel<>();
                for(Map.Entry<String,Integer> neededRole : shifts[1].getNeededRoles().entrySet()) {
                    neededRolesListModel.addElement(neededRole.getKey() + ": " + neededRole.getValue().toString());
                }
                JList<String> neededRolesList = new JList<String>(neededRolesListModel);
                eveningShiftPanel.add(neededRolesList);

                // Shift Requests
                JLabel requestsLabel = new JLabel("Requests:");
                requestsLabel.setFont(new Font("Arial", Font.BOLD, 20));
                requestsLabel.setForeground(Colors.getForegroundColor());
                eveningShiftPanel.add(requestsLabel);

                JPanel requestsPanel = new JPanel(new GridLayout(0,2));
                for (Role role : Role.values()) {
                    JLabel roleLabel = new JLabel(role.toString());
                    DefaultListModel<String> requestsInRoleListModel = new DefaultListModel<>();
                    if (shifts[1].getShiftRequestsEmployees(role.toString()) != null) {
                        requestsPanel.add(roleLabel);
                        for (SEmployee employee : shifts[1].getShiftRequestsEmployees(role.toString())) {
                            requestsInRoleListModel.addElement(employee.getId());
                        }
                        JList<String> neededInRole = new JList<String>(requestsInRoleListModel);
                        requestsPanel.add(neededInRole);
                    }
                }
                eveningShiftPanel.add(requestsPanel);

                // Shift Workers
                JLabel workersLabel = new JLabel("Workers:");
                workersLabel.setFont(new Font("Arial", Font.BOLD, 20));
                workersLabel.setForeground(Colors.getForegroundColor());
                eveningShiftPanel.add(workersLabel);

                JPanel workersPanel = new JPanel(new GridLayout(0,2));
                for (Role role : Role.values()) {
                    JLabel roleLabel = new JLabel(role.toString());
                    DefaultListModel<String> requestsInRoleListModel = new DefaultListModel<>();
                    if (shifts[1].getShiftWorkersEmployees(role.toString()) != null) {
                        workersPanel.add(roleLabel);
                        for (SEmployee employee : shifts[1].getShiftWorkersEmployees(role.toString())) {
                            requestsInRoleListModel.addElement(employee.getId());
                        }
                        JList<String> neededInRole = new JList<String>(requestsInRoleListModel);
                        workersPanel.add(neededInRole);
                    }
                }
                eveningShiftPanel.add(workersPanel);

                // Cancel Card Applies
                JLabel cancelCardAppliesLabel = new JLabel("Cancel Card Applies:");
                cancelCardAppliesLabel.setFont(new Font("Arial", Font.BOLD, 20));
                cancelCardAppliesLabel.setForeground(Colors.getForegroundColor());
                eveningShiftPanel.add(cancelCardAppliesLabel);

                DefaultListModel<String> cancelCardAppliesListModel = new DefaultListModel<>();
                for(String cancelCartApply : shifts[1].getCancelCardApplies()) {
                    cancelCardAppliesListModel.addElement(cancelCartApply);
                }
                JList<String> cancelCartAppliesList = new JList<String>(cancelCardAppliesListModel);
                eveningShiftPanel.add(cancelCartAppliesList);

                // Shift Activities
                JLabel shiftActivitiesLabel = new JLabel("Shift Activities:");
                shiftActivitiesLabel.setFont(new Font("Arial", Font.BOLD, 20));
                shiftActivitiesLabel.setForeground(Colors.getForegroundColor());
                eveningShiftPanel.add(shiftActivitiesLabel);

                DefaultListModel<String> shiftActivitiesListModel = new DefaultListModel<>();
                for(String shiftActivity : shifts[1].getShiftActivities()) {
                    shiftActivitiesListModel.addElement(shiftActivity);
                }
                JList<String> shiftActivitiesList = new JList<String>(shiftActivitiesListModel);
                eveningShiftPanel.add(shiftActivitiesList);

                // Is Approved
                JLabel approvedLabel = new JLabel("Approved:");
                approvedLabel.setFont(new Font("Arial", Font.BOLD, 20));
                approvedLabel.setForeground(Colors.getForegroundColor());
                eveningShiftPanel.add(approvedLabel);

                JLabel approvedDescription = new JLabel(String.valueOf(shifts[1].isApproved()));
                approvedDescription.setFont(new Font("Arial", Font.BOLD, 20));
                approvedDescription.setForeground(Colors.getForegroundColor());
                eveningShiftPanel.add(approvedDescription);
            }
            eveningRadioButton = new JRadioButton("Evening");
            buttonGroup = new ButtonGroup();
            buttonGroup.add(morningRadioButton);
            buttonGroup.add(eveningRadioButton);

            morningRadioButton.setBounds(20,135,20,20);

            eveningShiftLabel.setFont(new Font("Arial", Font.BOLD, 20));
            eveningShiftLabel.setForeground(Colors.getForegroundColor());
            eveningShiftLabel.setBounds(50, 300, 800, 350);

            eveningRadioButton.setBounds(20,390,20,20);

            JButton deleteButton = new JButton("Delete");
            deleteButton.setPreferredSize(new Dimension(100, 30));
            deleteButton.setBounds(550, 550, 100, 30);
            JButton updateButton = new JButton("Update");
            updateButton.setPreferredSize(new Dimension(100, 30));
            updateButton.setBounds(100, 550, 100, 30);

            newOpenPanel.add(morningShiftScrollPane);
            newOpenPanel.add(eveningShiftScrollPane);
            newOpenPanel.add(morningRadioButton);
            newOpenPanel.add(eveningRadioButton);
            newOpenPanel.add(deleteButton);
            newOpenPanel.add(updateButton);
            newOpenPanel.revalidate();
            newOpenPanel.repaint();
            contentPanel.revalidate();
            contentPanel.repaint();

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (morningRadioButton.isSelected()) {
                        showConfirmationDialog(shifts[0]);
                    } else if (eveningRadioButton.isSelected()) {
                        showConfirmationDialog(shifts[1]);
                    } else {
                        JOptionPane.showMessageDialog(null,"Please choose one of the shifts.");
                    }
                }
            });

            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateButtonClicked(shifts);
                }
            });
        }
        else if (dayShifts instanceof String) {
            JTextArea errorTextArea = new JTextArea("Error: \n" + dayShifts);
            errorTextArea.setFont(new Font("Arial", Font.BOLD, 20));
            errorTextArea.setForeground(Colors.getForegroundColor());
            errorTextArea.setBounds(50, 50, 800, 200);
            newOpenWindow.add(errorTextArea);
        } else {
            JTextArea errorTextArea = new JTextArea("An error has occurred when retrieving the shifts in this day.\n");
            errorTextArea.setFont(new Font("Arial", Font.BOLD, 20));
            errorTextArea.setForeground(Colors.getForegroundColor());
            errorTextArea.setBounds(50, 50, 800, 200);
            newOpenWindow.add(errorTextArea);
        }
        newOpenWindow.add(newOpenPanel);
        newOpenWindow.revalidate();
        newOpenWindow.repaint();
        newOpenWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int month = calendar.get(Calendar.MONTH);
                hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                calendar.set(Calendar.MONTH, month);
//                displayMonth(calendar);
            }
        });
        newOpenWindow.setVisible(true);
    }

    private void updateButtonClicked(SShift[] shifts) {
        String[] updateOptions = {"Needed Roles", "Shift Workers", "Approve"};
        String selectedOption = (String) JOptionPane.showInputDialog(contentPanel, "Select Update Option:", "Update Selection", JOptionPane.PLAIN_MESSAGE, null, updateOptions, updateOptions[0]);
        if (selectedOption == null) {
            JOptionPane.showMessageDialog(null, "Invalid Update Option", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (selectedOption.equals("Needed Roles")) {
            if (morningRadioButton.isSelected()) {
                if (shifts[0] == null) {
                    JOptionPane.showMessageDialog(null,"Cannot choose a non existent shift.");
                } else {
                    // Create an array of button text
                    String[] buttonText = {"Modify", "Add", "Remove"};

                    // Set the custom button text
                    UIManager.put("OptionPane.yesButtonText", buttonText[0]);
                    UIManager.put("OptionPane.noButtonText", buttonText[1]);
                    UIManager.put("OptionPane.cancelButtonText", buttonText[2]);
                    // Show the option pane with custom button text
                    int choice = JOptionPane.showConfirmDialog(null, "Choose update option:", "Update Option", JOptionPane.YES_NO_CANCEL_OPTION);
                    UIManager.put("OptionPane.yesButtonText", null);
                    UIManager.put("OptionPane.noButtonText", null);
                    UIManager.put("OptionPane.cancelButtonText", null);
                    // Get the selected button index
                    // Modify / Add / Remove
                    if (choice == JOptionPane.YES_OPTION) { // Modify
                        Map<String, Integer> neededRoles = shifts[0].getNeededRoles();
                        List<String> neededRolesList = neededRoles.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
                        String neededRoleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Needed Role:", "Update Selection", JOptionPane.PLAIN_MESSAGE, null, neededRolesList.toArray(), neededRolesList.toArray()[0]);
                        int numberSelection = Integer.parseInt((String) JOptionPane.showInputDialog(contentPanel, "Select Needed Amount:"));
                        String result = ((ShiftsControl) control).setShiftNeededAmount(shifts[0].getShiftDate(), shifts[0].getShiftType(), neededRoleSelection, numberSelection);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    } else if (choice == JOptionPane.NO_OPTION) { // Add
                        List<String> neededRolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String neededRoleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Needed Role:", "Update Selection", JOptionPane.PLAIN_MESSAGE, null, neededRolesList.toArray(), neededRolesList.toArray()[0]);
                        int numberSelection = Integer.parseInt((String) JOptionPane.showInputDialog(contentPanel, "Select Needed Amount:"));
                        String result = ((ShiftsControl) control).setShiftNeededAmount(shifts[0].getShiftDate(), shifts[0].getShiftType(), neededRoleSelection, numberSelection);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    } else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) { // Remove
                        List<String> neededRolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String neededRoleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Needed Role:", "Update Selection", JOptionPane.PLAIN_MESSAGE, null, neededRolesList.toArray(), neededRolesList.toArray()[0]);
                        String result = ((ShiftsControl) control).setShiftNeededAmount(shifts[0].getShiftDate(), shifts[0].getShiftType(), neededRoleSelection, 0);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    }
                }
            } else if (eveningRadioButton.isSelected()) {
                if (shifts[1] == null) {
                    JOptionPane.showMessageDialog(null,"Cannot choose a non existent shift.");
                } else {
                    // Create an array of button text
                    String[] buttonText = {"Modify", "Add", "Remove"};

                    // Set the custom button text
                    UIManager.put("OptionPane.yesButtonText", buttonText[0]);
                    UIManager.put("OptionPane.noButtonText", buttonText[1]);
                    UIManager.put("OptionPane.cancelButtonText", buttonText[2]);
                    // Show the option pane with custom button text
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to proceed?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                    UIManager.put("OptionPane.yesButtonText", null);
                    UIManager.put("OptionPane.noButtonText", null);
                    UIManager.put("OptionPane.cancelButtonText", null);
                    // Get the selected button index
                    // Modify / Add / Remove
                    if (choice == JOptionPane.YES_OPTION) { // Modify
                        Map<String, Integer> neededRoles = shifts[1].getNeededRoles();
                        List<String> neededRolesList = neededRoles.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
                        String neededRoleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Needed Role:", "Modify Selection", JOptionPane.PLAIN_MESSAGE, null, neededRolesList.toArray(), neededRolesList.toArray()[0]);
                        int numberSelection = Integer.parseInt((String) JOptionPane.showInputDialog(contentPanel, "Select Needed Amount:"));
                        String result = ((ShiftsControl) control).setShiftNeededAmount(shifts[1].getShiftDate(), shifts[1].getShiftType(), neededRoleSelection, numberSelection);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    } else if (choice == JOptionPane.NO_OPTION) { // Add
                        List<String> neededRolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String neededRoleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Needed Role:", "Add Selection", JOptionPane.PLAIN_MESSAGE, null, neededRolesList.toArray(), neededRolesList.toArray()[0]);
                        int numberSelection = Integer.parseInt((String) JOptionPane.showInputDialog(contentPanel, "Select Needed Amount:"));
                        String result = ((ShiftsControl) control).setShiftNeededAmount(shifts[1].getShiftDate(), shifts[1].getShiftType(), neededRoleSelection, numberSelection);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    } else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) { // Remove
                        List<String> neededRolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String neededRoleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Needed Role:", "Remove Selection", JOptionPane.PLAIN_MESSAGE, null, neededRolesList.toArray(), neededRolesList.toArray()[0]);
                        String result = ((ShiftsControl) control).setShiftNeededAmount(shifts[1].getShiftDate(), shifts[1].getShiftType(), neededRoleSelection, 0);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null,"Please choose one of the shifts.");
            }
        }
        else if (selectedOption.equals("Shift Workers")) {
            if (morningRadioButton.isSelected()) {
                if (shifts[0] == null) {
                    JOptionPane.showMessageDialog(null,"Cannot choose a non existent shift.");
                } else {
                    // Create an array of button text
                    String[] buttonText = {"Modify", "Add", "Remove"};

                    // Set the custom button text
                    UIManager.put("OptionPane.yesButtonText", buttonText[0]);
                    UIManager.put("OptionPane.noButtonText", buttonText[1]);
                    UIManager.put("OptionPane.cancelButtonText", buttonText[2]);
                    // Show the option pane with custom button text
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to proceed?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                    UIManager.put("OptionPane.yesButtonText", null);
                    UIManager.put("OptionPane.noButtonText", null);
                    UIManager.put("OptionPane.cancelButtonText", null);
                    // Get the selected button index
                    // Modify / Add / Remove
                    if (choice == JOptionPane.YES_OPTION) { // Modify
                        List<String> rolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String roleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Role:", "Modify Selection", JOptionPane.PLAIN_MESSAGE, null, rolesList.toArray(), rolesList.toArray()[0]);
                        List<String> requestingWorkerIds = shifts[0].getShiftRequestsEmployees(roleSelection).stream().map(SEmployee::getId).toList();
                        String requestingWorkersString = String.join("\n", requestingWorkerIds);
                        String requestingWorkersSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Workers:\n" + requestingWorkersString);
                        List<String> selectedWorkerIds = Arrays.asList(requestingWorkersSelection.split(" ", -1));
                        String result = ((ShiftsControl) control).setShiftWorkers(shifts[0].getShiftDate(), shifts[0].getShiftType(), roleSelection, selectedWorkerIds);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    } else if (choice == JOptionPane.NO_OPTION) { // Add
                        List<String> rolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String roleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Role:", "Add Selection", JOptionPane.PLAIN_MESSAGE, null, rolesList.toArray(), rolesList.toArray()[0]);
                        List<String> requestingWorkerIds = shifts[0].getShiftRequestsEmployees(roleSelection).stream().map(SEmployee::getId).toList();
                        String requestingWorkersString = String.join("\n", requestingWorkerIds);
                        String requestingWorkersSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Workers:\n" + requestingWorkersString);
                        List<String> selectedWorkerIds = Arrays.asList(requestingWorkersSelection.split(" ", -1));
                        String result = ((ShiftsControl) control).setShiftWorkers(shifts[0].getShiftDate(), shifts[0].getShiftType(), roleSelection, selectedWorkerIds);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    } else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) { // Remove
                        List<String> rolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String roleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Role:", "Remove Selection", JOptionPane.PLAIN_MESSAGE, null, rolesList.toArray(), rolesList.toArray()[0]);
                        String result = ((ShiftsControl) control).setShiftWorkers(shifts[0].getShiftDate(), shifts[0].getShiftType(), roleSelection, new ArrayList<String>());
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    }
                }
            } else if (eveningRadioButton.isSelected()) {
                if (shifts[1] == null) {
                    JOptionPane.showMessageDialog(null,"Cannot choose a non existent shift.");
                } else {
                    // Create an array of button text
                    String[] buttonText = {"Modify", "Add", "Remove"};

                    // Set the custom button text
                    UIManager.put("OptionPane.yesButtonText", buttonText[0]);
                    UIManager.put("OptionPane.noButtonText", buttonText[1]);
                    UIManager.put("OptionPane.cancelButtonText", buttonText[2]);
                    // Show the option pane with custom button text
                    int choice = JOptionPane.showConfirmDialog(null, "Choose update option:", "Update Option", JOptionPane.YES_NO_CANCEL_OPTION);
                    UIManager.put("OptionPane.yesButtonText", null);
                    UIManager.put("OptionPane.noButtonText", null);
                    UIManager.put("OptionPane.cancelButtonText", null);
                    // Get the selected button index
                    // Modify / Add / Remove
                    if (choice == JOptionPane.YES_OPTION) { // Modify
                        List<String> rolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String roleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Role:", "Modify Selection", JOptionPane.PLAIN_MESSAGE, null, rolesList.toArray(), rolesList.toArray()[0]);
                        List<String> requestingWorkerIds = shifts[1].getShiftRequestsEmployees(roleSelection).stream().map(SEmployee::getId).toList();
                        String requestingWorkersString = String.join("\n", requestingWorkerIds);
                        String requestingWorkersSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Workers:\n" + requestingWorkersString);
                        List<String> selectedWorkerIds = Arrays.asList(requestingWorkersSelection.split(" ", -1));
                        String result = ((ShiftsControl) control).setShiftWorkers(shifts[1].getShiftDate(), shifts[1].getShiftType(), roleSelection, selectedWorkerIds);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    } else if (choice == JOptionPane.NO_OPTION) { // Add
                        List<String> rolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String roleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Role:", "Add Selection", JOptionPane.PLAIN_MESSAGE, null, rolesList.toArray(), rolesList.toArray()[0]);
                        List<String> requestingWorkerIds = shifts[1].getShiftRequestsEmployees(roleSelection).stream().map(SEmployee::getId).toList();
                        String requestingWorkersString = String.join("\n", requestingWorkerIds);
                        String requestingWorkersSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Workers:\n" + requestingWorkersString);
                        List<String> selectedWorkerIds = Arrays.asList(requestingWorkersSelection.split(" ", -1));
                        String result = ((ShiftsControl) control).setShiftWorkers(shifts[1].getShiftDate(), shifts[1].getShiftType(), roleSelection, selectedWorkerIds);
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    } else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) { // Remove
                        List<String> rolesList = Arrays.stream(Role.values()).map(Enum::toString).toList();
                        String roleSelection = (String) JOptionPane.showInputDialog(contentPanel, "Select Shift Role:", "Remove Selection", JOptionPane.PLAIN_MESSAGE, null, rolesList.toArray(), rolesList.toArray()[0]);
                        String result = ((ShiftsControl) control).setShiftWorkers(shifts[1].getShiftDate(), shifts[1].getShiftType(), roleSelection, new ArrayList<String>());
                        JOptionPane.showMessageDialog(contentPanel, result);
                        int month = calendar.get(Calendar.MONTH);
                        hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                        hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                        calendar.set(Calendar.MONTH, month);
//                        displayMonth(calendar);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null,"Please choose one of the shifts.");
            }
        }
        else if (selectedOption.equals("Approve")) {
            if (morningRadioButton.isSelected()) {
                if (shifts[0] == null) {
                    JOptionPane.showMessageDialog(null,"Cannot choose a non existent shift.");
                } else {
                    String result = ((ShiftsControl) control).approveShift(shifts[0].getShiftDate(), shifts[0].getShiftType());
                    JOptionPane.showMessageDialog(contentPanel, result);
                    int month = calendar.get(Calendar.MONTH);
                    hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                    hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                    calendar.set(Calendar.MONTH, month);
//                    displayMonth(calendar);
                }
            } else if (eveningRadioButton.isSelected()) {
                if (shifts[1] == null) {
                    JOptionPane.showMessageDialog(null,"Cannot choose a non existent shift.");
                }
                else {
                    String result = ((ShiftsControl) control).approveShift(shifts[1].getShiftDate(), shifts[0].getShiftType());
                    JOptionPane.showMessageDialog(contentPanel, result);
                    int month = calendar.get(Calendar.MONTH);
                    hrManagerView.setCurrentPanel(new CreateShiftsPanel((ShiftsControl) control));
                    hrManagerView.setCurrentPanel(new ViewShiftsPanel((ShiftsControl) control, hrManagerView));
                    calendar.set(Calendar.MONTH, month);
//                    displayMonth(calendar);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Please choose one of the shifts.");
            }
        }
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension contentPreferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight() * 0.6));
        Dimension contentPanelSize = new Dimension(contentPreferredSize.width, contentPreferredSize.height + 250);
        contentPanel.setPreferredSize(contentPanelSize);
        scrollPane.revalidate();
    }

    
}
