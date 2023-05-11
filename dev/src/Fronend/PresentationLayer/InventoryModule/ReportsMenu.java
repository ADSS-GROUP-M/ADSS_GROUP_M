package Fronend.PresentationLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;

import java.util.ArrayList;
import java.util.List;

public class ReportsMenu extends MainMenu {
    public void run(String branch) {
        this.branch = branch;
        System.out.println("Please select an option:");
        System.out.println("1. Get products reports per category");
        System.out.println("2. Inventory running-out products report");
        System.out.println("3. Inventory defective products report");
        System.out.println("4. Back to main manu");
        System.out.println("5. Exit");
        int option = in.nextInt();
        switch (option) {
            case 1 -> getProductPerCategory();
            case 2 -> inventoryRunningOutReport();
            case 3 -> inventoryDefectiveReport();
            case 4 -> new MainMenu().Menu();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void getProductPerCategory() {
        in.nextLine();
        List<String> categories = new ArrayList<String>();
        String category_name = "";
        while (category_name != "null") {
            System.out.println("Please enter category's name , if you done choose 'null'");
            category_name = in.nextLine();
            if (category_name != "null")
                categories.add(category_name);
        }
        System.out.println(categoriesService.getCategoryReport(category_name, categories).getReturnValue());
    }

    private void inventoryRunningOutReport() {
        String output = "";
        for (Record r: (List<Record>) stockService.getShortagesProducts(Branch.valueOf(branch)).getReturnValue()){
            output += r.toString() + "\n";
        }
        System.out.println(output);
    }

    private void inventoryDefectiveReport() {
        String output = "";
        for (Record r: (List<Record>) stockService.getDefectiveProducts(Branch.valueOf(branch)).getReturnValue()){
            output += r.toString() + "\n";
        }
        System.out.println(output);
    }
}
