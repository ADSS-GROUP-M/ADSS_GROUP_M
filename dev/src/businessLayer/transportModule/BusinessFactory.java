package businessLayer.transportModule;

import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import serviceLayer.employeeModule.Services.EmployeesService;

public class BusinessFactory {

    private final TransportsController transportsController;
    private final DriversController driversController;

    private final SitesController sitesController;

    private final ItemListsController itemListsController;
    private final TrucksController trucksController;

    public BusinessFactory() throws DalException{

        DalFactory dalFactory = new DalFactory();

        trucksController = new TrucksController(dalFactory.trucksDAO());
        itemListsController = new ItemListsController(dalFactory.itemListsDAO());
        sitesController = new SitesController(dalFactory.sitesDAO());
        driversController = new DriversController(dalFactory.driversDAO());

        EmployeesService employeesService = EmployeesService.getInstance();

        transportsController = new TransportsController(trucksController,
                driversController,
                sitesController,
                itemListsController,
                employeesService,
                dalFactory.transportsDAO());

    }

    public TransportsController transportsController() {
        return transportsController;
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
}
