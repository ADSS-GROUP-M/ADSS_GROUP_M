package serviceLayer.employeeModule.Services;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.Controllers.UserController;
import businessLayer.employeeModule.User;
import utils.Response;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserService {

    public static final String TRANSPORT_MANAGER_USERNAME = "transport";
    public static final String HR_MANAGER_USERNAME = "admin123";

    private final UserController userController;

    public UserService(UserController userController) {
        this.userController = userController;
    }

    public String login(String username, String password) {
        try {
            this.userController.login(username, password);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String logout(String username) {
        try {
            this.userController.logout(username);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getUser(String username) {
        try {
            User user = this.userController.getUser(username);
            return new Response(true,user).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String createUser(String actorUsername, String username, String password) {
        try {
            if(!userController.isAuthorized(actorUsername, Authorization.HRManager)) // Throws an exception if actor user is not found
                throw new Exception("User " + actorUsername + " is not authorized to create users.");
            userController.createUser(username, password);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String createManagerUser(String actorUsername, String username, String password) {
        try {
            if(!userController.isAuthorized(actorUsername, Authorization.HRManager)) // Throws an exception if actor user is not found
                throw new Exception("User " + actorUsername + " is not authorized to create manager users.");
            userController.createManagerUser(username, password);
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String isAuthorized(String username, String authorization) {
        try {
            boolean result = userController.isAuthorized(username, Authorization.valueOf(authorization));
            return new Response(true,result).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String isAuthorized(String username, Authorization authorization) {
        try {
            boolean result = userController.isAuthorized(username, authorization);
            return new Response(true,result).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getUserAuthorizations(String username) {
        try {
            List<Authorization> authorizations = userController.getUserAuthorizations(username);
            List<String> result = authorizations.stream().map(Objects::toString).collect(Collectors.toList());
            return new Response(true,result).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String authorizeUser(String actorUsername, String username, String authorization) {
        try {
            if(!userController.isAuthorized(actorUsername, Authorization.HRManager)) // Throws an exception if actor user is not found
                throw new Exception("User " + actorUsername + " is not authorized to authorize other users.");
            userController.authorizeUser(username, Authorization.valueOf(authorization));
            return new Response(true).toJson();
        } catch (Exception e) {
            return Response.getErrorResponse(e).toJson();
        }
    }
}
