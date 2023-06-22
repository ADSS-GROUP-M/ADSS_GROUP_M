package presentationLayer.gui.transportModule.view.panels.sites;

import presentationLayer.gui.plAbstracts.AbstractModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyList;
import presentationLayer.gui.transportModule.control.SitesControl;

import java.awt.*;
import java.util.Comparator;

public class ViewSitesPanel extends AbstractModulePanel {
    private PrettyList sitesList;


    public ViewSitesPanel(SitesControl control) {
        super(control);
        init();
    }

    private void init() {

        ObservableList emptySiteList = new ObservableList<>();
        control.getAll(this,emptySiteList);
        ObservableList<Searchable> sites = emptySiteList;

        sites.sort(new Comparator<Searchable>() {
            @Override
            public int compare(Searchable o1, Searchable o2) {
                return o1.getShortDescription().compareTo(o2.getShortDescription());
            }
        });

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;


        sitesList = new PrettyList(sites,panel);
        contentPanel.add(sitesList.getComponent(),constraints);

    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        sitesList.componentResized(new Dimension((int) (contentPanelSize.width * 0.8), (int) (contentPanelSize.height * 0.9)));

        contentPanel.revalidate();
    }

    @Override
    protected void clearFields() {
        // do nothing
    }

    @Override
    public void notify(ObservableModel observable) {
        // do nothing
    }
}
