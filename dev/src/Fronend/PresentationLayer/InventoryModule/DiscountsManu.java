package Fronend.PresentationLayer.InventoryModule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiscountsManu extends MainMenu {


    private String branch;
    public void run(String branch) {
        this.branch = branch;
        System.out.println("Please select an option:");
        System.out.println("1. Add product's discount");
        System.out.println("2. Add category's discount");
        System.out.println("3. Back to main manu");
        System.out.println("4. Exit");
        int option = in.nextInt();
        switch (option) {
            case 1 -> productDiscount();
            case 2 -> categoryDiscount();
            case 4 -> new MainMenu().Menu();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void productDiscount() {
        //the dates are supposed to be: "2016-03-04 11:30"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("product catalog number:(number)");
        String catalog_num = in.nextLine();
        System.out.println("discount:(number)");
        double discount = in.nextDouble();
        in.nextLine();
        System.out.println("start date:(\"2016-03-04 11:30\" - example)");
        String start_date = in.nextLine();
        System.out.println("end date:(\"2016-03-04 11:30\" - example)");
        String end_date = in.nextLine();
        System.out.println(stockService.updateDiscountPerProduct(catalog_num, branch, discount, LocalDateTime.parse(start_date, formatter), LocalDateTime.parse(end_date, formatter)).getReturnValue());
    }

    private void categoryDiscount() {
        //the dates are supposed to be: "2016-03-04 11:30"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("category name:(number)");
        String name = in.nextLine();
        System.out.println("discount:(number)");
        double discount = in.nextDouble();
        in.nextLine();
        System.out.println("start date:(\"2016-03-04 11:30\" - example)");
        String start_date = in.nextLine();
        System.out.println("end date:(\"2016-03-04 11:30\" - example)");
        String end_date = in.nextLine();
        System.out.println(stockService.updateDiscountPerCategory(name, branch, discount, LocalDateTime.parse(start_date, formatter), LocalDateTime.parse(end_date, formatter)).getReturnValue());
    }
}
