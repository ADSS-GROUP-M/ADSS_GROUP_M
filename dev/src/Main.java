import org.junit.jupiter.api.Test;
import presentationLayer.DataGenerator;
import presentationLayer.PresentationFactory;
import presentationLayer.cli.employeeModule.View.MenuManager;
import presentationLayer.cli.transportModule.TransportCLI;
import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.transportModule.control.*;
import presentationLayer.gui.transportModule.view.TransportView;
import serviceLayer.ServiceFactory;

@SuppressWarnings("NewClassNamingConvention")
public class Main {
    public static void main(String[] args) {

        String argsMsg = "Required arguments: <GUI | CLI> <TransportManager | HRManager | StoreManager | Employee>";

        if(args.length != 2){
            System.out.println(argsMsg);
            return;
        }

        String guiOrCli = args[0].toLowerCase();
        if(!guiOrCli.equals("gui") && !guiOrCli.equals("cli")){
            System.out.println(argsMsg);
            return;
        }

        String module = args[1].toLowerCase();
        if(!module.equals("transportmanager") && !module.equals("hrmanager") && !module.equals("storemanager") && !module.equals("employee")){
            System.out.println(argsMsg);
            return;
        }

        boolean gui = guiOrCli.equals("gui");

        switch(module){
            case "transportmanager" -> transportModule(gui);
            case "hrmanager" -> employeeModule(gui);
            case "storemanager" -> storeManager(gui);
            case "employee" -> employee(gui);
        }

    }

    private static void employee(boolean gui) {

    }

    private static void storeManager(boolean gui) {

    }

    public static void transportModule(boolean gui){

        if(gui){

            PresentationFactory factory = new PresentationFactory();

            TransportsControl transportsControl = factory.getTransportsControl();
            ItemListsControl itemListsControl = factory.getItemListsControl();
            DriversControl driversControl = factory.getDriversControl();
            TrucksControl trucksControl = factory.getTrucksControl();
            SitesControl sitesControl = factory.getSitesControl();
            new TransportView(transportsControl, itemListsControl, driversControl, trucksControl, sitesControl);
        } else {

            ServiceFactory serviceFactory = new ServiceFactory();
            TransportCLI transportCLI = new TransportCLI(serviceFactory);
            MenuManager menuManager = new MenuManager(transportCLI);
            menuManager.run();
        }


    }

    public static void employeeModule(boolean gui){

    }



//    public static void main(String[] args) {
//        System.out.println("Welcome to the Transport-Employees CLI");
//        new MenuManager().run();
//    }
//
    @Test
    public void generate(){
        new DataGenerator().generateData();
    }
}
