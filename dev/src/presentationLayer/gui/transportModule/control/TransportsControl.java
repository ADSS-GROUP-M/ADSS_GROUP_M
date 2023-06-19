package presentationLayer.gui.transportModule.control;

import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.transportModule.model.ObservableTransport;
import serviceLayer.transportModule.ResourceManagementService;
import serviceLayer.transportModule.TransportsService;

import java.util.List;

public class TransportsControl extends AbstractControl {
    private final TransportsService rms;

    public TransportsControl(TransportsService rms) {

        this.rms = rms;
    }

//    @Override
//    public void add(ObservableUIElement observable, ObservableModel model) {
//        ObservableTransport transportModel = (ObservableTransport) model;
//
//        Transport transport = new Transport(transport.de);
//        String json = rms.addTruck(transport.toJson());
//        Response response;
//        try {
//            response = Response.fromJsonWithValidation(json);
//        } catch (ErrorOccurredException e) {
//            throw new RuntimeException(e);
//            //TODO: handle exception
//        }
//        truckModel.response = response.message();
//        truckModel.notifyObservers();
//    }

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
