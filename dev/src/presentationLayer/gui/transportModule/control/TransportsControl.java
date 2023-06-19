package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.Transport;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.transportModule.model.ObservableTransport;
import serviceLayer.transportModule.ResourceManagementService;
import serviceLayer.transportModule.TransportsService;

import java.util.List;

public class TransportsControl extends AbstractControl {
    private final TransportsService rms;

    public TransportsControl(TransportsService rms) {

        this.rms = rms;
    }

    @Override
    public void add(ObservableUIElement observable, ObservableModel model) {
        ObservableTransport transportModel = (ObservableTransport) model;

        Transport transport = new Transport(
                transportModel.id,
                transportModel.route,
                transportModel.destinations_itemListIds
                transportModel.truckId,
                transportModel.driverId,
                transportModel.
        );
        String json = rms.addTransport(transport.toJson());
        transportModel.response = json;
        transportModel.notifyObservers();

    }

    @Override
    public ObservableModel getModel(ObservableModel lookupObject) {
        return null;
    }

    @Override
    public ObservableModel getEmptyModel() {
        return new ObservableTransport();
    }

    @Override
    public List<ObservableModel> getAllModels() {
        return null;
    }
}
