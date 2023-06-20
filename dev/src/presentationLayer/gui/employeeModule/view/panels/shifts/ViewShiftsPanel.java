package presentationLayer.gui.employeeModule.view.panels.shifts;


import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.Colors;
import serviceLayer.employeeModule.Objects.SShift;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ViewShiftsPanel extends AbstractTransportModulePanel {
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    JTable calendarTable;
    JLabel monthLabel;
    Calendar calendar;

    public ViewShiftsPanel(ShiftsControl control) {
        super(control);
        init();
    }

    private void init() {
        // TODO: Swap all this example implementation
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
//        listModel = new DefaultListModel<>();
//
//        // Create the JList
//        list = new JList<>(listModel);
//        listPanel = new JScrollPane(list);
//
//        list.setBackground(new Color(0,0,0,0));
//        listPanel.setBorder(new Border() {
//            @Override
//            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//                Graphics2D g2 = (Graphics2D)g;
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2.setColor(Colors.getContentPanelColor());
//                g2.setStroke(new BasicStroke(1));
////                g2.drawArc(0,height-25,25,25,180,90);
//                g2.fillRect(0,0,10,height);
//                g2.fillRect(0,height-10,width,10);
//            }
//
//            @Override
//            public Insets getBorderInsets(Component c) {
//                return new Insets(0,20,20,20);
//            }
//
//            @Override
//            public boolean isBorderOpaque() {
//                return false;
//            }
//        });
//        list.setBorder(new EmptyBorder(0,20,20,20));
//        listPanel.setBackground(new Color(0,0,0,0));
//
//        list.setCellRenderer(new DefaultListCellRenderer() {
//            @Override
//            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                renderer.setBackground(new Color(0,0,0,0));
//                if(isSelected) {
//                    renderer.setBackground(new Color(200,200,200,128));
//                }
//                panel.repaint();
//                setBorder(new Border() {
//                    @Override
//                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//                        Graphics2D g2 = (Graphics2D) g;
//                        g2.setStroke(new BasicStroke(1));
//                        g2.setColor(new Color(200,200,200,100));
//                        g2.drawLine(0,height-1, width,height-1);
//                    }
//
//                    @Override
//                    public Insets getBorderInsets(Component c) {
//                        return new Insets(10,5,10,5);
//                    }
//
//                    @Override
//                    public boolean isBorderOpaque() {
//                        return false;
//                    }
//                });
//                return renderer;
//            }
//        });
//
//        list.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) { // Double-click event
//                    int selectedIndex = list.getSelectedIndex();
//                    if (selectedIndex != -1) {
//                        String selectedItem = listModel.getElementAt(selectedIndex);
//
//                        openNewWindow(selectedItem);
//                    }
//                }
//            }
//        });
//

//        innerPanel = new JPanel();
//        innerPanel.setBackground(new Color(0, 0, 0, 0));

//        innerPanel.add(listPanel);
//
//        addItem(s1.getShortDescription());
//        addItem(s2.getShortDescription());
//        addItem(s3.getShortDescription());
//        addItem(s4.getShortDescription());
//
//        // Create the remove button
//        //JButton removeButton = new JButton("Remove");
//        //removeButton.setPreferredSize(new Dimension(100, 30));
//        //innerPanel.add(removeButton,constraints);
//        contentPanel.add(innerPanel,constraints);
//      /*  removeButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                showConfirmationDialog();
//            }
//        });*/
//         //Set up the confirmation dialog on window close
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                showConfirmationDialog();
//            }
//        });

        monthLabel = new JLabel("");
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        constraints.gridx = 0;
        constraints.gridy = 0;
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
                    openNewWindow(((ShiftsControl)control).getShifts(LocalDate.of(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1, day)));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(calendarTable);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        contentPanel.add(scrollPane, constraints);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        constraints.gridx = 0;
        constraints.gridy = 2;
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
            int offsetX = 0;
            int offsetY = 100;
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


//    private void removeSelectedItems(int selectedIndex) {
//            listModel.remove(selectedIndex);
//    }
//
//    public void addItem(String item) {
//        listModel.addElement(item);
//    }
//
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
        newOpenWindow.setSize(800, 650);
        newOpenWindow.setLocationRelativeTo(contentPanel);
        newOpenWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //newOpenWindow.setLayout(new GridBagLayout());

        newOpenWindow.getContentPane();
        newOpenPanel.setLayout(null);

        if (dayShifts instanceof SShift[] shifts){
            JTextArea morningShiftTextArea = new JTextArea("Morning Shift: \n" + (shifts[0] != null ? shifts[0].toString() : "-"));
            JTextArea eveningShiftTextArea = new JTextArea("Evening Shift: \n" + (shifts[1] != null ? shifts[1].toString() : "-"));
            JRadioButton morningRadioButton = new JRadioButton("Morning");
            JRadioButton eveningRadioButton = new JRadioButton("Evening");
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(morningRadioButton);
            buttonGroup.add(eveningRadioButton);

            morningShiftTextArea.setFont(new Font("Arial", Font.BOLD, 20));
            morningShiftTextArea.setForeground(Colors.getForegroundColor());
            morningShiftTextArea.setBounds(50, 50, 800, 200);
            morningShiftTextArea.setEditable(false);
            morningRadioButton.setBounds(20,140,20,20);

            eveningShiftTextArea.setFont(new Font("Arial", Font.BOLD, 20));
            eveningShiftTextArea.setForeground(Colors.getForegroundColor());
            eveningShiftTextArea.setBounds(50, 300, 800, 200);
            eveningShiftTextArea.setEditable(false);
            eveningRadioButton.setBounds(20,390,20,20);

            //newOpenWindow.add(label);

            //Create the remove button
            //JPanel buttonPanel = new JPanel();
            JButton deleteButton = new JButton("Delete");
            deleteButton.setPreferredSize(new Dimension(100, 30));
            deleteButton.setBounds(550, 550, 100, 30);
            JButton updateButton = new JButton("Update");
            updateButton.setPreferredSize(new Dimension(100, 30));
            updateButton.setBounds(100, 550, 100, 30);

            newOpenPanel.add(morningShiftTextArea);
            newOpenPanel.add(eveningShiftTextArea);
            newOpenPanel.add(morningRadioButton);
            newOpenPanel.add(eveningRadioButton);
            newOpenPanel.add(deleteButton);
            newOpenPanel.add(updateButton);

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
                    // TODO: Open the update window

                }
            });
            //GridBagConstraints constraints = new GridBagConstraints();
            //constraints.fill = GridBagConstraints.WEST;
            //constraints.anchor = GridBagConstraints.PAGE_END;
            //newOpenPanel.add(buttonPanel);
            //buttonPanel.setLocation(0,20);
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
