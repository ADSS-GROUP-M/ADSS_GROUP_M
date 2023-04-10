package dev.Inventory.PresentationLayer;

public class InventoryProductsManu extends MainMenu {

    public void run() {
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
            case 6 -> super.run();
            case 7 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void createNewProduct() {
        System.out.println("Enter the following product details:");
        System.out.println("Catalog Number: ");
        int catalog_num = in.nextInt();
        System.out.println("Name: ");
        String name = in.nextLine();
        System.out.println("Manufacturer: ");
        String manufacturer = in.nextLine();
        System.out.println("Supplier Price: ");
        double supplier_price = in.nextDouble();
        System.out.println("Store Price: ");
        double store_price = in.nextDouble();
        System.out.println("Amount in store: ");
        int amount_store = in.nextInt();
        System.out.println("Amount in warehouse: ");
        int amount_warehouse = in.nextInt();
        System.out.println(inventoryService.addProductType(catalog_num, name, manufacturer, supplier_price, store_price, branch, amount_store, amount_warehouse).getReturnValue());
    }

    private void createNewItem() {
        //supplier should be ID
        System.out.println("Enter the following product details:");
        System.out.println("Catalog Number: ");
        int catalog_num = in.nextInt();
        System.out.println("Serial Number: ");
        int serial_num = in.nextInt();
        System.out.println("Supplier: ");
        int supplier = in.nextInt();
        System.out.println("Supplier Price: ");
        int supplier_price = in.nextInt();
        System.out.println("location: ");
        String location = in.nextLine();
        System.out.println(inventoryService.addProduct(serial_num, catalog_num, supplier, supplier_price, branch, location).getReturnValue());
    }

    private void updateProduct() {
        System.out.println("what is the product's catalog num?:");
        int catalog_num = in.nextInt();
        System.out.println("Choose details to update:");
        System.out.println("1. name");
        System.out.println("2. manufacturer");
        System.out.println("3. supplier price");
        System.out.println("4. store price");
        System.out.println("5. category");
        System.out.println("6. sub category");
        System.out.println("7. minimum amount for notification about running out of products");
        System.out.println("8. Back to main manu");
        System.out.println("9. Exit");
        int option = in.nextInt();
        System.out.println("the new value is:");
        String new_val = in.nextLine();
        switch (option) {
            case 1 -> inventoryService.updateProductType(new_val, catalog_num, null, -1, -1, null, null, -1, branch);
            case 2 -> inventoryService.updateProductType(null, catalog_num, new_val, -1, -1, null, null, -1, branch);
            case 3 -> inventoryService.updateProductType(null, catalog_num, null, Integer.parseInt(new_val), -1, null, null, -1, branch);
            case 4 -> inventoryService.updateProductType(null, catalog_num, null, -1, Integer.parseInt(new_val), null, null, -1, branch);
            case 5 -> inventoryService.updateProductType(null, catalog_num, null, -1, -1, new_val, null, -1, branch);
            case 6 -> inventoryService.updateProductType(null, catalog_num, null, -1, -1, null, new_val, -1, branch);
            case 7 -> inventoryService.updateProductType(null, catalog_num, null, -1, -1, null, null, Integer.parseInt(new_val), branch);
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
        System.out.println("1. is defective?");
        System.out.println("2. is sold?");
        System.out.println("3. supplier");
        System.out.println("4. supplier price");
        System.out.println("5. sold price");
        System.out.println("6. location");
        System.out.println("7. Back to main manu");
        System.out.println("8. Exit");
        int option = in.nextInt();
        System.out.println("the new value is: [[for condition question -> 1 is yes and 2 is no]]");
        String new_val = in.nextLine();
        switch (option) {
            case 1 -> inventoryService.updateProduct(Integer.parseInt(new_val), catalog_num, serial_number, -1, -1, -1, -1, null, branch);
            case 2 -> inventoryService.updateProduct(-1, catalog_num, serial_number, Integer.parseInt(new_val), -1, -1, -1, null, branch);
            case 3 -> inventoryService.updateProduct(-1, catalog_num, serial_number, -1 , Integer.parseInt(new_val), -1, -1, null, branch);
            case 4 -> inventoryService.updateProduct(-1, catalog_num, serial_number, -1, -1, Integer.parseInt(new_val), -1, null, branch);
            case 5 -> inventoryService.updateProduct(-1, catalog_num, serial_number, -1, -1, -1, Integer.parseInt(new_val), null, branch);
            case 6 -> inventoryService.updateProduct(-1, catalog_num, serial_number, -1, -1, -1, -1, new_val, branch);
            case 7 -> super.run();
            case 8 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void getProductDetails() {
        System.out.println("what is the product's catalog num?");
        int catalog_num = in.nextInt();


    }
}