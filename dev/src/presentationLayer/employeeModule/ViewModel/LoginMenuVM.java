package presentationLayer.employeeModule.ViewModel;

import presentationLayer.employeeModule.Model.BackendController;
import serviceLayer.ServiceFactory;

import java.util.List;

public class LoginMenuVM {
    private final BackendController backendController;

    public LoginMenuVM() {
        backendController = new BackendController();
    }

    public LoginMenuVM(ServiceFactory factory) {
        backendController = new BackendController(factory);
    }

    public String login(String username, String password) {
        return backendController.login(username, password);
    }

    public boolean isLoggedIn() {
        return backendController.isLoggedIn();
    }

    public List<String> getUserAuthorizations() {
        return backendController.getUserAuthorizations();
    }

    public ServiceFactory serviceFactory() {
        return backendController.serviceFactory();
    }
}
