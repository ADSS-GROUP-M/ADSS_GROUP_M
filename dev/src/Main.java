import org.junit.jupiter.api.Test;
import presentationLayer.DataGenerator;
import presentationLayer.employeeModule.View.MenuManager;
import presentationLayer.transportModule.TransportUI;
import serviceLayer.ServiceFactory;

@SuppressWarnings("NewClassNamingConvention")
public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the Transport-Employees CLI");
        //DataGenerator.generateData();
        //new TransportUI(new ServiceFactory()).run();
        new MenuManager().run();
    }

    @Test
    public void generate(){
        DataGenerator.generateData();
    }
}
