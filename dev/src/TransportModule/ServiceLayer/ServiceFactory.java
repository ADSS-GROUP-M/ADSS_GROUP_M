package TransportModule.ServiceLayer;

public class ServiceFactory {
    private static ServiceFactory instance = null;

    private final TransportsService transportsService;
    private final ModuleManagementService moduleManagementService;

    private ServiceFactory(){
        transportsService = new TransportsService();
        moduleManagementService = new ModuleManagementService();
    }

    public TransportsService getTransportsService() {
        return transportsService;
    }

    public ModuleManagementService getModuleManagementService() {
        return moduleManagementService;
    }

    public static ServiceFactory getInstance(){
        if(instance == null){
            instance = new ServiceFactory();
        }
        return instance;
    }
}
