package dev.Inventory.PresentationLayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InventoryProductsManu extends MainMenu {

    private String branch;

    public void run(String branch) {
        this.branch = branch;
        System.out.println("Please select an option:");
        System.out.println("1. create new product");
        System.out.println("2. create new item");
        System.out.println("3. update product");
        System.out.println("4. update item");
        System.out.println("5. get all product's details");
        System.out.println("6. Back to main manu");
        System.out.println("7. Exit");
        int option = in.nextInt();
        switch (option) {
            case 1 -> createNewProduct();
            case 2 -> createNewItem();
            case 3 -> updateProduct();
            case 4 -> updateItem();
            case 5 -> getProductDetails();
            case 6 -> new MainMenu().run();
            case 7 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void createNewProduct() {
        System.out.println("Enter the following product details:");
        System.out.println("Catalog Number: (number)");
        String catalog_num = in.nextLine();
        System.out.println("Name: (string)");
        String name = in.nextLine();
        in.nextLine();
        System.out.println("Manufacturer: (string)");
        String manufacturer = in.nextLine();
        System.out.println("Supplier Price: (double)");
        double supplier_price = in.nextDouble();
        System.out.println("Store Price: (double)");
        double store_price = in.nextDouble();
        System.out.println(stockService.addProduct(catalog_num, name, manufacturer, store_price, branch).getReturnValue());
    }

    private void createNewItem() {
        //supplier should be ID
        System.out.println("Enter the following product details:");
        System.out.println("Catalog Number: (int)");
        String catalog_num = in.nextLine();
        System.out.println("Serial Number: (int)");
        String serial_num = in.nextLine();
        System.out.println("Supplier: (int)");
        String supplier = in.nextLine();
        in.nextLine();
        System.out.println("location: (string)");
        String location = in.nextLine();
        System.out.println("expiration date: (string format: yyyy-MM-dd)");
        String date = in.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime expirationDate = LocalDateTime.parse(date, formatter);
        System.out.println(stockService.addProductItem(serial_num, catalog_num, supplier,  branch, location, expirationDate).getReturnValue());
    }

    private void updateProduct() {
        System.out.println("what is the product's catalog num?: (int)");
        int catalog_num = in.nextInt();
        System.out.println("Choose details to update:");
        System.out.println("1. name (String)");
        System.out.println("2. manufacturer (String)");
        System.out.println("3. supplier price (double)");
        System.out.println("4. store price (double)");
        System.out.println("5. category (string)");
        System.out.println("6. sub category (string)");
        System.out.println("7. minimum amount for notification about running out of products (int)");
        System.out.println("8. Back to main manu");
        System.out.println("9. Exit");
        int option = in.nextInt();
        in.nextLine();
        System.out.println("the new value is:");
        String new_val = in.nextLine();
        switch (option) {
            case 1 -> stockService.updateProductType(new_val, catalog_num, null, -1, -1, null, null, -1, branch);
            case 2 -> stockService.updateProductType(null, catalog_num, new_val, -1, -1, null, null, -1, branch);
            case 3 -> stockService.updateProductType(null, catalog_num, null, Integer.parseInt(new_val), -1, null, null, -1, branch);
            case 4 -> stockService.updateProductType(null, catalog_num, null, -1, Integer.parseInt(new_val), null, null, -1, branch);
            case 5 -> stockService.updateProductType(null, catalog_num, null, -1, -1, new_val, null, -1, branch);
            case 6 -> stockService.updateProductType(null, catalog_num, null, -1, -1, null, new_val, -1, branch);
            case 7 -> stockService.updateProductType(null, catalog_num, null, -1, -1, null, null, Integer.parseInt(new_val), branch);
            case 8 -> super.run();
            case 9 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void updateItem() {
        System.out.println("what is the product's catalog number?");
        int catalog_num = in.nextInt();
        System.out.println("what is the product's serial number?");
        int serial_number = in.nextInt();
        System.out.println("Choose details to update:");
        System.out.println("1. is defective? (0 - no, 1- yes)");
        System.out.println("2. is sold? (0 - no, 1- yes)");
        System.out.println("3. supplier (int)");
        System.out.println("4. supplier price");
        System.out.println("5. sold price");
        System.out.println("6. location (String)");
        System.out.println("7. Back to main manu");
        System.out.println("8. Exit");
        int option = in.nextInt();
        in.nextLine();
        System.out.println("the new value is: ");
        String new_val = in.nextLine();
        switch (option) {
            case 1 -> stockService.updateProduct(Integer.parseInt(new_val), catalog_num, serial_number, -1, -1, -1, -1, null, branch);
            case 2 -> stockService.updateProduct(-1, catalog_num, serial_number, Integer.parseInt(new_val), -1, -1, -1, null, branch);
            case 3 -> stockService.updateProduct(-1, catalog_num, serial_number, -1 , Integer.parseInt(new_val), -1, -1, null, branch);
            case 4 -> stockService.updateProduct(-1, catalog_num, serial_number, -1, -1, Integer.parseInt(new_val), -1, null, branch);
            case 5 -> stockService.updateProduct(-1, catalog_num, serial_number, -1, -1, -1, Integer.parseInt(new_val), null, branch);
            case 6 -> stockService.updateProduct(-1, catalog_num, serial_number, -1, -1, -1, -1, new_val, branch);
            case 7 -> super.run();
            case 8 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void getProductDetails() {
        System.out.println("what is the product's catalog num? (int)");
        String catalog_num = in.nextLine();
        System.out.println("what is the product's serial num? (int)");
        int serial_num = in.nextInt();
        System.out.println(stockService.getProductDetails(catalog_num, serial_num, branch).getReturnValue().toString());
    }
}