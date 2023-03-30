package dev.PresentationLayer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import dev.BusinessLayer.Employees.EmploymentConditions;
import dev.ServiceLayer.EmployeesService;
import dev.ServiceLayer.Response;
import dev.ServiceLayer.UserService;

public class EmployeesCLI {
    private static Scanner scanner;
    private static UserService userService;
    private static EmployeesService employeesService;
    private static String loggedUsername;
    private static final String DATE_PATTERN = "d/M/yyyy";

    private static void initialize() {
        scanner = new Scanner(System.in);
        userService = UserService.getInstance();
        employeesService = EmployeesService.getInstance();
        loggedUsername = null;

        // Data Initialization
        userService.createManagerUser("admin123", "123");
    }

    /* Valid commands list:
     * login command: "login (username) (password)"
     * logout command: "logout"
     * recruit_employee command: "recruit_employee (private name) (last name) (id) (bank number) (branch number) (salary) (employmentCondition1),(employmentCondition2),...,(employmentConditionN) or `none` (employmentDate: DD/MM/YYYY)"
     * sign up to shift command: "register_shift (empId) (branch_name) (shift_time) (shift_date: DD/MM/YYYY) (role)"
     * exit command: "exit"
     */
    public static void main(String[] args) {
        initialize();
        boolean finish = false;
        System.out.println("Welcome to the Employees CLI, please log in to the system. (Usage: `login <username> <password>`)");
        while (!finish) {
            String input = scanner.nextLine();
            String[] command = input.split(" ", -1);
            String output;
            if (command.length == 0)
                output = "Invalid command, command cannot be empty.";
            else if (command[0].equals("exit") && command.length == 1) {
                output = "Exiting CLI.";
                finish = true;
            }
            else if (loggedUsername == null) {
                if (!command[0].equals("login"))
                    output = "You must log in to the system before using it. (Usage: `login <username> <password>`)";
                else if (command.length != 3)
                    output = "Invalid login command. (Usage: `login <username> <password>`)";
                else {
                    Response<Boolean> response = userService.login(command[1], command[2]);
                    if (response.errorOccurred())
                        output = response.getErrorMessage();
                    else if (response.getReturnValue() == true) {
                        output = "Login successful.";
                        loggedUsername = command[1];
                    }
                    else
                        output = "Login failed.";
                }
            }
            else if (command[0].equals("login"))
                output = "You are already logged in to the system.";
            else if (command[0].equals("logout") && command.length == 1) {
                Response<Boolean> response = userService.logout(loggedUsername);
                if (response.errorOccurred())
                    output = response.getErrorMessage();
                else if (response.getReturnValue() == true) {
                    output = "Logged out successfully.";
                    loggedUsername = null;
                }
                else
                    output = "Logout failed.";
            }
            else if (command[0].equals("recruit_employee") && command.length == 9) {
                try {
                    String firstName = command[1];
                    String lastName = command[2];
                    String id = command[3];
                    int bankNumber = Integer.parseInt(command[4]);
                    int branchNumber = Integer.parseInt(command[5]);
                    LocalDate employmentDate = LocalDate.parse(command[7], DateTimeFormatter.ofPattern(DATE_PATTERN));
                    List<String> employmentConditions = new ArrayList<>();
                    if (!command[6].toLowerCase().equals("none")) {
                        String[] conditions = command[6].split(",", -1);
                        for (String str : conditions) {
                            employmentConditions.add(str);
                        }
                    }

                    Response<String> response = employeesService.recruitEmployee(loggedUsername, firstName, lastName, id, bankNumber, branchNumber, employmentConditions, employmentDate);
                    if (response.errorOccurred())
                        output = response.getErrorMessage();
                    else
                        output = response.getReturnValue();

                } catch (NumberFormatException e) {
                    output = "Invalid input, expected an integer value. " + e.getMessage();
                } catch (DateTimeParseException e) {
                    output = "Invalid input, expected a date in the form " + DATE_PATTERN + ".";
                }
            }
            else if (command[0].equals("register_shift") && command.length == 6) {
                String empId = command[1];
                String branch = command[2];
                String shiftTime = command[3];
                LocalDate shiftDate = LocalDate.parse(command[4], DateTimeFormatter.ofPattern(DATE_PATTERN));
                String role = command[5];

                Response<Boolean> response = employeesService.registerShift(loggedUsername, empId, branch, shiftTime, shiftDate, role);
                if (response.errorOccurred())
                    output = response.getErrorMessage();
                else {
                    boolean succeeded = response.getReturnValue();
                    if(succeeded)
                        output = "Registered to the shift successfully.";
                    else
                        output = "Could not register to the shift.";
                }
            }
            else
                output = "Invalid command was given.";
            System.out.println(output);
        }
    }
}
