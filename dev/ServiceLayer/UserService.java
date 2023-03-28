package dev.ServiceLayer;

import dev.BusinessLayer.Employees.UserController;
import dev.BusinessLayer.Employees.User;

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

    public Response<Boolean> login(String username, String password) {
        try {
            this.userController.login(username, password);
            return new Response(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> logout(String username) {
        try {
            this.userController.logout(username);
            return new Response(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<User> getUser(String username) {
        try {
            User user = this.userController.getUser(username);
            return new Response(user);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> createUser(String username, String password) {
        try {
            userController.createUser(username, password, null); // TODO: check if a linked employee is necessary for all users.
            return new Response(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<Boolean> createManagerUser(String username, String password) {
        try {
            userController.createManagerUser(username, password, null);
            return new Response(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }
}
