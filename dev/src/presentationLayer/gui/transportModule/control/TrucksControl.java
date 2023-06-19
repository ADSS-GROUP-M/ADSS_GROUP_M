package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.Truck;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.transportModule.model.ObservableTruck;
import serviceLayer.transportModule.ResourceManagementService;
import utils.Response;

import java.util.ArrayList;
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
    public void update(ObservableUIElement observable, ObservableModel model) {
        ObservableTruck truckModel = (ObservableTruck) model;

        Truck truck = new Truck(truckModel.id,
                truckModel.model,
                truckModel.baseWeight,
                truckModel.maxWeight,
                truckModel.coolingCapacity);
        String json = rms.updateTruck(truck.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        truckModel.response = response.message();
        truckModel.notifyObservers();
    }

    @Override
    public void remove(ObservableUIElement observable, ObservableModel model) {
        ObservableTruck truckModel = (ObservableTruck) model;

        String json = rms.removeTruck(Truck.getLookupObject(truckModel.id).toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        truckModel.response = response.message();
        truckModel.notifyObservers();
    }

    @Override
    public void get(ObservableUIElement observable, ObservableModel model) {
        ObservableTruck truckModel = (ObservableTruck) model;

        String json = rms.getTruck(Truck.getLookupObject(truckModel.id).toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        Truck fetched = Truck.fromJson(response.data());
        truckModel.id = fetched.id();
        truckModel.model = fetched.model();
        truckModel.baseWeight = fetched.baseWeight();
        truckModel.maxWeight = fetched.maxWeight();
        truckModel.coolingCapacity = fetched.coolingCapacity();

        truckModel.response = response.message();
        truckModel.notifyObservers();
    }

    @Override
    public void getAll(ObservableUIElement observable, ObservableList<ObservableModel> models) {

        String json = rms.getAllTrucks();
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        List<Truck> fetched = Truck.listFromJson(response.data());
        for (Truck truck : fetched) {
            ObservableTruck truckModel = new ObservableTruck();
            truckModel.id = truck.id();
            truckModel.model = truck.model();
            truckModel.baseWeight = truck.baseWeight();
            truckModel.maxWeight = truck.maxWeight();
            truckModel.coolingCapacity = truck.coolingCapacity();
            models.add(truckModel);
        }

        models.message = response.message();
        models.notifyObservers();
    }
}
