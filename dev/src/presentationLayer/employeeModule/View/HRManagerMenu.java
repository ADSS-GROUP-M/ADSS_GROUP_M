package presentationLayer.employeeModule.View;

import presentationLayer.employeeModule.ViewModel.HRManagerMenuVM;
import utils.employeeUtils.DateUtils;

import java.time.LocalDate;
import java.time.LocalTime;
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
        System.out.println("11. `uncertify <employee_id> <role>` - Removes the employee's certification of the given role");
        System.out.println("12. `approve_shift <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening>` - Approve the specified shift");
        System.out.println("13. `add_employee_to_branch <employee_id> <branch_id>` - Adds an existing employee to a branch");
        System.out.println("14. `delete_shift <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening>` Deletes the specified shift");
        System.out.println("15. `create_branch <branch_id>` - Creates a new branch with the given branch id");
        System.out.println("16. `update_branch_hours <branch_id> <morning_start> <morning_end> <evening_start> <evening_end>` - Updates the branch's working hours");
        System.out.println("17. `update_employee <employee_id>` - Updates an existing employee's details");
        System.out.println("18. `authorize <username> <authorization>` - Authorizes an existing user to the given authorization");
        System.out.println("19. `exit` - Exit command");
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
                output = hrManagerMenuVM.recruitEmployee(branchId, firstName + " " + lastName, employeeId, bankNumber + " " + branchNumber, hourlyRate, employmentDate, employmentConditions, details);
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
            if (role.equals("Driver")) {
                System.out.println("Please enter the driver's license: (A1,A2,B1,B2,B3,C1,C2,C3)");
                String driverLicense = scanner.nextLine();
                output = hrManagerMenuVM.certifyDriver(employeeId, driverLicense);
            }
            else
                output = hrManagerMenuVM.certifyEmployee(employeeId, role);
        }
        else if (command[0].equals("uncertify") && command.length == 3) {
            String employeeId = command[1];
            String role = command[2];
            output = hrManagerMenuVM.uncertifyEmployee(employeeId, role);
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
        else if (command[0].equals("add_employee_to_branch") && command.length == 3) {
            String employeeId = command[1];
            String branchId = command[2];
            output = hrManagerMenuVM.addEmployeeToBranch(employeeId, branchId);
        }
        else if (command[0].equals("delete_shift") && command.length == 4) {
            try {
                String branchId = command[1];
                LocalDate shiftDate = DateUtils.parse(command[2]);
                String shiftType = command[3];
                output = hrManagerMenuVM.deleteShift(branchId, shiftDate, shiftType);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("create_branch") && command.length == 2) {
            String branchId = command[1];
            output = hrManagerMenuVM.createBranch(branchId);
        }
        else if (command[0].equals("update_branch_hours") && command.length == 6) {
            try {
                String branchId = command[1];
                LocalTime morningStart = LocalTime.parse(command[2]);
                LocalTime morningEnd = LocalTime.parse(command[3]);
                LocalTime eveningStart = LocalTime.parse(command[4]);
                LocalTime eveningEnd = LocalTime.parse(command[5]);
                output = hrManagerMenuVM.updateBranchWorkingHours(branchId, morningStart, morningEnd, eveningStart, eveningEnd);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a time in the form HH:MM.";
            }
        }
        else if (command[0].equals("update_employee") && command.length == 2) {
            String employeeId = command[1];
            System.out.println("Please choose which detail to update:");
            System.out.println("1. Salary");
            System.out.println("2. Bank Details");
            System.out.println("3. Employment Conditions");
            System.out.println("4. Optional Details");
            String detailsInput = scanner.nextLine();
            switch (detailsInput) {
                case "1": // Salary
                    String hourlyRateString = "", salaryBonusString = "";
                    try {
                        System.out.println("Please enter the updated employee's hourly rate:");
                        hourlyRateString = scanner.nextLine();
                        System.out.println("Please enter the updated employee's salary bonus:");
                        salaryBonusString = scanner.nextLine();
                        double hourlySalaryRate = Double.parseDouble(hourlyRateString);
                        double salaryBonus = Double.parseDouble(salaryBonusString);
                        output = hrManagerMenuVM.updateEmployeeSalary(employeeId, hourlySalaryRate, salaryBonus);
                    } catch (NumberFormatException e) {
                        output = "Invalid input, expected only decimal numbers, but received: " + hourlyRateString + " " + salaryBonusString +  " try again.";
                    }
                    break;
                case "2": // Bank Details
                    System.out.println("Please enter the updated employee's bank details:");
                    String bankDetails = scanner.nextLine();
                    output = hrManagerMenuVM.updateEmployeeBankDetails(employeeId, bankDetails);
                    break;
                case "3": // Employment Conditions
                    System.out.println("Please enter the updated employee's employment conditions:");
                    String employmentConditions = scanner.nextLine();
                    output = hrManagerMenuVM.updateEmployeeEmploymentConditions(employeeId, employmentConditions);
                    break;
                case "4": // Optional Details
                    System.out.println("Please enter the updated employee's optional details:");
                    String details = scanner.nextLine();
                    output = hrManagerMenuVM.updateEmployeeDetails(employeeId, details);
                    break;
                default:
                    output = "Invalid input, expected a number between 1 and 4, try again.";
            }
        }
        else if (command[0].equals("authorize") && command.length == 3) {
            String username = command[1];
            String authorization = command[2];
            output = hrManagerMenuVM.authorizeUser(username, authorization);
        }
        else
            output = "Invalid command was given, try again.";
        System.out.println(output);
        return this;
    }
}
