package BusinessLayer;
import java.util.TreeSet;
public class SitesController {
    TreeSet <Site> sites;
    public SitesController(){
        sites = new TreeSet<>();
    }
    public Site createSite(String transportZone, String address, String phoneNumber, String contactName){
        Site site = new Site(transportZone, address, phoneNumber, contactName);
        sites.add(site);
        return site;
    }
    public Site removeSite(String address) throws Exception {
        Site site = getSite(address);
        if (site != null) {
            Site removedSite = site;
            sites.remove(site);
            return removedSite;
        }
        throw new Exception("Site not found");
    }
    public Site getSite(String address){
        for (Site site : sites) {
            if (site.getAddress().equals(address)) {
                return site;
            }
        }
        return null;
    }
    public boolean updateSite(String address, Site newSite){
        Site site = getSite(address);
        if (site != null) {
            sites.remove(site);
            sites.add(newSite);
            return true;
        }
        return false;
    }

}
