package Fronend.PresentationLayer.InventoryModule;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriesMenu extends MainMenu {

    private String branch;
    public void run(String branch) {
        this.branch = branch;
        System.out.println("Please select an option:");
        System.out.println("1. Add new category");
        System.out.println("2. Remove category");
        System.out.println("3. Add product to category");
        System.out.println("4. Remove product from category");
        System.out.println("5. Back to main manu");
        System.out.println("6. Exit");
        int option = in.nextInt();
        switch (option) {
            case 1 -> createCategory();
            case 2 -> removeCategory();
            case 3 -> addProductToCategory();
            case 4 -> removeProductFromCategory();
            case 5 -> new MainMenu().Menu();
            case 6 -> System.exit(0);
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
        System.out.println(categoriesService.createCategory(category_name, Optional.of(subcategories)).getReturnValue());
    }


    private void removeCategory() {
        in.nextLine();
        System.out.println("category name:");
        String category_name = in.nextLine();
        System.out.println(categoriesService.removeCategory(category_name).getReturnValue());
    }

    private void addProductToCategory(){
        in.nextLine();
        System.out.println("category name:");
        String category_name = in.nextLine();
        System.out.println("catalog number: (string)");
        String catalog_number = in.nextLine();
        System.out.println(categoriesService.addProductToCategory(category_name,catalog_number).getReturnValue());
    }

    private void removeProductFromCategory(){
        in.nextLine();
        System.out.println("category name:");
        String category_name = in.nextLine();
        System.out.println("catalog number: (string)");
        String catalog_number = in.nextLine();
        System.out.println(categoriesService.removeProductFromCategory(category_name,catalog_number).getReturnValue());
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
