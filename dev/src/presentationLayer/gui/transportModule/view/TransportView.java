package presentationLayer.gui.transportModule.view;

import presentationLayer.gui.plAbstracts.*;
import presentationLayer.gui.plAbstracts.Panel;
import presentationLayer.gui.plUtils.Link;
import presentationLayer.gui.plUtils.QuickAccess;
import presentationLayer.gui.transportModule.control.DriversControl;
import presentationLayer.gui.transportModule.control.ItemListsControl;
import presentationLayer.gui.transportModule.control.SitesControl;
import presentationLayer.gui.transportModule.control.TransportsControl;
import presentationLayer.gui.transportModule.view.panels.drivers.UpdateDriversPanel;
import presentationLayer.gui.transportModule.view.panels.drivers.ViewDriversPanel;
import presentationLayer.gui.transportModule.view.panels.itemsLists.AddItemListPanel;
import presentationLayer.gui.transportModule.view.panels.itemsLists.UpdateItemListPanel;
import presentationLayer.gui.transportModule.view.panels.itemsLists.ViewItemListPanel;
import presentationLayer.gui.transportModule.view.panels.sites.AddSitePanel;
import presentationLayer.gui.transportModule.view.panels.sites.UpdateSitePanel;
import presentationLayer.gui.transportModule.view.panels.sites.ViewSitesPanel;
import presentationLayer.gui.transportModule.view.panels.transports.AddTransportPanel;
import presentationLayer.gui.transportModule.view.panels.transports.UpdateTransportPanel;
import presentationLayer.gui.transportModule.view.panels.transports.ViewTransportsPanel;
import presentationLayer.gui.transportModule.view.panels.trucks.AddTruckPanel;
import presentationLayer.gui.transportModule.view.panels.trucks.UpdateTruckPanel;
import presentationLayer.gui.transportModule.view.panels.trucks.ViewTrucksPanel;

public class TransportView extends MainWindow {

    private Panel currentPanel;
    private final PanelManager panelManager;
    private final UIElementObserver transportsControl;
    private final UIElementObserver itemListsControl;
    private final UIElementObserver driversControl;
    private final UIElementObserver trucksControl;
    private final UIElementObserver sitesControl;

    public TransportView(UIElementObserver transportsControl,
                         UIElementObserver itemListsControl,
                         UIElementObserver driversControl,
                         UIElementObserver trucksControl,
                         UIElementObserver sitesControl) {
        super("Transport Module");
        this.transportsControl = transportsControl;
        this.itemListsControl = itemListsControl;
        this.driversControl = driversControl;
        this.trucksControl = trucksControl;
        this.sitesControl = sitesControl;
        currentPanel = new ViewTransportsPanel();
        panelManager = new PanelManager(currentPanel);
        super.addUIElement(panelManager);
        super.addUIElement(initQuickAccess());
        super.init();
        super.setVisible(true);
    }

    private QuickAccess initQuickAccess(){
        return new QuickAccess()
                .addCategory("Transport Management",
                        new Link("View Transports", () -> subscribeAndSet(new ViewTransportsPanel(), transportsControl)),
                        new Link("Add Transport", () -> subscribeAndSet(new AddTransportPanel(), transportsControl)),
                        new Link("Update Transport", () -> subscribeAndSet(new UpdateTransportPanel(), transportsControl))
                )
                .addCategory("Item List Management",
                        new Link("View Item Lists", () -> subscribeAndSet(new ViewItemListPanel(), itemListsControl)),
                        new Link("Add Item List", () -> subscribeAndSet(new AddItemListPanel(), itemListsControl)),
                        new Link("Update Item List", () -> subscribeAndSet(new UpdateItemListPanel(), itemListsControl))
                )
                .addCategory("Drivers Management",
                        new Link("View Drivers", () -> subscribeAndSet(new ViewDriversPanel(), driversControl)),
                        new Link("Update Driver", () -> subscribeAndSet(new UpdateDriversPanel(), driversControl))
                )
                .addCategory("Trucks Management",
                        new Link("View Trucks", () -> subscribeAndSet(new ViewTrucksPanel(), trucksControl)),
                        new Link("Add Truck", () -> subscribeAndSet(new AddTruckPanel(), trucksControl)),
                        new Link("Update Truck", () -> subscribeAndSet(new UpdateTruckPanel(), trucksControl))
                )
                .addCategory("Sites Management",
                        new Link("View Sites", () -> subscribeAndSet(new ViewSitesPanel(), sitesControl)),
                        new Link("Add Site", () -> subscribeAndSet(new AddSitePanel(), sitesControl)),
                        new Link("Update Site", () -> subscribeAndSet(new UpdateSitePanel(), sitesControl))
                );
    }

    private void subscribeAndSet(Panel panel, UIElementObserver observer) {
        panel.subscribe(observer);
        setCurrentPanel(panel);
    }

    private void setCurrentPanel(Panel panel) {
        panelManager.setPanel(panel);
        currentPanel = panel;
        currentPanel.componentResized(super.container.getSize());
        super.container.revalidate();
    }

    public static void main(String[] args) {
        TransportsControl transportsControl = new TransportsControl();
        ItemListsControl itemListsControl = new ItemListsControl();
        DriversControl driversControl = new DriversControl();
        TransportsControl trucksControl = new TransportsControl();
        SitesControl sitesControl = new SitesControl();
        MainWindow mainWindow = new TransportView(transportsControl, itemListsControl, driversControl, trucksControl, sitesControl);
    }
}
