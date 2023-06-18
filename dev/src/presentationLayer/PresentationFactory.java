package presentationLayer;

import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.transportModule.control.TrucksControl;
import serviceLayer.ServiceFactory;

public class PresentationFactory {

    private ServiceFactory serviceFactory;
    private TrucksControl trucksControl;
    private ShiftsControl shiftsControl;



    public PresentationFactory() {

        serviceFactory = new ServiceFactory();

        trucksControl = new TrucksControl(serviceFactory.resourceManagementService());
        shiftsControl = new ShiftsControl(serviceFactory.employeesService(), serviceFactory.resourceManagementService());
    }

    public TrucksControl getTrucksControl() {
        return trucksControl;
    }

    public ShiftsControl shiftsControl() {
        return shiftsControl;
    }
}
