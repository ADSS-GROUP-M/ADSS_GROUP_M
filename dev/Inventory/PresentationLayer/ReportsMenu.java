package dev.Inventory.PresentationLayer;
import dev.Inventory.BusinessLayer.Record;

import java.util.List;

public class ReportsMenu extends MainMenu {
    public void run(String branch) {
        this.branch = branch;
        System.out.println("Please select an option:");
        System.out.println("1. Inventory products report");
        System.out.println("2. Inventory running-out products report");
        System.out.println("3. Inventory defective products report");
        System.out.println("4. Back to main manu");
        System.out.println("5. Exit");
        int option = in.nextInt();
        switch (option) {
//            case 1 -> inventoryReport();
            case 2 -> inventoryRunningOutReport();
            case 3 -> inventoryDefectiveReport();
            case 4 -> new MainMenu().Menu();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

//    private void inventoryReport() {
//        throw new RuntimeException();
//    }

    private void inventoryRunningOutReport() {
        String output = "";
        for (Record r: (List<Record>) stockService.getShortagesProducts(branch).getReturnValue()){
            output += r.toString() + "\n";
        }
        System.out.println(output);
    }

    private void inventoryDefectiveReport() {
        String output = "";
        for (Record r: (List<Record>) stockService.getDefectiveProducts(branch).getReturnValue()){
            output += r.toString() + "\n";
        }
        System.out.println(output);
    }
}
