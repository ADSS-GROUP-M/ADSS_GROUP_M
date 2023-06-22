import org.junit.jupiter.api.Test;
import presentationLayer.DataGenerator;
import presentationLayer.PresentationFactory;
import presentationLayer.cli.employeeModule.Model.BackendController;
import presentationLayer.cli.employeeModule.View.EmployeeMenu;
import presentationLayer.cli.employeeModule.View.HRManagerMenu;
import presentationLayer.cli.employeeModule.View.LoginMenu;
import presentationLayer.cli.employeeModule.View.MenuManager;
import presentationLayer.cli.transportModule.TransportCLI;
import presentationLayer.gui.employeeModule.controller.EmployeesControl;
import presentationLayer.gui.employeeModule.controller.ShiftsControl;
import presentationLayer.gui.employeeModule.controller.UsersControl;
import presentationLayer.gui.employeeModule.view.EmployeeLoginView;
import presentationLayer.gui.employeeModule.view.EmployeeView;
import presentationLayer.gui.employeeModule.view.HRManagerView;
import presentationLayer.gui.employeeModule.view.StoreManagerView;
import presentationLayer.gui.transportModule.control.*;
import presentationLayer.gui.transportModule.view.TransportView;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Services.UserService;

@SuppressWarnings("NewClassNamingConvention")
public class Main {
    public static void main(String[] args) {

        String argsMsg = "Required arguments: <GUI | CLI> <TransportManager | HRManager | StoreManager | Employee | EmployeeLogin>";

        if(args.length != 2){
            System.out.println(argsMsg);
            return;
        }

        String guiOrCli = args[0].toLowerCase();
        if(!guiOrCli.equals("gui") && !guiOrCli.equals("cli")){
            System.out.println(argsMsg);
            return;
        }

        String module = args[1].toLowerCase();
        if(!module.equals("transportmanager") && !module.equals("hrmanager") && !module.equals("storemanager") && !module.equals("employee") && !module.equals("employeelogin")){
            System.out.println(argsMsg);
            return;
        }

        boolean gui = guiOrCli.equals("gui");

        switch(module){
            case "transportmanager" -> transportModule(gui);
            case "hrmanager" -> hrManager(gui);
            case "storemanager" -> storeManager(gui);
            case "employee" -> employee(gui);
            case "employeelogin" -> employeeLogin(gui);
        }

    }

    public static void employee(boolean gui) {
        if(gui){
            ServiceFactory serviceFactory = new ServiceFactory();
            PresentationFactory factory = new PresentationFactory();
            EmployeesControl employeesControl = factory.employeesControl();
            ShiftsControl shiftsControl = new ShiftsControl(serviceFactory.employeesService());
            new EmployeeView(employeesControl, shiftsControl, "111");
        } else {
            BackendController backendController = new BackendController();
            backendController.login("111", "1234");
            EmployeeMenu employeeMenu = new EmployeeMenu(backendController);
            MenuManager menuManager = new MenuManager(employeeMenu);
            menuManager.run();
        }
    }

    private static void employeeLogin(boolean gui) {
        if (gui) {
            new EmployeeLoginView();
        } else {
            LoginMenu loginMenu = new LoginMenu();
            MenuManager menuManager = new MenuManager(loginMenu);
            menuManager.run();
        }
    }

    private static void storeManager(boolean gui) {
        if (gui) {
            new StoreManagerView();
        } else {
            System.out.println("Store manager view is not supported in the CLI, please choose the GUI parameter, or choose your needed CLI window.");
        }
    }

    public static void transportModule(boolean gui){

        if(gui){

            PresentationFactory factory = new PresentationFactory();

            TransportsControl transportsControl = factory.getTransportsControl();
            ItemListsControl itemListsControl = factory.getItemListsControl();
            DriversControl driversControl = factory.getDriversControl();
            TrucksControl trucksControl = factory.getTrucksControl();
            SitesControl sitesControl = factory.getSitesControl();
            new TransportView(transportsControl, itemListsControl, driversControl, trucksControl, sitesControl);
        } else {

            ServiceFactory serviceFactory = new ServiceFactory();
            serviceFactory.userService().login(UserService.TRANSPORT_MANAGER_USERNAME, "123");
            TransportCLI transportCLI = new TransportCLI(serviceFactory);
            MenuManager menuManager = new MenuManager(transportCLI);
            menuManager.run();
        }


    }

    public static void hrManager(boolean gui){
        if(gui){
             ServiceFactory serviceFactory = new ServiceFactory();
             PresentationFactory factory = new PresentationFactory();
             EmployeesControl employeesControl = factory.employeesControl();
             ShiftsControl shiftsControl = new ShiftsControl(serviceFactory.employeesService());
             UsersControl usersControl = new UsersControl(serviceFactory.userService());
            new HRManagerView(employeesControl, shiftsControl, usersControl);
        } else {
            BackendController backendController = new BackendController();
            backendController.login(UserService.HR_MANAGER_USERNAME, "123");
            HRManagerMenu hrManagerView = new HRManagerMenu(backendController);
            MenuManager menuManager = new MenuManager(hrManagerView);
            menuManager.run();
        }
    }



//    public static void main(String[] args) {
//        System.out.println("Welcome to the Transport-Employees CLI");
//        new MenuManager().run();
//    }
//
    @Test
    public void generate(){
        new DataGenerator().generateData();
    }
}
