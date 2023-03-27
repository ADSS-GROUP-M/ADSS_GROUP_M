package BusinessLayer;

import java.util.Objects;

public class Site {

    private String TransportZone;
    private String Address;
    private String phoneNumber;
    private String contactName;

    public Site(String TransportZone, String Address, String phoneNumber, String contactName){
        this.TransportZone = TransportZone;
        this.Address = Address;
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
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
}
