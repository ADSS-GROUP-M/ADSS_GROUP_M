package TransportModule.BusinessLayer;

import java.util.Objects;

public class Driver {
    private final int id;
    private final String name;
    private String licenseType;

    public Driver(int id, String name, String licenseType){
        this.id = id;
        this.name = name;
        this.licenseType = licenseType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void updateLicenseType(String licenseNumber) {
        this.licenseType = licenseNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return id == driver.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
