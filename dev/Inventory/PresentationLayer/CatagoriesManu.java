package dev.Inventory.PresentationLayer;
package dev.Inventory.ServiceLayer;

public class CatagoriesMenu : MainManu {

    public CatagoriesMenu run() {
        System.out.println("Please select an option:");
        System.out.println("1. Add new main catagory");
        System.out.println("2. Add new sub catagory");
        System.out.println("3. Remove main catagory");
        System.out.println("4. Remove sub catagory");
        System.out.println("5. Back to main manu");
        System.out.println("6. Exit");
        int option = appData.readInt();
        switch (option) {
            case 1 -> mainCatagory();
            case 2 -> subCatagory();
            case 3 -> removeMainCatagory();
            case 4 -> removeSubCatagory();
            case 5 -> MainManu.run();
            case 6 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void mainCatagory() {
        System.out.println("Please enter new catagory's name");
        int catagory_name = in.nextLine();
        InventoryService.createMainCatagory(catagory_name)
    }

    private void subCatagory() {
        System.out.println("Please enter new catagory's name");
        int catagory_name = in.nextLine();
        System.out.println(InventoryService.createSubCatagory(catagory_name).getReturnValue())
    }

    private void removeMainCatagory() {
        System.out.println("catagory name:");
        int catagory_name = in.nextLine();
        System.out.println(InventoryService.removeMainCatagory(catagory_name).getReturnValue())
    }

    private void removeSubCatagory() {
        System.out.println("catagory name:");
        int catagory_name = in.nextLine();
        System.out.println(InventoryService.removeSubCatagory(catagory_name).getReturnValue())
    }
}
