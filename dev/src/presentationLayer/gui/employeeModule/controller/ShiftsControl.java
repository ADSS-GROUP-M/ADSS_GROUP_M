package presentationLayer.gui.employeeModule.controller;

import domainObjects.transportModule.Truck;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.employeeModule.model.ObservableShift;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import presentationLayer.gui.transportModule.model.ObservableTruck;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.transportModule.ResourceManagementService;
import utils.Response;

import java.util.List;

public class ShiftsControl extends AbstractControl {

    private final EmployeesService emp;
    private final ResourceManagementService rms;

    public ShiftsControl(EmployeesService emp, ResourceManagementService rms) {
        this.emp = emp;
        this.rms = rms;
    }

    @Override
    public void add(ObservableUIElement observable, ObservableModel model) {

        ObservableShift shiftModel = (ObservableShift) model;

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
