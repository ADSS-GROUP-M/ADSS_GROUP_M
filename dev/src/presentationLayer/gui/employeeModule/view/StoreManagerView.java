package presentationLayer.gui.employeeModule.view;

import presentationLayer.DataGenerator;
import presentationLayer.PresentationFactory;
import presentationLayer.cli.employeeModule.Model.BackendController;
import presentationLayer.cli.employeeModule.View.EmployeeMenu;
import presentationLayer.cli.employeeModule.View.HRManagerMenu;
import presentationLayer.cli.employeeModule.View.MenuManager;
import presentationLayer.cli.transportModule.TransportCLI;
import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.employeeModule.controller.UsersControl;
import presentationLayer.gui.employeeModule.view.panels.EmployeesPanel;
import presentationLayer.gui.employeeModule.view.panels.StoreManagerPanel;
import presentationLayer.gui.employeeModule.view.panels.employees.EmployeeDetailsPanel;
import presentationLayer.gui.employeeModule.view.panels.shifts.*;
import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.plAbstracts.PanelManager;
import presentationLayer.gui.plAbstracts.interfaces.Panel;
import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.Link;
import presentationLayer.gui.plUtils.QuickAccess;
import presentationLayer.gui.transportModule.control.*;
import presentationLayer.gui.transportModule.view.TransportView;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Services.UserService;

import javax.swing.*;

public class StoreManagerView extends MainWindow {

    private Panel currentPanel;
    private final PanelManager panelManager;

    public StoreManagerView() {
        super("Store Manager Page");
        Colors.colorPalette = Colors.ColorPalette.storeManager;
        currentPanel = new StoreManagerPanel();
        panelManager = new PanelManager(currentPanel);
        super.addUIElement(panelManager);
        super.addUIElement(initQuickAccess());
        super.init();
        super.setVisible(true);
    }

    private QuickAccess initQuickAccess(){
        return new QuickAccess()
                .addCategory("Choose Page",
                        new Link("HR Manager", () -> hrManager()),
                        new Link("Transport Manager", () -> transportModule()),
                        new Link("Employee Page", () -> employee()))
                .addCategory("Data Generation",
                        new Link("Generate Data",
                                () -> JOptionPane.showMessageDialog(null,new DataGenerator().generateData()))
                );
    }

    public void setCurrentPanel(Panel panel) {
        panelManager.setPanel(panel);
        currentPanel = panel;
        currentPanel.componentResized(panelManager.getComponent().getSize());
        super.container.revalidate();
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new StoreManagerView();
    }

    public void employee() {
        ServiceFactory serviceFactory = new ServiceFactory();
        PresentationFactory factory = new PresentationFactory();
        EmployeesControl employeesControl = factory.employeesControl();
        ShiftsControl shiftsControl = new ShiftsControl(serviceFactory.employeesService());
        new EmployeeView(employeesControl, shiftsControl, "111");
        this.container.dispose();
    }

    public void transportModule(){
        PresentationFactory factory = new PresentationFactory();
        TransportsControl transportsControl = factory.getTransportsControl();
        ItemListsControl itemListsControl = factory.getItemListsControl();
        DriversControl driversControl = factory.getDriversControl();
        TrucksControl trucksControl = factory.getTrucksControl();
        SitesControl sitesControl = factory.getSitesControl();
        new TransportView(transportsControl, itemListsControl, driversControl, trucksControl, sitesControl);
        this.container.dispose();
    }

    public void hrManager(){
        ServiceFactory serviceFactory = new ServiceFactory();
        PresentationFactory factory = new PresentationFactory();
        EmployeesControl employeesControl = factory.employeesControl();
        ShiftsControl shiftsControl = new ShiftsControl(serviceFactory.employeesService());
        UsersControl usersControl = new UsersControl(serviceFactory.userService());
        new HRManagerView(employeesControl, shiftsControl, usersControl);
        this.container.dispose();
    }
}