package presentationLayer.employeeModule.View;
import java.util.*;

import presentationLayer.employeeModule.ViewModel.LoginMenuVM;
import presentationLayer.transportModule.TransportUI;
import serviceLayer.transportModule.ServiceFactory;

public class LoginMenu implements Menu {
    private final LoginMenuVM loginMenuVM;
    private static Scanner scanner;

    public LoginMenu() {
        this.loginMenuVM = new LoginMenuVM();
        scanner = new Scanner(System.in);
        System.out.println("Please log in to the system.");
    }

    public LoginMenu(ServiceFactory factory) {
        this.loginMenuVM = new LoginMenuVM(factory);
        scanner = new Scanner(System.in);
        System.out.println("Please log in to the system.");
    }

    public void printCommands() {
        System.out.println("1. Login command: `login <username> <password>`");
        System.out.println("2. Exit command: `exit`");
    }

    /* Valid commands list:
     * login command: "login (username) (password)"
     * exit command: "exit"
     */
    public Menu run() {
        printCommands();
        String input = scanner.nextLine();
        String[] command = input.split(" ", -1);
        String output;
        if (command.length == 0)
            output = "Invalid command, command cannot be empty.";
        else if (command[0].equals("exit") && command.length == 1) {
            output = "Exiting CLI.";
            MenuManager.terminate();
        }
        else if (command[0].equals("login")) {
            if (command.length != 3)
                output = "Invalid login command. (Usage: `login <username> <password>`)";
            else {
                output = loginMenuVM.login(command[1], command[2]);
                if (loginMenuVM.isLoggedIn()) {
                    System.out.println(output);
                    List<String> authorizations = loginMenuVM.getUserAuthorizations();
                    if (authorizations.contains("HRManager"))
                        return new HRManagerMenu();
                    else if (authorizations.contains("LogisticsManager"))
                        return new TransportUI(loginMenuVM.serviceFactory());
                    else
                        return new EmployeeMenu();
                }
            }
        }
        else
            output = "You must log in to the system before using it. (Usage: `login <username> <password>`)";
        System.out.println(output);
        return this;
    }
}
