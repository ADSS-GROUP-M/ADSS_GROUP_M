package CMDApp;

import static CMDApp.Main.getInt;
import static CMDApp.Main.getString;

public class TrucksManagement {
    static void manageTrucks() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Trucks management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new truck");
            System.out.println("2. Update truck");
            System.out.println("3. Remove truck");
            System.out.println("4. View full truck information");
            System.out.println("5. View all trucks");
            System.out.println("6. Return to previous menu");
            int option = getInt();
            switch (option){
                case 1:
                    createTruck();
                    break;
                case 2:
                    updateTruck();
                    break;
                case 3:
                    removeTruck();
                    break;
                case 4:
                    getTruck();
                    break;
                case 5:
                    getAllTrucks();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void createTruck() {
        System.out.println("=========================================");
        System.out.println("Enter truck details:");
        String licensePlate = getString("License plate: ");
        String model = getString("Model: ");
        int baseWeight = getInt("Base weight: ");
        int maxWeight = getInt("Max weight: ");
        //TODO: code for adding truck

        System.out.println("\nTruck added successfully!");
    }

    private static void updateTruck() {

    }

    private static void removeTruck() {

    }

    private static void getTruck() {

    }

    private static void getAllTrucks() {

    }

}
