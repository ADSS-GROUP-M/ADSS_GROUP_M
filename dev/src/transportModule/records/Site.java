package transportModule.records;

import com.google.gson.reflect.TypeToken;
import utils.JSON;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Objects;

public record Site (String transportZone, String address, String phoneNumber, String contactName, SiteType siteType) {

    public enum SiteType{
        BRANCH,
        LOGISTICAL_CENTER,
        SUPPLIER
    }

    public static Site getLookupObject(String address){
        return new Site(null, address, null, null, null);
    }

    public String toJson(){
        return JSON.serialize(this);
    }

    public static Site fromJson(String json){
        return JSON.deserialize(json, Site.class);
    }

    public static LinkedList<Site> listFromJson(String json){
        Type type = new TypeToken<LinkedList<Site>>(){}.getType();
        return JSON.deserialize(json, type);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return address.equals(site.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
