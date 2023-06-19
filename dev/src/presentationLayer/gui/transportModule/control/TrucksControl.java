package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.Truck;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
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
    public ObservableModel getModel(ObservableModel lookupObject) {
        ObservableTruck truck = (ObservableTruck) lookupObject;
        Truck toLookUp = Truck.getLookupObject(truck.id);
        String json = rms.getTruck(toLookUp.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }
        Truck fetched = Truck.fromJson(response.data());
        ObservableTruck toReturn = new ObservableTruck();
        toReturn.id = fetched.id();
        toReturn.model = fetched.model();
        toReturn.coolingCapacity = fetched.coolingCapacity();
        toReturn.baseWeight = fetched.baseWeight();
        toReturn.maxWeight = fetched.maxWeight();
        return toReturn;
    }

    @Override
    public ObservableModel getEmptyModel() {
        return new ObservableTruck();
    }

    @Override
    public List<ObservableModel> getAllModels() {
        List<ObservableModel> toReturn = new ArrayList<>();
        String json = rms.getAllTrucks();
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }
        List<Truck> fetchedList = Truck.listFromJson(response.data());
        ObservableTruck observableTruck = new ObservableTruck();
        for(Truck truck : fetchedList){
            observableTruck.id = truck.id();
            observableTruck.model = truck.model();
            observableTruck.coolingCapacity = truck.coolingCapacity();
            observableTruck.baseWeight = truck.baseWeight();
            observableTruck.maxWeight = truck.maxWeight();
            toReturn.add(observableTruck);
        }
        return toReturn;
    }
}
