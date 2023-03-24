import java.util.*;
import dev.BuissnessLayer;

class EmployeeController{

    private static EmployeeController thisInstance;
    private List<Buissness.Employee> allEmployees;

    private EmployeeController(){

    }

    public static EmployeeController getInstance(){
        if(thisInstance == null)
            thisInstance = new EmployeeController();
        return EmployeeController();
    }




}