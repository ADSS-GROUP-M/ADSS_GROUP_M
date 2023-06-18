import org.junit.jupiter.api.Test;
import presentationLayer.DataGenerator;
import presentationLayer.PresentationFactory;
import presentationLayer.cli.employeeModule.View.MenuManager;
import presentationLayer.gui.plAbstracts.MainWindow;
import presentationLayer.gui.transportModule.control.*;
import presentationLayer.gui.transportModule.view.TransportView;

@SuppressWarnings("NewClassNamingConvention")
public class Main {
    public static void main(String[] args) {

        PresentationFactory factory = new PresentationFactory();

        TransportsControl transportsControl = factory.getTransportsControl();
        ItemListsControl itemListsControl = factory.getItemListsControl();
        DriversControl driversControl = factory.getDriversControl();
        TrucksControl trucksControl = factory.getTrucksControl();
        SitesControl sitesControl = factory.getSitesControl();
        MainWindow mainWindow = new TransportView(transportsControl, itemListsControl, driversControl, trucksControl, sitesControl);
    }

//    public static void main(String[] args) {
//        System.out.println("Welcome to the Transport-Employees CLI");
//        new MenuManager().run();
//    }
//
//    @Test
//    public void generate(){
//        new DataGenerator().generateData();
//    }
}
