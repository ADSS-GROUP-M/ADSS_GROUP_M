package presentationLayer.employeeModule.View;

import presentationLayer.employeeModule.Model.BackendController;
import presentationLayer.employeeModule.ViewModel.HRManagerMenuVM;
import utils.DateUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;

public class HRManagerMenu implements Menu {
    private HRManagerMenuVM hrManagerMenuVM;
    private Scanner scanner;
    private Menu nextMenu;
    private JFrame frame = new JFrame("HRManagerMenu");
    private JPanel buttonPanel = new JPanel();
    private JPanel fieldsPanel = new JPanel();
    private JPanel rolesPanel = new JPanel();
    private JPanel shiftTypesPanel = new JPanel();
    private JPanel authorizationPanel = new JPanel();
    private JPanel notificationsPanel = new JPanel();
    private JButton recruit_employee = new JButton("Recruit Employee");
    private JButton create_week_shifts = new JButton("Create Week Shifts");
    private JButton update_shift_needed = new JButton("Update Shift Needed");
    private JButton update_shift_employees = new JButton("Update Shift Employees");
    private JButton week_shifts = new JButton("Week Shifts");
    private JButton certify = new JButton("Certify");
    private JButton uncertify = new JButton("Uncertify");
    private JButton approve_shift = new JButton("Approve Shift");
    private JButton add_employee_to_branch = new JButton("Add Employee To Branch");
    private JButton delete_shift = new JButton("Delete Shift");
    private JButton update_branch_hours = new JButton("Update Branch Hours");
    private JButton update_employee = new JButton("Update Employee");
    private JButton authorize = new JButton("Authorize");
    private JButton exit = new JButton("Exit");
    private JTextField usernameField = new JTextField("-enter username-");
    private DateFormat txtDate = new SimpleDateFormat("dd/mm/yyyy");
    private JFormattedTextField dateField = new JFormattedTextField(txtDate);
    private ButtonGroup authorizationField = new ButtonGroup();
    private JRadioButton hrManagerA = new JRadioButton("HRManager");
    private JRadioButton storekeeperA = new JRadioButton("Storekeeper");
    private JRadioButton cashierA = new JRadioButton("Cashier");
    private JRadioButton shiftManagerA = new JRadioButton("ShiftManager");
    private JRadioButton transportManagerA = new JRadioButton("TransportManager");
    private ButtonGroup roleField = new ButtonGroup();
    private JRadioButton cashierR = new JRadioButton("Cashier");
    private JRadioButton storekeeperR = new JRadioButton("Storekeeper");
    private JRadioButton shiftManagerR = new JRadioButton("ShiftManager");
    private JRadioButton generalWorkerR = new JRadioButton("GeneralWorker");
    private JRadioButton securityGuardR = new JRadioButton("SecurityGuard");
    private JRadioButton stewardR = new JRadioButton("Steward");
    private JRadioButton cleanerR = new JRadioButton("Cleaner");
    private JRadioButton driverR = new JRadioButton("Driver");
    private JTextField freeTextField = new JTextField("-additional text-");
    private ButtonGroup shiftTypeField = new ButtonGroup();
    private JRadioButton morningShift = new JRadioButton("Morning");
    private JRadioButton eveningShift = new JRadioButton("Evening");
    private JTextField amountField = new JTextField("-amount-");
    private JTextField branchIdField = new JTextField("-branch id-");
    private JTextField nameField = new JTextField("-full name-");
    private JTextField bankDetailsField = new JTextField("-bank details-");
    private JTextField hourlyRateField = new JTextField("-hourly rate-");
    private JTextField employmentConditionsField = new JTextField("-employment conditions-");
    private JLabel notice = new JLabel("welcome to HR manager menu");
    private ActionListener insert = new InsertAction();
    private ActionListener command = new CommandAction();
    private String lastCommand = "";
    private boolean isCreatingNewUser = false;
    private boolean isUpdatingUser = false;


    public HRManagerMenu() {
        hrManagerMenuVM = new HRManagerMenuVM();
        scanner = new Scanner(System.in);
        System.out.println("Welcome to the HR Manager Menu.");
        initiateGUI();
    }
    public HRManagerMenu(BackendController backendController) {
        hrManagerMenuVM = new HRManagerMenuVM(backendController);
        scanner = new Scanner(System.in);
        System.out.println("Welcome to the HR Manager Menu.");
        initiateGUI();
    }


    public void printCommands() {
        System.out.println();
        System.out.println("1. `logout` - Logout command");
        System.out.println("2. `recruit_employee` - Recruit a new employee");
        System.out.println("3. `create_week_shifts` - Create week shifts");
        System.out.println("4. `update_shift_needed` - Update shift needed amount by role");
        System.out.println("5. `update_shift_employees` - Update the week's shifts needed amount by role");
        System.out.println("6. `week_shifts` - Show next week shifts");
        System.out.println("7. `certify` - Certify employee to the given role");
        System.out.println("8. `uncertify` - Removes the employee's certification of the given role");
        System.out.println("9. `approve_shift` - Approve the specified shift");
        System.out.println("10. `add_employee_to_branch` - Adds an existing employee to a branch");
        System.out.println("11. `delete_shift` - Deletes the specified shift");
        //System.out.println("12. `create_branch` - Creates a new branch with the given branch id"); // This operation has moved to Transport Module when creating a new site
        System.out.println("12. `update_branch_hours` - Updates the branch's working hours");
        System.out.println("13. `update_employee` - Updates an existing employee's details");
        System.out.println("14. `authorize` - Authorizes an existing user to the given authorization");
        System.out.println("15. `exit` - Exit command");
    }

    private void initiateGUI() {
        nextMenu = this;
        frame = new JFrame("HR Manager Menu");
        frame.setSize(1300, 900);
        BoxLayout boxLayoutButton = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        BoxLayout boxLayoutFields = new BoxLayout(fieldsPanel, BoxLayout.X_AXIS);
        BoxLayout boxLayoutNotification = new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(boxLayoutButton);
        fieldsPanel.setLayout(boxLayoutFields);
        notificationsPanel.setLayout(boxLayoutNotification);
        rolesPanel.setLayout(new GridLayout(3,3));
        shiftTypesPanel.setLayout(new GridLayout(3,3));
        authorizationPanel.setLayout(new GridLayout(3,3));
        buttonPanel.setBorder(new EmptyBorder(new Insets(450, 700, 1000, 700)));
        buttonPanel.setLayout(new GridLayout(3, 5));
        fieldsPanel.setBorder(new EmptyBorder(new Insets(45, 70, 45, 100)));
        fieldsPanel.setLayout((new GridLayout(3,3)));
        notificationsPanel.setBorder(new EmptyBorder(new Insets(45, 70, 450, 700)));
        buttonPanel.setBorder(new TitledBorder(new TitledBorder("Buttons")));
        fieldsPanel.setBorder(new TitledBorder(new TitledBorder("Fields")));
        notificationsPanel.setBorder(new TitledBorder(new TitledBorder("Notifications")));
        frame.setLayout(new GridLayout(3, 2));
        recruit_employee.addActionListener(command);
        create_week_shifts.addActionListener(command);
        update_shift_needed.addActionListener(command);
        update_shift_employees.addActionListener(command);
        week_shifts.addActionListener(command);
        certify.addActionListener(command);
        uncertify.addActionListener(command);
        approve_shift.addActionListener(command);
        add_employee_to_branch.addActionListener(command);
        delete_shift.addActionListener(command);
        update_branch_hours.addActionListener(command);
        update_employee.addActionListener(command);
        authorize.addActionListener(command);
        exit.addActionListener(command);
        usernameField.setEnabled(true);
        dateField.setEnabled(true);
        dateField.setText("-insert date-\n dd-mm-yyyy");
        hrManagerA.setEnabled(true);
        storekeeperA.setEnabled(true);
        cashierA.setEnabled(true);
        shiftManagerA.setEnabled(true);
        transportManagerA.setEnabled(true);
        freeTextField.setEnabled(true);
        notice = new JLabel("Welcome to HR Manager menu!");
        frame.add(buttonPanel);
        frame.add(fieldsPanel);
        frame.add(notificationsPanel);
        authorizationField.add(hrManagerA);
        authorizationField.add(storekeeperA);
        authorizationField.add(cashierA);
        authorizationField.add(shiftManagerA);
        authorizationField.add(transportManagerA);
        roleField.add(cashierR);
        roleField.add(storekeeperR);
        roleField.add(shiftManagerR);
        roleField.add(generalWorkerR);
        roleField.add(securityGuardR);
        roleField.add(stewardR);
        roleField.add(cleanerR);
        roleField.add(driverR);
        shiftTypeField.add(morningShift);
        shiftTypeField.add(eveningShift);
        amountField.setEnabled(true);
        branchIdField.setEnabled(true);
        nameField.setEnabled(true);
        bankDetailsField.setEnabled(true);
        hourlyRateField.setEnabled(true);
        employmentConditionsField.setEnabled(true);
        notificationsPanel.add(notice);
        buttonPanel.add(recruit_employee);
        buttonPanel.add(create_week_shifts);
        buttonPanel.add(update_shift_needed);
        buttonPanel.add(update_shift_employees);
        buttonPanel.add(week_shifts);
        buttonPanel.add(certify);
        buttonPanel.add(uncertify);
        buttonPanel.add(approve_shift);
        buttonPanel.add(add_employee_to_branch);
        buttonPanel.add(delete_shift);
        buttonPanel.add(update_branch_hours);
        buttonPanel.add(update_employee);
        buttonPanel.add(authorize);
        buttonPanel.add(exit);
        shiftTypesPanel.add(morningShift);
        shiftTypesPanel.add(eveningShift);
        authorizationPanel.add(hrManagerA);
        authorizationPanel.add(storekeeperA);
        authorizationPanel.add(cashierA);
        authorizationPanel.add(shiftManagerA);
        authorizationPanel.add(transportManagerA);
        rolesPanel.add(cashierR);
        rolesPanel.add(storekeeperR);
        rolesPanel.add(shiftManagerR);
        rolesPanel.add(generalWorkerR);
        rolesPanel.add(securityGuardR);
        rolesPanel.add(stewardR);
        rolesPanel.add(cleanerR);
        rolesPanel.add(driverR);
    }

    public String getSelectedButtonName(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    private void addShiftTypeButtons(JPanel p){
        p.add(shiftTypesPanel);
    }
    private void addAuthorizationButtons(JPanel p){
        p.add(authorizationPanel);
    }
    private void addRoleButtons(JPanel p){
        p.add(rolesPanel);
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
        } else if (command[0].equals("logout") && command.length == 1) {
            output = hrManagerMenuVM.logout();
            System.out.println(output);
            return new LoginMenu();
        } else if (command[0].equals("recruit_employee") && command.length == 1) {
            // Parameters: <first_name> <last_name> <branch_id> <employee_id> <bank_number> <bank branch> <hourly_rate> <employment_date: DD/MM/YYYY>
            try {
                System.out.println("Please enter the name of the employee:");
                String employeeName = scanner.nextLine();
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the employee id:");
                String employeeId = scanner.nextLine();
                System.out.println("Please enter the employee's bank number and bank branch number: <bank_number> <bank_branch>");
                String[] bankDetails = scanner.nextLine().split(" ", -1);
                int bankNumber = Integer.parseInt(bankDetails[0]);
                int branchNumber = Integer.parseInt(bankDetails[1]);
                System.out.println("Please enter the employee's hourly salary rate:");
                String hourlyRateStr = scanner.nextLine();
                double hourlyRate = Double.parseDouble(hourlyRateStr);
                System.out.println("Please enter the employee's employment date <DD/MM/YYYY>:");
                String employmentDateStr = scanner.nextLine();
                LocalDate employmentDate = DateUtils.parse(employmentDateStr);
                System.out.println("Please enter the employment conditions:");
                String employmentConditions = scanner.nextLine();
                System.out.println("Please enter other employee details (optional):");
                String details = scanner.nextLine();
                output = hrManagerMenuVM.recruitEmployee(employeeName, branchId, employeeId, bankNumber + " " + branchNumber, hourlyRate, employmentDate, employmentConditions, details);
                if (output == null) {
                    System.out.println("Recruited employee successfully.");
                    System.out.println("Would you like to create a user for the new employee?");
                    System.out.println("1. To create user, enter: <password>");
                    System.out.println("2. Enter any other input to skip it for now.");
                    input = scanner.nextLine();
                    command = input.split(" ", -1);
                    if (command.length == 1) {
                        String password = command[0];
                        output = hrManagerMenuVM.createUser(employeeId, password);
                    }
                }
            } catch (NumberFormatException e) {
                output = "Invalid input, expected an integer value. " + e.getMessage();
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        } else if (command[0].equals("create_week_shifts") && command.length == 1) {
            // Parameters: <branch_id>  |  <branch_id> <week_start: DD/MM/YYYY>
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty to create for next week):");
            String weekStartStr = scanner.nextLine();
            if (weekStartStr.isEmpty()) {
                output = hrManagerMenuVM.createNextWeekShifts(branchId);
            } else {
                try {
                    LocalDate weekStart = DateUtils.parse(weekStartStr);
                    output = hrManagerMenuVM.createWeekShifts(branchId, weekStart);
                } catch (DateTimeParseException e) {
                    output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                }
            }
        } else if (command[0].equals("update_shift_needed") && command.length == 1) {
            // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening> <role> <amount>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the shift date <DD/MM/YYYY>:");
                String shiftDateStr = scanner.nextLine();
                LocalDate shiftDate = DateUtils.parse(shiftDateStr);
                System.out.println("Please enter the shift type (Morning/Evening):");
                String shiftType = scanner.nextLine();
                System.out.println("Please enter the shift role:");
                String role = scanner.nextLine();
                System.out.println("Please enter the needed amount of employees for this role:");
                String amountStr = scanner.nextLine();
                int amount = Integer.parseInt(amountStr);
                output = hrManagerMenuVM.setShiftNeededAmount(branchId, shiftDate, shiftType, role, amount);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            } catch (NumberFormatException e) {
                output = "Invalid input, expected an integer amount of employees.";
            }
        } else if (command[0].equals("update_shift_employees") && command.length == 1) {
            // Parameters: <branch_id>  |  <branch_id> <week_start: DD/MM/YYYY>
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
            String weekStartStr = scanner.nextLine();
            if (!weekStartStr.isEmpty()) {
                LocalDate weekStart = DateUtils.parse(weekStartStr);
                // Prints the week's shift requests
                System.out.println("The week's shift requests:");
                System.out.println(hrManagerMenuVM.getWeekShiftRequests(branchId, weekStart));
            } else { // Prints the next week's shift requests
                System.out.println("The next week's shift requests:");
                System.out.println(hrManagerMenuVM.getNextWeekShiftRequests(branchId));
            }
            System.out.println("Please enter which shift date (DD/MM/YYYY) to update:");
            String dateInput = scanner.nextLine();
            while (!DateUtils.validDate(dateInput)) {
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
        } else if (command[0].equals("week_shifts") && command.length == 1) {
            // Parameters: <branch_id>  |  <branch_id> <week_start: DD/MM/YYYY>
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
            String weekStartStr = scanner.nextLine();
            if (weekStartStr.isEmpty()) {
                output = hrManagerMenuVM.getNextWeekShifts(branchId);
            } else {
                try {
                    LocalDate weekStart = DateUtils.parse(weekStartStr);
                    output = hrManagerMenuVM.getWeekShifts(branchId, weekStart);
                } catch (DateTimeParseException e) {
                    output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                }
            }
        } else if (command[0].equals("certify") && command.length == 1) {
            // Parameters: <employee_id> <role>
            System.out.println("Please enter the employee id:");
            String employeeId = scanner.nextLine();
            System.out.println("Please enter the role to certify:");
            String role = scanner.nextLine();
            if (role.equals("Driver")) {
                System.out.println("Please enter the driver's license: (A1,A2,B1,B2,B3,C1,C2,C3)");
                String driverLicense = scanner.nextLine();
                output = hrManagerMenuVM.certifyDriver(employeeId, driverLicense);
            } else
                output = hrManagerMenuVM.certifyEmployee(employeeId, role);
        } else if (command[0].equals("uncertify") && command.length == 1) {
            // Parameters: <employee_id> <role>
            System.out.println("Please enter the employee id:");
            String employeeId = scanner.nextLine();
            System.out.println("Please enter the role to uncertify:");
            String role = scanner.nextLine();
            output = hrManagerMenuVM.uncertifyEmployee(employeeId, role);
        } else if (command[0].equals("approve_shift") && command.length == 1) {
            // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
                String shiftDateStr = scanner.nextLine();
                LocalDate shiftDate = DateUtils.parse(shiftDateStr);
                System.out.println("Please enter the shift type (Morning/Evening):");
                String shiftType = scanner.nextLine();
                output = hrManagerMenuVM.approveShift(branchId, shiftDate, shiftType);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        } else if (command[0].equals("add_employee_to_branch") && command.length == 1) {
            // Parameters: <employee_id> <branch_id>
            System.out.println("Please enter the employee id:");
            String employeeId = scanner.nextLine();
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            output = hrManagerMenuVM.addEmployeeToBranch(employeeId, branchId);
        } else if (command[0].equals("delete_shift") && command.length == 1) {
            // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
                String shiftDateStr = scanner.nextLine();
                LocalDate shiftDate = DateUtils.parse(shiftDateStr);
                String shiftType = scanner.nextLine();
                output = hrManagerMenuVM.deleteShift(branchId, shiftDate, shiftType);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        } else if (command[0].equals("update_branch_hours") && command.length == 1) {
            // Parameters: <branch_id> <morning_start> <morning_end> <evening_start> <evening_end>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the morning start time (HH:MM):");
                LocalTime morningStart = LocalTime.parse(scanner.nextLine());
                System.out.println("Please enter the morning end time (HH:MM):");
                LocalTime morningEnd = LocalTime.parse(scanner.nextLine());
                System.out.println("Please enter the evening start time (HH:MM):");
                LocalTime eveningStart = LocalTime.parse(scanner.nextLine());
                System.out.println("Please enter the evening end time (HH:MM):");
                LocalTime eveningEnd = LocalTime.parse(scanner.nextLine());
                output = hrManagerMenuVM.updateBranchWorkingHours(branchId, morningStart, morningEnd, eveningStart, eveningEnd);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a time in the form HH:MM.";
            }
        } else if (command[0].equals("update_employee") && command.length == 1) {
            // Parameters: <employee_id>
            System.out.println("Please enter the employee id:");
            String employeeId = scanner.nextLine();
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
                        output = "Invalid input, expected only decimal numbers, but received: " + hourlyRateString + " " + salaryBonusString + " try again.";
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
        } else if (command[0].equals("authorize") && command.length == 1) {
            // Parameters: <username> <authorization>
            System.out.println("Please enter the username to authorize:");
            String username = scanner.nextLine();
            System.out.println("Please enter the authorization to authorize:");
            String authorization = scanner.nextLine();
            output = hrManagerMenuVM.authorizeUser(username, authorization);
        } else
            output = "Invalid command was given, try again.";
        System.out.println(output);
        return this;
    }

    @Override
    public Menu runGUI() {
        frame.setVisible(true);
        try{
            Thread.sleep(3);
        } catch(Exception e){}
        if (nextMenu != this) {
            frame.setVisible(false);
            terminate();
        }
        return nextMenu;
    }

    class InsertAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           // notice.setText(e.getActionCommand());
        }
    }

    class CommandAction implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            String command = ev.getActionCommand();
            String output = "";

            if (command.equals("Exit")) {
                output = "Exiting CLI.";
                nextMenu = null;
                MenuManager.terminate();
            }  else if (command.equals("Recruit Employee")) {
                // Parameters: <first_name> <last_name> <branch_id> <employee_id> <bank_number> <bank branch> <hourly_rate> <employment_date: DD/MM/YYYY>
                try {
                    if (!lastCommand.equals(command)) {
                        isCreatingNewUser = false;
                        fieldsPanel.removeAll();
                        nameField.setText("Please enter the name of the employee:");
                        branchIdField.setText("Please enter the branch id (branch address):");
                        usernameField.setText("Please enter the employee id:");
                        bankDetailsField.setText("Please enter the employee's bank number and bank branch number: <bank_number> <bank_branch>");
                        hourlyRateField.setText("Please enter the employee's hourly salary rate:");
                        dateField.setText("Please enter the employee's employment date <DD/MM/YYYY>:");
                        employmentConditionsField.setText("Please enter the employment conditions:");
                        freeTextField.setText("Please enter other employee details (optional):");

                        fieldsPanel.add(nameField);
                        fieldsPanel.add(branchIdField);
                        fieldsPanel.add(usernameField);
                        fieldsPanel.add(bankDetailsField);
                        fieldsPanel.add(hourlyRateField);
                        fieldsPanel.add(dateField);
                        fieldsPanel.add(employmentConditionsField);
                        fieldsPanel.add(freeTextField);

                    } else if (lastCommand.equals(command)) {

                        String employeeName = nameField.getText();
                        String branchId = branchIdField.getText();
                        String employeeId = usernameField.getText();
                        String[] bankDetails = bankDetailsField.getText().split(" ", -1);
                        int bankNumber = Integer.parseInt(bankDetails[0]);
                        int branchNumber = Integer.parseInt(bankDetails[1]);
                        String hourlyRateStr = hourlyRateField.getText();
                        double hourlyRate = Double.parseDouble(hourlyRateStr);
                        String employmentDateStr = dateField.getText();
                        LocalDate employmentDate = DateUtils.parse(employmentDateStr);
                        String employmentConditions = employmentConditionsField.getText();
                        String details = freeTextField.getText();
                        if (!isCreatingNewUser) {
                            output = hrManagerMenuVM.recruitEmployee(employeeName, branchId, employeeId, bankNumber + " " + branchNumber, hourlyRate, employmentDate, employmentConditions, details);
                            if (output == null) {
                                fieldsPanel.removeAll();
                                freeTextField.setText("To create user, enter: <password> and click Recruit Employee");
                                fieldsPanel.add(freeTextField);
                                notice.setText("Recruited employee successfully.\n Would you like to create a user for the new employee?");
                                isCreatingNewUser = true;
                            }
                        } else if (isCreatingNewUser) {
                            String input = "";
                            input = freeTextField.getText();
                            String[] command2 = input.split(" ", -1);
                            String password = command2[0];
                            output = hrManagerMenuVM.createUser(employeeId, password);
                            isCreatingNewUser = false;

                        }
                    }
                } catch (NumberFormatException ex) {
                    output = "Invalid input, expected an integer value. " + ex.getMessage();
                } catch (DateTimeParseException ex) {
                    output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                }

            }
             else if (command.equals("Create Week Shifts")) {
                // Parameters: <branch_id>  |  <branch_id> <week_start: DD/MM/YYYY>
                if(!lastCommand.equals(command)) {
                    fieldsPanel.removeAll();
                    branchIdField.setText("Please enter the branch id (branch address):");
                    dateField.setText("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty to create for next week):");
                    fieldsPanel.add(branchIdField);
                    fieldsPanel.add(dateField);
                } else {
                    String branchId = branchIdField.getText();
                    String weekStartStr = dateField.getText();
                    if (weekStartStr.isEmpty()) {
                        output = hrManagerMenuVM.createNextWeekShifts(branchId);
                    } else {
                        try {
                            LocalDate weekStart = DateUtils.parse(weekStartStr);
                            output = hrManagerMenuVM.createWeekShifts(branchId, weekStart);
                        } catch (DateTimeParseException e) {
                            output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                        }
                    }
                }
            } else if (command.equals("Update Shift Needed")) {
                // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening> <role> <amount>
                try {
                    if (!lastCommand.equals(command)) {
                        fieldsPanel.removeAll();
                        branchIdField.setText("Please enter the branch id (branch address):");
                        dateField.setText("Please enter the shift date <DD/MM/YYYY>:");
                        amountField.setText("Please enter the needed amount of employees for this role:");
                        fieldsPanel.add(branchIdField);
                        fieldsPanel.add(dateField);
                        fieldsPanel.add(amountField);
                        addShiftTypeButtons(fieldsPanel);
                        addRoleButtons(fieldsPanel);
                    } else {
                        String shiftDateStr = dateField.getText();
                        LocalDate shiftDate = DateUtils.parse(shiftDateStr);
                        String branchId = branchIdField.getText();
                        String amountStr = amountField.getText();
                        int amount = Integer.parseInt(amountStr);
                        String shiftType = getSelectedButtonName(shiftTypeField);
                        String role = getSelectedButtonName(roleField);
                        output = hrManagerMenuVM.setShiftNeededAmount(branchId, shiftDate, shiftType, role, amount);
                    }
                    } catch(DateTimeParseException e){
                        output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                    } catch(NumberFormatException e){
                        output = "Invalid input, expected an integer amount of employees.";
                    }

            } else if (command.equals("Update Shift Employees")) {
                // Parameters: <branch_id>  |  <branch_id> <week_start: DD/MM/YYYY>
                if (!lastCommand.equals(command)) {
                    fieldsPanel.removeAll();
                    branchIdField.setText("Please enter the branch id (branch address):");
                    //dateField.setText("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
                    dateField.setText("Please enter which shift date (DD/MM/YYYY) to update:");
                    usernameField.setText("Please enter the employee ids for this role: `<id1> <id2> <id3> ...`");

                    fieldsPanel.add(branchIdField);
                    fieldsPanel.add(dateField);
                    addRoleButtons(fieldsPanel);
                    fieldsPanel.add(usernameField);
                    addShiftTypeButtons(fieldsPanel);
                } else {
                String branchId = branchIdField.getText();
                String dateInput = dateField.getText();
                String shiftType = getSelectedButtonName(shiftTypeField);
                String role = getSelectedButtonName(roleField);
                String idsInput = usernameField.getText();
                String[] employeeIds = idsInput.split(" ", -1);
                if (!DateUtils.validDate(dateInput)) {
                    output = ("Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ", try again.");
                } else if (!shiftType.equals("Morning") && !shiftType.equals("Evening")) {
                    output = "Invalid input, expected a shift type of `Morning` or `Evening`.";
                } else {
                    LocalDate shiftDate = DateUtils.parse(dateInput);
                    //System.out.println("Please enter which shift type (Morning/Evening):");

                    //("Please enter which role (Cashier/Storekeeper/ShiftManager/GeneralWorker/Driver):");
                    output = hrManagerMenuVM.setShiftEmployees(branchId, shiftDate, shiftType, role, Arrays.stream(employeeIds).toList());
                }
            }
            } else if (command.equals("Week Shifts")) {
                // Parameters: <branch_id>  |  <branch_id> <week_start: DD/MM/YYYY>
                if(!lastCommand.equals(command)) {
                    fieldsPanel.removeAll();
                    branchIdField.setText("Please enter the branch id (branch address):");
                    dateField.setText("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
                    fieldsPanel.add(branchIdField);
                    fieldsPanel.add(dateField);
                } else if( lastCommand.equals(command)) {
                    String branchId = branchIdField.getText();
                    String weekStartStr = dateField.getText();
                    if (weekStartStr.isEmpty()) {
                        output = hrManagerMenuVM.getNextWeekShifts(branchId);
                    } else {
                        try {
                            LocalDate weekStart = DateUtils.parse(weekStartStr);
                            output = hrManagerMenuVM.getWeekShifts(branchId, weekStart);
                        } catch (DateTimeParseException e) {
                            output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                        }
                    }
                }
            } else if (command.equals("Certify")) {
                // Parameters: <employee_id> <role>
                if(!lastCommand.equals(command)){
                fieldsPanel.removeAll();
                usernameField.setText("Please enter the employee id:");
                freeTextField.setText("If a Driver is being certified, Please enter the driver's license: (A1,A2,B1,B2,B3,C1,C2,C3)");
                //System.out.println("Please enter the role to certify:");
                addRoleButtons(fieldsPanel);
                fieldsPanel.add(usernameField);
                fieldsPanel.add(freeTextField);
                } else if(lastCommand.equals(command)) {
                    String role = getSelectedButtonName(roleField);
                    String driverLicense = freeTextField.getText();
                    String employeeId = usernameField.getText();
                    if (role.equals("Driver")) {
                        output = hrManagerMenuVM.certifyDriver(employeeId, driverLicense);
                    } else
                        output = hrManagerMenuVM.certifyEmployee(employeeId, role);
                }
            } else if (command.equals("Uncertify")) {
                // Parameters: <employee_id> <role>
                if(!lastCommand.equals(command)) {
                    fieldsPanel.removeAll();
                    usernameField.setText("Please enter the employee id:");
                    addRoleButtons(fieldsPanel);
                   fieldsPanel.add(usernameField);
                } else if(lastCommand.equals(command)) {
                    String role = getSelectedButtonName(roleField);
                    String employeeId = usernameField.getText();
                    output = hrManagerMenuVM.uncertifyEmployee(employeeId, role);
                }
            } else if (command.equals("Approve Shift")) {
                // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening>
                    if (!lastCommand.equals(command)) {
                        fieldsPanel.removeAll();
                        branchIdField.setText("Please enter the branch id (branch address):");
                        dateField.setText("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
                        addShiftTypeButtons(fieldsPanel);
                        fieldsPanel.add(branchIdField);
                        fieldsPanel.add(dateField);
                    } else if (lastCommand.equals(command)) {
                        try {
                        String branchId = branchIdField.getText();
                        String shiftDateStr = dateField.getText();
                        LocalDate shiftDate = DateUtils.parse(shiftDateStr);
                        //System.out.println("Please enter the shift type (Morning/Evening):");
                        String shiftType = getSelectedButtonName(shiftTypeField);
                        output = hrManagerMenuVM.approveShift(branchId, shiftDate, shiftType);
                    } catch(DateTimeParseException ex){
                        output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                    }
                }
            } else if (command.equals("Add Employee To Branch")) {
                // Parameters: <employee_id> <branch_id>
                if(!lastCommand.equals(command)) {
                    fieldsPanel.removeAll();
                    usernameField.setText("Please enter the employee id:");
                    branchIdField.setText("Please enter the branch id (branch address):");
                    fieldsPanel.add(usernameField);
                    fieldsPanel.add(branchIdField);
                } else if (lastCommand.equals(command)) {
                    String employeeId = usernameField.getText();
                    String branchId = branchIdField.getText();
                    output = hrManagerMenuVM.addEmployeeToBranch(employeeId, branchId);
                }
            } else if (command.equals("Delete Shift")) {
                // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening>
                    if (!lastCommand.equals(command)) {
                        fieldsPanel.removeAll();
                        branchIdField.setText("Please enter the branch id (branch address):");
                        dateField.setText("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
                        fieldsPanel.add(branchIdField);
                        fieldsPanel.add(dateField);
                        addShiftTypeButtons(fieldsPanel);
                    } else if (lastCommand.equals(command)) {
                        try {
                        String branchId = branchIdField.getText();
                        String shiftDateStr = dateField.getText();
                        LocalDate shiftDate = DateUtils.parse(shiftDateStr);
                        String shiftType = getSelectedButtonName(shiftTypeField);
                        output = hrManagerMenuVM.deleteShift(branchId, shiftDate, shiftType);
                    } catch(DateTimeParseException e){
                        output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                    }
                }
            } else if (command.equals("Update Branch Hours")) {
                // Parameters: <branch_id> <morning_start> <morning_end> <evening_start> <evening_end>
                    if (!lastCommand.equals(command)) {
                        fieldsPanel.removeAll();
                        branchIdField.setText("Please enter the branch id (branch address):");
                        freeTextField.setText("Please enter the morning start time (HH:MM):");
                        employmentConditionsField.setText("Please enter the morning end time (HH:MM):");
                        nameField.setText("Please enter the evening start time (HH:MM):");
                        bankDetailsField.setText("Please enter the evening end time (HH:MM):");
                        fieldsPanel.add(branchIdField);
                        fieldsPanel.add(freeTextField);
                        fieldsPanel.add(employmentConditionsField);
                        fieldsPanel.add(nameField);
                        fieldsPanel.add(bankDetailsField);
                    } else if (lastCommand.equals(command)) {
                        try {
                        String branchId = branchIdField.getText();
                        LocalTime morningStart = LocalTime.parse(freeTextField.getText());
                        LocalTime morningEnd = LocalTime.parse(employmentConditionsField.getText());
                        LocalTime eveningStart = LocalTime.parse(nameField.getText());
                        LocalTime eveningEnd = LocalTime.parse(bankDetailsField.getText());
                        output = hrManagerMenuVM.updateBranchWorkingHours(branchId, morningStart, morningEnd, eveningStart, eveningEnd);
                    } catch(DateTimeParseException e){
                        output = "Invalid input, expected a time in the form HH:MM.";
                    }
                }
            } else if (command.equals("Update Employee")) {
                // Parameters: <employee_id>
                if(!lastCommand.equals(command)) {
                    fieldsPanel.removeAll();
                    isUpdatingUser = false;
                    usernameField.setText("Please enter the employee id:");
                    freeTextField.setText("Please choose which detail to update:\n " +
                            "1. Salary \n " +
                            "2. Bank Details \n " +
                            "3. Employment Conditions \n" +
                            "4. Optional Details");
                    fieldsPanel.add(usernameField);
                    fieldsPanel.add(freeTextField);
                } else if(lastCommand.equals(command)) {
                    String employeeId = usernameField.getText();
                    String detailsInput = freeTextField.getText();


                    switch (detailsInput) {
                        case "1": // Salary
                            String hourlyRateString = "", salaryBonusString = "";
                            try {
                                if(!isUpdatingUser) {
                                    fieldsPanel.removeAll();
                                    hourlyRateField.setText("Please enter the updated employee's hourly rate:");
                                    amountField.setText("Please enter the updated employee's salary bonus:");
                                    fieldsPanel.add(hourlyRateField);
                                    fieldsPanel.add(amountField);
                                    isUpdatingUser = true;
                                } else if(isUpdatingUser) {
                                    hourlyRateString = hourlyRateField.getText();
                                    salaryBonusString = amountField.getText();
                                    double hourlySalaryRate = Double.parseDouble(hourlyRateString);
                                    double salaryBonus = Double.parseDouble(salaryBonusString);
                                    output = hrManagerMenuVM.updateEmployeeSalary(employeeId, hourlySalaryRate, salaryBonus);
                                }
                            } catch (NumberFormatException e) {
                                output = "Invalid input, expected only decimal numbers, but received: " + hourlyRateString + " " + salaryBonusString + " try again.";
                            }
                            break;
                        case "2": // Bank Details
                            if(!isUpdatingUser) {
                                fieldsPanel.removeAll();
                                bankDetailsField.setText("Please enter the updated employee's bank details:");
                                fieldsPanel.add(bankDetailsField);
                                isUpdatingUser = true;
                            } else if (isUpdatingUser) {
                                String bankDetails = bankDetailsField.getText();
                                output = hrManagerMenuVM.updateEmployeeBankDetails(employeeId, bankDetails);
                            }
                            break;
                        case "3": // Employment Conditions
                            if(!isUpdatingUser) {
                                fieldsPanel.removeAll();
                                employmentConditionsField.setText("Please enter the updated employee's employment conditions:");
                                fieldsPanel.add(employmentConditionsField);
                                isUpdatingUser = true;
                            } else if(isUpdatingUser) {
                                String employmentConditions = employmentConditionsField.getText();
                                output = hrManagerMenuVM.updateEmployeeEmploymentConditions(employeeId, employmentConditions);

                            }
                            break;
                        case "4": // Optional Details
                            if(!isUpdatingUser) {
                                fieldsPanel.removeAll();
                                nameField.setText("Please enter the updated employee's optional details:");
                                fieldsPanel.add(nameField);
                                isUpdatingUser = true;
                            } else if(isUpdatingUser) {
                                String details = nameField.getText();
                                output = hrManagerMenuVM.updateEmployeeDetails(employeeId, details);
                            }
                            break;
                        default:
                            output = "Invalid input, expected a number between 1 and 4, try again.";
                    }
                }
            } else if (command.equals("Authorize")) {
                // Parameters: <username> <authorization>
                if(!lastCommand.equals(command)) {
                    fieldsPanel.removeAll();
                    usernameField.setText("Please enter the username to authorize:");
                    addAuthorizationButtons(fieldsPanel);
                    fieldsPanel.add(usernameField);

                } else if (lastCommand.equals(command)) {
                    String username = usernameField.getText();
                    String authorization = getSelectedButtonName(authorizationField);
                    output = hrManagerMenuVM.authorizeUser(username, authorization);
                }

            } else
                output = "Invalid command was given, try again.";
            //System.out.println(output);
            //nextMenu = this;
            notice.setText(output);
            frame.repaint();
            lastCommand = command;
        }
        }

        public void terminate(){
        frame.dispose();
        }
    }
