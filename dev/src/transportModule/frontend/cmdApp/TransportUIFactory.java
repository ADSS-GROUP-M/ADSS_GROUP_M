package transportModule.frontend.cmdApp;

import transportModule.backend.serviceLayer.ModuleFactory;

public class TransportUIFactory {

    private static final ModuleFactory factory = new ModuleFactory();

    public static TransportAppData getAppData() {

        return new TransportAppData(
                factory.getResourceManagementService(),
                factory.getItemListsService(),
                factory.getTransportsService()
        );
    }
    public static ModuleFactory getFactory() {
        return factory;
    }
}
