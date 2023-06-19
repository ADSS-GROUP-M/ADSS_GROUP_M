package presentationLayer.gui.transportModule.control;

import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.transportModule.model.ObservableDriver;
import presentationLayer.gui.transportModule.model.ObservableTruck;
import serviceLayer.transportModule.ResourceManagementService;

import java.util.List;

public class DriversControl extends AbstractControl {
    private final ResourceManagementService rms;

    public DriversControl(ResourceManagementService rms) {

        this.rms = rms;
    }


    @Override
    public ObservableModel getModel(ObservableModel lookupObject) {
        return null;
    }

    @Override
    public ObservableModel getEmptyModel() {
        return new ObservableDriver();
    }

    @Override
    public List<ObservableModel> getAllModels() {
        return null;
    }
}
