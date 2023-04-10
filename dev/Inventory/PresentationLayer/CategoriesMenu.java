package dev.Inventory.PresentationLayer;


public class CategoriesMenu extends MainMenu {

    private String branch;
    public void run(String branch) {
        this.branch = branch;
        System.out.println("Please select an option:");
        System.out.println("1. Add new main category");
        System.out.println("2. Add new sub category");
        System.out.println("3. Remove main category");
        System.out.println("4. Remove sub category");
        System.out.println("5. Back to main manu");
        System.out.println("6. Exit");
        int option = in.nextInt();
        switch (option) {
            case 1 -> mainCategory();
            case 2 -> subCategory();
            case 3 -> removeMainCategory();
            case 4 -> removeSubCategory();
            case 5 -> new MainMenu().run();
            case 6 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void mainCategory() {
        in.nextLine();
        System.out.println("Please enter new category's name");
        String category_name = in.nextLine();
        System.out.println(inventoryService.createMainCategory(category_name, branch).getReturnValue());
    }

    private void subCategory() {
        in.nextLine();
        System.out.println("Please enter new category's name");
        String category_name = in.nextLine();
        System.out.println(inventoryService.createSubCategory(category_name, branch).getReturnValue());
    }

    private void removeMainCategory() {
        in.nextLine();
        System.out.println("category name:");
        String category_name = in.nextLine();
        System.out.println(inventoryService.removeMainCategory(category_name, branch).getReturnValue());
    }

    private void removeSubCategory() {
        in.nextLine();
        System.out.println("category name:");
        String category_name = in.nextLine();
        System.out.println(inventoryService.removeSubCategory(category_name, branch).getReturnValue());
    }
}
