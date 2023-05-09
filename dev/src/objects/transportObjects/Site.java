package objects.transportObjects;

import com.google.gson.reflect.TypeToken;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Objects;

public record Site (String transportZone, String address, String phoneNumber, String contactName, SiteType siteType, double latitude, double longitude) {

    // 1) "14441 S Inglewood Ave, Hawthorne, CA 90250"
    // 2) "19503 S Normandie Ave, Torrance, CA 90501"
    // 3) "22015 Hawthorne Blvd, Torrance, CA 90503"
    // 4) "2100 N Long Beach Blvd, Compton, CA 90221"
    // 5) "19340 Hawthorne Blvd, Torrance, CA 90503"
    // 6) "4651 Firestone Blvd, South Gate, CA 90280"
    // 7) "1301 N Victory Pl, Burbank, CA 91502"
    // 8) "6433 Fallbrook Ave, West Hills, CA 91307"
    // 9) "20226 Avalon Blvd, Carson, CA 90746"
    // 10) "8333 Van Nuys Blvd, Panorama City, CA 91402"
    // 11) "14501 Lakewood Blvd, Paramount, CA 90723"
    // 12) "9001 Apollo Way, Downey, CA 90242"
    // 13) "8500 Washington Blvd, Pico Rivera, CA 90660"
    // 14) "3705 E South St, Long Beach, CA 90805"
    // 15) "2770 E Carson St, Lakewood, CA 90712"


    public enum SiteType{
        BRANCH,
        LOGISTICAL_CENTER,
        SUPPLIER
    }

    public Site(String transportZone, String address, String phoneNumber, String contactName, SiteType siteType) {
        this(transportZone, address, phoneNumber, contactName, siteType, 0.0,0.0);
    }

    public Site(Site old, double latitude, double longitude){
        this(old.transportZone(), old.address(), old.phoneNumber(), old.contactName(), old.siteType(), latitude, longitude);
    }

    public static Site getLookupObject(String address){
        return new Site(null, address, null, null, null,0.0,0.0);
    }

    public String toJson(){
        return JsonUtils.serialize(this);
    }

    public static Site fromJson(String json){
        return JsonUtils.deserialize(json, Site.class);
    }

    public static LinkedList<Site> listFromJson(String json){
        Type type = new TypeToken<LinkedList<Site>>(){}.getType();
        return JsonUtils.deserialize(json, type);
    }

    @Override
    public String toString(){
        return "Trn. zone: " + transportZone +
                " | Address: " + address +
                " | Phone: " + phoneNumber +
                " | Contact: " + contactName +
                " | Type: " + siteType;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Site site = (Site) o;
        return address.equals(site.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
