package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.User;
import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class UserDAO extends DAOBase<User> {

    public static final String primaryKey = Columns.username.name();
    public static final String tableName = "users";
    public static final String[] ALL_COLUMNS = {Columns.username.name(), Columns.password.name()};

    private final UserAuthorizationsDAO userAuthorizationsDAO;

    private enum Columns {
        username,
        password
    }

    public UserDAO(SQLExecutor cursor, UserAuthorizationsDAO userAuthorizationsDAO) throws DalException{
        super(cursor, tableName);
        this.userAuthorizationsDAO = userAuthorizationsDAO;
    }

    public User select(String username) throws DalException {
        return select(User.getLookupObject(username));
    }

    /**
     * Used to insert data into {@link DAOBase#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
            createTableQueryBuilder
                    .addColumn(Columns.username.name(), ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                    .addColumn(Columns.password.name(), ColumnType.TEXT, ColumnModifier.NOT_NULL);
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public User select(User object) throws DalException {
        if (cache.contains(object)) {
            return cache.get(object);
        }
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, primaryKey, object.getUsername());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select User from database");
        }

        User ans;
        if (resultSet.next()) {
            ans = getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("No user with username " + object.getUsername() + " was found");
        }
        cache.put(ans);
        return ans;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<User> selectAll() throws DalException {
        String query = "SELECT * FROM " + TABLE_NAME;
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all Users from database");
        }
        List<User> ans = new LinkedList<>();
        while (resultSet.next()) {
            User user = getObjectFromResultSet(resultSet);
            ans.add(user);
            cache.put(user);
        }
        return ans;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(User object) throws DalException {
        try {
            String queryString = String.format("""
                INSERT INTO %s (%s,%s) VALUES ('%s','%s')
                        """, TABLE_NAME, ALL_COLUMNS[0], ALL_COLUMNS[1],
                    object.getUsername(), object.getPassword());
            cursor.executeWrite(queryString);
            cache.put(object);
            this.userAuthorizationsDAO.insert(object); // Should insert the dependent values only after creating the user in the database
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    /**
     * @param user The user to update
     * @throws DalException if an error occurred while trying to update the user
     */
    @Override
    public void update(User user) throws DalException {
        try {
            this.userAuthorizationsDAO.update(user);
            String queryString = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'",
                    TABLE_NAME,
                    Columns.password.name(), user.getPassword(),
                    Columns.username.name(), user.getUsername());
            if (cursor.executeWrite(queryString) == 0)
                throw new DalException("No user with username " + user.getUsername() + " was found");
            cache.put(user);
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }

    /**
     * @param user The user to delete
     * @throws DalException if an error occurred while trying to delete the user
     */
    @Override
    public void delete(User user) throws DalException{
        cache.remove(user);
        this.userAuthorizationsDAO.delete(user);

        String query = String.format("DELETE FROM %s WHERE %s = '%s';", TABLE_NAME, Columns.username.name(), user.getUsername());
        try {
            if (cursor.executeWrite(query) == 0)
                throw new DalException("No user with username " + user.getUsername() + " was found");
        } catch (SQLException e) {
            throw new DalException("Failed to delete User", e);
        }
    }

    @Override
    public boolean exists(User object) throws DalException {
        try {
            select(object);
            return true;
        } catch (DalException e) {
            return false;
        }
    }

    @Override
    protected User getObjectFromResultSet (OfflineResultSet resultSet){
        User ans = new User(
                resultSet.getString(ALL_COLUMNS[0]),
                resultSet.getString(ALL_COLUMNS[1])
        );

        Set<Authorization> authorizations = null;
        try {
            authorizations = this.userAuthorizationsDAO.selectAll(ans);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        ans.setAuthorizations(authorizations);
        return ans;
    }

    @Override
    public void clearTable () {
        userAuthorizationsDAO.clearTable();
        super.clearTable();
    }
}

