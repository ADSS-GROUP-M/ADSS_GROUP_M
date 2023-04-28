package businessLayer;

import businessLayer.employeeModule.Controllers.EmployeesController;
import businessLayer.employeeModule.Controllers.ShiftsController;
import businessLayer.transportModule.*;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import serviceLayer.employeeModule.Services.EmployeesService;
import utils.transportUtils.TransportException;

public class BusinessFactory {

    private TransportsController transportsController;
    private DriversController driversController;
    private SitesController sitesController;
    private ItemListsController itemListsController;
    private TrucksController trucksController;
    private EmployeesController employeesController;
    private ShiftsController shiftsController;
    private final DalFactory dalFactory;

    public BusinessFactory() throws DalException{
        dalFactory = new DalFactory();
        buildInstances();
    }

    private void buildInstances() {
        trucksController = new TrucksController(dalFactory.trucksDAO());
        sitesController = new SitesController(dalFactory.sitesDAO());
        driversController = new DriversController(dalFactory.driversDAO());
        shiftsController = new ShiftsController(dalFactory.shiftDAO());
        employeesController = new EmployeesController(shiftsController, dalFactory.branchesDAO(), dalFactory.employeeDAO());

        try {
            itemListsController = new ItemListsController(dalFactory.itemListsDAO());
            transportsController = new TransportsController(trucksController,
                    driversController,
                    sitesController,
                    itemListsController,
                    dalFactory.transportsDAO());
        } catch (TransportException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public BusinessFactory(String dbName) throws DalException{

        dalFactory = new DalFactory(dbName);
        buildInstances();
    }

    public TransportsController transportsController() {
        return transportsController;
    }

    public void injectDependencies(EmployeesService employeesService) {
        transportsController.injectDependencies(employeesService);
        sitesController.injectDependencies(employeesService);
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
