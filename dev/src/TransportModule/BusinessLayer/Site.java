package TransportModule.BusinessLayer;

import java.util.Objects;

public class Site {

    public enum SiteType{
        BRANCH,
        LOGISTICAL_CENTER,
        SUPPLIER
    }

    private final String TransportZone;
    private final String Address;
    private final String phoneNumber;
    private final String contactName;
    private final SiteType siteType;

    public Site(String TransportZone, String Address, String phoneNumber, String contactName, SiteType siteType){
        this.TransportZone = TransportZone;
        this.Address = Address;
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
        this.siteType = siteType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return Address.equals(site.Address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Address);
    }

    public String getTransportZone() {
        return TransportZone;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public SiteType getSiteType() {
        return siteType;
    }
}
