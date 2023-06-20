package presentationLayer.gui.employeeModule.controller;

import presentationLayer.gui.employeeModule.model.ObservableShift;
import presentationLayer.gui.employeeModule.model.ObservableUser;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import serviceLayer.employeeModule.Services.UserService;
import serviceLayer.transportModule.ResourceManagementService;

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
}
