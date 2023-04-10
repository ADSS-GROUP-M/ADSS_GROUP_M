package dev.PresentationLayer;

public class DiscountsMenu : MainManu {

    public DiscountsMenu run() {
        System.out.println("Please select an option:");
        System.out.println("1. Add product's discount");
        System.out.println("2. Add catagory's discount");
        System.out.println("3. Back to main manu");
        System.out.println("4. Exit");
        int option = in.nextLine();
        switch (option) {
            case 1 -> productDiscount().run();
            case 2 -> catagoryDiscount().run();
            case 4 -> MainManu.run();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void productDiscount() {

    }

    private void catagoryDiscount() {

    }

}
