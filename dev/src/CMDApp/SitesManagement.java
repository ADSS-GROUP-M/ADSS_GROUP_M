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
                        break;
                }

                //TODO: code for updating site
            }
        }
    }

    private static void removeSite() {
    }

    private static void getSite() {
    }

    private static void getAllSites() {
    }

}
