package presentationLayer.suppliersModule;

public class SupplierMain {
    public void run() {
        UI ui = new UI();
        boolean run = true;
        while (run)
            run = ui.run();
        System.out.println("bye!\nhope you enjoyed");
    }
}
