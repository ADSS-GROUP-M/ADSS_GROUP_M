package Fronend.PresentationLayer.InventoryModule;
import Fronend.PresentationLayer.InventoryModule.MainMenu;

import java.util.*;

public class InventoryMain {

    public void run()
    {
        System.out.println("Welcome to the Inventory CLI");
        new MainMenu().run();
    }

}
