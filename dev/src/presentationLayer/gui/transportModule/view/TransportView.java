package presentationLayer.gui.transportModule.view;

import presentationLayer.gui.plAbstracts.Panel;
import presentationLayer.gui.plAbstracts.PanelManager;
import presentationLayer.gui.transportModule.PanelExample;
import presentationLayer.gui.transportModule.panels.*;
import presentationLayer.gui.plAbstracts.Window;
import presentationLayer.gui.plUtils.Link;
import presentationLayer.gui.plUtils.QuickAccess;
import presentationLayer.gui.transportModule.panels.trucks.AddTruckPanel;
import presentationLayer.gui.transportModule.panels.trucks.RemoveTruckPanel;
import presentationLayer.gui.transportModule.panels.trucks.UpdateTruckPanel;
import presentationLayer.gui.transportModule.panels.trucks.ViewTrucksPanel;

public class TransportView extends Window{

    private Panel currentPanel;
    private final PanelManager panelManager;

    public TransportView() {
        super("Transport Module");
//        currentPanel = new PanelExample();
        currentPanel = new ViewTransportsPanel();
        panelManager = new PanelManager(currentPanel);
        super.addUIElement(panelManager);
        super.addUIElement(initQuickAccess());
        super.init();
        super.setVisible(true);
    }

    private QuickAccess initQuickAccess(){
        return new QuickAccess()
                .addCategory("Example Category",
                        new Link("Example Panel", () -> setCurrentPanel(new PanelExample()))
                )
                .addCategory("Transport Management",
                        new Link("View Transports", () -> setCurrentPanel(new ViewTransportsPanel())),
                        new Link("Add Transport", () -> setCurrentPanel(new AddTransportPanel())),
                        new Link("Update Transport", () -> setCurrentPanel(new UpdateTransportPanel())),
                        new Link("Remove Transport", () -> setCurrentPanel(new RemoveTransportPanel()))
                )
                .addCategory("Item List Management",
                        new Link("View Item Lists", () -> setCurrentPanel(new ViewItemListPanel())),
                        new Link("Add Item List", () -> setCurrentPanel(new AddItemListPanel())),
                        new Link("Update Item List", () -> setCurrentPanel(new UpdateItemListPanel())),
                        new Link("Remove Item List", () -> setCurrentPanel(new RemoveItemListPanel()))
                )
                .addCategory("Drivers Management",
                        new Link("View Drivers", () -> setCurrentPanel(new ViewDriversPanel())),
                        new Link("Update Driver", () -> setCurrentPanel(new UpdateDriversPanel()))
                )
                .addCategory("Trucks Management",
                        new Link("View Trucks", () -> setCurrentPanel(new ViewTrucksPanel())),
                        new Link("Add Truck", () -> setCurrentPanel(new AddTruckPanel())),
                        new Link("Update Truck", () -> setCurrentPanel(new UpdateTruckPanel())),
                        new Link("Remove Truck", () -> setCurrentPanel(new RemoveTruckPanel()))
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
    }

    public static void main(String[] args) {
        Window window = new TransportView();
    }
}
