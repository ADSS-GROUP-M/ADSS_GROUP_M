import org.junit.jupiter.api.Test;
import presentationLayer.DataGenerator;
import presentationLayer.employeeModule.View.MenuManager;

import javax.xml.crypto.Data;

@SuppressWarnings("NewClassNamingConvention")
public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the Transport-Employees CLI");
        new MenuManager().run();
    }

    @Test
    public void generate(){
        new DataGenerator().generateData("SuperLiDB.db");
    }
}
