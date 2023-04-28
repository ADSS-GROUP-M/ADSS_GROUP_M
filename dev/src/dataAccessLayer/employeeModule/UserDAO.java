package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.User;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;


import java.sql.SQLException;

import java.util.*;

public class UserDAO extends DAO {

    public static final String tableName = "USERS";
    public static final String[] primaryKeys = {Columns.Username.name()};
    private HashMap<Integer, User> cache;
    private UserAuthorizationsDAO userAuthorizationsDAO;

    private enum Columns {
        Username,
        Password
    }

    //needed roles HashMap<Role,Integer>, shiftRequests HashMap<Role,List<Employees>>, shiftWorkers Map<Role,List<Employees>>, cancelCardApplies List<String>, shiftActivities List<String>.
    public UserDAO(UserAuthorizationsDAO userAuthorizationsDAO){
        super(tableName,
                primaryKeys,
                new String[]{"TEXT", "TEXT", "TEXT"},
                "Username",
                "Password"
        );
        this.userAuthorizationsDAO = userAuthorizationsDAO;
        this.cache = new HashMap<>();
    }

    public UserDAO(String dbName, UserAuthorizationsDAO userAuthorizationsDAO){
        super(dbName,
                tableName,
                primaryKeys,
                new String[]{"TEXT", "TEXT", "TEXT"},
                "Username",
                "Password"
        );
        this.userAuthorizationsDAO = userAuthorizationsDAO;
        this.cache = new HashMap<>();
    }


    private int getHashCode(String username){
        return (username).hashCode();
    }
    public void create(User user) throws DalException {
        try {
            String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s) VALUES('%s','%s')",
                    Columns.Username.name(), Columns.Password.name(),
                    user.getUsername(), user.getPassword());
            if (cursor.executeWrite(queryString) != 1)
                throw new DalException("Could not create the user with username " + user.getUsername());
            this.userAuthorizationsDAO.create(user);
            this.cache.put(getHashCode(user.getUsername()), user);
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    public User get(String username) throws DalException {
        if (this.cache.get(getHashCode(username))!=null)
            return this.cache.get(getHashCode(username));
        User ans = this.select(username);
        this.cache.put(getHashCode(username),ans);
        return ans;
    }

    public List<User> getAll() throws DalException {
        List<User> list = new LinkedList<>();
        for(Object o: selectAll()) {
            if(!(o instanceof Employee))
                throw new DalException("Something went wrong");
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

    public void update(User user) throws DalException {
        if(!this.cache.containsValue(user))
            throw new DalException("Object doesn't exist in the database! Create it first.");
        if(!this.cache.containsKey(getHashCode(user.getUsername())) || this.cache.get(getHashCode(user.getUsername()))!= user)
            throw new DalException("Cannot change primary key of an object. You must delete it and then create a new one.");
        Exception ex = null;
        try {
            Object[] key = {user.getUsername()};
            this.userAuthorizationsDAO.update(user);
            String queryString = String.format("UPDATE "+TABLE_NAME+" SET %s = ? , %s = ? WHERE",
                    Columns.Username.name(), Columns.Password.name());
            queryString = queryString.concat(createConditionForPrimaryKey(key));
            if (cursor.executeWrite(queryString) != 1)
                throw new DalException("No user with username " + user.getUsername() + " was found");
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }
    public void delete(User user) throws DalException {
        this.userAuthorizationsDAO.delete(user);
        Object[] keys = {user.getUsername()};
        super.delete(keys);
        this.cache.remove(getHashCode(user.getUsername()));
    }

    @Override
    public void clearTable() throws DalException {
        try {
            userAuthorizationsDAO.clearTable();
        } catch (Exception ignore) {}
        super.clearTable();
    }

    User select(String id) throws DalException {
        Object[] keys = {id};
        return ((User) super.select(keys));
    }

    protected User convertReaderToObject(OfflineResultSet reader) {
        User ans = null;
        try{
            String username = reader.getString(Columns.Username.name());
            String password = reader.getString(Columns.Password.name());

            ans = new User(username,password);

            Set<Authorization> auth = this.userAuthorizationsDAO.getAll(username);
            ans.setAuthorizations(auth);

        } catch (Exception throwables) {
            // throwables.printStackTrace();
        }
        return ans;
    }

}


