package dev.Employees.BusinessLayer;

import java.time.LocalDate;
import java.util.*;

public class UserController {

    private Map<String,User> users;
    private static UserController instance;

    private UserController(){
        users = new HashMap<>();
    }

    public static UserController getInstance() {
        if(instance == null){
            instance = new UserController();
            //instance.registerManager("admin123", "123", null); // Should be located in the data initialization function
        }
        return instance;
    }

    public User getUser(String username) throws Exception {
        if (users.containsKey(username))
            return users.get(username);
        throw new Exception ("User does not exist");
    }

    protected void createUser(String username, String password, Employee linkedEmployee) throws Exception{
        if (users.containsKey(username))
            throw new Exception("Username exists");
        User user = new User(this, username, password, linkedEmployee, false);
        users.put(username, user);
    }

    private void createManagerUser(String username, String password, Employee linkedEmployee) throws Exception{
        if (users.containsKey(username))
            throw new Exception("Username exists");
        User user = new User(this, username, password, linkedEmployee, true);
        users.put(username,user);
    }

    public void login(String username, String password) throws Exception {
        if (!users.containsKey(username))
            throw new Exception("The given username doesn't exist.");
        User user = users.get(username);
        boolean success = user.login(password);
        if(!success)
            throw new Exception("Invalid password.");
    }

    public void logout(String username) throws Exception {
        if (!this.users.containsKey(username))
            throw new Exception("The given username doesn't exist.");
        this.users.get(username).logout();
    }

    public boolean isLoggedIn(String username) throws Exception {
        if (!this.users.containsKey(username))
            throw new Exception("The given username doesn't exist.");
        return this.users.get(username).isLoggedIn();
    }

    /* Valid commands list:
     * recruit_employee command: "recruit_employee (private name) (last name) (id) (bank number) (branch number) (salary) [(employmentCondition1),(employmentCondition2),...,(employmentConditionN)] (employmentYear) (employmentMonth) (employmentDay)"
     * login command: "login (username) (password)"
     * logout command: "logout"
     * sign up to shift command: "register_shift (empId) (branch_name) (shift_time) (year) (month) (day) (role)"
     */
    public String serve(String username, String command) throws Exception {
        if (!this.users.containsKey(username))
            throw new Exception("The given username doesn't exist.");
        User user = this.users.get(username);
        if (!user.isLoggedIn())
            throw new Exception("The user is not logged in, please log in first");

        String[] splitted = command.split(" ", -1);
        String response = "success";
        try {
            if(splitted.length == 9 && splitted[0].equals("recruit_employee")){
                String name = splitted[1] + " " + splitted[2];
                int id = Integer.parseInt(splitted[3]);
                int bankNumber = Integer.parseInt(splitted[4]);
                int branchNumber = Integer.parseInt(splitted[5]);
                LocalDate employmentDate = LocalDate.parse(""); // TODO: Remove later
                List<EmploymentConditions> employmentConditions = new LinkedList<>();
                if(!splitted[6].equals("none")){
                    String[] conditions = splitted[6].split(",", -1);
                    for(String str : conditions){
                        employmentConditions.add(EmploymentConditions.valueOf(str));
                    }
                }


                String[] userDetails = user.recruitEmployeeAndNewUser(name, id,bankNumber,branchNumber, employmentConditions, employmentDate);
                response = "Employee registered. User details for the new employee: \n username: " + userDetails[0] + " password: "+userDetails[1];
            }
            else if(splitted.length == 1 && splitted[0].equals("logout")){
                this.logout(username);
            }
            else if(splitted.length == 8 && splitted[0].equals("register_shift")){
                int empId = Integer.parseInt(splitted[1]);
                String branch = splitted[2];
                String shiftTime = splitted[3];
                int year =Integer.parseInt(splitted[4]), month = Integer.parseInt(splitted[5]), day = Integer.parseInt(splitted[6]);
                String role = splitted[7];
                //user.signUpEmployeeToShift(empId,year,month,day,branch,shiftTime,role);
            }
            else if(false) { //make more commands

            }
            else
                throw new Exception("Invalid command was given.");
        } catch(Exception e) {
            if(splitted.length == 0)
                response = "Invalid command, the command cannot be empty.";
            else{
                response = e.getMessage();
                e.printStackTrace();
            }
        }
        return response;
    }

}