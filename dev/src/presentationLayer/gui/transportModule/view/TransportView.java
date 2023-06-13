package presentationLayer.gui.transportModule.view;

import presentationLayer.plAbstracts.Window;
import presentationLayer.plUtils.Link;
import presentationLayer.plUtils.QuickAccess;
import presentationLayer.gui.transportModule.view.panels.*;

public class TransportView extends Window {
    public TransportView() {
        super("Transport Module",
                new PanelImpl());

        super.init();
        super.setVisible(true);
//        super.addComponent(initQuickAccess());
    }

    private QuickAccess initQuickAccess(){
        return new QuickAccess()
                .addCategory("Transport Management",
                        new Link("View Transports", () -> super.setCurrentPanel(new ViewTransportsPanel())),
                        new Link("Add Transport", () -> super.setCurrentPanel(new AddTransportPanel())),
                        new Link("Update Transport", () -> super.setCurrentPanel(new UpdateTransportPanel())),
                        new Link("Remove Transport", () -> super.setCurrentPanel(new RemoveTransportPanel()))
                )
                .addCategory("Item List Management",
                        new Link("View Item Lists", () -> super.setCurrentPanel(new ViewItemListPanel())),
                        new Link("Add Item List", () -> super.setCurrentPanel(new AddItemListPanel())),
                        new Link("Update Item List", () -> super.setCurrentPanel(new UpdateItemListPanel())),
                        new Link("Remove Item List", () -> super.setCurrentPanel(new RemoveItemListPanel()))
                )
                .addCategory("Drivers Management",
                        new Link("View Drivers", () -> super.setCurrentPanel(new ViewDriversPanel())),
                        new Link("Update Driver", () -> super.setCurrentPanel(new UpdateDriversPanel()))
                )
                .addCategory("Trucks Management",
                        new Link("View Trucks", () -> super.setCurrentPanel(new ViewTrucksPanel())),
                        new Link("Add Truck", () -> super.setCurrentPanel(new AddTruckPanel())),
                        new Link("Update Truck", () -> super.setCurrentPanel(new UpdateTruckPanel())),
                        new Link("Remove Truck", () -> super.setCurrentPanel(new RemoveTruckPanel()))
                )
                .addCategory("Sites Management",
                        new Link("View Sites", () -> super.setCurrentPanel(new ViewSitesPanel())),
                        new Link("Add Site", () -> super.setCurrentPanel(new AddSitePanel())),
                        new Link("Update Site", () -> super.setCurrentPanel(new UpdateSitePanel()))
                );
    }
    public static void main(String[] args) {
        Window window = new TransportView();
    }
}
