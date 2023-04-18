package ServiceLayer.employeeModule.Services;

import BusinessLayer.employeeModule.Authorization;
import BusinessLayer.employeeModule.Controllers.UserController;
import BusinessLayer.employeeModule.User;
import utils.Response;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserService {
    private static UserService instance;
    private UserController userController;

    private UserService() {
        userController = UserController.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService();
        return instance;
    }

    /**
     * This method loads the initial user data into the system, during the initial load of the system.
     */
    public void loadData() {
        resetData();
        try {
            userController.createManagerUser("admin123", "123");
            userController.createUser("logistic", "123");
            userController.authorizeUser("logistic", Authorization.LogisticsManager);
        }
        catch (Exception ignore) {}
    }

    /**
     * This method resets the user data from the system.
     */
    public void resetData() {
        userController.resetData();
    }

    public Response login(String username, String password) {
        try {
            this.userController.login(username, password);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response logout(String username) {
        try {
            this.userController.logout(username);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response getUser(String username) {
        try {
            User user = this.userController.getUser(username);
            return new Response(true,user);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response createUser(String actorUsername, String username, String password) {
        try {
            if(!userController.isAuthorized(actorUsername, Authorization.HRManager)) // Throws an exception if actor user is not found
                throw new Exception("User " + actorUsername + " is not authorized to create users.");
            userController.createUser(username, password);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response createManagerUser(String actorUsername, String username, String password) {
        try {
            if(!userController.isAuthorized(actorUsername, Authorization.HRManager)) // Throws an exception if actor user is not found
                throw new Exception("User " + actorUsername + " is not authorized to create manager users.");
            userController.createManagerUser(username, password);
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response isAuthorized(String username, String authorization) {
        try {
            boolean result = userController.isAuthorized(username, Authorization.valueOf(authorization));
            return new Response(true,result);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response isAuthorized(String username, Authorization authorization) {
        try {
            boolean result = userController.isAuthorized(username, authorization);
            return new Response(true,result);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response getUserAuthorizations(String username) {
        try {
            List<Authorization> authorizations = userController.getUserAuthorizations(username);
            List<String> result = authorizations.stream().map(Objects::toString).collect(Collectors.toList());
            return new Response(true,result);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }

    public Response authorizeUser(String actorUsername, String username, String authorization) {
        try {
            if(!userController.isAuthorized(actorUsername, Authorization.HRManager)) // Throws an exception if actor user is not found
                throw new Exception("User " + actorUsername + " is not authorized to authorize other users.");
            userController.authorizeUser(username, Authorization.valueOf(authorization));
            return new Response(true);
        } catch (Exception e) {
            return Response.getErrorResponse(e);
        }
    }
}
