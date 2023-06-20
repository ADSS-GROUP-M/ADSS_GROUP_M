package presentationLayer.gui.transportModule.view.panels.drivers;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyList;
import presentationLayer.gui.transportModule.control.DriversControl;
import presentationLayer.gui.transportModule.control.TrucksControl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ViewDriversPanel extends AbstractTransportModulePanel {
    JPanel newOpenPanel = new JPanel();
    JFrame newOpenWindow;
    private PrettyList driverList;
    private ObservableList emptyDriverList;
    private ObservableList<Searchable> drivers;

    public ViewDriversPanel(DriversControl control) {
        super(control);
        init();
    }

    private void init() {

        emptyDriverList = new ObservableList<>();
        control.getAll(this,emptyDriverList);
        drivers = emptyDriverList;
        drivers.sort(new Comparator<Searchable>() {
            @Override
            public int compare(Searchable o1, Searchable o2) {
                return o1.getShortDescription().compareTo(o2.getShortDescription());
            }
        });

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(0,0,0,0));
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        JButton editButton = new JButton();
        editButton.setPreferredSize(new Dimension(30, 30));
        buttonsPanel.add(editButton);

        JButton removeButton = new JButton();
        removeButton.setPreferredSize(new Dimension(30, 30));
        removeButton.setBorder(new EmptyBorder(0,0,0,0));
        removeButton.setBackground(new Color(0,0,0,0));
        buttonsPanel.add(removeButton);
        contentPanel.add(buttonsPanel, constraints);
        removeButton.setUI(new BasicButtonUI(){
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                Graphics2D g2 = (Graphics2D)g;
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.RED);
                g2.fillRoundRect(3,13,c.getWidth()-4,4,1,1);
            }
        });

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 1;

        driverList = new PrettyList(drivers,panel);
        contentPanel.add(driverList.getComponent(),constraints);

        //Set up the confirmation dialog on window close
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                showConfirmationDialog();
//            }
//        });
    }

//    private void removeSelectedItems(int selectedIndex) {
//            listModel.remove(selectedIndex);
//
//    }
//
//    private void showConfirmationDialog() {
//        int[] selectedIndices = list.getSelectedIndices();
//        if (selectedIndices.length > 0) {
//            int choice = JOptionPane.showConfirmDialog(newOpenPanel, "Are you sure you want to remove the selected item?", "Confirmation", JOptionPane.YES_NO_OPTION);
//            if (choice == JOptionPane.YES_OPTION) {
//                removeSelectedItems(selectedIndices[0]);
//                newOpenWindow.dispose();
//                //newOpenWindow = null;
//            }
//        }
//    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);

        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        driverList.componentResized(new Dimension((int) (contentPanelSize.width*0.8), (int) (contentPanelSize.height*0.9)));

        panel.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
