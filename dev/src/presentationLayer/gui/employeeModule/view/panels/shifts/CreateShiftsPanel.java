package presentationLayer.gui.employeeModule.view.panels.shifts;


import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.employeeModule.view.HRManagerView;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateShiftsPanel extends AbstractTransportModulePanel {
    HRManagerView hrManagerView;
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    JTable calendarTable;
    JLabel monthLabel;
    Calendar calendar;

    public CreateShiftsPanel(ShiftsControl control) {
        super(control);
        init();
    }

    private void init() {
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel headerLabel = new JLabel("Select the starting day of the week shifts to create:");
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
                    showConfirmationDialog(LocalDate.of(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1, day));
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

    private void showConfirmationDialog(LocalDate weekStart) {
        int choice = JOptionPane.showConfirmDialog(contentPanel, "Are you sure you want to create the week shifts from the selected date?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            String result = ((ShiftsControl)control).createWeekShifts(weekStart);
            JOptionPane.showMessageDialog(contentPanel, result);
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

    @Override
    public void notify(ObservableModel observable) {

    }
}
