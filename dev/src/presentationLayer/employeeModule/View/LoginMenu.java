package presentationLayer.employeeModule.View;

import presentationLayer.DataGenerator;
import presentationLayer.employeeModule.Model.BackendController;
import presentationLayer.employeeModule.ViewModel.LoginMenuVM;
import presentationLayer.transportModule.cli.TransportCLI;
import serviceLayer.ServiceFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Scanner;

public class LoginMenu implements Menu {
    private final LoginMenuVM loginMenuVM;
    private static Scanner scanner;
    private Menu nextMenu;
    private JFrame frame;
    private JPanel panel;
    private JButton login;
    private JButton generateData;
    private JButton exit;
    private JTextField usernameField;
    private JTextField passwordField;
    private JLabel notice;
    private ActionListener insert = new InsertAction();
    private ActionListener command = new CommandAction();

    public LoginMenu() {
        this.loginMenuVM = new LoginMenuVM();
        scanner = new Scanner(System.in);
        System.out.println("Please log in to the system.");
        initiateGUI();
    }

    public LoginMenu(ServiceFactory factory) {
        this.loginMenuVM = new LoginMenuVM(factory);
        scanner = new Scanner(System.in);
        System.out.println("Please log in to the system.");
        initiateGUI();
    }


    private void initiateGUI(){
        nextMenu = this;
        frame = new JFrame("Login Menu");
        frame.setSize(800,500);
        panel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxlayout);
        panel.setBorder(new EmptyBorder(new Insets(45, 70, 45, 70)));
        frame.setLayout(new GridLayout(2,1));
        login = new JButton("Login");
        login.addActionListener(command);
        generateData = new JButton("Generate Data");
        generateData.addActionListener(command);
        usernameField = new JTextField("");
        passwordField = new JTextField("");
        usernameField.setColumns(13);
        passwordField.setColumns(13);
        usernameField.addActionListener(insert);
        usernameField.setEnabled(true);
        passwordField.addActionListener(insert);
        passwordField.setEnabled(true);
        notice = new JLabel("");
        exit = new JButton("Exit");
        exit.addActionListener(command);
        frame.add(panel);
        panel.add(notice);
        panel.add(login);
        panel.add(generateData);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(exit);

    }

    public void printCommands() {
        System.out.println("1. Login command: `login <username> <password>`");
        System.out.println("2. Generate initial data: `generate_data`");
        System.out.println("3. Exit command: `exit`");
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
        if (command.length == 0) {
            output = "Invalid command, command cannot be empty.";
        } else if (command[0].equals("exit") && command.length == 1) {
            output = "Exiting CLI.";
            MenuManager.terminate();
        }
        else if (command[0].equals("generate_data") && command.length == 1) {
            // Call the generate_data function
            System.out.println("Generating data... this may take a while....");
            output = new DataGenerator().generateData();
        }
        else if (command[0].equals("login")) {
            if (command.length != 3) {
                output = "Invalid login command. (Usage: `login <username> <password>`)";
            } else {
                output = loginMenuVM.login(command[1], command[2]);
                if (loginMenuVM.isLoggedIn()) {
                    System.out.println(output);
                    List<String> authorizations = loginMenuVM.getUserAuthorizations();
                    if (authorizations != null && authorizations.contains("HRManager")) {
                        return new HRManagerMenu();
                    } else if (authorizations != null && authorizations.contains("TransportManager")) {
                        return new TransportCLI(loginMenuVM.serviceFactory());
                    } else {
                        return new EmployeeMenu(new BackendController());
                    }
                }
            }
        }
        else {
            output = "You must log in to the system before using it. (Usage: `login <username> <password>`)";
        }
        System.out.println(output);
        return this;
    }

    @Override
    public Menu runGUI() {
        frame.setVisible(true);
        try{
            Thread.sleep(3);
        } catch(Exception e){}
        if(nextMenu != this) {
            frame.setVisible(false);
            terminate();
        }
        return nextMenu;
    }

    public void terminate(){
        frame.dispose();
    }

    class InsertAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        }
    }

    class CommandAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String output = "";

                if (command.equals("Login")) {
                    output = loginMenuVM.login(usernameField.getText(),passwordField.getText());
                    if (loginMenuVM.isLoggedIn()) {
                        notice.setText(output);
                        List<String> authorizations = loginMenuVM.getUserAuthorizations();
                        if (authorizations != null && authorizations.contains("HRManager")) {
                            nextMenu = new HRManagerMenu(new BackendController());
                        } else if (authorizations != null && authorizations.contains("TransportManager")) {
                            nextMenu = new TransportCLI(loginMenuVM.serviceFactory());
                        } else {
                            nextMenu = new EmployeeMenu(new BackendController());
                        }
                    }
                } else if(command.equals("Generate Data")){
                    notice.setText(new DataGenerator().generateData());
                }
                else if(command.equals("Exit")){
                    MenuManager.terminate();
                    nextMenu = null;
                }
                frame.repaint();

        }
    }

}
