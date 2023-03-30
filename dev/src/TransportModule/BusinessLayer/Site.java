package TransportModule.BusinessLayer;

import java.util.Objects;

public record Site (String TransportZone, String Address, String phoneNumber, String contactName, SiteType siteType) {

    public enum SiteType{
        BRANCH,
        LOGISTICAL_CENTER,
        SUPPLIER
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
}
