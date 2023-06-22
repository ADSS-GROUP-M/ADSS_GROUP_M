package presentationLayer.cli.employeeModule.View;

import org.junit.jupiter.api.Test;
import presentationLayer.DataGenerator;

import javax.xml.crypto.Data;

public class MenuManager {
    private Menu menu;
    private static boolean finished;

    public MenuManager() {
        finished = false;
        menu = new LoginMenu();
    }

    public MenuManager(Menu menu) {
        finished = false;
        this.menu = menu;
    }

    public void run() {
        while (!finished) {
            menu = menu.run();
        }
        System.exit(0);
    }

    public static void terminate() {
        finished = true;
    }

    public static void main(String[] args) {
        new MenuManager().run();
    }

    @Test
    public void generateData() {
        new DataGenerator().generateData();
    }
}
