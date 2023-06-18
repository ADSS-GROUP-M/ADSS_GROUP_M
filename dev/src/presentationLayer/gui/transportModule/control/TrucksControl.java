package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.Truck;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.transportModule.model.ObservableTruck;
import serviceLayer.transportModule.ResourceManagementService;
import utils.Response;

import java.awt.event.MouseAdapter;
import java.util.List;

public class TrucksControl extends AbstractControl {
    private final ResourceManagementService rms;

    public TrucksControl(ResourceManagementService rms) {

        this.rms = rms;
    }

    @Override
    public void add(ObservableUIElement observable, ObservableModel model) {
        ObservableTruck truckModel = (ObservableTruck) model;

        Truck truck = new Truck(truckModel.id,
                truckModel.model,
                truckModel.baseWeight,
                truckModel.maxWeight,
                truckModel.coolingCapacity);
        String json = rms.addTruck(truck.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
            //TODO: handle exception
        }
        truckModel.response = response.message();
        truckModel.notifyObservers();
    }

    @Override
    public void remove(ObservableUIElement observable, ObservableModel model) {
        ObservableTruck truckModel = (ObservableTruck) model;

        Truck truck = new Truck(truckModel.id,
                truckModel.model,
                truckModel.baseWeight,
                truckModel.maxWeight,
                truckModel.coolingCapacity);
        String json = rms.addTruck(truck.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
            //TODO: handle exception
        }
        truckModel.response = response.message();
        truckModel.notifyObservers();
    }


    @Override
    public ObservableModel getModel(ObservableModel lookupObject) {
        return null;
    }

    @Override
    public ObservableModel getEmptyModel() {
        return new ObservableTruck();
    }

    @Override
    public List<ObservableModel> getAllModels() {
        return null;
    }
}
