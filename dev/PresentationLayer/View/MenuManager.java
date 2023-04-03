package dev.PresentationLayer.View;

public class MenuManager {
    private Menu menu;
    private static boolean finished;

    public MenuManager() {
        finished = false;
        menu = new LoginMenu();
    }

    public void run() {
        while (!finished)
            menu = menu.run();
    }

    public static void terminate() {
        finished = true;
    }
}
