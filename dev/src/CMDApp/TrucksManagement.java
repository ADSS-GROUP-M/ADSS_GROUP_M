package CMDApp;

import CMDApp.Records.Truck;
import TransportModule.ServiceLayer.ResourceManagementService;

import static CMDApp.Main.*;
import static TransportModule.BusinessLayer.Records.Truck.CoolingCapacity.*;

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
            switch (option) {
                case 1 -> createTruck();
                case 2 -> updateTruck();
                case 3 -> removeTruck();
                case 4 -> getTruck();
                case 5 -> getAllTrucks();
                case 6 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private static void createTruck() {
        System.out.println("=========================================");
        System.out.println("Enter truck details:");
        String licensePlate = getLine("License plate: ");
        String model = getLine("Model: ");
        int baseWeight = getInt("Base weight: ");
        if(baseWeight <= 0) {
            System.out.println("\nInvalid base weight!");
            return;
        }
        int maxWeight = getInt("Max weight: ");
        if(maxWeight <= 0) {
            System.out.println("\nInvalid max weight!");
            return;
        }
        Truck.CoolingCapacity coolingCapacity = pickCoolingCapacity();

        Truck newTruck = new Truck(licensePlate, model, baseWeight, maxWeight,coolingCapacity);
        String json = JSON.serialize(newTruck);
        String responseJson = rms.addTruck(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()) trucks.put(licensePlate, newTruck);
        System.out.println("\n"+response.getMessage());
    }


    private static Truck.CoolingCapacity pickCoolingCapacity() {

        for (int i = 0; i < Truck.CoolingCapacity.values().length; i++) {
            System.out.println(i + ". " + Truck.CoolingCapacity.values()[i]);
        }
        int option = getInt();
        if (option < 0 || option >= Truck.CoolingCapacity.values().length) {
            System.out.println("\nInvalid cooling capacity!");
            return null;
        }
        return Truck.CoolingCapacity.values()[option];
    }

    private static void updateTruck() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select truck to update:");
            Truck truck = pickTruck(true);
            if (truck == null) return;

            while(true) {
                System.out.println("=========================================");
                System.out.println("Truck details:");
                printTruckDetails(truck);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update base weight");
                System.out.println("2. Update max weight");
                System.out.println("3. Update cooling capacity");
                System.out.println("4. Return to previous menu");
                int option = getInt();
                switch (option) {
                    case 1 -> {
                        int baseWeight = getInt("Base weight: ");
                        if (baseWeight <= 0) {
                            System.out.println("\nInvalid base weight!");
                            continue;
                        }
                        updateTruckHelperMethod(truck.id(), truck.model(), baseWeight, truck.maxWeight(),truck.coolingCapacity());
                    }
                    case 2 -> {
                        int maxWeight = getInt("Max weight: ");
                        if (maxWeight <= 0) {
                            System.out.println("\nInvalid max weight!");
                            continue;
                        }
                        updateTruckHelperMethod(truck.id(), truck.model(), truck.baseWeight(), maxWeight,truck.coolingCapacity());
                    }
                    case 3 -> {
                        Truck.CoolingCapacity coolingCapacity = pickCoolingCapacity();
                        updateTruckHelperMethod(truck.id(), truck.model(), truck.baseWeight(), truck.maxWeight(),coolingCapacity);
                    }
                    case 4 -> {
                        return;
                    }
                    default->{
                        System.out.println("\nInvalid option!");
                        continue;
                    }
                }
                break;
            }
        }
    }

    private static void updateTruckHelperMethod(String licensePlate, String model, int baseWeight, int maxWeight, Truck.CoolingCapacity coolingCapacity){
        Truck newTruck = new Truck(licensePlate, model, baseWeight, maxWeight,coolingCapacity);
        String json = JSON.serialize(newTruck);
        String responseJson = rms.updateTruck(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()) trucks.put(licensePlate, newTruck);
        System.out.println("\n"+response.getMessage());
    }

    private static void removeTruck() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select truck to remove:");
            Truck truck = pickTruck(true);
            if (truck == null) return;
            System.out.println("=========================================");
            System.out.println("Truck details:");
            printTruckDetails(truck);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this truck? (y/n)");
            String option = getLine();
            switch(option){
                case "y"->{
                    String json = JSON.serialize(truck);
                    String responseJson = rms.removeTruck(json);
                    Response<String> response = JSON.deserialize(responseJson, Response.class);
                    if(response.isSuccess()) trucks.remove(truck.id());
                    System.out.println("\n"+response.getMessage());
                }
                case "n"->{}
                default-> System.out.println("\nInvalid option!");
            }
        }
    }

    private static void getTruck() {
        while(true) {
            System.out.println("=========================================");
            String truckId = getLine("Enter license plate of truck to view ('done!' to return to previous menu): ");
            if (truckId.equalsIgnoreCase("done!")) return;
            Truck truck = trucks.get(truckId);
            System.out.println("=========================================");
            System.out.println("Truck details:");
            printTruckDetails(truck);
            System.out.println("=========================================");
            System.out.println("\nEnter 'done!' to return to previous menu");
            String option = getLine();
        }

    }

    private static void getAllTrucks() {
        System.out.println("=========================================");
        System.out.println("All trucks:");
        for (Truck truck : trucks.values()) {
            printTruckDetails(truck);
            System.out.println("-----------------------------------------");
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        getLine();
    }

    private static void printTruckDetails(Truck truck) {
        System.out.println("License plate:    " + truck.id());
        System.out.println("Model:            " + truck.model());
        System.out.println("Base weight:      " + truck.baseWeight());
        System.out.println("Max weight:       " + truck.maxWeight());
        System.out.println("Cooling capacity: " + truck.coolingCapacity());
    }

}
