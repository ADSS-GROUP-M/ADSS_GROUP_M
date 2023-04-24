package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.User;


import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;

public class UserDAO extends DAO {

    private static UserDAO instance;
    private HashMap<Integer, User> cache;
    private UserAuthorizationsDAO userAuthorizationsDAO;

    private enum Columns {
        Username,
        Password,
        LoggedIn;
    }

    //needed roles HashMap<Role,Integer>, shiftRequests HashMap<Role,List<Employees>>, shiftWorkers Map<Role,List<Employees>>, cancelCardApplies List<String>, shiftActivities List<String>.
    private UserDAO() throws Exception {
        super("USERS", new String[]{Columns.Username.name()});
        userAuthorizationsDAO = UserAuthorizationsDAO.getInstance();
        this.cache = new HashMap<>();
    }

    public static UserDAO getInstance() throws Exception {
        if (instance == null)
            instance = new UserDAO();
        return instance;

    }
    private int getHashCode(String username){
        return (username).hashCode();
    }
    public void create(User user) throws Exception {
        try {
            this.userAuthorizationsDAO.create(user);
            String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s) VALUES(?,?,?)",
                    Columns.Username.name(), Columns.Password.name(), Columns.LoggedIn.name());
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, user.getUsername());
            ptmt.setString(2, user.getPassword());
            ptmt.setString(3, String.valueOf(user.isLoggedIn()));
            ptmt.executeUpdate();
            this.cache.put(getHashCode(user.getUsername()), user);
        } catch (SQLException e) {
           // e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                throw  new Exception("Failed closing connection to DB.");
            }
        }
    }

    public User get(String username) throws Exception {
        if (this.cache.get(getHashCode(username))!=null)
            return this.cache.get(getHashCode(username));
        User ans = this.select(username);
        this.cache.put(getHashCode(username),ans);
        return ans;
    }

    public List<User> getAll() throws Exception {
        List<User> list = new LinkedList<>();
        for(Object o: selectAll()) {
            if(!(o instanceof Employee))
                throw new Exception("Something went wrong");
            User user = ((User)o);
            if (this.cache.get(getHashCode(user.getUsername())) != null)
                list.add(this.cache.get(getHashCode(user.getUsername())));
            else {
                list.add(user);
                this.cache.put(getHashCode(user.getUsername()), user);
            }
        }
        return list;
    }

    public void update(User user) throws Exception {
        if(!this.cache.containsValue(user))
            throw new Exception("Object doesn't exist in the database! Create it first.");
        if(!this.cache.containsKey(getHashCode(user.getUsername())) || this.cache.get(getHashCode(user.getUsername()))!= user)
            throw new Exception("Cannot change primary key of an object. You must delete it and then create a new one.");
        Exception ex = null;
        try {
            Object[] key = {user.getUsername()};
            this.userAuthorizationsDAO.update(user);
            String queryString = String.format("UPDATE "+TABLE_NAME+" SET %s = ? , %s = ? , %s = ? WHERE",
                    Columns.Username.name(), Columns.Password.name(), Columns.LoggedIn.name());
            queryString = queryString.concat(createConditionForPrimaryKey(key));
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, user.getUsername());
            ptmt.setString(2, user.getPassword());
            ptmt.setString(3, String.valueOf(user.isLoggedIn()));
            ptmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void delete(User user) {
        this.cache.remove(getHashCode(user.getUsername()));
        Object[] keys = {user.getUsername()};
        this.userAuthorizationsDAO.delete(user);
        super.delete(keys);
    }

    User select(String id) throws Exception {
        Object[] keys = {id};
        return ((User) super.select(keys));
    }

    protected User convertReaderToObject(ResultSet reader) {
        User ans = null;
        try{
            String username = reader.getString(Columns.Username.name());
            String password = reader.getString(Columns.Password.name());
            String loggedIn = reader.getString(Columns.LoggedIn.name());

            ans = new User(username,password);

            Set<Authorization> auth = this.userAuthorizationsDAO.getAll(username);
            ans.setAuthorizations(auth);

        } catch (Exception throwables) {
            // throwables.printStackTrace();
        }
        return ans;
    }

}


