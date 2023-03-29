package TransportModule.BusinessLayer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class SitesController {
    TreeMap<String,Site> sites;

    public SitesController(){
        sites = new TreeMap<>();
    }

    public Site createSite(String transportZone, String address, String phoneNumber, String contactName, Site.SiteType siteType) throws IOException {
        if(sites.containsKey(address))
            throw new IOException("Site already exists");

        Site site = new Site(transportZone, address, phoneNumber, contactName, siteType);
        sites.put(address,site);
        return site;
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
