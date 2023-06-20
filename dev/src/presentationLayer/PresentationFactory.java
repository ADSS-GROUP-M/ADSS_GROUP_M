package presentationLayer;

import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.controller.UsersControl;
import presentationLayer.gui.transportModule.control.*;
import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.transportModule.control.TrucksControl;
import serviceLayer.ServiceFactory;

public class PresentationFactory {
    private ServiceFactory serviceFactory;
    private DriversControl driversControl;
    private ItemListsControl itemListsControl;
    private SitesControl sitesControl;
    private TransportsControl transportsControl;
    private TrucksControl trucksControl;
    private EmployeesControl employeesControl;
    private ShiftsControl shiftsControl;
    private UsersControl usersControl;

    public PresentationFactory() {
        serviceFactory = new ServiceFactory();
        transportsControl = new TransportsControl(serviceFactory.transportsService());
        itemListsControl = new ItemListsControl(serviceFactory.itemListsService());
        driversControl = new DriversControl(serviceFactory.resourceManagementService());
        sitesControl = new SitesControl(serviceFactory.resourceManagementService());
        trucksControl = new TrucksControl(serviceFactory.resourceManagementService());
        employeesControl = new EmployeesControl(serviceFactory.employeesService());
        shiftsControl = new ShiftsControl(serviceFactory.employeesService());
        usersControl = new UsersControl(serviceFactory.userService());
    }

    public DriversControl getDriversControl() {
        return driversControl;
    }
    public ItemListsControl getItemListsControl() {
        return itemListsControl;
    }
    public SitesControl getSitesControl() {
        return sitesControl;
    }
    public TransportsControl getTransportsControl() {
        return transportsControl;
    }
    public TrucksControl getTrucksControl() {
        return trucksControl;
    }

    public EmployeesControl employeesControl() {
        return employeesControl;
    }

    public ShiftsControl shiftsControl() {
        return shiftsControl;
    }

    public UsersControl usersControl() {
        return usersControl;
    }
}
