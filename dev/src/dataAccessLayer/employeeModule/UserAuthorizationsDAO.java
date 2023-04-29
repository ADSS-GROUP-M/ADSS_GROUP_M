package dataAccessLayer.employeeModule;


import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.User;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UserAuthorizationsDAO extends DAO {

    private static final String[] primaryKeys = {Columns.Username.name()};
    private static final String tableName = "USER_AUTHORIZATIONS";
    private static UserAuthorizationsDAO instance;
    private HashMap<Integer, Set<Authorization>> cache;
    private enum Columns {
        Username,
        Authorization;
    }

    public UserAuthorizationsDAO()throws DalException {
        super(tableName,
                primaryKeys,
                new String[]{"TEXT", "TEXT"},
                "Username",
                "Authorization"
        );
        this.cache = new HashMap<>();
    }
    public UserAuthorizationsDAO(String dbName)throws DalException {
        super(dbName,
                tableName,
                primaryKeys,
                new String[]{"TEXT", "TEXT"},
                "Username",
                "Authorization"
        );
        this.cache = new HashMap<>();
    }

    private int getHashCode(String id){
        return (id).hashCode();
    }

    Set<Authorization> getAll(String username) throws DalException {
        if (this.cache.get(getHashCode(username))!=null)
            return this.cache.get(getHashCode(username));
        Set<Authorization> ans = this.select(username);
        this.cache.put(getHashCode(username),ans);
        return ans;
    }

    void create(User user) throws DalException {
        try {
            if(this.cache.containsKey(getHashCode(user.getUsername())))
                throw new DalException("Key already exists!");
            Set<Authorization> entries = new HashSet<>();
            for(Authorization auth: user.getAuthorizations()) {
                String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s) VALUES('%s','%s')",
                        Columns.Username.name(), Columns.Authorization.name(),
                        user.getUsername(), auth.name());
                if (cursor.executeWrite(queryString) != 1)
                    throw new DalException("Could not create the user authorization for username " + user.getUsername());
                entries.add(auth);
            }
            this.cache.put(getHashCode(user.getUsername()),entries );
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }

    void update(User user) throws DalException {
        if(!this.cache.containsKey(getHashCode(user.getUsername())))
            throw new DalException("Key doesnt exist! Create it first.");
        this.delete(user);
        this.create(user);
    }

    void delete(User user) throws DalException{
        Object[] keys = {user.getUsername()};
        super.delete(keys);
        this.cache.remove(getHashCode(user.getUsername()));
    }

    private Set<Authorization> select(String username) throws DalException {
        Object[] keys = {username};
        return ((Set<Authorization>) super.select(keys));
    }

    protected Set<Authorization> convertReaderToObject(OfflineResultSet reader) {
        Set<Authorization> ans = new HashSet<>();
        try {
            while (reader.next()) {
                String authString = reader.getString(Columns.Authorization.name());
                if(authString == null)
                    continue;
                ans.add(Authorization.valueOf(reader.getString(Columns.Authorization.name())));
            }
        }catch (Exception e){ }
        return ans;
    }
}