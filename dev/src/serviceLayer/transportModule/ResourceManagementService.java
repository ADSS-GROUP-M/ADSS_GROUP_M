package serviceLayer.transportModule;

import businessLayer.transportModule.DriversController;
import businessLayer.transportModule.SitesController;
import businessLayer.transportModule.TrucksController;
import domainObjects.transportModule.Driver;
import domainObjects.transportModule.Site;
import domainObjects.transportModule.Truck;
import exceptions.TransportException;
import utils.JsonUtils;
import utils.Response;

public class ResourceManagementService {

    private final SitesController sitesController;
    private final DriversController driversController;
    private final TrucksController trucksController;

    public ResourceManagementService(SitesController sitesController,
                                     DriversController driversController,
                                     TrucksController trucksController)
    {
        this.sitesController = sitesController;
        this.driversController = driversController;
        this.trucksController = trucksController;
    }

    public String addDriver(String json){
        Driver driver = JsonUtils.deserialize(json, Driver.class);
        try{
            driversController.addDriver(driver);
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Driver added successfully", true).toJson();
    }

    /**
     * @param json serialized {@link Driver#getLookupObject(String)}
     */
    public String removeDriver(String json){
        Driver driver = JsonUtils.deserialize(json, Driver.class);
        try{
            driversController.removeDriver(driver.id());
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Driver removed successfully", true).toJson();
    }

    public String updateDriver(String json){
        Driver driver = JsonUtils.deserialize(json, Driver.class);
        try{
            driversController.updateDriver(driver.id(), driver);
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Driver updated successfully", true).toJson();
    }

    /**
     * @param json serialized {@link Driver#getLookupObject(String)}
     */
    public String getDriver(String json){
        Driver driver = JsonUtils.deserialize(json, Driver.class);
        try{
            driver = driversController.getDriver(driver.id());
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Driver found successfully", true, driver).toJson();
    }

    public String getAllDrivers(){
        try {
            return new Response("Drivers found successfully", true, driversController.getAllDrivers()).toJson();
        } catch (TransportException e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String addTruck(String json){
        Truck truck = JsonUtils.deserialize(json, Truck.class);
        try{
            trucksController.addTruck(truck);
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Truck added successfully", true).toJson();
    }

    /**
     * @param json serialized {@link Truck#getLookupObject(String)}
     */
    public String removeTruck(String json){
        Truck truck = JsonUtils.deserialize(json, Truck.class);
        try{
            trucksController.removeTruck(truck.id());
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Truck removed successfully", true).toJson();
    }

    public String updateTruck(String json){
        Truck truck = JsonUtils.deserialize(json, Truck.class);
        try{
            trucksController.updateTruck(truck.id(), truck);
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Truck updated successfully", true).toJson();
    }

    /**
     * @param json serialized {@link Truck#getLookupObject(String)}
     */
    public String getTruck(String json){
        Truck truck = JsonUtils.deserialize(json, Truck.class);
        try{
            truck = trucksController.getTruck(truck.id());
        }catch(Exception e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Truck found successfully", true, truck).toJson();
    }

    public String getAllTrucks(){
        try {
            return new Response("Trucks found successfully", true, trucksController.getAllTrucks()).toJson();
        } catch (TransportException e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String addSite(String json){
        Site site = JsonUtils.deserialize(json, Site.class);
        Site added;
        try{
            added = sitesController.addSite(site);
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Site added successfully", true,added).toJson();
    }

    /**
     * @param json serialized {@link Site#getLookupObject(String)}
     * @deprecated this operation is not fully supported and may result in inconsistent state
     */
    @Deprecated
    public String removeSite(String json){
        Site site = JsonUtils.deserialize(json, Site.class);
        try{
            sitesController.removeSite(site.name());
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Site removed successfully", true).toJson();
    }

    public String updateSite(String json){
        Site site = JsonUtils.deserialize(json, Site.class);
        Site updated;
        try{
            updated = sitesController.updateSite(site.name(), site);
        }catch(TransportException e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Site updated successfully", true, updated).toJson();
    }

    /**
     * @param json serialized {@link Site#getLookupObject(String)}
     */
    public String getSite(String json){
        Site site = JsonUtils.deserialize(json, Site.class);
        try{
            site = sitesController.getSite(site.name());
        }catch(Exception e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Site found successfully", true, site).toJson();
    }

    public String getAllSites(){
        try {
            return new Response("Sites found successfully", true, sitesController.getAllSites()).toJson();
        } catch (TransportException e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

}
