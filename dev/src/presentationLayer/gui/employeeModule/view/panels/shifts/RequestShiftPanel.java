package presentationLayer.gui.employeeModule.view.panels.shifts;


import businessLayer.employeeModule.Role;
import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.employeeModule.view.EmployeeView;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.List;

public class RequestShiftPanel extends AbstractTransportModulePanel {
    EmployeeView employeeView;
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    JTable calendarTable;
    JLabel monthLabel;
    Calendar calendar;
    JRadioButton morningRadioButton, eveningRadioButton;
    ButtonGroup buttonGroup;

    String employeeId;

    public RequestShiftPanel(String employeeId, ShiftsControl control, EmployeeView employeeView) {
        super(control);
        this.employeeId = employeeId;
        this.employeeView = employeeView;
        init();
    }

    private void init() {
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel headerLabel = new JLabel("Select the day of shift to request:");
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
        List<String> roleOptions = Arrays.stream(Role.values()).map(Enum::toString).toList();
        String selectedRole = (String) JOptionPane.showInputDialog(contentPanel, "Select Role:", "Role Selection", JOptionPane.PLAIN_MESSAGE, null, roleOptions.toArray(), roleOptions.toArray()[0]);
        if (selectedRole == null) {
            JOptionPane.showMessageDialog(null, "Invalid role", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            String result = ((ShiftsControl) control).requestShift(employeeId, shift, selectedRole);
            if (result != null) {
                JOptionPane.showMessageDialog(null, result);
            }
        }
        newOpenWindow.dispose();
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

            JButton requestShiftButton = new JButton("Request Shift");
            requestShiftButton.setPreferredSize(new Dimension(200, 30));
            requestShiftButton.setBounds(350, 550, 200, 30);

            newOpenPanel.add(morningShiftScrollPane);
            newOpenPanel.add(eveningShiftScrollPane);
            newOpenPanel.add(morningRadioButton);
            newOpenPanel.add(eveningRadioButton);
            newOpenPanel.add(requestShiftButton);
            newOpenPanel.revalidate();
            newOpenPanel.repaint();
            contentPanel.revalidate();
            contentPanel.repaint();

            requestShiftButton.addActionListener(new ActionListener() {
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
        newOpenWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                int month = calendar.get(Calendar.MONTH);
                employeeView.setCurrentPanel(new CancelCardPanel(employeeId, (ShiftsControl)control, employeeView));
                employeeView.setCurrentPanel(new RequestShiftPanel(employeeId, (ShiftsControl) control, employeeView));
                calendar.set(Calendar.MONTH, month);
//                displayMonth(calendar);
            }
        });
        newOpenWindow.setVisible(true);
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension contentPreferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight() * 0.6));
        Dimension contentPanelSize = new Dimension(contentPreferredSize.width, contentPreferredSize.height + 250);
        contentPanel.setPreferredSize(contentPanelSize);
        scrollPane.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
