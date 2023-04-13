package employeeModule.PresentationLayer;

import dev.PresentationLayer.View.MenuManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Employees CLI");
        new MenuManager().run();
    }
}
