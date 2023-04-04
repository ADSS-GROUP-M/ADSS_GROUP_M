package dev.PresentationLayer.View;

import dev.PresentationLayer.ViewModel.HRManagerMenuVM;
import dev.Utils.DateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

public class HRManagerMenu implements Menu {
    private HRManagerMenuVM hrManagerMenuVM;
    private Scanner scanner;

    public HRManagerMenu() {
        hrManagerMenuVM = new HRManagerMenuVM();
        scanner = new Scanner(System.in);
        System.out.println("Welcome to the HR Manager Menu.");
    }

    public void printCommands() {
        System.out.println();
        System.out.println("1. `logout` - Logout command");
        System.out.println("2. `recruit_employee <first_name> <last_name> <branch_id> <employee_id> <bank_number> <bank branch> <hourly_rate> <employment_date: DD/MM/YYYY>` - Recruit employee command");
        System.out.println("3. `create_week_shifts <branch_id>` - Create next week shifts");
        System.out.println("4. `create_week_shifts <branch_id> <week_start: DD/MM/YYYY>` - Create week shifts.");
        System.out.println("5. `update_shift_needed <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening> <role> <amount>` - Update shift needed amount by role");
        System.out.println("6. `update_shift_employees <branch_id>` - Update next week's shifts needed amount by role");
        System.out.println("7. `update_shift_employees <branch_id> <week_start: DD/MM/YYYY>` - Update another week's shift needed amount by role");
        System.out.println("8. `week_shifts <branch_id>` - Show next week shifts");
        System.out.println("9. `week_shifts <branch_id> <week_start: DD/MM/YYYY>` - Show weekly shifts");
        System.out.println("10. `certify <employee_id> <role>` - Certify employee to the given role");
        System.out.println("11. `approve_shift <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening>` - Approve the specified shift");
        System.out.println("11. `exit` - Exit command");
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
            output = hrManagerMenuVM.logout();
            System.out.println(output);
            return new LoginMenu();
        }
        else if (command[0].equals("recruit_employee") && command.length == 9) {
            try {
                String firstName = command[1];
                String lastName = command[2];
                String branchId = command[3];
                String employeeId = command[4];
                int bankNumber = Integer.parseInt(command[5]);
                int branchNumber = Integer.parseInt(command[6]);
                double hourlyRate = Double.parseDouble(command[7]);
                LocalDate employmentDate = DateUtils.parse(command[8]);
                System.out.println("Please enter the employment conditions:");
                String employmentConditions = scanner.nextLine();
                System.out.println("Please enter other employee details (optional):");
                String details = scanner.nextLine();
                output = hrManagerMenuVM.recruitEmployee(firstName + " " + lastName, branchId, employeeId, bankNumber + " " + branchNumber, hourlyRate, employmentDate, employmentConditions, details);
                if (output == null) {
                    System.out.println("Recruited employee successfully.");
                    System.out.println("Would you like to create a user for the new employee?");
                    System.out.println("1. To create user, enter: <password>");
                    System.out.println("2. Enter any other input to skip it for now.");
                    input = scanner.nextLine();
                    command = input.split(" ", -1);
                    if (command.length == 1) {
                        String password = command[0];
                        output = hrManagerMenuVM.createUser(employeeId,password);
                    }
                }
            } catch (NumberFormatException e) {
                output = "Invalid input, expected an integer value. " + e.getMessage();
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("create_week_shifts") && command.length == 2) {
            String branchId = command[1];
            output = hrManagerMenuVM.createNextWeekShifts(branchId);
        }
        else if (command[0].equals("create_week_shifts") && command.length == 3) {
            try {
                String branchId = command[1];
                LocalDate weekStart = DateUtils.parse(command[2]);
                output = hrManagerMenuVM.createWeekShifts(branchId, weekStart);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("update_shift_needed") && command.length == 6) {
            try {
                String branchId = command[1];
                LocalDate shiftDate = DateUtils.parse(command[2]);
                String shiftType = command[3];
                String role = command[4];
                int amount = Integer.parseInt(command[5]);
                output = hrManagerMenuVM.setShiftNeededAmount(branchId, shiftDate, shiftType, role, amount);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            } catch (NumberFormatException e) {
                output = "Invalid input, expected an integer amount of employees.";
            }
        }
        else if (command[0].equals("update_shift_employees") && (command.length == 2 || command.length == 3)) {
            String branchId = command[1];
            if (command.length == 3) {
                LocalDate weekStart = DateUtils.parse(command[2]);
                // Prints the week's shift requests
                System.out.println("The week's shift requests:");
                System.out.println(hrManagerMenuVM.getWeekShiftRequests(branchId, weekStart));
            }
            else { // Prints the next week's shift requests
                System.out.println("The next week's shift requests:");
                System.out.println(hrManagerMenuVM.getNextWeekShiftRequests(branchId));
            }
            System.out.println("Please enter which shift date (DD/MM/YYYY) to update:");
            String dateInput = scanner.nextLine();
            while(!DateUtils.validDate(dateInput)) {
                System.out.println("Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ", try again.");
                dateInput = scanner.nextLine();
            }
            LocalDate shiftDate = DateUtils.parse(dateInput);
            System.out.println("Please enter which shift type (Morning/Evening):");
            String shiftType = scanner.nextLine();
            while (!shiftType.equals("Morning") && !shiftType.equals("Evening")) {
                System.out.println("Invalid input, expected a shift type of `Morning` or `Evening`.");
                shiftType = scanner.nextLine();
            }
            System.out.println("Please enter which role (Cashier/Storekeeper/ShiftManager/GeneralWorker/Driver):");
            String role = scanner.nextLine();

            System.out.println("Please enter the employee ids for this role: `<id1> <id2> <id3> ...`");
            String idsInput = scanner.nextLine();
            String[] employeeIds = idsInput.split(" ", -1);
            output = hrManagerMenuVM.setShiftEmployees(branchId, shiftDate, shiftType, role, Arrays.stream(employeeIds).toList());
        }
        else if (command[0].equals("week_shifts") && command.length == 2) {
            String branchId = command[1];
            output = hrManagerMenuVM.getNextWeekShifts(branchId);
        }
        else if (command[0].equals("week_shifts") && command.length == 3) {
            try {
                String branchId = command[1];
                LocalDate weekStart = DateUtils.parse(command[2]);
                output = hrManagerMenuVM.getWeekShifts(branchId, weekStart);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("certify") && command.length == 3) {
            String employeeId = command[1];
            String role = command[2];
            output = hrManagerMenuVM.certifyEmployee(employeeId, role);
        }
        else if (command[0].equals("approve_shift") && command.length == 4) {
            try {
                String branchId = command[1];
                LocalDate shiftDate = DateUtils.parse(command[2]);
                String shiftType = command[3];
                output = hrManagerMenuVM.approveShift(branchId, shiftDate, shiftType);
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
