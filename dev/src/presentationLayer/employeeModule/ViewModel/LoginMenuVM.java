package presentationLayer.employeeModule.ViewModel;

import presentationLayer.employeeModule.Model.BackendController;
import serviceLayer.transportModule.ServiceFactory;

import java.util.List;

public class LoginMenuVM {
    private BackendController backendController;

    public LoginMenuVM() {
        backendController = BackendController.getInstance();
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
