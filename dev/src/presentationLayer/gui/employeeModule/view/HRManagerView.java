package presentationLayer.gui.employeeModule.view;

import presentationLayer.PresentationFactory;
import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.employeeModule.view.panels.EmployeesPanel;
import presentationLayer.gui.employeeModule.view.panels.shifts.ViewShiftsPanel;
import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.plAbstracts.PanelManager;
import presentationLayer.gui.plAbstracts.interfaces.Panel;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.Link;
import presentationLayer.gui.plUtils.QuickAccess;

public class HRManagerView extends MainWindow {

    private Panel currentPanel;
    private final PanelManager panelManager;
    private final ShiftsControl shiftsControl;

    public HRManagerView(ShiftsControl shiftsControl) {
        super("HRManager Page");
        this.shiftsControl = shiftsControl;
        Colors.colorPalette = Colors.ColorPalette.hr;
        currentPanel = new EmployeesPanel();
        panelManager = new PanelManager(currentPanel);
        super.addUIElement(panelManager);
        super.addUIElement(initQuickAccess());
        super.init();
        super.setVisible(true);
    }

    private QuickAccess initQuickAccess(){
//        return new QuickAccess()
//                .addCategory("Transport Management",
//                        new Link("View Transports", () -> super.setCurrentPanel(new ViewTransportsPanel())),
//                        new Link("Add Transport", () -> super.setCurrentPanel(new AddTransportPanel())),
//                        new Link("Update Transport", () -> super.setCurrentPanel(new UpdateTransportPanel())),
//                        new Link("Remove Transport", () -> super.setCurrentPanel(new RemoveTransportPanel()))
//                );
        return new QuickAccess()
//                .addCategory("Transport Management",
//                        new Link("View Transports",
//                                () -> setCurrentPanel(new ViewTransportsPanel(transportsControl))),
//                        new Link("Add Transport",
//                                () -> setCurrentPanel(new AddTransportPanel(transportsControl))),
//                        new Link("Update Transport",
//                                () -> setCurrentPanel(new UpdateTransportPanel(transportsControl)))
//                )
//                .addCategory("Item List Management",
//                        new Link("View Item Lists",
//                                () -> setCurrentPanel(new ViewItemListPanel(itemListsControl))),
//                        new Link("Add Item List",
//                                () -> setCurrentPanel(new AddItemListPanel(itemListsControl))),
//                        new Link("Update Item List",
//                                () -> setCurrentPanel(new UpdateItemListPanel(itemListsControl)))
//                )
//                .addCategory("Drivers Management",
//                        new Link("View Drivers",
//                                () -> setCurrentPanel(new ViewDriversPanel(driversControl))),
//                        new Link("Update Driver",
//                                () -> setCurrentPanel(new UpdateDriversPanel(driversControl)))
//                )
//                .addCategory("Trucks Management",
//                        new Link("View Trucks",
//                                () -> setCurrentPanel(new ViewTrucksPanel(trucksControl))),
//                        new Link("Add Truck",
//                                () -> setCurrentPanel(new AddTruckPanel(trucksControl))),
//                        new Link("Update Truck",
//                                () -> setCurrentPanel(new UpdateTruckPanel(trucksControl)))
//                )
                .addCategory("Shifts Management",
                        new Link("View Shifts",
                                () -> setCurrentPanel(new ViewShiftsPanel(shiftsControl)))
//                        new Link("Create Shift",
//                                () -> setCurrentPanel(new AddShiftPanel(shiftsControl))),
//                        new Link("Update Shift",
//                                () -> setCurrentPanel(new UpdateShiftPanel(shiftsControl)))
                );
    }

    private void setCurrentPanel(Panel panel) {
        panelManager.setPanel(panel);
        currentPanel = panel;
        currentPanel.componentResized(super.container.getSize());
        super.container.revalidate();
    }

    public static void main(String[] args) {
        PresentationFactory factory = new PresentationFactory();
        ShiftsControl shiftsControl = factory.shiftsControl();

        MainWindow mainWindow = new HRManagerView(shiftsControl);
    }
}