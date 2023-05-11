package presentationLayer.employeeModule.View;

import presentationLayer.employeeModule.ViewModel.EmployeeMenuVM;
import utils.DateUtils;

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
        System.out.println("2. `request_shift` - Request shift command");
        System.out.println("3. `week_shifts` - Show the week shifts in the branch"); // Shows the approved weekly shifts, or only the weekly structure if they aren't approved yet
        System.out.println("5. `my_shifts` - Show my shifts");
        System.out.println("6. `show_details` - Show my employee details");
        System.out.println("7. `cancel_shift` - Cancel a shift request");
        System.out.println("8. `report_shift_activity` - Reports a shift activity");
        System.out.println("9. `apply_cancel_card` - Apply cancel card on a product during a shift (Only available for Shift Manager)");
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
        else if (command[0].equals("request_shift") && command.length == 1) {
            // Parameters: <branch_id> <shift_type: Morning/Evening> <shift_date: DD/MM/YYYY> <role>
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            System.out.println("Please enter the shift type (Morning/Evening):");
            String shiftType = scanner.nextLine();
            System.out.println("Please enter the shift date <DD/MM/YYYY>:");
            LocalDate shiftDate = DateUtils.parse(scanner.nextLine());
            System.out.println("Please enter the requested shift role:");
            String role = scanner.nextLine();
            output = employeeMenuVM.requestShift(branchId, shiftType, shiftDate, role);
        }
        else if (command[0].equals("week_shifts") && command.length == 1) {
            // Parameters: <branch_id> <week_start: DD/MM/YYYY>
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
            String weekStartStr = scanner.nextLine();
            if (weekStartStr.isEmpty()) {
                output = employeeMenuVM.getNextWeekShifts(branchId);
            }
            else {
                try {
                    LocalDate weekStart = DateUtils.parse(command[2]);
                    output = employeeMenuVM.getWeekShifts(branchId, weekStart);
                } catch (DateTimeParseException e) {
                    output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                }
            }
        }
        else if (command[0].equals("my_shifts") && command.length == 1) {
            output = employeeMenuVM.getMyShifts();
        }
        else if (command[0].equals("show_details") && command.length == 1) {
            output = employeeMenuVM.getAllEmployeeDetails();
        }
        else if (command[0].equals("cancel_shift") && command.length == 1) {
            // Parameters: <branch_id> <shift_date: DD/MM/YY> <role> <shift_type: Morning/Evening>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the shift date <DD/MM/YYYY>:");
                LocalDate shiftDate = DateUtils.parse(scanner.nextLine());
                System.out.println("Please enter the shift type (Morning/Evening):");
                String shiftType = scanner.nextLine();
                System.out.println("Please enter the shift role:");
                String role = scanner.nextLine();
                output = employeeMenuVM.cancelShiftRequest(branchId, shiftDate, shiftType, role);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("report_shift_activity") && command.length == 1) {
            // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening> <activity>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the shift date <DD/MM/YYYY>:");
                LocalDate shiftDate = DateUtils.parse(scanner.nextLine());
                System.out.println("Please enter the shift type (Morning/Evening):");
                String shiftType = scanner.nextLine();
                System.out.println("Please enter the shift activity:");
                String activity = scanner.nextLine();
                output = employeeMenuVM.reportShiftActivity(branchId, shiftDate, shiftType, activity);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("apply_cancel_card") && command.length == 1) {
            // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening> <product_id>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the shift date <DD/MM/YYYY>:");
                LocalDate shiftDate = DateUtils.parse(scanner.nextLine());
                System.out.println("Please enter the shift type (Morning/Evening):");
                String shiftType = scanner.nextLine();
                System.out.println("Please enter the cancelled product id:");
                String productId = scanner.nextLine();
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