package presentationLayer.gui.transportModule.view.panels.trucks;

import presentationLayer.gui.plAbstracts.AbstractModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.*;
import presentationLayer.gui.transportModule.control.TrucksControl;
import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class ViewTrucksPanel extends AbstractModulePanel {
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

    @Override
    protected void clearFields() {
        // do nothing
    }
}
