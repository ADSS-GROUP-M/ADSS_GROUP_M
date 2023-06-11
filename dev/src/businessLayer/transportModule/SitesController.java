package businessLayer.transportModule;

import businessLayer.transportModule.bingApi.Point;
import dataAccessLayer.transportModule.SitesDAO;
import domainObjects.transportModule.Site;
import exceptions.DalException;
import exceptions.TransportException;
import serviceLayer.employeeModule.Services.EmployeesService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static serviceLayer.employeeModule.Services.UserService.TRANSPORT_MANAGER_USERNAME;

/**
 * The `SitesController` class is responsible for managing the sites in the TransportModule.
 * It provides methods for adding, removing, updating, and retrieving sites.
 */
public class SitesController {
    private final SitesDAO dao;
    private EmployeesService employeesService;
    private final SitesRoutesController sitesRoutesController;

    public SitesController(SitesDAO dao, SitesRoutesController sitesRoutesController){
        this.dao = dao;
        this.sitesRoutesController = sitesRoutesController;
    }

    public void injectDependencies(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    /**
     * Adds a site to the `SitesController`.
     *
     * @param site The site to be added.
     * @throws TransportException If the site already exists.
     */
    public Site addSite(Site site) throws TransportException {

        if (siteExists(site.name()) != false) {
            throw new TransportException("Site already exists");
        }

        validateSite(site);

        try {
            List<Site> otherSites = getAllSites();

            // get latitude and longitude
            Point coordinates = sitesRoutesController.getCoordinates(site);
            site = new Site(site,
                    coordinates.coordinates()[0],
                    coordinates.coordinates()[1]);
            dao.insert(site);
            sitesRoutesController.addRoutes(site,otherSites);

            if(site.siteType() == Site.SiteType.BRANCH){
                employeesService.createBranch(TRANSPORT_MANAGER_USERNAME,site.name());
            }

        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
        return site;
    }

    private void validateSite(Site site) throws TransportException {
        try {
            if(dao.isAddressUnique(site.address()) == false){
                throw new TransportException("Site address already exists");
            }
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Removes a site from the `SitesController`.
     *
     * @param name The name of the site to be removed.
     * @throws TransportException If the site is not found.
     */
    public void removeSite(String name) throws TransportException {
        if (siteExists(name) == false) {
            throw new TransportException("Site not found");
        }

        try {
            dao.delete(Site.getLookupObject(name));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves a site from the `SitesController`.
     *
     * @param name The name of the site to be retrieved.
     * @return The retrieved site.
     * @throws TransportException If the site is not found.
     */
    public Site getSite(String name) throws TransportException {
        if (siteExists(name) == false) {
            throw new TransportException("Site not found");
        }

        try {
            return dao.select(Site.getLookupObject(name));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Updates a site in the `SitesController`.
     *
     * @param name The name of the site to be updated.
     * @param newSite The updated site.
     * @throws TransportException If the site is not found.
     */
    public Site updateSite(String name, Site newSite) throws TransportException{
        if(siteExists(name) == false) {
            throw new TransportException("Site not found");
        }

        try {
            dao.update(newSite);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
        return newSite;
    }

    /**
     * Retrieves all sites from the `SitesController`.
     *
     * @return A linked list of all sites.
     */
    public List<Site> getAllSites() throws TransportException {
        try {
            return dao.selectAll();
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    public boolean siteExists(String name) throws TransportException{
        Site lookupObject = Site.getLookupObject(name);
        try {
            return dao.exists(lookupObject);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * this method is used only for the first time the system is loaded.
     * this method assumes that there are no sites in the system.
     */
    public void addAllSitesFirstTimeSystemLoad(List<Site> sites, boolean fetchTravelTimes) throws TransportException {

        if(fetchTravelTimes){
            // get latitude and longitude for each site
            Map<Site,Point> coordinates = sitesRoutesController.getCoordinates(sites);

            List<Site> sitesWithCoordinates = new LinkedList<>();
            for(var entry : coordinates.entrySet()){
                Site siteWithCoordinates = new Site(entry.getKey(), entry.getValue().latitude(), entry.getValue().longitude());
                sitesWithCoordinates.add(siteWithCoordinates);
            }
            sites = sitesWithCoordinates;
        }

        try {
            dao.insertAll(sites);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
        sites.stream()
                .filter(site -> site.siteType() == Site.SiteType.BRANCH)
                .forEach(site -> employeesService.createBranch(TRANSPORT_MANAGER_USERNAME,site.name()));

        if(fetchTravelTimes){
            // get route information for between all sites
            sitesRoutesController.addAllRouteObjectsFirstTimeLoad(sites);
        }
    }
}
