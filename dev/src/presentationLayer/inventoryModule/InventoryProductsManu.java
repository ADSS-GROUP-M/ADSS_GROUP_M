package presentationLayer.inventoryModule;

import utils.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
            case 6 -> new MainMenu().Menu();
            case 7 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void createNewProduct() {
        System.out.println("Enter the following product details:");
        System.out.println("Catalog Number: (number)");
        in.nextLine(); // Consume the remaining newline character
        String catalog_num = in.nextLine();
        System.out.println("Name: (string)");
        String name = in.nextLine();
        System.out.println("Manufacturer: (string)");
        String manufacturer = in.nextLine();
        System.out.println("Store Price: (double)");
        double store_price = in.nextDouble();
        in.nextLine(); // Consume the remaining newline character
//        System.out.println(stockService.addProduct(catalog_num, name, manufacturer, store_price, branch).getReturnValue());
        Response response = Response.fromJson(stockService.addProduct(catalog_num, name, manufacturer, store_price, branch));
        System.out.println(response.message());
    }

    private void createNewItem() {
        //supplier should be ID
        System.out.println("Enter the following product details:");
        System.out.println("Catalog Number: (string)");
        in.nextLine(); // Consume the remaining newline character
        String catalog_num = in.nextLine();
        System.out.println("Serial Number: (string - 23,24,25 for example)");
        String serial_num = in.nextLine();
        List<String> serialNumbers = Arrays.asList(serial_num.split(","));
        System.out.println("Supplier: (string)");
        String supplier = in.nextLine();
        System.out.println("Supplier price: (double)");
        double supplier_price = in.nextDouble();
        System.out.println("Supplier price after discount: (double)");
        double supplier_discount_price = in.nextDouble();
        System.out.println("location: (string)");
        in.nextLine();
        String location = in.nextLine();
        System.out.println("expiration date: (string format: yyyy-MM-dd)");
        String dateString = in.nextLine();
        LocalDate date = LocalDate.parse(dateString);
        LocalDateTime expirationDate = date.atStartOfDay();
        System.out.println("Is is periodic supplier? y/n");
        String periodicSupplier = in.nextLine();
//        System.out.println(stockService.addProductItem(serialNumbers, catalog_num, supplier, supplier_price, supplier_discount_price, branch, location, expirationDate,periodicSupplier).getReturnValue());
        Response response = Response.fromJson(stockService.addProductItem(serialNumbers, catalog_num, supplier, supplier_price, supplier_discount_price, branch, location, expirationDate,periodicSupplier));
        System.out.println(response.message());
    }

    private void updateProduct() {
        System.out.println("what is the product's catalog num?: (int)");
        in.nextLine();
        String catalog_num = in.nextLine();
        System.out.println("Choose details to update:");
        System.out.println("1. name (String)");
        System.out.println("2. manufacturer (String)");
        System.out.println("3. store price (double)");
        System.out.println("4. Back to main manu");
        System.out.println("5. Exit");
        int option = in.nextInt();
        in.nextLine();
        System.out.println("the new value is:");
        String new_val = in.nextLine();
        switch (option) {
            case 1 -> stockService.updateProduct(new_val, catalog_num, null, -1, -1, branch);
            case 2 -> stockService.updateProduct(null, catalog_num, new_val, -1,-1, branch);
            case 3 -> stockService.updateProduct(null, catalog_num, null, Integer.parseInt(new_val),-1,branch);
            case 4 -> super.Menu();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void updateItem() {
        System.out.println("what is the product's catalog number?");
        in.nextLine(); // Consume the remaining newline character
        String catalog_num = in.nextLine();
        System.out.println("what is the product's serial number?");
        String serial_number = in.nextLine();
        System.out.println("Choose details to update:");
        System.out.println("1. is defective? (0 - no, 1- yes)");
        System.out.println("2. is sold? (0 - no, 1- yes)");
        System.out.println("3. supplier ID (string)");
        System.out.println("4. supplier price");
        System.out.println("5. supplier discount");
        System.out.println("6. sold price");
        System.out.println("7. location (String)");
        System.out.println("8. Back to main manu");
        System.out.println("9. Exit");
        int option = in.nextInt();
        in.nextLine();
        System.out.println("the new value is: ");
        String new_val = in.nextLine();
        switch (option) {
            case 1 -> stockService.updateProduct(Integer.parseInt(new_val), catalog_num, serial_number, -1, null,-1,-1, -1, null, branch);
            case 2 -> stockService.updateProduct(-1, catalog_num, serial_number, Integer.parseInt(new_val), null,-1,-1, -1,  null, branch);
            case 3 -> stockService.updateProduct(-1, catalog_num, serial_number, -1 , new_val,-1,-1, -1,  null, branch);
            case 4 -> stockService.updateProduct(-1, catalog_num, serial_number, -1 , null,Double.parseDouble(new_val),-1, -1,  null, branch);
            case 5 -> stockService.updateProduct(-1, catalog_num, serial_number, -1 , null,-1,Double.parseDouble(new_val), -1,  null, branch);
            case 6 -> stockService.updateProduct(-1, catalog_num, serial_number, -1, null,-1,-1,  Integer.parseInt(new_val), null, branch);
            case 7 -> stockService.updateProduct(-1, catalog_num, serial_number, -1, null,-1,-1, -1,  new_val, branch);
            case 8 -> super.Menu();
            case 9 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void getProductDetails() {
        System.out.println("what is the product's catalog num? (int)");
        in.nextLine(); // Consume the remaining newline character
        String catalog_num = in.nextLine();
        System.out.println("what is the product's serial num? (int)");
        String serial_num = in.nextLine();
//        System.out.println(stockService.getProductDetails(catalog_num, serial_num, branch).getReturnValue().toString());
        Response response = Response.fromJson(stockService.getProductDetails(catalog_num, serial_num, branch));
        System.out.println(response.message());
    }
}