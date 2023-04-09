package dev.PresentationLayer;

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
        int option = appData.readInt();
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
        int id = appData.readInt("Catalog number: ");
        String fullName = appData.readLine("Manufacturer: ");
        System.out.println("Supplier Price: ");
        String fullName = appData.readLine("Manufacturer: ");
        System.out.println("Supplier Price: ");
        String fullName = appData.readLine("Manufacturer: ");
        System.out.println("Supplier Price: ");
        Driver.LicenseType licenseType = pickLicenseType();
        if (licenseType == null) return;
        Driver newDriver = new Driver(id, fullName, licenseType);
        String json = JSON.serialize(newDriver);
        String responseJson = rms.addDriver(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()) appData.drivers().put(id, newDriver);
        System.out.println("\n"+response.getMessage());

    }

    private void createNewItem() {

    }

    private void getProductDetails() {

    }

}
