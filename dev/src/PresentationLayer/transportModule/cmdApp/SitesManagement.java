package PresentationLayer.transportModule.cmdApp;

import ServiceLayer.transportModule.ResourceManagementService;
import Objects.transportObjects.Site;
import utils.JsonUtils;
import utils.Response;

public class SitesManagement {

    private final TransportAppData transportAppData;
    private final ResourceManagementService rms;

    public SitesManagement(TransportAppData transportAppData, ResourceManagementService rms) {
        this.transportAppData = transportAppData;
        this.rms = rms;
    }

    void manageSites() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Sites management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new site");
            System.out.println("2. Update site");
            System.out.println("3. Remove site");
            System.out.println("4. View full site information");
            System.out.println("5. View all sites");
            System.out.println("6. Return to previous menu");
            int option = transportAppData.readInt();
            switch (option) {
                case 1 -> createSite();
                case 2 -> updateSite();
                case 3 -> removeSite();
                case 4 -> viewSite();
                case 5 -> viewAllSites();
                case 6 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void createSite() {
        System.out.println("=========================================");
        System.out.println("Enter site details:");
        String transportZone = transportAppData.readLine("Transport zone: ");
        String address = transportAppData.readLine("Address: ");
        String contactPhone = transportAppData.readLine("Contact phone: ");
        String contactName = transportAppData.readLine("Contact name: ");
        System.out.println("Site type: ");
        System.out.println("1. Logistical center");
        System.out.println("2. Branch");
        System.out.println("3. Supplier");
        int siteType = transportAppData.readInt();
        Site.SiteType type;
        switch (siteType) {
            case 1 -> type = Site.SiteType.LOGISTICAL_CENTER;
            case 2 -> type = Site.SiteType.BRANCH;
            case 3 -> type = Site.SiteType.SUPPLIER;
            default -> {
                System.out.println("\nInvalid option!");
                return;
            }
        }
        Site newSite = new Site(transportZone, address, contactPhone, contactName, type);
        String json = newSite.toJson();
        String responseJson = rms.addSite(json);
        Response response = JsonUtils.deserialize(responseJson, Response.class);
        if(response.success()) {
            transportAppData.sites().put(newSite.address(), newSite);
        }
        System.out.println("\n"+response.message());
    }

    private void updateSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to update:");
            Site site = transportAppData.pickSite(true);
            if(site == null) {
                return;
            }
            while(true) {
                System.out.println("=========================================");
                System.out.println("Site details:");
                printSiteDetails(site);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update contact name");
                System.out.println("2. Update contact phone");
                System.out.println("3. Return to previous menu");
                int option = transportAppData.readInt();
                switch (option) {
                    case 1 -> {
                        String contactName = transportAppData.readLine("Contact name: ");
                        updateSiteHelperMethod(site.transportZone(),
                                site.address(),
                                site.phoneNumber(),
                                contactName,
                                site.siteType()
                        );
                    }
                    case 2 -> {
                        String contactPhone = transportAppData.readLine("Contact phone: ");
                        updateSiteHelperMethod(
                                site.transportZone(),
                                site.address(),
                                contactPhone,
                                site.contactName(),
                                site.siteType()
                        );
                    }
                    case 3 -> {
                        return;
                    }
                    default -> {
                        System.out.println("\nInvalid option!");
                        continue;
                    }
                }
                break;
            }
        }
    }

    private void updateSiteHelperMethod(String transportZone, String address, String phoneNumber, String contactName, Site.SiteType siteType) {
        Site newSite = new Site(transportZone, address, phoneNumber, contactName, siteType);
        String json = newSite.toJson();
        String responseJson = rms.updateSite(json);
        Response response = JsonUtils.deserialize(responseJson, Response.class);
        if(response.success()) {
            transportAppData.sites().put(newSite.address(), newSite);
        }
        System.out.println("\n"+response.message());
    }

    private void removeSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to remove:");
            Site site = transportAppData.pickSite(true);
            if (site == null) {
                return;
            }
            System.out.println("=========================================");
            System.out.println("Site details:");
            printSiteDetails(site);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this site? (y/n)");
            String option = transportAppData.readLine();
            switch (option) {
                case "y"-> {
                    String json = site.toJson();
                    String responseJson = rms.removeSite(json);
                    Response response = JsonUtils.deserialize(responseJson, Response.class);
                    if(response.success()) {
                        transportAppData.sites().remove(site.address());
                    }
                    System.out.println("\n"+response.message());
                }
                case "n"-> {}
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void viewSite() {
        while(true) {
            System.out.println("=========================================");
            String siteId = transportAppData.readLine("Enter address of site to view (enter 'done!' to return to previous menu): ");
            if(siteId.equals("done!")) {
                return;
            }
            if(transportAppData.sites().containsKey(siteId) == false) {
                System.out.println("Site not found!");
                continue;
            }
            Site site = transportAppData.sites().get(siteId);
            System.out.println("=========================================");
            System.out.println("Site details:");
            printSiteDetails(site);
            System.out.println("=========================================");
            System.out.println("Enter 'done!' to return to previous menu");
            transportAppData.readLine();
        }
    }

    private void viewAllSites() {
        System.out.println("=========================================");
        System.out.println("All sites:");
        for(Site site : transportAppData.sites().values()) {
            printSiteDetails(site);
            System.out.println("-----------------------------------------");
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        transportAppData.readLine();
    }

    void printSiteDetails(Site site) {
        System.out.println("Transport zone: " + site.transportZone());
        System.out.println("Address:        " + site.address());
        System.out.println("Phone number:   " + site.phoneNumber());
        System.out.println("Contact name:   " + site.contactName());
        System.out.println("Site type:      " + site.siteType());
    }

}
