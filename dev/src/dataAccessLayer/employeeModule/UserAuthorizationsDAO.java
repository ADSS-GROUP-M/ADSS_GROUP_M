package dataAccessLayer.employeeModule;


import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.User;
import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class UserAuthorizationsDAO extends DAOBase<Pair<String,Authorization>> {

//    private static final String[] types = new String[]{"TEXT", "TEXT"};
//    private static final String[] parent_table_names = {"users"};
//    private static final String[][] foreign_keys = {{Columns.username.name()}};
    //    private static final String[][] references = {{"username"}};
    public static final String[] primaryKey = {Columns.username.name(), Columns.authorization.name()};
    public static final String tableName = "user_authorizations";

    private enum Columns {
        username,
        authorization;
    }

    public UserAuthorizationsDAO(SQLExecutor cursor) throws DalException {
        super(cursor, tableName);
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
                .addColumn(Columns.authorization.name(), ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addForeignKey(Columns.username.name(), UserDAO.tableName, UserDAO.primaryKey);
    }

    /**
     * @param username
     * @return the set of authorizations of the user with the given identifier
     * @throws DalException if an error occurred while trying to select the objects
     */
    public Set<Authorization> selectAllByUsername(String username) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s';",
                TABLE_NAME, Columns.username.name(),username);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select user authorizations", e);
        }
        Set<Authorization> authorizations = new HashSet<>();
        while (resultSet.next()) {
            Authorization auth = getObjectFromResultSet(resultSet).getValue();
            authorizations.add(auth);
            cache.put(new Pair<>(username,auth));
        }
        return authorizations;
    }

    /**
     * @param user
     * @return the set of authorizations of the given user
     * @throws DalException if an error occurred while trying to select the objects
     */
    public Set<Authorization> selectAll(User user) throws DalException {
        return selectAllByUsername(user.getUsername());
    }

    /**
     * This method returns an authorization instance if exists in the database, but this method should not be used,
     * use selectByUsername(String username) to receive the set of authorizations of the given user
     * @param object - the pair of username and authorization to select
     * @return the pair object corresponding to the given username and authorization
     * @throws DalException if an error occurred while trying to select the object
     */
    @Deprecated
    @Override
    public Pair<String,Authorization> select(Pair<String,Authorization> object) throws DalException {
        if(cache.contains(object))
            return object;

        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.username.name(),
                object.getKey(),
                Columns.authorization.name(),
                object.getValue().name());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select user authorization", e);
        }
        if (resultSet.next()) {
            return getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("The user " + object.getKey() + " doesn't have the authorization " + object.getValue().name());
        }
    }

    /**
     * @return All the users authorizations in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Pair<String,Authorization>> selectAll() throws DalException {
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select user authorizations", e);
        }
        List<Pair<String,Authorization>> authorizations = new ArrayList<>();
        while (resultSet.next()) {
            Pair<String,Authorization> selected = getObjectFromResultSet(resultSet);
            authorizations.add(selected);
            cache.put(selected);
        }
        return authorizations;
    }

    /**
     * @param user - the user to insert its authorizations
     * @throws DalException if an error occurred while trying to insert the object
     */
    public void insert(User user) throws DalException {
        for(Authorization auth : user.getAuthorizations()) {
            insert(new Pair<>(user.getUsername(),auth));
        }
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Pair<String,Authorization> object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s');",
                TABLE_NAME,
                object.getKey(),
                object.getValue().name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while inserting user authorization");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert user authorization", e);
        }
    }

    /**
     * @param user - the user to update its authorizations
     * @throws DalException if an error occurred while trying to update the object
     */
    public void update(User user) throws DalException {
        delete(user);
        insert(user);
    }

    /** This method should not be used to update the user authorization, use delete and insert instead.
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Deprecated
    @Override
    public void update(Pair<String,Authorization> object) throws DalException {
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.authorization.name(),
                object.getValue().name(),
                Columns.username.name(),
                object.getKey(),
                Columns.authorization.name(),
                object.getValue().name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new DalException("The user " + object.getKey() + " doesn't have the authorization " + object.getValue().name());
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update user authorization", e);
        }
    }

    /**
     * @param user - the user to delete its authorizations
     * @throws DalException if an error occurred while trying to delete the object
     */
    public void delete(User user) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s';",
                TABLE_NAME,
                Columns.username.name(),
                user.getUsername());
        try {
            if(cursor.executeWrite(query) != 0) {
                for(Authorization auth : Authorization.values()) {
                    cache.remove(new Pair<>(user.getUsername(), auth));
                }
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete user authorizations", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Pair<String,Authorization> object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.username.name(),
                object.getKey(),
                Columns.authorization.name(),
                object.getValue().name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.remove(object);
            } else {
                throw new DalException("The user " + object.getKey() + " doesn't have the authorization " + object.getValue().name());
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete user authorization", e);
        }
    }

    @Override
    public boolean exists(Pair<String,Authorization> object) {
        try {
            select(object); // Throws a DAL exception if the given authorization object doesn't exist in the system.
            return true;
        } catch (DalException e) {
            return false;
        }
    }

    @Override
    protected Pair<String,Authorization> getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Pair<>(resultSet.getString(Columns.username.name()),
                Authorization.valueOf(resultSet.getString(Columns.authorization.name())));
    }
}