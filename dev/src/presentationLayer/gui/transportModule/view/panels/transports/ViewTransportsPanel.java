package presentationLayer.gui.transportModule.view.panels.transports;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyList;
import presentationLayer.gui.plUtils.SearchableString;
import presentationLayer.gui.transportModule.control.SitesControl;
import presentationLayer.gui.transportModule.control.TransportsControl;
import presentationLayer.gui.transportModule.model.ObservableSite;
import presentationLayer.gui.transportModule.model.ObservableTransport;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ViewTransportsPanel extends AbstractTransportModulePanel {


    private final SitesControl sitesControl;
    private PrettyList transportsList;
    private PrettyList destinationsList;

    public ViewTransportsPanel(TransportsControl control, SitesControl sitesControl) {
        super(control);
        this.sitesControl = sitesControl;
        init();
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void init() {

        ObservableList emptyTransportList = new ObservableList<>();
        control.getAll(this,emptyTransportList);
        ObservableList<Searchable> transports = emptyTransportList;
        ObservableList<Searchable> destinations = new ObservableList<>();

        ObservableList emptySiteList = new ObservableList<>();
        sitesControl.getAll(this,emptySiteList);
        ObservableList<ObservableSite> sites = emptySiteList;
        Map<String,ObservableSite> sitesMap = new HashMap<>(){{
            sites.forEach((s)->put(s.name,s));
        }};

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTH;
        transportsList = new PrettyList(transports,panel);
        contentPanel.add(transportsList.getComponent(),constraints);

        constraints.gridy = 1;
        constraints.ipady = 0;
        constraints.anchor = GridBagConstraints.SOUTH;
        destinationsList = new PrettyList(destinations,panel);
        contentPanel.add(destinationsList.getComponent(),constraints);

        transportsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ObservableTransport t = (ObservableTransport) ((JList)e.getSource()).getSelectedValue();
                DefaultListModel<Searchable> model = new DefaultListModel<>();
                t.route
                    .forEach(
                            (s)->model.addElement(new SearchableString("[%s] %s"
                                    .formatted(t.estimatedArrivalTimes.get(s),sitesMap.get(s).toString())))
                    );
                destinationsList.getList().setModel(model);
            }
        });


    }


    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension contentPreferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight() * 0.6));
        contentPanel.setPreferredSize(new Dimension(contentPreferredSize.width, contentPreferredSize.height + 250));
        transportsList.componentResized(new Dimension(scrollPane.getWidth(), (int) (2*contentPanel.getHeight()/3.0)));
        destinationsList.componentResized(new Dimension(scrollPane.getWidth(), contentPanel.getHeight() / 3));
        contentPanel.revalidate();
    }

    @Override
    public void notify(ObservableModel observable) {

    }
}
