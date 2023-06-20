package presentationLayer.gui.employeeModule.view.panels.shifts;


import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plUtils.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ViewShiftsPanel extends AbstractTransportModulePanel {
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    JTable calendarTable;
    JLabel monthLabel;

    public ViewShiftsPanel(ShiftsControl control) {
        super(control);
        init();
    }

    private void init() {
        // TODO: Swap all this example implementation
//        Searchable s1 = new Searchable() {
//            final String description = "Shift 1";
//            @Override
//            public boolean isMatch(String query) {
//                return description.toLowerCase().contains(query.toLowerCase());
//            }
//
//            @Override
//            public String getShortDescription() {
//                return description;
//            }
//
//            @Override
//            public String getLongDescription() {
//                return "Shift ID : 1\n" +
//                        "Shift Date : 12/12/2023\n";
//            }
//
//        };
//
//        Searchable s2 = new Searchable() {
//            final String description = "Shift 2";
//            @Override
//            public boolean isMatch(String query) {
//                return description.contains(query);
//            }
//
//            @Override
//            public String getShortDescription() {
//                return description;
//            }
//
//            @Override
//            public String getLongDescription() {
//                return "Shift ID : 2\n" +
//                        "Shift Date : 13/12/2023\n";
//            }
//        };
//
//        Searchable s3 = new Searchable() {
//            final String description = "Shift 3";
//            @Override
//            public boolean isMatch(String query) {
//                return description.contains(query);
//            }
//
//            @Override
//            public String getShortDescription() {
//                return description;
//            }
//
//            @Override
//            public String getLongDescription() {
//                return "Shift ID : 3\n" +
//                        "Shift Date : 13/12/2023\n";
//            }
//        };
//
//        Searchable s4 = new Searchable() {
//            final String description = "Shift 4";
//            @Override
//            public boolean isMatch(String query) {
//                return description.contains(query);
//            }
//
//            @Override
//            public String getShortDescription() {
//                return description;
//            }
//
//            @Override
//            public String getLongDescription() {
//                return "Shift ID : 4\n" +
//                        "Shift Date : 13/12/2023\n";
//            }
//        };
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
        JPanel innerPanel = new JPanel();
        innerPanel.setBackground(new Color(0,0,0,0));
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
//
//
//
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
        monthLabel.setBounds(100, 30, 600, 30);
        innerPanel.add(monthLabel);

        calendarTable = new JTable();
        calendarTable.setBounds(100, 80, 600, 400);
        calendarTable.setRowHeight(50);
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
        innerPanel.add(calendarTable);

        JButton prevButton = new JButton("<<");
        prevButton.addActionListener(e -> {
            // Go to the previous month
            // TODO: Implement the logic to load the shifts for the previous month
            // For this example, we are simply displaying the previous month's name

            displayPreviousMonth();
        });
        prevButton.setBounds(150, 500, 100, 30);
        innerPanel.add(prevButton);

        JButton nextButton = new JButton(">>");
        nextButton.addActionListener(e -> {
            // Go to the next month
            // TODO: Implement the logic to load the shifts for the next month
            // For this example, we are simply displaying the next month's name

            displayNextMonth();
        });
        nextButton.setBounds(550, 500, 100, 30);
        innerPanel.add(nextButton);
        contentPanel.add(innerPanel,constraints);

        // Initially, display the current month's shifts
        displayCurrentMonth();
    }

    private void displayCurrentMonth() {
        Calendar calendar = new GregorianCalendar();
        displayMonth(calendar);
    }

    private void displayPreviousMonth() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, -1);
        displayMonth(calendar);
    }

    private void displayNextMonth() {
        Calendar calendar = new GregorianCalendar();
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

        Object[][] data = new Object[rows][cols];
        String[] columnNames = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        calendarTable.setModel(tableModel);

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
    }


//    private void removeSelectedItems(int selectedIndex) {
//            listModel.remove(selectedIndex);
//
//    }
//
//    public void addItem(String item) {
//        listModel.addElement(item);
//    }
//
//    private void showConfirmationDialog() {
//        int[] selectedIndices = list.getSelectedIndices();
//        if (selectedIndices.length > 0) {
//            int choice = JOptionPane.showConfirmDialog(newOpenPanel, "Are you sure you want to remove the selected item?", "Confirmation", JOptionPane.YES_NO_OPTION);
//            if (choice == JOptionPane.YES_OPTION) {
//                removeSelectedItems(selectedIndices[0]);
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

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension contentPreferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight() * 0.6));
        Dimension contentPanelSize = new Dimension(contentPreferredSize.width, contentPreferredSize.height + 250);
        contentPanel.setPreferredSize(contentPanelSize);
        calendarTable.setPreferredSize(new Dimension((int) (contentPanelSize.width*0.8), (int) (- 50 + 2 *contentPanelSize.height/3.0)));
        scrollPane.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
