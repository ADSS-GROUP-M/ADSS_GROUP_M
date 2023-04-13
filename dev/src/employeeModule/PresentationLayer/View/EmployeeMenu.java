package employeeModule.PresentationLayer.View;

import employeeModule.PresentationLayer.ViewModel.EmployeeMenuVM;
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
        System.out.println("7. `cancel_shift <branch_id> <shift_type: Morning/Evening> <shift_date: DD/MM/YY> <role>` - Cancel a shift request");
        System.out.println("8. `report_shift_activity <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening> <activity>` - Reports a shift activity");
        System.out.println("9. `apply_cancel_card <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening> <product_id>` - Apply cancel card on a product during a shift (Only available for Shift Manager)");
        System.out.println("10. `exit` - Exit command");
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
            String shiftType = command[2];
            LocalDate shiftDate = DateUtils.parse(command[3]);
            String role = command[4];
            output = employeeMenuVM.requestShift(branchId, shiftType, shiftDate, role);
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
        else if (command[0].equals("cancel_shift") && command.length == 5) {
            try {
                String branchId = command[1];
                String shiftType = command[2];
                LocalDate shiftDate = DateUtils.parse(command[3]);
                String role = command[4];
                output = employeeMenuVM.cancelShiftRequest(branchId, shiftType, shiftDate, role);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("report_shift_activity") && command.length == 5) {
            try {
                String branchId = command[1];
                LocalDate shiftDate = DateUtils.parse(command[2]);
                String shiftType = command[3];
                String activity = command[4];
                output = employeeMenuVM.reportShiftActivity(branchId, shiftDate, shiftType, activity);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("apply_cancel_card") && command.length == 5) {
            try {
                String branchId = command[1];
                LocalDate shiftDate = DateUtils.parse(command[2]);
                String shiftType = command[3];
                String productId = command[4];
                output = employeeMenuVM.applyCancelCard(branchId, shiftDate, shiftType, productId);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else
            output = "Invalid command was given, try again.";
        System.out.println(output);
        return this;
    }
}
