package TransportModule.BusinessLayer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class SitesController {
    TreeMap<String,Site> sites;

    public SitesController(){
        sites = new TreeMap<>();
    }

    public void addSite(Site site) throws IOException {
        if (sites.containsKey(site.address()) == false)
            sites.put(site.address(), site);
        else throw new IOException("Site already exists");
    }

    public Site removeSite(String address) throws IOException {
        if (sites.containsKey(address) == false)
            throw new IOException("Site not found");

        return sites.remove(address);
    }
    public Site getSite(String address) throws IOException {
        if (sites.containsKey(address) == false)
            throw new IOException("Site not found");

        return sites.get(address);
    }

    public void updateSite(String address, Site newSite) throws IOException{
        if(sites.containsKey(address) == false)
            throw new IOException("Site not found");

        sites.put(address, newSite);
    }

    public LinkedList<Site> getAllSites(){
        LinkedList<Site> sitesList = new LinkedList<>();
        for(Site site : sites.values())
            sitesList.add(site);
        return sitesList;
    }

}
