package businessLayer;

import businessLayer.employeeModule.Controllers.EmployeesController;
import businessLayer.employeeModule.Controllers.ShiftsController;
import businessLayer.employeeModule.Controllers.UserController;
import businessLayer.transportModule.*;
import businessLayer.transportModule.bingApi.BingAPI;
import dataAccessLayer.DalFactory;
import exceptions.DalException;
import serviceLayer.employeeModule.Services.EmployeesService;
import exceptions.TransportException;

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

    private SitesRoutesController distancesController;

    public BusinessFactory() throws DalException{
        dalFactory = new DalFactory();
        buildInstances();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public BusinessFactory(String dbName) throws DalException{
        dalFactory = new DalFactory(dbName);
        buildInstances();
    }

    private void buildInstances() {
        userController = new UserController(dalFactory.userDAO());

        //==================== Dependencies ======================= |
        /*(1)*/ shiftsController = new ShiftsController(dalFactory.shiftDAO());
        /*(2)*/ employeesController = new EmployeesController(shiftsController, dalFactory.branchesDAO(), dalFactory.employeeDAO());
        //========================================================= |

        try {
            // ======================== Dependencies ===================== |
            /*(1)*/ distancesController = new SitesRoutesController(new BingAPI());
            /*(1)*/ trucksController = new TrucksController(dalFactory.trucksDAO());
            /*(1)*/ sitesController = new SitesController(dalFactory.sitesDAO(), dalFactory.sitesDistancesDAO(), distancesController);
            /*(1)*/ driversController = new DriversController(dalFactory.driversDAO());
            /*(1)*/ itemListsController = new ItemListsController(dalFactory.itemListsDAO());
            /*(2)*/ transportsController = new TransportsController(trucksController,
                    driversController,
                    sitesController,
                    itemListsController,
                    dalFactory.transportsDAO()
            );
            //========================================================= |
        } catch (TransportException e) {
            throw new RuntimeException(e);
        }
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

    public SitesRoutesController getDistancesController() {
        return distancesController;
    }

    public DalFactory dalFactory() {
        return dalFactory;
    }
}
