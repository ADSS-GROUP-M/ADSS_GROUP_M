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

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ViewDriversPanel extends AbstractTransportModulePanel {

    private PrettyList driverList;

    public ViewDriversPanel(DriversControl control) {
        super(control);
        init();
    }

    private void init() {

        ObservableList emptyDriverList = new ObservableList<>();
        control.getAll(this, emptyDriverList);
        ObservableList<Searchable> drivers = emptyDriverList;


        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;

        driverList = new PrettyList(drivers, panel);
        contentPanel.add(driverList.getComponent(), constraints);

    }

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
