package presentationLayer.gui.transportModule.view;

import presentationLayer.gui.plAbstracts.Panel;
import presentationLayer.gui.plAbstracts.PanelManager;

import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.plUtils.Link;
import presentationLayer.gui.plUtils.QuickAccess;
import presentationLayer.gui.transportModule.panels.drivers.UpdateDriversPanel;
import presentationLayer.gui.transportModule.panels.drivers.ViewDriversPanel;
import presentationLayer.gui.transportModule.panels.itemsLists.AddItemListPanel;
import presentationLayer.gui.transportModule.panels.itemsLists.UpdateItemListPanel;
import presentationLayer.gui.transportModule.panels.itemsLists.ViewItemListPanel;
import presentationLayer.gui.transportModule.panels.sites.AddSitePanel;
import presentationLayer.gui.transportModule.panels.sites.UpdateSitePanel;
import presentationLayer.gui.transportModule.panels.sites.ViewSitesPanel;
import presentationLayer.gui.transportModule.panels.transports.AddTransportPanel;
import presentationLayer.gui.transportModule.panels.transports.UpdateTransportPanel;
import presentationLayer.gui.transportModule.panels.transports.ViewTransportsPanel;
import presentationLayer.gui.transportModule.panels.trucks.AddTruckPanel;
import presentationLayer.gui.transportModule.panels.trucks.UpdateTruckPanel;
import presentationLayer.gui.transportModule.panels.trucks.ViewTrucksPanel;

public class TransportView extends MainWindow{

    private Panel currentPanel;
    private final PanelManager panelManager;

    public TransportView() {
        super("Transport Module");
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
                        new Link("View Transports", () -> setCurrentPanel(new ViewTransportsPanel())),
                        new Link("Add Transport", () -> setCurrentPanel(new AddTransportPanel())),
                        new Link("Update Transport", () -> setCurrentPanel(new UpdateTransportPanel()))
                )
                .addCategory("Item List Management",
                        new Link("View Item Lists", () -> setCurrentPanel(new ViewItemListPanel())),
                        new Link("Add Item List", () -> setCurrentPanel(new AddItemListPanel())),
                        new Link("Update Item List", () -> setCurrentPanel(new UpdateItemListPanel()))
                )
                .addCategory("Drivers Management",
                        new Link("View Drivers", () -> setCurrentPanel(new ViewDriversPanel())),
                        new Link("Update Driver", () -> setCurrentPanel(new UpdateDriversPanel()))
                )
                .addCategory("Trucks Management",
                        new Link("View Trucks", () -> setCurrentPanel(new ViewTrucksPanel())),
                        new Link("Add Truck", () -> setCurrentPanel(new AddTruckPanel())),
                        new Link("Update Truck", () -> setCurrentPanel(new UpdateTruckPanel()))
                )
                .addCategory("Sites Management",
                        new Link("View Sites", () -> setCurrentPanel(new ViewSitesPanel())),
                        new Link("Add Site", () -> setCurrentPanel(new AddSitePanel())),
                        new Link("Update Site", () -> setCurrentPanel(new UpdateSitePanel()))
                );
    }
    private void setCurrentPanel(Panel panel) {
        panelManager.setPanel(panel);
        currentPanel = panel;
        currentPanel.componentResized(super.container.getSize());
        super.container.revalidate();
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new TransportView();
    }
}
