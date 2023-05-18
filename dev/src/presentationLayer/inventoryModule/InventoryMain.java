package presentationLayer.inventoryModule;

public class InventoryMain {

    public void run()
    {
        System.out.println("Welcome to the Inventory CLI");
        new MainMenu().run();
    }

}
