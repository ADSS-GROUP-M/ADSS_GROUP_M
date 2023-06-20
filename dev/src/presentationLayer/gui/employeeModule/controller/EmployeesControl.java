package presentationLayer.gui.employeeModule.controller;

import presentationLayer.gui.employeeModule.model.ObservableEmployee;
import presentationLayer.gui.employeeModule.model.ObservableShift;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.transportModule.ResourceManagementService;

import java.util.List;

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
}
