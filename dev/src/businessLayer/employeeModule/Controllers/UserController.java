package businessLayer.employeeModule.Controllers;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.User;
import dataAccessLayer.employeeModule.UserDAO;

import java.util.List;

public class UserController {

    UserDAO userDAO;

    public UserController(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public void resetData() {
        try {
            this.userDAO.clearTable();
        } catch (Exception ignore) {}
    }

    public void login(String username, String password) throws Exception {
        User user = getUser(username); // Throws an exception if the user is not found
        boolean success = user.login(password);
        if(!success)
            throw new Exception("Invalid password.");
    }

    public void logout(String username) throws Exception {
        getUser(username).logout(); // Throws an exception if the user is not found
    }

    public User getUser(String username) throws Exception {
        User user = userDAO.get(username);
        if (user == null)
            throw new Exception("The given username doesn't exist.");
        return user;
    }

    public void createUser(String username, String password) throws Exception {
        User user = new User(username, password);
        userDAO.create(user);
    }

    public void createManagerUser(String username, String password) throws Exception {
        User user = new User(username, password, Authorization.HRManager);
        userDAO.create(user);
    }

    public boolean isAuthorized(String username, Authorization auth) throws Exception {
        User user = getUser(username);
        return user.isAuthorized(auth);
    }

    public List<Authorization> getUserAuthorizations(String username) throws Exception {
        User user = getUser(username);
        return user.getAuthorizations();
    }

    public void authorizeUser(String username, Authorization auth) throws Exception {
        User user = getUser(username);
        user.authorize(auth);
        userDAO.update(user);
    }

    public boolean isLoggedIn(String username) throws Exception {
        return getUser(username).isLoggedIn();
    }
}