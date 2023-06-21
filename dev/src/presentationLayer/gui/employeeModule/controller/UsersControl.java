package presentationLayer.gui.employeeModule.controller;

import exceptions.ErrorOccurredException;
import presentationLayer.gui.employeeModule.model.ObservableUser;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import serviceLayer.employeeModule.Services.UserService;
import utils.Response;

import java.util.List;

public class UsersControl extends AbstractControl {

    private final UserService userService;

    public UsersControl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void add(ObservableUIElement observable, ObservableModel model) {

        ObservableUser shiftModel = (ObservableUser) model;

//        Shift shift = new Truck(truckModel.id,
//                truckModel.model,
//                truckModel.baseWeight,
//                truckModel.maxWeight,
//                truckModel.coolingCapacity);
//        String json = r(truck.toJson());
//        Response response;
//        try {
//            response = Response.fromJsonWithValidation(json);
//        } catch (ErrorOccurredException e) {
//            throw new RuntimeException(e);
//            //TODO: handle exception
//        }

//        truckModel.response = response.message();
        shiftModel.notifyObservers();
    }

    public String authorizeUser(String username, String authorization) {
        String json = userService.authorizeUser(UserService.HR_MANAGER_USERNAME,username,authorization);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Authorized the user successfully.";
    }

    public Object findUserAuthorizations(String username) {
        String json = userService.getUserAuthorizations(username);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
            return response.data(List.class);
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
    }

    public Object login(String username, String password) {
        String json = userService.login(username,password);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
            return response.success();
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
    }
}
