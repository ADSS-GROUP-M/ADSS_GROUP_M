package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.Driver;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.transportModule.model.ObservableDriver;
import serviceLayer.transportModule.ResourceManagementService;
import utils.Response;

import java.util.List;

public class DriversControl extends AbstractControl {
    private final ResourceManagementService rms;

    public DriversControl(ResourceManagementService rms) {

        this.rms = rms;
    }

    @Override
    public void update(ObservableUIElement observable, ObservableModel model) {
        ObservableDriver driver = (ObservableDriver) model;
        Driver toAdd =  new Driver(driver.id, driver.name, driver.licenseType);
        String json = rms.updateDriver(toAdd.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        driver.response = response.message();
        driver.notifyObservers();
    }

    @Override
    public void remove(ObservableUIElement observable, ObservableModel model) {
        ObservableDriver driver = (ObservableDriver) model;
        Driver toRemove = Driver.getLookupObject(driver.id);
        String json = rms.removeDriver(toRemove.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }

        driver.response = response.message();
        driver.notifyObservers();
    }

    @Override
    public void get(ObservableUIElement observable, ObservableModel model) {
        ObservableDriver driver = (ObservableDriver) model;
        Driver toGet = Driver.getLookupObject(driver.id);
        String json = rms.getDriver(toGet.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }
        Driver fetched = Driver.fromJson(response.data());

        driver.id = fetched.id();
        driver.name = fetched.name();
        driver.licenseType = fetched.licenseType();
        driver.response = response.message();
        driver.notifyObservers();
    }

    @Override
    public void getAll(ObservableUIElement observable, List<ObservableModel> models) {
        super.getAll(observable, );
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
