package presentationLayer.employeeModule.View;

public class MenuManager {
    private Menu menu;
    private static boolean finished;

    public MenuManager() {
        finished = false;
        menu = new LoginMenu();
    }

    public void runCLI() {
        while (!finished){
            menu = menu.runGUI();
        }
        System.exit(0);
    }

    public void runGUI() {
        while (!finished){
            menu = menu.runGUI();
        }
        System.exit(0);
    }

    public static void terminate() {
        finished = true;
    }
}
