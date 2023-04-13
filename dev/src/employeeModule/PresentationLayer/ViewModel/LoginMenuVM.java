package employeeModule.PresentationLayer.ViewModel;

import dev.PresentationLayer.Model.BackendController;

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
}
