import dataAccessLayer.DalFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentationLayer.employeeModule.View.MenuManager;
import presentationLayer.transportModule.UiData;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("NewClassNamingConvention")
public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the Transport-Employees CLI");
        new MenuManager().run();
    }
    @Test
    public void generateData(){
        deleteData();
        ServiceFactory factory = new ServiceFactory();
        UserService userService = factory.userService();
        EmployeesService employeesService = factory.employeesService();
        userService.createData();
        employeesService.createData();
        UiData.generateAndAddData();
    }

    @Test
    public void deleteData(){
        DalFactory.clearDB("SuperLiDB.db");
    }

    @Test
    public void test_js(){
        // Create a ScriptEngineManager object
        ScriptEngineManager manager = new ScriptEngineManager();

        // Get the JavaScript engine
        ScriptEngine engine = manager.getEngineByName("javascript");

        try {
            // Execute JavaScript code
            String script = "var a = 1 + 2; a;";
            Object result = engine.eval(script);

            // Print the result
            System.out.println(result);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
