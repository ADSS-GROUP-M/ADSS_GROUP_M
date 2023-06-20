package presentationLayer.gui.transportModule.view.panels.trucks;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.*;
import presentationLayer.gui.transportModule.control.TrucksControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

public class ViewTrucksPanel extends AbstractTransportModulePanel {
    private PrettyList truckList;
    private ObservableList<Searchable> trucks;

    public ViewTrucksPanel(TrucksControl control) {
        super(control);
        init();
    }

    private void init() {

        ObservableList emptyTruckList = new ObservableList<>();
        control.getAll(this, emptyTruckList);
        trucks = emptyTruckList;

        trucks.sort(new Comparator<Searchable>() {
            @Override
            public int compare(Searchable o1, Searchable o2) {
                return o1.getShortDescription().compareTo(o2.getShortDescription());
            }
        });

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(0, 0, 0, 0));
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        JButton removeButton = new JButton();
        removeButton.setPreferredSize(new Dimension(30, 30));
        buttonsPanel.add(removeButton);

  removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
            showConfirmationDialog();
      }

    });


        contentPanel.add(buttonsPanel, constraints);





        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 1;

        truckList = new PrettyList(trucks, panel);
        contentPanel.add(truckList.getComponent(), constraints);

    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);

        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        truckList.componentResized(new Dimension((int) (contentPanelSize.width*0.8), (int) (contentPanelSize.height*0.9)));

        panel.revalidate();
    }


    private void showConfirmationDialog() {
        int[] selectedIndices = truckList.getList().getSelectedIndices();
        if (selectedIndices.length > 0) {
            int choice = JOptionPane.showConfirmDialog(contentPanel, "Are you sure you want to remove the selected item?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                truckList.getListModel().remove(selectedIndices[0]);
                updateListAfterRemove(selectedIndices[0]);

            }
        }
    }


    private void updateListAfterRemove(int selectedIndex){

        control.remove(this, (ObservableModel) trucks.remove(selectedIndex));
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
