package dev.Employees.ServiceLayer;

import dev.Employees.BusinessLayer.User;
import dev.Employees.BusinessLayer.UserController;

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

    public Response<boolean> login(String username, String password) {
        try {
            this.userController.login(username, password);
            return new Response(true);
        } catch (Exception e) {
            return Response.createErrorResponse(e.getMessage());
        }
    }

    public Response<boolean> logout(String username) {
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
}
