package dataAccessLayer.employeeModule;


import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.User;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UserAuthorizationsDAO extends DAO {

    private static final String[] primaryKeys = {Columns.Username.name(), Columns.Authorization.name()};
    private static final String tableName = "USER_AUTHORIZATIONS";
    private HashMap<Integer, Authorization> cache;
    private enum Columns {
        Username,
        Authorization;
    }

    public UserAuthorizationsDAO(SQLExecutor cursor)throws DalException {
        super(cursor,
				tableName,
                primaryKeys,
                new String[]{"TEXT", "TEXT"},
                "Username",
                "Authorization"
        );
        this.cache = new HashMap<>();
    }

    private int getHashCode(String id, Authorization authorization){
        return (id + authorization.name()).hashCode();
    }

    Set<Authorization> getAll(String username) throws DalException {
        Set<Authorization> ans = this.select(username);
        for(Authorization authorization : ans) {
            this.cache.put(getHashCode(username, authorization), authorization);
        }
        return ans;
    }

    void create(User user) throws DalException {
        try {
            for(Authorization auth: user.getAuthorizations()) {
                String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s) VALUES('%s','%s')",
                        Columns.Username.name(), Columns.Authorization.name(),
                        user.getUsername(), auth.name());
                if (cursor.executeWrite(queryString) != 1)
                    throw new DalException("Could not create the user authorization for username " + user.getUsername());
                this.cache.put(getHashCode(user.getUsername(),auth),auth);
            }
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }

    void update(User user) throws DalException {
        this.delete(user);
        this.create(user);
    }

    void delete(User user) throws DalException{
        Object[] columns = {Columns.Username.name()};
        Object[] values = {user.getUsername()};
        super.deleteBy(columns,values);
        for(Authorization authorization : user.getAuthorizations()) {
            this.cache.remove(getHashCode(user.getUsername(), authorization));
        }
    }

    private Set<Authorization> select(String username) throws DalException {
        Object[] columns = {Columns.Username.name()};
        Object[] values = {username};
        Set<Authorization> result = new HashSet<>();
        for(Object auth : super.selectBy(columns,values)) {
            result.add((Authorization)auth);
        }
        return result;
    }

    protected Authorization convertReaderToObject(OfflineResultSet reader) {
        //Set<Authorization> ans = new HashSet<>();
        Authorization ans = null;
        try {
            String authString = reader.getString(Columns.Authorization.name());
            if(authString == null)
                return null;
            ans = Authorization.valueOf(authString);
            //ans.add(Authorization.valueOf(reader.getString(Columns.Authorization.name())));
        }catch (Exception e){ }
        return ans;
    }

    @Override
    public void clearTable() {
        try {
            super.clearTable();
        } catch (DalException ignore) {}
        this.cache.clear();
    }
}