package presentationLayer.gui.transportModule.view.panels.sites;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyList;
import presentationLayer.gui.transportModule.control.SitesControl;
import presentationLayer.gui.transportModule.control.TrucksControl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ViewSitesPanel extends AbstractTransportModulePanel {
    private PrettyList sitesList;

    public ViewSitesPanel(SitesControl control) {
        super(control);
        init();
    }

    private void init() {

        ObservableList emptySiteList = new ObservableList<>();
        control.getAll(this,emptySiteList);
        ObservableList<Searchable> sites = emptySiteList;


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

        sitesList = new PrettyList(sites,panel);
        contentPanel.add(sitesList.getComponent(),constraints);

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
        Dimension contentPreferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight() * 0.6));
        contentPanel.setPreferredSize(new Dimension(contentPreferredSize.width, contentPreferredSize.height + 250));
        sitesList.componentResized(scrollPane.getSize());
        scrollPane.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
