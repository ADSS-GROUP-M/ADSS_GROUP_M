package businessLayer;

import businessLayer.employeeModule.Controllers.EmployeesController;
import businessLayer.employeeModule.Controllers.ShiftsController;
import businessLayer.transportModule.*;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.transportModule.ServiceFactory;
import utils.transportUtils.TransportException;

public class BusinessFactory {

    private final TransportsController transportsController;
    private final DriversController driversController;
    private final SitesController sitesController;
    private final ItemListsController itemListsController;
    private final TrucksController trucksController;
    private final EmployeesController employeesController;
    private final ShiftsController shiftsController;
    private final DalFactory dalFactory;

    public BusinessFactory(ServiceFactory serviceFactory) throws DalException{
        dalFactory = new DalFactory();

        trucksController = new TrucksController(dalFactory.trucksDAO());
        sitesController = new SitesController(dalFactory.sitesDAO());
        driversController = new DriversController(dalFactory.driversDAO());
        shiftsController = new ShiftsController(dalFactory.shiftDAO());
        employeesController = new EmployeesController(shiftsController, dalFactory.branchesDAO(), dalFactory.employeeDAO());

        EmployeesService employeesService = serviceFactory.employeesService();

        try {
            itemListsController = new ItemListsController(dalFactory.itemListsDAO());
            transportsController = new TransportsController(trucksController,
                    driversController,
                    sitesController,
                    itemListsController,
                    employeesService,
                    dalFactory.transportsDAO());

        } catch (TransportException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public BusinessFactory(ServiceFactory serviceFactory, String dbName) throws DalException{

        dalFactory = new DalFactory(dbName);

        trucksController = new TrucksController(dalFactory.trucksDAO());
        sitesController = new SitesController(dalFactory.sitesDAO());
        driversController = new DriversController(dalFactory.driversDAO());
        shiftsController = new ShiftsController(dalFactory.shiftDAO());
        employeesController = new EmployeesController(shiftsController,dalFactory.branchesDAO(), dalFactory.employeeDAO());

        EmployeesService employeesService = serviceFactory.employeesService();

        try {
            itemListsController = new ItemListsController(dalFactory.itemListsDAO());
            transportsController = new TransportsController(trucksController,
                    driversController,
                    sitesController,
                    itemListsController,
                    employeesService,
                    dalFactory.transportsDAO());

        } catch (TransportException e) {
            throw new RuntimeException(e);
        }
    }

    public TransportsController transportsController() {
        return transportsController;
    }

    public void updateTransportsController(EmployeesService employeesService) {
        transportsController.setEmployeesService(employeesService);
    }

    public DriversController driversController() {
        return driversController;
    }

    public SitesController sitesController() {
        return sitesController;
    }

    public ItemListsController itemListsController() {
        return itemListsController;
    }

    public TrucksController trucksController() {
        return trucksController;
    }

    public EmployeesController employeesController() {
        return employeesController;
    }

    public ShiftsController shiftsController() {
        return shiftsController;
    }

    public DalFactory dalFactory() {
        return dalFactory;
    }
}
