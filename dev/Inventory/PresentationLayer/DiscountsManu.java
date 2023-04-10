package dev.Inventory.PresentationLayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiscountsManu extends MainMenu {


    public void run() {
        System.out.println("Please select an option:");
        System.out.println("1. Add product's discount");
        System.out.println("2. Add category's discount");
        System.out.println("3. Back to main manu");
        System.out.println("4. Exit");
        int option = in.nextInt();
        switch (option) {
            case 1 -> productDiscount();
            case 2 -> categoryDiscount();
            case 4 -> super.run();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void productDiscount() {
        //the dates are supposed to be: "2016-03-04 11:30"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("product catalog number:");
        int catalog_num = in.nextInt();
        System.out.println("discount:");
        double discount = in.nextDouble();
        System.out.println("start date:");
        String start_date = in.nextLine();
        System.out.println("end date:");
        String end_date = in.nextLine();
        System.out.println(inventoryService.updateDiscountPerProduct(catalog_num, branch, discount, LocalDateTime.parse(start_date, formatter), LocalDateTime.parse(end_date, formatter)).getReturnValue());
    }

    private void categoryDiscount() {
        //the dates are supposed to be: "2016-03-04 11:30"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("category name:");
        String name = in.nextLine();
        System.out.println("discount:");
        double discount = in.nextDouble();
        System.out.println("start date:");
        String start_date = in.nextLine();
        System.out.println("end date:");
        String end_date = in.nextLine();
        System.out.println(inventoryService.updateDiscountPerCategory(name, branch, discount, LocalDateTime.parse(start_date, formatter), LocalDateTime.parse(end_date, formatter)).getReturnValue());
    }
}
