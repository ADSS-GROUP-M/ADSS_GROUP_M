package dataAccessLayer.employeeModule;


import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.*;
import dataAccessLayer.dalUtils.DalException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

class UserAuthorizationsDAO extends DAO {

    private static UserAuthorizationsDAO instance;
    private HashMap<Integer, Set<Authorization>> cache;
    private enum Columns {
        Username,
        Authorization;
    }

    private UserAuthorizationsDAO()throws DalException {
        super("USER_AUTHORIZATIONS", new String[]{Columns.Username.name()});
        this.cache = new HashMap<>();
    }

    static UserAuthorizationsDAO getInstance() throws DalException {
        if(instance == null)
            instance = new UserAuthorizationsDAO();
        return instance;
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
                String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s) VALUES(?,?)",
                        Columns.Username.name(), Columns.Authorization.name());
                connection = getConnection();
                ptmt = connection.prepareStatement(queryString);
                ptmt.setString(1, user.getUsername());
                ptmt.setString(2, auth.name());
                ptmt.executeUpdate();
                entries.add(auth);
            }
            this.cache.put(getHashCode(user.getUsername()),entries );
        } catch (SQLException e) {
           // e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                throw new DalException("Failed closing connection to DB.");
            }
        }
    }

    void update(User user) throws DalException {
        if(!this.cache.containsKey(getHashCode(user.getUsername())))
            throw new DalException("Key doesnt exist! Create it first.");
        this.delete(user);
        this.create(user);
    }

    void delete(User user) {
        this.cache.remove(getHashCode(user.getUsername()));
        Object[] keys = {user.getUsername()};
        super.delete(keys);
    }

    private Set<Authorization> select(String username) throws DalException {
        Object[] keys = {username};
        return ((Set<Authorization>) super.select(keys));
    }

    protected Set<Authorization> convertReaderToObject(ResultSet reader) {
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