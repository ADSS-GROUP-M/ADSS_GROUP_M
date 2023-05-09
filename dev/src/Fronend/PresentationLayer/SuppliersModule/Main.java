package Fronend.PresentationLayer.SuppliersModule;

public class Main {
    public static void main(String[] args) {
        UI ui = new UI();
        boolean run = true;
        while (run)
            run = ui.run();
        System.out.println("bye!\nhope you enjoyed");
    }
}
