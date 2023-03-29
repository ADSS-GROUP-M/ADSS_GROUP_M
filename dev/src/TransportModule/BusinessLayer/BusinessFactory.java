package TransportModule.ServiceLayer;

import TransportModule.BusinessLayer.*;

public class ServiceFactory {

    private static ServiceFactory instance = null;
    
    private final TransportsController transportsController;
    private final SitesController sitesController;

    public TransportsController getTransportsController() {
        return transportsController;
    }

    public SitesController getSitesController() {
        return sitesController;
    }

    public DriversController getDriversController() {
        return driversController;
    }

    public TrucksController getTrucksController() {
        return trucksController;
    }

    public ItemListsController getItemListsController() {
        return itemListsController;
    }

    private final DriversController driversController;
    private final TrucksController trucksController;
    private final ItemListsController itemListsController;

    private ServiceFactory(){
        transportsController = new TransportsController();
        sitesController = new SitesController();
        driversController = new DriversController();
        trucksController = new TrucksController();
        itemListsController = new ItemListsController();
    }

    public static ServiceFactory getInstance(){
        if(instance == null){
            instance = new ServiceFactory();
        }
        return instance;
    }

}
