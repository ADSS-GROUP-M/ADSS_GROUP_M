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
import java.util.HashMap;
import java.util.Map;

public class ViewTransportsPanel extends AbstractTransportModulePanel {

    private final SitesControl sitesControl;
    private PrettyList transportsList;
    private PrettyList destinationsList;
    private ObservableList<Searchable> transports;

    public ViewTransportsPanel(TransportsControl control, SitesControl sitesControl) {
        super(control);
        this.sitesControl = sitesControl;
        init();
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void init() {

        ObservableList emptyTransportList = new ObservableList<>();
        control.getAll(this,emptyTransportList);
        transports = emptyTransportList;
        ObservableList<Searchable> destinations = new ObservableList<>();

        ObservableList emptySiteList = new ObservableList<>();
        sitesControl.getAll(this,emptySiteList);
        ObservableList<ObservableSite> sites = emptySiteList;
        Map<String,ObservableSite> sitesMap = new HashMap<>(){{
            sites.forEach((s)->put(s.name,s));
        }};

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

        transportsList = new PrettyList(transports,panel);
        contentPanel.add(transportsList.getComponent(),constraints);

        constraints.gridy = 2;
        constraints.ipady = 0;
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

        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        transportsList.componentResized(new Dimension((int) (contentPanelSize.width*0.8), (int) (- 50 + 2 *contentPanelSize.height/3.0)));
        destinationsList.componentResized(new Dimension((int) (contentPanelSize.width * 0.8), (int) (-50 + contentPanelSize.height/3.0)));

        panel.revalidate();
    }


    @Override
    public void notify(ObservableModel observable) {
    }
}
