import org.junit.jupiter.api.Test;
import presentationLayer.DataGenerator;
import presentationLayer.employeeModule.View.MenuManager;

@SuppressWarnings("NewClassNamingConvention")
public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the Transport-Employees CLI");
        new MenuManager().run();
    }

    @Test
    public void generate(){
        DataGenerator.generateData("SuperLiDB.db");
    }
}
