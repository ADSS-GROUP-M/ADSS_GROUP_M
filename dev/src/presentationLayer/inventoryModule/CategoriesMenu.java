package presentationLayer.inventoryModule;


import businessLayer.businessLayerUsage.Branch;
import serviceLayer.inventoryModule.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        System.out.println("Please enter new subcategory's name as list [Dairy products,Bath products,Cleaning products]");
        String subcategories_name = in.nextLine();
        List<String> subcategories;
        if (subcategories_name.isEmpty()) {
            subcategories = new ArrayList<>(); // Creating an empty ArrayList
        } else {
            subcategories = Arrays.asList(subcategories_name.split(","));
        }
        Response response = categoriesService.createCategory(Branch.valueOf(branch), subcategories, category_name);
        if(response.errorOccurred())
            System.out.println(response.getErrorMessage());
        else
            System.out.println(response.getReturnValue());
    }


    private void removeCategory() {
        in.nextLine();
        System.out.println("category name:");
        String category_name = in.nextLine();
        Response response = categoriesService.removeCategory(Branch.valueOf(branch), category_name);
        if(response.errorOccurred())
            System.out.println(response.getErrorMessage());
        else
            System.out.println(response.getReturnValue());
    }

    private void addProductToCategory(){
        in.nextLine();
        System.out.println("category name:");
        String category_name = in.nextLine();
        System.out.println("catalog number: (string)");
        String catalog_number = in.nextLine();
        Response response = categoriesService.addProductToCategory(Branch.valueOf(branch), catalog_number, category_name);
        if(response.errorOccurred())
            System.out.println(response.getErrorMessage());
        else
            System.out.println(response.getReturnValue());
    }

    private void removeProductFromCategory(){
        in.nextLine();
        System.out.println("category name:");
        String category_name = in.nextLine();
        System.out.println("catalog number: (string)");
        String catalog_number = in.nextLine();
        Response response = categoriesService.removeProductFromCategory(Branch.valueOf(branch), category_name,catalog_number);
        if(response.errorOccurred())
            System.out.println(response.getErrorMessage());
        else
            System.out.println(response.getReturnValue());
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
