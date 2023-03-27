package BusinessLayer;

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
}
