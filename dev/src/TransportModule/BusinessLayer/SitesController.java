package TransportModule.BusinessLayer;

import TransportModule.BusinessLayer.Records.Site;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * The `SitesController` class is responsible for managing the sites in the TransportModule.
 * It provides methods for adding, removing, updating, and retrieving sites.
 */
public class SitesController {
    private final TreeMap<String, Site> sites;

    public SitesController(){
        sites = new TreeMap<>();
    }

    /**
     * Adds a site to the `SitesController`.
     *
     * @param site The site to be added.
     * @throws IOException If the site already exists.
     */
    public void addSite(Site site) throws IOException {
        if (sites.containsKey(site.address()) == false)
            sites.put(site.address(), site);
        else throw new IOException("Site already exists");
    }

    /**
     * Removes a site from the `SitesController`.
     *
     * @param address The address of the site to be removed.
     * @throws IOException If the site is not found.
     */
    public void removeSite(String address) throws IOException {
        if (sites.containsKey(address) == false)
            throw new IOException("Site not found");

        sites.remove(address);
    }

    /**
     * Retrieves a site from the `SitesController`.
     *
     * @param address The address of the site to be retrieved.
     * @return The retrieved site.
     * @throws IOException If the site is not found.
     */
    public Site getSite(String address) throws IOException {
        if (sites.containsKey(address) == false)
            throw new IOException("Site not found");

        return sites.get(address);
    }

    /**
     * Updates a site in the `SitesController`.
     *
     * @param address The address of the site to be updated.
     * @param newSite The updated site.
     * @throws IOException If the site is not found.
     */
    public void updateSite(String address, Site newSite) throws IOException{
        if(sites.containsKey(address) == false)
            throw new IOException("Site not found");

        sites.put(address, newSite);
    }

    /**
     * Retrieves all sites from the `SitesController`.
     *
     * @return A linked list of all sites.
     */
    public LinkedList<Site> getAllSites(){
        return new LinkedList<>(sites.values());
    }

}
