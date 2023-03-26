package dev.BuissnessLayer;

import java.util.*;

import javax.lang.model.util.ElementScanner14;

public class UserController{

    private static HashMap<Integer,User> users;
    private static UserController instance;

    private UserController(){
        if(users == null)
            users = new HashMap<Integer,User>();
    }

    public static UserController getInstance() throws Exception{
        if(instance == null){
            instance = new UserController();
            instance.registerManager("admin123", "123", null);
            
        }
        return instance;
    }

    protected User getUser(String username) throws Exception{
        for (User u: this.users.values()){
            if(u.getUsername() == username)
                return u;
        }
        throw new Exception ("User does not exist");
    }

    protected void register(String username, String password, Employee linkedEmployee) throws Exception{
        for(User s: users.values()){
            if(s.getUsername().equals(username))
                throw new Exception("Username exists");
        }
        User s = new User(this, username, password, linkedEmployee, false);
        users.put(generateKey(),s);
    }

    private void registerManager(String username, String password, Employee linkedEmployee) throws Exception{
        for(User s: users.values()){
            if(s.getUsername().equals(username))
                throw new Exception("Username exists");
        }
        User s = new User(this, username, password, linkedEmployee, true);
        users.put(generateKey(),s);
    }

    public int login(String username, String password) throws Exception{
        for(int key: this.users.keySet()){
            User s = this.users.get(key);
            if(s.getUsername().equals(username))//user found
            {
                if(s.getPassword().equals(password)) // authentication complete
                    return key;
                else
                    throw new Exception("wrong password");
            }
        }

        throw new Exception("user not found");
    }
    protected void logout(int key, User u){
        int newKey = generateKey();
        this.users.remove(key, u);
        this.users.put(newKey, u);
    }

    protected static int generateKey(){
        return (int)(Math.random()*100000000);
    }
    public boolean amIloggedIn(int key) {
        return this.users.containsKey(key);
    }

    /*
     * recruit_employee command: "recruit_employee (private name) (last name) (id) (bank number) (branch number) (salary) [(employmentCondition1),(employmentCondition2),...,(employmentConditionN)] (employmentYear) (employmentMonth) (employmentDay) "
     * login command: "login (username) (password)"
     * logout command: "logout"
     * sign up to shift command: "register to shift (empId) (branch_name) (shift_time) (year) (month) (day) (role)"
     */
    public String serve(int userKey, String command){
        String[] splitted = command.split(" ", -1);
        String response = "success";
        User u = null;
        if(this.users.containsKey(userKey))
            u = this.users.get(userKey);
        else{
                response = "not logged in, please log in first";
                return response;
        }

       
        
        try{
        if(splitted[0].equals("recruit_employee") && splitted.length == 11){
            String name =splitted[1] +" " + splitted[2];
            int id = Integer.parseInt(splitted[3]);
            int bankNumber = Integer.parseInt(splitted[4]);
            int branchNumber = Integer.parseInt(splitted[5]);
            int salary = Integer.parseInt(splitted[6]);
            List<EmploymentConditions> ec = new LinkedList<>();
            if(!splitted[7].equals("none")){
                String[] conditions = splitted[7].split(",", -1);
                for(String str : conditions){
                    ec.add(EmploymentConditions.valueOf(str));
                }
            }
            int employmentYear = Integer.parseInt(splitted[8]);
            int employmentMonth = Integer.parseInt(splitted[9]);
            int employmentDay = Integer.parseInt(splitted[10]);

            String[] userDetails = u.recruitEmployeeAndNewUser(name, id,bankNumber,branchNumber,salary,ec,employmentYear,employmentMonth,employmentDay);
            response = "Employee registered. User details for the new employee: \n username: " + userDetails[0] + " password: "+userDetails[1];
        }
        else if(splitted.length == 1 && splitted[0].equals("logout")){
            this.logout(userKey, u);
        }
        else if(splitted.length == 10 && splitted[0].equals("register") && splitted[1].equals("to") && splitted[2].equals("shift")){
            int empId =Integer.parseInt(splitted[3]);
            String branch = splitted[4];
            String shiftTime = splitted[5];
            int year =Integer.parseInt(splitted[6]), month =Integer.parseInt(splitted[7]), day = Integer.parseInt(splitted[8]);
            String role = splitted[9];
            u.signUpEmployeeToShift(empId,year,month,day,branch,shiftTime,role);
        }
        else if(false){ //make more commands

        }
        else
            response = "insert a legal command";
        }catch(Exception e){
            if(splitted.length == 0)
                response = "please enter some text";
            else
                {response = e.getMessage(); e.printStackTrace();}
        }
        return response;
    }

}