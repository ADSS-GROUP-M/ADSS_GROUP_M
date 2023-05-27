package businessLayer.employeeModule.Controllers;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.User;
import dataAccessLayer.employeeModule.UserDAO;
import exceptions.DalException;
import exceptions.EmployeeException;

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

    public void login(String username, String password) throws EmployeeException {
        User user = getUser(username); // Throws an exception if the user is not found
        boolean success = false;
        success = user.login(password);
        if(!success) {
            throw new EmployeeException("Invalid password.");
        }
    }

    public void logout(String username) throws EmployeeException {
        getUser(username).logout(); // Throws an exception if the user is not found
    }

    public User getUser(String username) throws EmployeeException {
        User user = null;
        try {
            user = userDAO.select(username);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
        if (user == null) {
            throw new EmployeeException("The given username doesn't exist.");
        }
        return user;
    }

    public void createUser(String username, String password) throws EmployeeException {
        User user = new User(username, password);
        try {
            userDAO.insert(user);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public void createManagerUser(String username, String password) throws EmployeeException {
        User user = new User(username, password, Authorization.HRManager);
        try {
            userDAO.insert(user);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public boolean isAuthorized(String username, Authorization auth) throws EmployeeException {
        User user = getUser(username);
        return user.isAuthorized(auth);
    }

    public List<Authorization> getUserAuthorizations(String username) throws EmployeeException {
        User user = getUser(username);
        return user.getAuthorizations();
    }

    public void authorizeUser(String username, Authorization auth) throws EmployeeException {
        User user = null;
        try {
            user = getUser(username);
            user.authorize(auth);
            userDAO.update(user);
        } catch (DalException e) {
            throw new EmployeeException(e.getMessage(),e);
        }
    }

    public boolean isLoggedIn(String username) throws EmployeeException {
        return getUser(username).isLoggedIn();
    }
}