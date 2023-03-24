import java.util.*;

public class UserController{

    private static List<User> users;
    private static UserController instance;

    private UserController(){
        if(users == null)
            users = new LinkedList();
    }

    public static UserController getInstance(){
        if(instance == null)
            instance = new UserController();
        return instance;
    }

    public void register(String username, String password, boolean isHrManager){
        for(User s: users){
            if(s.getUsername().equals(username))
                throw new Exception("Username exists");
        }
        User s = new User(username, password, null, isHrManager);
        users.add(s);
    }

    public User login(String username, String password){
        User us = null;
        for(User s: users){
            if(s.getUsername().equals(username))//user found
            {
                if(s.getPassword().equals(password)) // authentication complete
                    return s;
                else
                    throw new Exception("wrong password");
            }
        }

        throw new Exception("user not found");
    }
}