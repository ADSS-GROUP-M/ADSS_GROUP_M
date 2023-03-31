package CMDApp;

import static CMDApp.Main.*;

public class SitesManagement {
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
            switch (option){
                case 1:
                    createSite();
                    break;
                case 2:
                    updateSite();
                    break;
                case 3:
                    removeSite();
                    break;
                case 4:
                    getSite();
                    break;
                case 5:
                    getAllSites();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void createSite() {
        System.out.println("=========================================");
        System.out.println("Enter site details:");
        String shippingZone = getString("Shipping zone: ");
        String address = getString("Address: ");
        String contactPhone = getString("Contact phone: ");
        String contactName = getString("Contact name: ");
        //TODO: code for adding site

        System.out.println("\nSite added successfully!");
    }

    private static void updateSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to update:");
            int siteId = pickSite(true);
            if(siteId == -1) return;
            while(true) {
                System.out.println("=========================================");
                System.out.println("Site details:");
                System.out.println("Shipping zone: " + shippingZones[siteId]);
                System.out.println("Address: " + sites[siteId]);
                System.out.println("Contact name: " + sites[siteId]);
                System.out.println("Contact phone: " + sites[siteId]);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update shipping zone");
                System.out.println("2. Update address");
                System.out.println("3. Update contact name");
                System.out.println("4. Update contact phone");
                System.out.println("5. Return to previous menu");
                int option = getInt();
                switch (option) {
                    case 1:
                        String shippingZone = getString("Shipping zone: ");
                        break;
                    case 2:
                        String address = getString("Address: ");
                        break;
                    case 3:
                        String contactName = getString("Contact name: ");
                        break;
                    case 4:
                        String contactPhone = getString("Contact phone: ");
                        break;
                    case 5:
                        return;
                }
                //TODO: code for updating site
                System.out.println("\nSite updated successfully!");
                break;
            }
        }
    }

    private static void removeSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to remove:");
            int siteId = pickSite(true);
            if (siteId == -1) return;
            System.out.println("=========================================");
            System.out.println("Site details:");
            System.out.println("Shipping zone: " + shippingZones[siteId]);
            System.out.println("Address: " + sites[siteId]);
            System.out.println("Contact name: " + sites[siteId]);
            System.out.println("Contact phone: " + sites[siteId]);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this site? (y/n)");
            String option = getString();
            switch (option) {
                case "y":
                    //TODO: code for removing site
                    System.out.println("\nSite removed successfully!");
                    break;
                case "n":
                    break;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void getSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to view:");
            //TODO: code for getting site

            int siteId = pickSite(true);
            if(siteId == -1) return;
            System.out.println("=========================================");
            System.out.println("Site details:");
            System.out.println("Shipping zone: " + shippingZones[siteId]);
            System.out.println("Address: " + sites[siteId]);
            System.out.println("Contact name: " + sites[siteId]);
            System.out.println("Contact phone: " + sites[siteId]);
            System.out.println("=========================================");
            System.out.println("Press enter to return to previous menu");
            getString();
        }
    }

    private static void getAllSites() {
        System.out.println("=========================================");
        System.out.println("All sites:");
        //TODO: code for getting all sites

        for(int i = 0; i < sites.length; i++){
            System.out.println("Site " + i + ":");
            System.out.println("Shipping zone: " + shippingZones[i]);
            System.out.println("Address: " + sites[i]);
            System.out.println("Contact name: " + sites[i]);
            System.out.println("Contact phone: " + sites[i]);
            System.out.println("-----------------------------------------");
        }
        System.out.println("Press enter to return to previous menu");
        getString();
    }

}
