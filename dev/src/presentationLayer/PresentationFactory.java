package presentationLayer;

import presentationLayer.gui.transportModule.control.TrucksControl;
import serviceLayer.ServiceFactory;

public class PresentationFactory {

    private ServiceFactory serviceFactory;
    private TrucksControl trucksControl;



    public PresentationFactory() {

        serviceFactory = new ServiceFactory();

        trucksControl = new TrucksControl(serviceFactory.resourceManagementService());
    }

    public TrucksControl getTrucksControl() {
        return trucksControl;
    }

}
