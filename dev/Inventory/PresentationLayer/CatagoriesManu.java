package dev.PresentationLayer;

public class CatagoriesMenu : MainManu {

    public CatagoriesMenu run() {
        System.out.println("Please select an option:");
        System.out.println("1. Add new main catagory");
        System.out.println("2. Add new sub catagory");
        System.out.println("3. Remove catagory");
        System.out.println("4. Back to main manu");
        System.out.println("5. Exit");
        int option = appData.readInt();
        switch (option) {
            case 1 -> mainCatagory();
            case 2 -> subCatagory();
            case 3 -> removeCatagory();
            case 3 -> removeSubCatagory();
            case 4 -> MainManu.run();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void mainCatagory() {

    }

    private void subCatagory() {

    }

    private void removeMainCatagory() {

    }

    private void removeSubCatagory() {

    }

}
