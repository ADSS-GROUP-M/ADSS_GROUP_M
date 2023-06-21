package presentationLayer.gui.employeeModule.controller;

import businessLayer.employeeModule.Branch;
import com.google.gson.reflect.TypeToken;
import exceptions.ErrorOccurredException;
import presentationLayer.gui.employeeModule.model.ObservableShift;
import presentationLayer.gui.plAbstracts.AbstractControl;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableUIElement;
import serviceLayer.employeeModule.Objects.SShift;
import serviceLayer.employeeModule.Objects.SShiftType;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import utils.Response;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

public class ShiftsControl extends AbstractControl {

    public static final Type STRING_LIST_TYPE = new TypeToken<List<String>>(){}.getType();
    private static final Type LIST_SSHIFT_ARRAY_TYPE = new TypeToken<List<SShift[]>>(){}.getType();

    private final EmployeesService employeesService;

    public ShiftsControl(EmployeesService employeesService) {
        this.employeesService = employeesService;
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

    public Object getShifts(LocalDate day) {
        String json = employeesService.getWeekShifts(UserService.HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID,day);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
            List<SShift[]> weekShifts = response.data(LIST_SSHIFT_ARRAY_TYPE);
            for(SShift[] dayShifts : weekShifts) {
                if (dayShifts != null && ((dayShifts[0] != null && dayShifts[0].getShiftDate().equals(day))
                                       || (dayShifts[1] != null && dayShifts[1].getShiftDate().equals(day))))
                    return dayShifts;
            }
            return "There are no shifts in the specified day";
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
    }

    public String deleteShift(SShift shift) {
        String json = employeesService.deleteShift(UserService.HR_MANAGER_USERNAME,Branch.HEADQUARTERS_ID,shift.getShiftDate(),shift.getShiftType());
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return null;
    }

    public String setShiftNeededAmount(LocalDate shiftDate, SShiftType shiftType, String role, int amount) {
        String json = employeesService.setShiftNeededAmount(UserService.HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, shiftDate, shiftType, role, amount);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Updated the role needed amount successfully.";
    }

    public String setShiftWorkers(LocalDate shiftDate, SShiftType shiftType, String role, List<String> workerIds) {
        String json = employeesService.setShiftEmployees(UserService.HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, shiftDate, shiftType, role, workerIds);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Updated the shift workers successfully.";
    }

    public String approveShift(LocalDate shiftDate, SShiftType shiftType) {
        String json = employeesService.approveShift(UserService.HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, shiftDate, shiftType);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Approved the shift successfully.";
    }

    public String createWeekShifts(LocalDate weekStart) {
        String json = employeesService.createWeekShifts(UserService.HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, weekStart);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Create the week shifts successfully.";
    }

    public String applyCancelCard(SShift shift, String productId) {
        String json = employeesService.applyCancelCard(UserService.HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, shift.getShiftDate(), shift.getShiftType(), productId);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Applied the cancel card successfully.";
    }

    public String reportShiftActivity(SShift shift, String activity) {
        String json = employeesService.reportShiftActivity(UserService.HR_MANAGER_USERNAME, Branch.HEADQUARTERS_ID, shift.getShiftDate(), shift.getShiftType(), activity);
        try {
            Response response = Response.fromJsonWithValidation(json); // Throws an exception if an error has occurred.
        } catch (ErrorOccurredException e) {
            return e.getMessage();
        }
        return "Applied the cancel card successfully.";
    }
}
