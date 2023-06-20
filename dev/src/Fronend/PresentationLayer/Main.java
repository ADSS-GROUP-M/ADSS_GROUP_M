package Fronend.PresentationLayer;


import Fronend.PresentationLayer.InventoryModule.cli.InventoryMain;
import Fronend.PresentationLayer.SuppliersModule.CLI.SupplierMain;
import Fronend.PresentationLayer.SuppliersModule.GUI.SuppliersGUI;

public class Main {
    public static void main(String[] args) {
        if(args.length != 2)
            System.out.println("invalid number of parameters");
        else {
            String UI = args[0];
            String role = args[1];
            if(UI.equals("GUI")){
                if(role.equals("StoreManager"))
                    new ManagerWindowGUI();
                else if(role.equals("WarehouseMen")){
                    //TODO: CALL THE MAIN GUI OF WAREHOUSEMEN
                }
                else if(role.equals("SuppliersManager")){
                    new SuppliersGUI(false);
                }
                else
                    System.out.println("Invalid Role");
            }
            else if (UI.equals("CLI")) {
                if(role.equals("StoreManager"))
                    new ManagerWindowCLI();
                else if(role.equals("WarehouseMen"))
                    new InventoryMain().run();
                else if (role.equals("SuppliersManager")) {
                    new SupplierMain().run();
                }
                else
                    System.out.println("Invalid Role");
            }
            else
                System.out.println("invalid UI");
        }
    }
}
