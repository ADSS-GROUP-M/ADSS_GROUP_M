package dev.Inventory.PresentationLayer.InventoryModule;


import java.util.ArrayList;
import java.util.List;

public class CategoriesMenu extends MainMenu {

    private String branch;
    public void run(String branch) {
        this.branch = branch;
        System.out.println("Please select an option:");
        System.out.println("1. Add new category");
        System.out.println("2. Remove category");
        System.out.println("3. Back to main manu");
        System.out.println("4. Exit");
        int option = in.nextInt();
        switch (option) {
            case 1 -> createCategory();
            case 2 -> removeCategory();
            case 3 -> new MainMenu().Menu();
            case 4 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void createCategory() {
        in.nextLine();
        System.out.println("Please enter new category's name");
        String category_name = in.nextLine();
        String subcategory = "";
        List<String> subcategories = new ArrayList<String>();
        while (subcategory != "null"){
            System.out.println("enter subcategory, if you done choose 'null' ");
            subcategory = in.nextLine();
            if(subcategory != "null")
                subcategories.add(subcategory);
        }
        System.out.println(categoriesService.createCategory(category_name,subcategories).getReturnValue());
    }


    private void removeCategory() {
        in.nextLine();
        System.out.println("category name:");
        String category_name = in.nextLine();
        System.out.println(categoriesService.removeCategory(category_name).getReturnValue());
    }

//    private void addSubcategory() {
//        in.nextLine();
//        System.out.println("category name:");
//        String category_name = in.nextLine();
//        String subcategory = "";
//        List<String> subcategories = new ArrayList<String>();
//        while (subcategory != "null"){
//            System.out.println("enter subcategory, if you done choose 'null' ");
//            subcategory = in.nextLine();
//            if(subcategory != "null")
//                subcategories.add(subcategory);
//        }
//        System.out.println(categoriesService.removeSubCategory(category_name, subcategories).getReturnValue());
//    }

}
