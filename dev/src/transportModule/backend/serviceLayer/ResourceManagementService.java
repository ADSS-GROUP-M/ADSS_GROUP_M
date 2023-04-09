package transportModule.backend.serviceLayer;

import transportModule.backend.businessLayer.DriversController;
import transportModule.backend.businessLayer.SitesController;
import transportModule.backend.businessLayer.TrucksController;
import transportModule.backend.businessLayer.records.Driver;
import transportModule.backend.businessLayer.records.Site;
import transportModule.backend.businessLayer.records.Truck;

import java.io.IOException;

public class ResourceManagementService {

    private final SitesController sitesController;
    private final DriversController driversController;
    private final TrucksController trucksController;

    public ResourceManagementService(SitesController sitesController, DriversController driversController, TrucksController trucksController) {
        this.sitesController = sitesController;
        this.driversController = driversController;
        this.trucksController = trucksController;
    }

    public String addDriver(String json){
        Driver driver = JSON.deserialize(json, Driver.class);
        try{
            driversController.addDriver(driver);
        }catch(IOException e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Driver added successfully", true).getJson();
    }

    public String removeDriver(String json){
        Driver driver = JSON.deserialize(json, Driver.class);
        try{
            driversController.removeDriver(driver.id());
        }catch(IOException e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Driver removed successfully", true).getJson();
    }

    public String updateDriver(String json){
        Driver driver = JSON.deserialize(json, Driver.class);
        try{
            driversController.updateDriver(driver.id(), driver);
        }catch(IOException e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Driver updated successfully", true).getJson();
    }

    public String getDriver(String json){
        Driver driver = JSON.deserialize(json, Driver.class);
        try{
            driver = driversController.getDriver(driver.id());
        }catch(Exception e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Driver found successfully", true, driver).getJson();
    }

    public String getAllDrivers(){
        return new Response<>("Drivers found successfully", true, driversController.getAllDrivers()).getJson();
    }

    public String addTruck(String json){
        Truck truck = JSON.deserialize(json, Truck.class);
        try{
            trucksController.addTruck(truck);
        }catch(IOException e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Truck added successfully", true).getJson();
    }

    public String removeTruck(String json){
        Truck truck = JSON.deserialize(json, Truck.class);
        try{
            trucksController.removeTruck(truck.id());
        }catch(IOException e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Truck removed successfully", true).getJson();
    }

    public String updateTruck(String json){
        Truck truck = JSON.deserialize(json, Truck.class);
        try{
            trucksController.updateTruck(truck.id(), truck);
        }catch(IOException e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Truck updated successfully", true).getJson();
    }

    public String getTruck(String json){
        Truck truck = JSON.deserialize(json, Truck.class);
        try{
            truck = trucksController.getTruck(truck.id());
        }catch(Exception e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Truck found successfully", true, truck).getJson();
    }

    public String getAllTrucks(){
        return new Response<>("Trucks found successfully", true, trucksController.getAllTrucks()).getJson();
    }

    public String addSite(String json){
        Site site = JSON.deserialize(json, Site.class);
        try{
            sitesController.addSite(site);
        }catch(IOException e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Site added successfully", true).getJson();
    }

    public String removeSite(String json){
        Site site = JSON.deserialize(json, Site.class);
        try{
            sitesController.removeSite(site.address());
        }catch(IOException e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Site removed successfully", true).getJson();
    }

    public String updateSite(String json){
        Site site = JSON.deserialize(json, Site.class);
        try{
            sitesController.updateSite(site.address(), site);
        }catch(IOException e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Site updated successfully", true).getJson();
    }

    public String getSite(String json){
        Site site = JSON.deserialize(json, Site.class);
        try{
            site = sitesController.getSite(site.address());
        }catch(Exception e){
            return Response.getErrorResponse(e).getJson();
        }
        return new Response<>("Site found successfully", true, site).getJson();
    }

    public String getAllSites(){
        return new Response<>("Sites found successfully", true, sitesController.getAllSites()).getJson();
    }

}
