package presentationLayer.gui.employeeModule.controller;

import exceptions.ErrorOccurredException;
import presentationLayer.gui.employeeModule.model.ObservableEmployee;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import utils.Response;

public class EmployeesControl extends AbstractControl {

    private final EmployeesService employeesService;

    public EmployeesControl(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @Override
    public void add(ObservableUIElement observable, ObservableModel model) {

        ObservableEmployee employeeModel = (ObservableEmployee) model;

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
        employeeModel.notifyObservers();
    }

    public String certifyEmployee(String employeeId, String role) {
        String json = employeesService.certifyEmployee(UserService.HR_MANAGER_USERNAME,employeeId,role);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Certified the employee successfully.";
    }

    public String uncertifyEmployee(String employeeId, String role) {
        String json = employeesService.uncertifyEmployee(UserService.HR_MANAGER_USERNAME,employeeId,role);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Uncertified the employee successfully.";
    }
}
