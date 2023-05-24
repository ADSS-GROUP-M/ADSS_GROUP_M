package presentationLayer.inventoryModule;

import businessLayer.businessLayerUsage.Branch;

import java.util.ArrayList;
import java.util.Arrays;
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
        System.out.println("Please enter categories' name as list [Dairy products,Bath products,Cleaning products]");
        String subcategories_name = in.nextLine();
        List<String> subcategories;
        if (subcategories_name.isEmpty()) {
            subcategories = new ArrayList<>(); // Creating an empty ArrayList
        } else {
            subcategories = Arrays.asList(subcategories_name.split(","));
        }
        StringBuilder output = new StringBuilder();
        for (businessLayer.inventoryModule.Record r : categoriesService.getCategoryReport(branch, subcategories).getReturnValue()) {
            output.append(r.toString()).append("\n");
        }
        System.out.println(output);
    }

    private void inventoryRunningOutReport() {
        StringBuilder output = new StringBuilder();
        for (businessLayer.inventoryModule.Record r: stockService.getShortagesProducts(Branch.valueOf(branch)).getReturnValue()){
            output.append(r.toString()).append("\n");
        }
        System.out.println(output);
    }

    private void inventoryDefectiveReport() {
        StringBuilder output = new StringBuilder();
        for (businessLayer.inventoryModule.Record r: stockService.getDefectiveProducts(Branch.valueOf(branch)).getReturnValue()){
            output.append(r.toString()).append("\n");
        }
        System.out.println(output);
    }
}
