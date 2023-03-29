package TransportModule.BusinessLayer;

public class BusinessFactory {

    private static BusinessFactory instance = null;

    private final TransportsController transportsController;
    private final SitesController sitesController;

    private final DriversController driversController;

    private final TrucksController trucksController;
    private final ItemListsController itemListsController;
    private BusinessFactory(){
        transportsController = new TransportsController();
        sitesController = new SitesController();
        driversController = new DriversController();
        trucksController = new TrucksController();
        itemListsController = new ItemListsController();
    }

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

    public static BusinessFactory getInstance(){
        if(instance == null){
            instance = new BusinessFactory();
        }
        return instance;
    }

}
