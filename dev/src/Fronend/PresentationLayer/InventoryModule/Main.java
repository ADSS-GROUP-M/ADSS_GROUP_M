package Fronend.PresentationLayer.InventoryModule;
import dev.Inventory.PresentationLayer.InventoryModule.MainMenu;

import java.util.*;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("Welcome to the Inventory CLI");
        new MainMenu().run();
    }

}
