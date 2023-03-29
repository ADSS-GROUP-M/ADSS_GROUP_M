package TransportModule.ServiceLayer;

import TransportModule.BusinessLayer.*;

import java.io.IOException;

public class ModuleManagementService {

    private final TrucksController trucksController;
    private final DriversController driversController;
    private final SitesController sitesController;

    public ModuleManagementService(){
        trucksController = BusinessFactory.getInstance().getTrucksController();
        driversController = BusinessFactory.getInstance().getDriversController();
        sitesController = BusinessFactory.getInstance().getSitesController();
    }

    public String addDriver(String json){
        Driver driver = JSON.deserialize(json, Driver.class);
        try{
            driversController.addDriver(driver);
        }catch(IOException e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Driver added successfully", true).getJson();
    }

    public String removeDriver(String json){
        Driver driver = JSON.deserialize(json, Driver.class);
        try{
            driversController.removeDriver(driver.getId());
        }catch(IOException e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Driver removed successfully", true).getJson();
    }

    public String updateDriver(String json){
        Driver driver = JSON.deserialize(json, Driver.class);
        try{
            driversController.updateDriver(driver.getId(), driver);
        }catch(IOException e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Driver updated successfully", true).getJson();
    }

    public String getDriver(String json){
        Driver driver = JSON.deserialize(json, Driver.class);
        try{
            driver = driversController.getDriver(driver.getId());
        }catch(Exception e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Driver found successfully", true, driver).getJson();
    }

    public String getAllDrivers(){
        return new Response("Drivers found successfully", true, driversController.getAllDrivers()).getJson();
    }

    public String addTruck(String json){
        Truck truck = JSON.deserialize(json, Truck.class);
        try{
            trucksController.addTruck(truck);
        }catch(IOException e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Truck added successfully", true).getJson();
    }

    public String removeTruck(String json){
        Truck truck = JSON.deserialize(json, Truck.class);
        try{
            trucksController.removeTruck(truck.getId());
        }catch(IOException e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Truck removed successfully", true).getJson();
    }

    public String updateTruck(String json){
        Truck truck = JSON.deserialize(json, Truck.class);
        try{
            trucksController.updateTruck(truck.getId(), truck);
        }catch(IOException e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Truck updated successfully", true).getJson();
    }

    public String getTruck(String json){
        Truck truck = JSON.deserialize(json, Truck.class);
        try{
            truck = trucksController.getTruck(truck.getId());
        }catch(Exception e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Truck found successfully", true, truck).getJson();
    }

    public String getAllTrucks(){
        return new Response("Trucks found successfully", true, trucksController.getAllTrucks()).getJson();
    }

    public String addSite(String json){
        Site site = JSON.deserialize(json, Site.class);
        try{
            sitesController.addSite(site);
        }catch(IOException e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Site added successfully", true).getJson();
    }

    public String removeSite(String json){
        Site site = JSON.deserialize(json, Site.class);
        try{
            sitesController.removeSite(site.getAddress());
        }catch(IOException e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Site removed successfully", true).getJson();
    }

    public String updateSite(String json){
        Site site = JSON.deserialize(json, Site.class);
        try{
            sitesController.updateSite(site.getAddress(), site);
        }catch(IOException e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Site updated successfully", true).getJson();
    }

    public String getSite(String json){
        Site site = JSON.deserialize(json, Site.class);
        try{
            site = sitesController.getSite(site.getAddress());
        }catch(Exception e){
            return new Response(e.getMessage(), false).getJson();
        }
        return new Response("Site found successfully", true, site).getJson();
    }

    public String getAllSites(){
        return new Response("Sites found successfully", true, sitesController.getAllSites()).getJson();
    }

}
