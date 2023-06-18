package presentationLayer;

import presentationLayer.gui.transportModule.control.*;
import serviceLayer.ServiceFactory;

public class PresentationFactory {
    private ServiceFactory serviceFactory;
    private DriversControl driversControl;
    private ItemListsControl itemListsControl;
    private SitesControl sitesControl;
    private TransportsControl transportsControl;
    private TrucksControl trucksControl;

    public PresentationFactory() {
        serviceFactory = new ServiceFactory();
        driversControl = new DriversControl(serviceFactory.resourceManagementService());
        itemListsControl = new ItemListsControl(serviceFactory.resourceManagementService());
        sitesControl = new SitesControl(serviceFactory.resourceManagementService());
        transportsControl = new TransportsControl(serviceFactory.resourceManagementService());
        trucksControl = new TrucksControl(serviceFactory.resourceManagementService());
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
}
