package dev.PresentationLayer.View;

import dev.PresentationLayer.ViewModel.EmployeeMenuVM;
import dev.Utils.DateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class EmployeeMenu implements Menu {
    private EmployeeMenuVM employeeMenuVM;
    private Scanner scanner;

    public EmployeeMenu() {
        this.employeeMenuVM = new EmployeeMenuVM();
        this.scanner = new Scanner(System.in);
        System.out.println("Welcome to the Employee Menu.");
    }

    public void printCommands() {
        System.out.println();
        System.out.println("1. `logout` - Logout command");
        System.out.println("2. `request_shift <branch_id> <shift_type: Morning/Evening> <shift_date: DD/MM/YYYY> <role>` - Request shift command");
        System.out.println("3. `week_shifts <branch_id>` - Show next week's shifts in the branch"); // Shows the approved weekly shifts, or only the weekly structure if they aren't approved yet
        System.out.println("4. `week_shifts <branch_id> <week_start: DD/MM/YYYY>` - Show the week shifts in the branch");
        System.out.println("5. `my_shifts` - Show my shifts");
        System.out.println("6. `show_details` - Show my employee details");
        System.out.println("7. `exit` - Exit command");
    }

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
        else if (command[0].equals("logout") && command.length == 1) {
            output = employeeMenuVM.logout();
            System.out.println(output);
            return new LoginMenu();
        }
        else if (command[0].equals("request_shift") && command.length == 5) {
            String branchId = command[1];
            String shiftTime = command[2];
            LocalDate shiftDate = DateUtils.parse(command[3]);
            String role = command[4];
            output = employeeMenuVM.requestShift(branchId, shiftTime, shiftDate, role);
        }
        else if (command[0].equals("week_shifts") && command.length == 2) {
            String branchId = command[1];
            output = employeeMenuVM.getNextWeekShifts(branchId);
        }
        else if (command[0].equals("week_shifts") && command.length == 3) {
            try {
                String branchId = command[1];
                LocalDate weekStart = DateUtils.parse(command[2]);
                output = employeeMenuVM.getWeekShifts(branchId, weekStart);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("my_shifts") && command.length == 1) {
            output = employeeMenuVM.getMyShifts();
        }
        else if (command[0].equals("show_details") && command.length == 1) {
            output = employeeMenuVM.getAllEmployeeDetails();
        }
        else
            output = "Invalid command was given, try again.";
        System.out.println(output);
        return this;
    }
}
