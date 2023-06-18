package presentationLayer.gui.employeeModule.view.panels.shifts;


import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.transportModule.control.TrucksControl;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ViewShiftsPanel extends AbstractTransportModulePanel {
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JScrollPane listPanel;
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;

    public ViewShiftsPanel(ShiftsControl control) {
        super(control);
        init();
    }

    private void init() {

        // Create the list model
        Searchable s1 = new Searchable() {
            final String description = "Shift 1";
            @Override
            public boolean isMatch(String query) {
                return description.toLowerCase().contains(query.toLowerCase());
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return "Shift ID : 1\n" +
                        "Shift Date : 12/12/2023\n";
            }

        };

        Searchable s2 = new Searchable() {
            final String description = "Shift 2";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return "Shift ID : 2\n" +
                        "Shift Date : 13/12/2023\n";
            }
        };

        Searchable s3 = new Searchable() {
            final String description = "Shift 3";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return "Shift ID : 3\n" +
                        "Shift Date : 13/12/2023\n";
            }
        };

        Searchable s4 = new Searchable() {
            final String description = "Shift 4";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return "Shift ID : 4\n" +
                        "Shift Date : 13/12/2023\n";
            }
        };
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        listModel = new DefaultListModel<>();

        // Create the JList
        list = new JList<>(listModel);
        listPanel = new JScrollPane(list);

        list.setBackground(new Color(0,0,0,0));
        listPanel.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Colors.getContentPanelColor());
                g2.setStroke(new BasicStroke(1));
//                g2.drawArc(0,height-25,25,25,180,90);
                g2.fillRect(0,0,10,height);
                g2.fillRect(0,height-10,width,10);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(0,20,20,20);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });
        list.setBorder(new EmptyBorder(0,20,20,20));
        listPanel.setBackground(new Color(0,0,0,0));

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setBackground(new Color(0,0,0,0));
                if(isSelected) {
                    renderer.setBackground(new Color(200,200,200,128));
                }
                panel.repaint();
                setBorder(new Border() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke(1));
                        g2.setColor(new Color(200,200,200,100));
                        g2.drawLine(0,height-1, width,height-1);
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(10,5,10,5);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return false;
                    }
                });
                return renderer;
            }
        });

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click event
                    int selectedIndex = list.getSelectedIndex();
                    if (selectedIndex != -1) {
                        String selectedItem = listModel.getElementAt(selectedIndex);

                        openNewWindow(selectedItem);
                    }
                }
            }
        });

        JPanel innerPanel = new JPanel();
        innerPanel.setBackground(new Color(0,0,0,0));
        innerPanel.add(listPanel);

        addItem(s1.getShortDescription());
        addItem(s2.getShortDescription());
        addItem(s3.getShortDescription());
        addItem(s4.getShortDescription());

        // Create the remove button
        //JButton removeButton = new JButton("Remove");
        //removeButton.setPreferredSize(new Dimension(100, 30));
        //innerPanel.add(removeButton,constraints);
        contentPanel.add(innerPanel,constraints);
      /*  removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showConfirmationDialog();
            }
        });*/



         //Set up the confirmation dialog on window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showConfirmationDialog();
            }
        });
    }

    private void removeSelectedItems(int selectedIndex) {
            listModel.remove(selectedIndex);

    }

    public void addItem(String item) {
        listModel.addElement(item);
    }

    private void showConfirmationDialog() {
        int[] selectedIndices = list.getSelectedIndices();
        if (selectedIndices.length > 0) {
            int choice = JOptionPane.showConfirmDialog(newOpenPanel, "Are you sure you want to remove the selected item?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                removeSelectedItems(selectedIndices[0]);
                newOpenWindow.dispose();
            }
        }
    }
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

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showConfirmationDialog();
            }
        });

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
        contentPanel.setPreferredSize(new Dimension(contentPreferredSize.width, contentPreferredSize.height + 250));
        Dimension preferredSize = new Dimension((int) (scrollPane.getWidth() * 0.6), (int) (scrollPane.getHeight() * 0.8));
        listPanel.setPreferredSize(preferredSize);
        listPanel.revalidate();
        scrollPane.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
