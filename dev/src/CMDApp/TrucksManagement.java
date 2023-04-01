package CMDApp;

import CMDApp.Records.Truck;
import TransportModule.ServiceLayer.ResourceManagementService;

import static CMDApp.Main.*;

public class TrucksManagement {

    static ResourceManagementService rms = factory.getResourceManagementService();

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
        Truck newTruck = new Truck(licensePlate, model, baseWeight, maxWeight);
        String json = JSON.serialize(newTruck);
        String responseJson = rms.addTruck(json);
        Truck truck = JSON.deserialize(responseJson, Truck.class);

        System.out.println("\nTruck added successfully!");
    }

    private static void updateTruck() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select truck to update:");
            int truckId = pickTruck(true);
            if (truckId == -1) return;
            Truck truck = trucks.get(truckId);
            while(true) {
                System.out.println("=========================================");
                System.out.println("Truck details:");
                printTruckDetails(truck);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update license plate");
                System.out.println("2. Update model");
                System.out.println("3. Update base weight");
                System.out.println("4. Update max weight");
                System.out.println("5. Return to previous menu");
                int option = getInt();
                switch (option) {
                    case 1:
                        String licensePlate = getString("License plate: ");
                        break;
                    case 2:
                        String model = getString("Model: ");
                        break;
                    case 3:
                        int baseWeight = getInt("Base weight: ");
                        break;
                    case 4:
                        int maxWeight = getInt("Max weight: ");
                        break;
                    case 5:
                        return;
                }
                //TODO: code for updating truck
                System.out.println("\nTruck updated successfully!");
                break;
            }
        }
    }

    private static void removeTruck() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select truck to remove:");
            int truckId = pickTruck(true);
            if (truckId == -1) return;
            String truck = trucks[truckId];
            System.out.println("=========================================");
            System.out.println("Truck details:");
            printTruckDetails(truck);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this truck? (y/n)");
            String option = getString();
            switch(option){
                case "y":
                    //TODO: code for removing truck
                    System.out.println("\nTruck removed successfully!");
                    break;
                case "n":
                    break;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void getTruck() {

    }

    private static void getAllTrucks() {

    }

    static void fetchTrucks() {
    }

    private static void printTruckDetails(Truck truck) {
        System.out.println("License plate: " + truck.id());
        System.out.println("Model: " + truck.model());
        System.out.println("Base weight: " + truck.baseWeight());
        System.out.println("Max weight: " + truck.maxWeight());
    }

}
