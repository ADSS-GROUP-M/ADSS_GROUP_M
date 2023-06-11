package businessLayer;

import businessLayer.employeeModule.Controllers.EmployeesController;
import businessLayer.employeeModule.Controllers.ShiftsController;
import businessLayer.employeeModule.Controllers.UserController;
import businessLayer.transportModule.*;
import businessLayer.transportModule.bingApi.BingAPI;
import dataAccessLayer.DalFactory;
import exceptions.DalException;
import exceptions.TransportException;
import serviceLayer.employeeModule.Services.EmployeesService;

public class BusinessFactory {

    private TransportsController transportsController;
    private DriversController driversController;
    private SitesController sitesController;
    private ItemListsController itemListsController;
    private TrucksController trucksController;
    private EmployeesController employeesController;
    private ShiftsController shiftsController;
    private UserController userController;
    private final DalFactory dalFactory;
    private SitesRoutesController sitesRoutesController;

    public BusinessFactory() throws TransportException{
        try {
            dalFactory = new DalFactory();
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
        buildInstances();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public BusinessFactory(String dbName) throws TransportException{
        try {
            dalFactory = new DalFactory(dbName);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
        buildInstances();
    }

    private void buildInstances() throws TransportException {
        userController = new UserController(dalFactory.userDAO()); // independent

        //==================== Dependencies ======================= |
        /*(1)*/ shiftsController = new ShiftsController(dalFactory.shiftDAO());
        /*(2)*/ employeesController = new EmployeesController(
                    shiftsController,
                    dalFactory.branchesDAO(),
                    dalFactory.employeeDAO());
        //========================================================= |

        // ======================== Dependencies ===================== |
        /*(1)*/ sitesRoutesController = new SitesRoutesController(new BingAPI(),dalFactory.sitesRoutesDAO());
        /*(1)*/ trucksController = new TrucksController(dalFactory.trucksDAO());
        /*(1)*/ driversController = new DriversController(dalFactory.driversDAO());
        /*(1)*/ itemListsController = new ItemListsController(dalFactory.itemListsDAO());
        /*(2)*/ sitesController = new SitesController(dalFactory.sitesDAO(), sitesRoutesController);
        /*(3)*/ transportsController = new TransportsController(
                    trucksController,
                    driversController,
                    sitesController,
                    itemListsController,
                    sitesRoutesController,
                    dalFactory.transportsDAO());
        //========================================================= |
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

    public UserController userController() {
        return userController;
    }

    public SitesRoutesController sitesRoutesController() {
        return sitesRoutesController;
    }

    public DalFactory dalFactory() {
        return dalFactory;
    }
}
