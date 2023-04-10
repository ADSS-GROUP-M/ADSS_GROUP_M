package dev.Inventory.PresentationLayer;

public class ReportsMenu extends MainMenu {

    public void run() {
        System.out.println("Please select an option:");
        System.out.println("1. Inventory products report");
        System.out.println("2. Inventory running-out products report");
        System.out.println("3. Inventory defective products report");
        System.out.println("4. Back to main manu");
        System.out.println("5. Exit");
        int option = in.nextInt();
        switch (option) {
            case 1 -> inventoryReport();
            case 2 -> inventoryRunningOutReport();
            case 3 -> inventoryDefectiveReport();
            case 4 -> super.run();
            case 5 -> System.exit(0);
            default -> System.out.println("\nInvalid command");
        }
    }

    private void inventoryReport() {
        throw new RuntimeException();
    }

    private void inventoryRunningOutReport() {
        throw new RuntimeException();
    }

    private void inventoryDefectiveReport() {
        throw new RuntimeException();
    }
}
