package presentationLayer.gui.transportModule.view.panels.trucks;


import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.PrettyScrollBar;
import presentationLayer.gui.transportModule.control.TrucksControl;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ViewTrucksPanel extends AbstractTransportModulePanel {
    private DefaultListModel<JPanel> listModel;
    private JList<JPanel> list;
    //private JScrollPane listPanel;
    //private JPanel cellPanel;
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;

    public ViewTrucksPanel(TrucksControl control) {
        super(control);
        init();
    }

    private void init() {

        // Create the list model
        Searchable s1 = new Searchable() {
            final String description = "Truck 1";
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
                return "Truck ID : 1\n" +
                        "License Plate : 123456\n" +
                        "Model : Toyota 2019\n" +
                        "Weight : 10000\n" ;
            }

        };

        Searchable s2 = new Searchable() {
            final String description = "Truck 2";
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
                return "Truck ID : 2\n" +
                        "License Plate : 123456\n" +
                        "Model : Toyota 2019\n" +
                        "Weight : 10000\n";
            }
        };

        Searchable s3 = new Searchable() {
            final String description = "Truck 3";
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
                return "Truck ID : 3\n" +
                        "License Plate : 123456\n" +
                        "Model : Toyota 2019\n" +
                        "Weight : 10000\n";
            }
        };

        Searchable s4 = new Searchable() {
            final String description = "Truck 4";
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
                return "Truck ID : 4\n" +
                        "License Plate : 123456\n" +
                        "Model : Toyota 2019\n" +
                        "Weight : 10000\n";
            }
        };
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(0,0,0,0));
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        JButton editButton = new JButton("Edit");
        editButton.setPreferredSize(new Dimension(100, 30));
        buttonsPanel.add(editButton);

        JButton removeButton = new JButton("Remove");
        removeButton.setPreferredSize(new Dimension(100, 30));
        buttonsPanel.add(removeButton);
        contentPanel.add(buttonsPanel, constraints);



        listModel = new DefaultListModel<>();

        // Create the JList
        list = new JList<>(listModel);
        JScrollPane listPanel = new JScrollPane(list);


        list.setBackground(new Color(0,0,0,0));
        listPanel.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Colors.getForegroundColor());
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(0,0,0,c.getHeight()-26);
                g2.drawArc(0,c.getHeight()-50,50,50,180,90);
                g2.drawLine(26,c.getHeight(),c.getWidth()-25,c.getHeight());
                g2.drawArc(c.getWidth()-50-1,c.getHeight()-50,50,50,270,90);
                g2.drawLine(c.getWidth()-1,c.getHeight()-26,c.getWidth()-1,0);
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
        listPanel.setVerticalScrollBar(new PrettyScrollBar(160));
        listPanel.getVerticalScrollBar().setUnitIncrement(30);
        listPanel.getVerticalScrollBar().setBackground(new Color(0,0,0,0));


        list.setCellRenderer(new ListCellRenderer<JPanel>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends JPanel> list, JPanel value, int index, boolean isSelected, boolean cellHasFocus) {

                list.setOpaque(true);

                // Clear the panel and add the custom component
                list.add(value);
                list.setFixedCellHeight(-1);
                value.setPreferredSize(new Dimension(500, 100));

                value.setBackground(new Color(0,0,0,0));
                if(isSelected) {
                    value.setBackground(new Color(200,200,200,128));
                }

                value.setBorder(new Border() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke(1));
                        g2.setColor(new Color(200, 200, 200, 100));
                        g2.drawLine(0, height - 1, width, height - 1);
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(0, 0, 0, 0);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return false;
                    }
                });
                Dimension preferredSize = new Dimension((int) (scrollPane.getWidth() * 0.6), (int) (scrollPane.getHeight() * 0.8));
                listPanel.setPreferredSize(preferredSize);
                listPanel.revalidate();
                panel.repaint();
                return value;
            }
        });

        JPanel innerPanel = new JPanel();
        innerPanel.setBackground(new Color(0,0,0,0));
        innerPanel.add(listPanel);

        for(int i = 1; i <= 20; i++) {
            JPanel cellPanel = new JPanel();
            cellPanel.setLayout( new GridBagLayout());

            JLabel cellLabel = new JLabel("Truck " + i);
            cellLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            cellLabel.setForeground(Colors.getForegroundColor());

            GridBagConstraints c = new GridBagConstraints();

//            c.gridy = 0;
//            c.gridx = 0;
//            c.anchor = GridBagConstraints.NORTHWEST;
            cellPanel.add(cellLabel,c);

//            c.gridy = 1;
//            c.gridx = 2;
//            c.anchor = GridBagConstraints.EAST;
//            cellPanel.add(editButton,c);
//
//
//            c.gridy = 1;
//            c.gridx = 3;
//            c.anchor = GridBagConstraints.EAST;
//            cellPanel.add(removeButton,c);

            listPanel.add(cellPanel);
            listModel.addElement(cellPanel);
        }

//        list.addListSelectionListener(new ListSelectionListener() {
//
//            JButton prevAdd;
//            JButton prevRem;
//
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if(prevAdd != null) prevAdd.setVisible(false);
//                if(prevRem != null) prevRem.setVisible(false);
//                JPanel selectedPanel = list.getSelectedValue();
//                prevAdd = (JButton) selectedPanel.getComponent(1);
//                prevRem = (JButton) selectedPanel.getComponent(2);
//                prevRem.setVisible(true);
//                prevRem.setEnabled(true);
//                prevAdd.setVisible(true);
//                prevAdd.setEnabled(true);
//            }
//        });


        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 1;

        contentPanel.add(innerPanel,constraints);

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

    private void showConfirmationDialog() {
        int[] selectedIndices = list.getSelectedIndices();
        if (selectedIndices.length > 0) {
            int choice = JOptionPane.showConfirmDialog(newOpenPanel, "Are you sure you want to remove the selected item?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                removeSelectedItems(selectedIndices[0]);
                newOpenWindow.dispose();
                //newOpenWindow = null;
            }
        }
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension contentPreferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight() * 0.6));
        contentPanel.setPreferredSize(new Dimension(contentPreferredSize.width, contentPreferredSize.height + 250));

        scrollPane.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
