package employeeModule.BusinessLayer.Employees.Controllers;

import employeeModule.BusinessLayer.Employees.Authorization;
import employeeModule.BusinessLayer.Employees.User;

import java.util.*;

public class UserController {

    private Map<String, User> users;
    private static UserController instance;

    private UserController(){
        users = new HashMap<>();
    }

    public static UserController getInstance() {
        if(instance == null){
            instance = new UserController();
        }
        return instance;
    }

    public void resetData() {
        this.users.clear();
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
        if (users.containsKey(username))
            return users.get(username);
        throw new Exception ("The given username doesn't exist.");
    }

    public void createUser(String username, String password) throws Exception {
        if (users.containsKey(username))
            throw new Exception("Username " + username + " already exists in the system.");
        User user = new User(username, password);
        users.put(username, user);
    }

    public void createManagerUser(String username, String password) throws Exception {
        if (users.containsKey(username))
            throw new Exception("Username already exists.");
        User user = new User(username, password, Authorization.HRManager);
        users.put(username,user);
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
    }

    public boolean isLoggedIn(String username) throws Exception {
        return getUser(username).isLoggedIn();
    }
}