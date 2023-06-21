package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.Driver;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.plUtils.ObservableList;
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
            driver.errorOccurred = true;
            driver.errorMessage = e.getMessage();
            driver.notifyObservers();
            return;
        }

        driver.message = response.message();
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
            driver.errorOccurred = true;
            driver.errorMessage = e.getMessage();
            driver.notifyObservers();
            return;
        }
        Driver fetched = Driver.fromJson(response.data());

        driver.id = fetched.id();
        driver.name = fetched.name();
        driver.licenseType = fetched.licenseType();
        driver.message = response.message();
        driver.notifyObservers();
    }

    @Override
    public void getAll(ObservableUIElement observable, ObservableList<ObservableModel> models) {

        String json = rms.getAllDrivers();
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            models.errorOccurred = true;
            models.errorMessage = e.getMessage();
            models.notifyObservers();
            return;
        }

        List<Driver> fetched = Driver.listFromJson(response.data());
        for (Driver driver : fetched) {
            ObservableDriver observableDriver = new ObservableDriver();
            observableDriver.id = driver.id();
            observableDriver.name = driver.name();
            observableDriver.licenseType = driver.licenseType();
            models.add(observableDriver);
        }

        models.message = response.message();
        models.notifyObservers();
    }
}
