package objects.transportObjects;

import com.google.gson.reflect.TypeToken;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Objects;

public record Site (String transportZone, String address, String phoneNumber, String contactName, SiteType siteType, double latitude, double longitude) {

    public enum SiteType{
        BRANCH,
        LOGISTICAL_CENTER,
        SUPPLIER
    }

    public Site(String transportZone, String address, String phoneNumber, String contactName, SiteType siteType) {
        this(transportZone, address, phoneNumber, contactName, siteType, 0.0,0.0);
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
