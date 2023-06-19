package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.Site;
import domainObjects.transportModule.Truck;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.transportModule.model.ObservableSite;
import presentationLayer.gui.transportModule.model.ObservableTruck;
import serviceLayer.transportModule.ResourceManagementService;
import utils.Response;

import java.util.List;

public class SitesControl extends AbstractControl {
    private final ResourceManagementService rms;

    public SitesControl(ResourceManagementService rms) {

        this.rms = rms;
    }

    @Override
    public void add(ObservableUIElement observable, ObservableModel model) {
        ObservableSite siteModel = (ObservableSite) model;

        Site site = new Site(siteModel.name,
                siteModel.address,
                siteModel.transportZone,
                siteModel.phoneNumber,
                siteModel.contactName,
                siteModel.siteType);
        String json = rms.addSite(site.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
            //TODO: handle exception
        }
        siteModel.response = response.message();
        siteModel.notifyObservers();
    }

    @Override
    public ObservableModel getModel(ObservableModel lookupObject) {
        ObservableSite site = (ObservableSite) lookupObject;
        Site toLookUp = Site.getLookupObject(site.name);
        String json = rms.getSite(toLookUp.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            throw new RuntimeException(e);
        }
        Site fetched = Site.fromJson(response.data());
        ObservableSite toReturn = new ObservableSite();
        toReturn.name = fetched.name();
        toReturn.address = fetched.address();
        toReturn.transportZone = fetched.transportZone();
        toReturn.phoneNumber = fetched.phoneNumber();
        toReturn.contactName = fetched.contactName();
        toReturn.siteType = fetched.siteType();
        return toReturn;
    }

    @Override
    public ObservableModel getEmptyModel() {
        return new ObservableSite();
    }

    @Override
    public List<ObservableModel> getAllModels() {
        return null;
    }
}
