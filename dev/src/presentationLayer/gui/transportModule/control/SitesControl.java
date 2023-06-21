package presentationLayer.gui.transportModule.control;

import domainObjects.transportModule.Site;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.transportModule.model.ObservableSite;
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
            siteModel.errorOccurred = true;
            siteModel.errorMessage = e.getMessage();
            siteModel.notifyObservers();
            return;
        }
        siteModel.message = response.message();
        siteModel.notifyObservers();
    }

    @Override
    public void update(ObservableUIElement observable, ObservableModel model) {
        ObservableSite siteModel = (ObservableSite) model;

        Site site = new Site(siteModel.name,
                siteModel.address,
                siteModel.transportZone,
                siteModel.phoneNumber,
                siteModel.contactName,
                siteModel.siteType);
        String json = rms.updateSite(site.toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            siteModel.errorOccurred = true;
            siteModel.errorMessage = e.getMessage();
            siteModel.notifyObservers();
            return;
        }

        siteModel.message = response.message();
        siteModel.notifyObservers();
    }

    @Override
    public void get(ObservableUIElement observable, ObservableModel model) {
        ObservableSite siteModel = (ObservableSite) model;

        String json = rms.getSite(Site.getLookupObject(siteModel.name).toJson());
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            siteModel.errorOccurred = true;
            siteModel.errorMessage = e.getMessage();
            siteModel.notifyObservers();
            return;
        }
        Site fetched = Site.fromJson(response.data());
        siteModel.name = fetched.name();
        siteModel.address = fetched.address();
        siteModel.transportZone = fetched.transportZone();
        siteModel.phoneNumber = fetched.phoneNumber();
        siteModel.contactName = fetched.contactName();
        siteModel.siteType = fetched.siteType();
        siteModel.latitude = fetched.latitude();
        siteModel.longitude = fetched.longitude();

        siteModel.message = response.message();
        siteModel.notifyObservers();
    }

    @Override
    public void getAll(ObservableUIElement observable, ObservableList<ObservableModel> models) {
        String json = rms.getAllSites();
        Response response;
        try {
            response = Response.fromJsonWithValidation(json);
        } catch (ErrorOccurredException e) {
            models.errorOccurred = true;
            models.errorMessage = e.getMessage();
            models.notifyObservers();
            return;
        }
        List<Site> sites = Site.listFromJson(response.data());
        for (Site site : sites) {
            ObservableSite siteModel = new ObservableSite();
            siteModel.name = site.name();
            siteModel.address = site.address();
            siteModel.transportZone = site.transportZone();
            siteModel.phoneNumber = site.phoneNumber();
            siteModel.contactName = site.contactName();
            siteModel.siteType = site.siteType();
            models.add(siteModel);
        }
        models.notifyObservers();
    }
}
