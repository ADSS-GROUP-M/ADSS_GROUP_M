package presentationLayer.gui.transportModule.view.panels.trucks;


import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableObject;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.Colors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ViewTrucksPanel extends AbstractTransportModulePanel {
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JScrollPane listPanel;

    public ViewTrucksPanel() {
        super();
        init();
    }

    private void init() {

        // Create the list model
        Searchable s1 = new Searchable() {
            final String description = "ItEm NuMbEr 1, beri cool. Case-insensitive Searchable example";
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
                return null;
            }

        };

        Searchable s2 = new Searchable() {
            final String description = "Item number 2, muy suave. case-sensitive Searchable example";
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
                return null;
            }
        };

        Searchable s3 = new Searchable() {
            final String description = "Item number 3, un poco loco";
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
                return null;
            }
        };

        Searchable s4 = new Searchable() {
            final String description = "Item number 4, el gato es en la mesa";
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
                return null;
            }
        };
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        listModel = new DefaultListModel<>();

        // Create the JList
        list = new JList<>(listModel);
        listPanel = new JScrollPane(list);

        list.setBackground(new Color(0,0,0,0));
        listPanel.setBorder(new EmptyBorder(0,0,0,0));
        listPanel.setBackground(new Color(0,0,0,0));

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setBackground(new Color(0,0,0,0));
                if(isSelected) {
                    renderer.setBackground(new Color(200,200,200));
                }
                panel.repaint();
                setBorder(new Border() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke(3));
                        g2.setColor(Colors.getForegroundColor());
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

        JPanel innerPanel = new JPanel();
        innerPanel.setBackground(new Color(0,0,0,0));
        innerPanel.add(listPanel);

        addItem(s1.getShortDescription());
        addItem(s2.getShortDescription());
        addItem(s3.getShortDescription());
        addItem(s4.getShortDescription());

        // Create the remove button
        JButton removeButton = new JButton("Remove");
        removeButton.setPreferredSize(new Dimension(100, 30));
        innerPanel.add(removeButton,constraints);
        contentPanel.add(innerPanel,constraints);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showConfirmationDialog();
            }
        });



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
            int choice = JOptionPane.showConfirmDialog(getComponent(), "Are you sure you want to remove the selected item?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                removeSelectedItems(selectedIndices[0]);
            }
        }
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
    public void notify(ObservableObject observable) {

    }
}
