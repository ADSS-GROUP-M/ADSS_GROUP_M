package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.Transport;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.transportModule.model.ObservableTransport;
import serviceLayer.transportModule.TransportsService;
import utils.Response;

import java.util.List;

public class TransportsControl extends AbstractControl {
    private final TransportsService ts;

    public TransportsControl(TransportsService ts) {

        this.ts = ts;
    }

    @Override
    public void add(ObservableUIElement observable, ObservableModel model) {
        ObservableTransport transportModel = (ObservableTransport) model;

        Transport transport = new Transport(
                transportModel.route,
                transportModel.destinations_itemListIds,
                transportModel.driverId,
                transportModel.truckId,
                transportModel.departureTime,
                transportModel.weight);
        String json = ts.addTransport(transport.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            transportModel.errorOccurred = true;
            transportModel.errorMessage = e.getMessage();
            transportModel.notifyObservers();
            return;
        }
        Transport added = Transport.fromJson(response.data());
        transportModel.id = added.id();
        transportModel.estimatedArrivalTimes = added.estimatedArrivalTimes();

        transportModel.message = response.message();
        transportModel.notifyObservers();
    }

    @Override
    public void update(ObservableUIElement observable, ObservableModel model) {
        ObservableTransport transportModel = (ObservableTransport) model;

        Transport transport = new Transport(
                transportModel.id,
                transportModel.route,
                transportModel.destinations_itemListIds,
                transportModel.driverId,
                transportModel.truckId,
                transportModel.departureTime,
                transportModel.weight,
                transportModel.estimatedArrivalTimes,
                transportModel.manualOverride);
        String json = ts.updateTransport(transport.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            transportModel.errorOccurred = true;
            transportModel.errorMessage = e.getMessage();
            transportModel.notifyObservers();
            return;
        }
        Transport updated = Transport.fromJson(response.data());
        transportModel.estimatedArrivalTimes = updated.estimatedArrivalTimes();
        transportModel.message = response.message();
        transportModel.notifyObservers();
    }

    @Override
    public void remove(ObservableUIElement observable, ObservableModel model) {
        ObservableTransport transportModel = (ObservableTransport) model;

        String json = ts.removeTransport(Transport.getLookupObject(transportModel.id).toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            transportModel.errorOccurred = true;
            transportModel.errorMessage = e.getMessage();
            transportModel.notifyObservers();
            return;
        }
        transportModel.message = response.message();
        transportModel.notifyObservers();
    }

    @Override
    public void get(ObservableUIElement observable, ObservableModel model) {
        ObservableTransport transportModel = (ObservableTransport) model;
        Transport toGet = Transport.getLookupObject(transportModel.id);
        String json = ts.getTransport(toGet.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            transportModel.errorOccurred = true;
            transportModel.errorMessage = e.getMessage();
            transportModel.notifyObservers();
            return;
        }
        Transport got = Transport.fromJson(response.data());
        transportModel.route = got.route();
        transportModel.destinations_itemListIds = got.itemLists();
        transportModel.driverId = got.driverId();
        transportModel.truckId = got.truckId();
        transportModel.departureTime = got.departureTime();
        transportModel.weight = got.weight();
        transportModel.estimatedArrivalTimes = got.estimatedArrivalTimes();
        transportModel.manualOverride = got.arrivalTimesManualOverride();
        transportModel.message = response.message();
        transportModel.notifyObservers();
    }

    @Override
    public void getAll(ObservableUIElement observable, ObservableList<ObservableModel> models) {
        String json = ts.getAllTransports();
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            models.errorOccurred = true;
            models.errorMessage = e.getMessage();
            models.notifyObservers();
            return;
        }
        List<Transport> got = Transport.listFromJson(response.data());
        for (Transport transport : got) {
            ObservableTransport transportModel = new ObservableTransport();
            transportModel.id = transport.id();
            transportModel.route = transport.route();
            transportModel.destinations_itemListIds = transport.itemLists();
            transportModel.driverId = transport.driverId();
            transportModel.truckId = transport.truckId();
            transportModel.departureTime = transport.departureTime();
            transportModel.weight = transport.weight();
            transportModel.estimatedArrivalTimes = transport.estimatedArrivalTimes();
            transportModel.manualOverride = transport.arrivalTimesManualOverride();
            models.add(transportModel);
        }
    }
}
