package CMDApp;

import CMDApp.Records.Site;
import TransportModule.ServiceLayer.ResourceManagementService;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static CMDApp.Main.*;

public class SitesManagement {

    static ResourceManagementService rms = factory.getResourceManagementService();

    static void manageSites() {
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
            int option = getInt();
            switch (option) {
                case 1 -> createSite();
                case 2 -> updateSite();
                case 3 -> removeSite();
                case 4 -> getSite();
                case 5 -> getAllSites();
                case 6 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private static void createSite() {
        System.out.println("=========================================");
        System.out.println("Enter site details:");
        String transportZone = getLine("Transport zone: ");
        String address = getLine("Address: ");
        String contactPhone = getLine("Contact phone: ");
        String contactName = getLine("Contact name: ");
        System.out.println("Site type: ");
        System.out.println("1. Logistical center");
        System.out.println("2. Branch");
        System.out.println("3. Supplier");
        int siteType = getInt();
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
        String json = JSON.serialize(newSite);
        String responseJson = rms.addSite(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()) sites.put(newSite.address(), newSite);
        System.out.println("\n"+response.getMessage());
    }

    private static void updateSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to update:");
            Site site = pickSite(true);
            if(site == null) return;
            while(true) {
                System.out.println("=========================================");
                System.out.println("Site details:");
                printSiteDetails(site);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update contact name");
                System.out.println("2. Update contact phone");
                System.out.println("3. Return to previous menu");
                int option = getInt();
                switch (option) {
                    case 1 -> {
                        String contactName = getLine("Contact name: ");
                        updateSiteHelperMethod(site.transportZone(),
                                site.address(),
                                site.phoneNumber(),
                                contactName,
                                site.siteType()
                        );
                    }
                    case 2 -> {
                        String contactPhone = getLine("Contact phone: ");
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

    private static void updateSiteHelperMethod(String transportZone, String address, String phoneNumber, String contactName, Site.SiteType siteType) {
        Site newSite = new Site(transportZone, address, phoneNumber, contactName, siteType);
        String json = JSON.serialize(newSite);
        String responseJson = rms.updateSite(json);
        Type type = new TypeToken<Response<String>>(){}.getType();
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()) sites.put(newSite.address(), newSite);
        System.out.println("\n"+response.getMessage());
    }

    private static void removeSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to remove:");
            Site site = pickSite(true);
            if (site == null) return;
            System.out.println("=========================================");
            System.out.println("Site details:");
            printSiteDetails(site);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this site? (y/n)");
            String option = getLine();
            switch (option) {
                case "y"-> {
                    String json = JSON.serialize(site);
                    String responseJson = rms.removeSite(json);
                    Response<String> response = JSON.deserialize(responseJson, Response.class);
                    if(response.isSuccess()) sites.remove(site.address());
                    System.out.println("\n"+response.getMessage());
                }
                case "n"-> {}
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private static void getSite() {
        while(true) {
            System.out.println("=========================================");
            String siteId = getLine("Enter address of site to view (enter 'done!' to return to previous menu): ");
            if(siteId.equals("done!")) return;
            if(sites.containsKey(siteId) == false) {
                System.out.println("Site not found!");
                continue;
            }
            Site site = sites.get(siteId);
            System.out.println("=========================================");
            System.out.println("Site details:");
            printSiteDetails(site);
            System.out.println("=========================================");
            System.out.println("Enter 'done!' to return to previous menu");
            getLine();
        }
    }

    private static void getAllSites() {
        System.out.println("=========================================");
        System.out.println("All sites:");
        for(Site site : sites.values()) {
            printSiteDetails(site);
            System.out.println("-----------------------------------------");
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        getLine();
    }

    private static void printSiteDetails(Site site) {
        System.out.println("Transport zone: " + site.transportZone());
        System.out.println("Address: " + site.address());
        System.out.println("Phone number: " + site.phoneNumber());
        System.out.println("Contact name: " + site.contactName());
    }

}
