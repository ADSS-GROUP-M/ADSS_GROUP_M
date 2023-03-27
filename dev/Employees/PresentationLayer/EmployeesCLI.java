package dev.Employees.PresentationLayer;
import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import dev.Employees.BusinessLayer.EmploymentConditions;
import dev.Employees.BusinessLayer.User;
import dev.Employees.BusinessLayer.UserController;
import dev.Employees.ServiceLayer.EmployeesService;
import dev.Employees.ServiceLayer.Response;
import dev.Employees.ServiceLayer.UserService;

public class EmployeesCLI {

    private static Scanner scanner;
    private static UserService userService;
    private static EmployeesService employeesService;
    private static String username;
    private static final String DATE_PATTERN = "DD/MM/YYYY";

    private static void initialize() {
        scanner = new Scanner(System.in);
        userService = UserService.getInstance();
        username = null;
    }

    /* Valid commands list:
     * recruit_employee command: "recruit_employee (private name) (last name) (id) (bank number) (branch number) (salary) (employmentCondition1),(employmentCondition2),...,(employmentConditionN) or `none` (employmentDate: DD/MM/YYYY)"
     * login command: "login (username) (password)"
     * logout command: "logout"
     * sign up to shift command: "register_shift (empId) (branch_name) (shift_time) (shift_date: DD/MM/YYYY) (role)"
     */
    public static void main(String[] args) {
        initialize();
        boolean loggedIn = false;
        while (true) {
            String input = scanner.nextLine();
            String[] command = input.split(" ", -1);
            String output;
            if (command.length == 0)
                output = "Invalid command, command cannot be empty.";
            else if (!loggedIn) {
                if (!command[0].equals("login"))
                    output = "You must log in to the system before using it. (Usage: `login <username> <password>`)";
                else if (command.length != 3)
                    output = "Invalid login command. (Usage: `login <username> <password>`)";
                else {
                    Response<boolean> response = userService.login(command[1], command[2]);
                    if (response.errorOccurred())
                        output = response.getErrorMessage();
                    else if (response.getReturnValue() == true) {
                        output = "Login successful.";
                        username = command[1];
                    }
                    else
                        output = "Login failed.";
                }
            }
            else if (command[0] == "logout" && command.length == 1) {
                Response<boolean> response = userService.logout(username);
                if (response.errorOccurred())
                    output = response.getErrorMessage();
                else if (response.getReturnValue() == true) {
                    output = "Logged out successfully.";
                    username = null;
                }
                else
                    output = "Logout failed.";
            }
            else if (command[0] == "recruit_employee" && command.length == 9) {
                try {
                    String firstName = command[1];
                    String lastName = command[2];
                    int id = Integer.parseInt(command[3]);
                    int bankNumber = Integer.parseInt(command[4]);
                    int branchNumber = Integer.parseInt(command[5]);
                    LocalDate employmentDate = LocalDate.parse(command[7], DateTimeFormatter.ofPattern(DATE_PATTERN));
                    List<EmploymentConditions> employmentConditions = new ArrayList<>();
                    if (!command[6].toLowerCase().equals("none")) {
                        String[] conditions = command[6].split(",", -1);
                        for (String str : conditions) {
                            employmentConditions.add(EmploymentConditions.valueOf(str));
                        }
                    }

                    Response<String> response = employeesService.recruitEmployee(username, firstName, lastName, id, bankNumber, branchNumber, employmentConditions, employmentDate);
                    if (response.errorOccurred())
                        output = response.getErrorMessage();
                    else
                        output = response.getReturnValue();

                } catch (NumberFormatException e) {
                    output = "Invalid input, expected an integer value. " + e.getMessage();
                }
            }
            else if (command[0] == "register_shift" && command.length == 6) {
                try {
                    int empId = Integer.parseInt(command[1]); // TODO: Should probably be a String instead of int
                    String branch = command[2];
                    String shiftTime = command[3];
                    LocalDate shiftDate = LocalDate.parse(command[4], DateTimeFormatter.ofPattern(DATE_PATTERN));
                    String role = command[5];

                    Response<boolean> response = employeesService.registerShift(username, empId, branch, shiftTime, shiftDate, role);
                    if (response.errorOccurred())
                        output = response.getErrorMessage();
                    else {
                        boolean succeeded = response.getReturnValue();
                        if(succeeded)
                            output = "Registered to the shift successfully.";
                        else
                            output = "Could not register to the shift.";
                    }
                } catch (NumberFormatException e) {
                    output = "Invalid input, expected an integer value. " + e.getMessage(); // TODO: Remove after switching id to be String
                }
            }
            else
                output = "Invalid command was given.";
            System.out.println(output);
        }
    }
}
