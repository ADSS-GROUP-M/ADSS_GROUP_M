package dev.PresentationLayer;
package dev.ServiceLayer;

public class InventoryProductsManu : MainManu {

    public InventoryProductsManu run() {
        System.out.println("Please select an option:");
        System.out.println("1. create new product");
        System.out.println("2. create new item");
        System.out.println("3. update product");
        System.out.println("4. update item");
        System.out.println("5. get all product's detalis");
        System.out.println("6. Back to main manu");
        System.out.println("7. Exit");
        int option = in.nextLine();
        switch (option) {
            case 1 -> createNewProduct();
            case 2 -> createNewItem();
            case 3 -> updateProduct();
            case 4 -> updateItem();
            case 5 -> getProductDetails();
            case 6 -> MainManu.run();
            case 7 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void createNewProduct() {
        System.out.println("Enter the following product details:");
        System.out.println("Catalog Number: ");
        String catalog_num = in.nextLine();
        System.out.println("Name: ");
        String name = in.nextLine();
        System.out.println("Supplier: ");
        String supplier = in.nextLine();
        System.out.println("Manufacturer: ");
        String manufacturer = in.nextLine();
        System.out.println("Supplier Price: ");
        String supplier_price = in.nextLine();
        System.out.println("Store Price: ");
        String store_price = in.nextLine();
        InventoryService.addProductType(catalog_num, name, manufacturer, supplier, supplier_price, store_price);
    }

    private void createNewItem() {
        System.out.println("Enter the following product details:");
        System.out.println("Catalog Number: ");
        String catalog_num = in.nextLine();
        System.out.println("Serial Number: ");
        String serial_num = in.nextLine();
        System.out.println("Supplier: ");
        String manufacturer = in.nextLine();
        System.out.println("Supplier Price: ");
        String supplier_price = in.nextLine();
        InventoryService.addProduct(catalog_num, serial_num, supplier, supplier_price);
    }

    private void updateProduct() {
        System.out.println("what is the product's catalog num?:");
        int catalog_num = in.nextLine();
        System.out.println("Choose details to update:");
        System.out.println("1. name");
        System.out.println("2. manufacturer");
        System.out.println("3. supplier price");
        System.out.println("4. store price");
        System.out.println("5. catagory");
        System.out.println("6. sub catagory");
        System.out.println("7. minimum amount for notification about running out of products");
        System.out.println("8. Back to main manu");
        System.out.println("9. Exit");
        int option = in.nextLine();
        System.out.println("the new value is:");
        int new_val = in.nextLine();
        switch (option) {
        case 1 -> updateProductType(new_val, catalog_num, null, null, -1, -1, null, null, -1);
        case 2 -> updateProductType(null, catalog_num, new_val, null, -1, -1, null, null, -1);
        case 3 -> updateProductType(null, catalog_num, null, new_val, -1, -1, null, null, -1);
        case 4 -> updateProductType(null, catalog_num, null, null, new_val, -1, null, null, -1);
        case 5 -> updateProductType(null, catalog_num, null, null, -1, new_val, null, null, -1);
        case 6 -> updateProductType(null, catalog_num, null, null, -1, -1, new_val, null, -1);
        case 7 -> updateProductType(null, catalog_num, null, null, -1, new_val, null, null, -1);
        case 8 -> MainManu.run();
        case 9 -> System.exit(0);
        default -> System.out.println("\nInvalid command");
    }

    private void updateItem() {
        System.out.println("what is the product's catalog num?:");
        int catalog_num = in.nextLine();
        System.out.println("Choose details to update:");
        System.out.println("1. is defective?");
        System.out.println("2. is sold?");
        System.out.println("3. supplier");
        System.out.println("4. supplier price");
        System.out.println("5. sold price");
        System.out.println("6. location");
        System.out.println("7. Back to main manu");
        System.out.println("8. Exit");
        int option = in.nextLine();
        System.out.println("the new value is: [[for condition question -> 1 is yes and 2 is no]]");
        int new_val = in.nextLine();
        switch (option) {
        case 1 -> updateProductType(new_val, catalog_num, -1, null, -1, -1, null);
        case 2 -> updateProductType(-1, catalog_num, new_val, null, -1, -1, null);
        case 3 -> updateProductType(-1, catalog_num, -1, new_val, -1, -1, null);
        case 4 -> updateProductType(-1, catalog_num, -1, null, new_val, -1, null);
        case 5 -> updateProductType(-1, catalog_num, -1, null, -1, new_val, null);
        case 6 -> updateProductType(-1, catalog_num, -1, null, -1, -1, new_val);
        case 7 -> MainManu.run();
        case 8 -> System.exit(0);
        default -> System.out.println("\nInvalid command");
    }

    private void getProductDetails() {
        System.out.println("what is the product's catalog num?:");
        int catalog_num = in.nextLine();

        }
}
