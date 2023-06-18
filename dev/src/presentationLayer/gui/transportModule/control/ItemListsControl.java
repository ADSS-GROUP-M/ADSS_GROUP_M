package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.ItemList;
import domainObjects.transportModule.Truck;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.transportModule.model.ObservableItemList;
import presentationLayer.gui.transportModule.model.ObservableTruck;
import serviceLayer.transportModule.ResourceManagementService;
import utils.Response;

import java.util.List;

public class ItemListsControl extends AbstractControl {
    private final ResourceManagementService rms;

    public ItemListsControl(ResourceManagementService rms) {

        this.rms = rms;
    }


    @Override
    public ObservableModel getModel(ObservableModel lookupObject) {
        return null;
    }

    @Override
    public ObservableModel getEmptyModel() {
        return new ObservableItemList();
    }

    @Override
    public List<ObservableModel> getAllModels() {
        return null;
    }
}
