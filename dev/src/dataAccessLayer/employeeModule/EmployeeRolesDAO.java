package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;

import dataAccessLayer.dalAbstracts.DAO;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class EmployeeRolesDAO extends DAO<Pair<String,Role>> {

    public static final String[] primaryKey = {Columns.employee_id.name(), Columns.role.name()};
    public static final String tableName = "employee_roles";

    private enum Columns {
        employee_id,
        role;
    }

    public EmployeeRolesDAO(SQLExecutor cursor) throws DalException {
        super(cursor, tableName);
    }

    /**
     * @param id the employee id
     * @return the set of roles of the employee with the given identifier
     * @throws DalException if an error occurred while trying to select the objects
     */
    public Set<Role> selectAllByEmployeeId(String id) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s';",
                TABLE_NAME, Columns.employee_id.name(),id);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select employee roles", e);
        }
        Set<Role> roles = new HashSet<>();
        while (resultSet.next()) {
            Role role = getObjectFromResultSet(resultSet).getValue();
            roles.add(role);
            cache.put(new Pair<>(id,role));
        }
        return roles;
    }

    /**
     * @param employee
     * @return the set of roles of the given employee
     * @throws DalException if an error occurred while trying to select the objects
     */
    public Set<Role> selectAll(Employee employee) throws DalException {
        return selectAllByEmployeeId(employee.getId());
    }

    /**
     * Used to insert data into {@link DAO#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
        createTableQueryBuilder
                .addColumn(Columns.employee_id.name(), ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn(Columns.role.name(), ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addForeignKey(Columns.employee_id.name(), EmployeeDAO.tableName, EmployeeDAO.primaryKey);
    }

    /**
     * This method returns a role instance if exists in the database, but this method should not be used,
     * use selectByEmployeeId(String employeeId) to receive the set of roles of the given employee
     * @param object - the pair of employeeId and role to select
     * @return the pair object corresponding to the given employeeId and role
     * @throws DalException if an error occurred while trying to select the object
     */
    @Deprecated
    @Override
    public Pair<String,Role> select(Pair<String,Role> object) throws DalException {
        if(cache.contains(object))
            return object;

        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.employee_id.name(),
                object.getKey(),
                Columns.role.name(),
                object.getValue().name());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select employee role", e);
        }
        if (resultSet.next()) {
            return getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("The employee " + object.getKey() + " doesn't have the role " + object.getValue().name());
        }
    }

    /**
     * @return All the employees roles in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Pair<String,Role>> selectAll() throws DalException {
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select employee roles", e);
        }
        List<Pair<String,Role>> authorizations = new LinkedList<>();
        while (resultSet.next()) {
            authorizations.add(getObjectFromResultSet(resultSet));
        }
        cache.putAll(authorizations);
        return authorizations;
    }

    /**
     * @param employee - the employee to insert its roles
     * @throws DalException if an error occurred while trying to insert the object
     */
    public void insert(Employee employee) throws DalException {
        for(Role role : employee.getRoles()) {
            insert(new Pair<>(employee.getId(),role));
        }
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Pair<String,Role> object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s');",
                TABLE_NAME,
                object.getKey(),
                object.getValue().name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while inserting employee roles");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert employee roles", e);
        }
    }

    /**
     * @param employee - the employee to update its roles
     * @throws DalException if an error occurred while trying to update the object
     */
    public void update(Employee employee) throws DalException {
        delete(employee);
        insert(employee);
    }

    /** This method should not be used to update the employee role, use delete and insert instead.
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Deprecated
    @Override
    public void update(Pair<String,Role> object) throws DalException {
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.role.name(),
                object.getValue().name(),
                Columns.employee_id.name(),
                object.getKey(),
                Columns.role.name(),
                object.getValue().name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new DalException("The employee " + object.getKey() + " doesn't have the role " + object.getValue().name());
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update employee role", e);
        }
    }

    /**
     * @param employee - the employee to delete its roles
     * @throws DalException if an error occurred while trying to delete the object
     */
    public void delete(Employee employee) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s';",
                TABLE_NAME,
                Columns.employee_id.name(),
                employee.getId());
        try {
            if(cursor.executeWrite(query) != 0) {
                for(Role role : Role.values()) {
                    cache.remove(new Pair<>(employee.getId(), role));
                }
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete employee roles", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Pair<String,Role> object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.employee_id.name(),
                object.getKey(),
                Columns.role.name(),
                object.getValue().name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.remove(object);
            } else {
                throw new DalException("The employee " + object.getKey() + " doesn't have the role " + object.getValue().name());
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete employee role", e);
        }
    }

    @Override
    public boolean exists(Pair<String,Role> object) throws DalException {
        try {
            select(object); // Throws a DAL exception if the given role object doesn't exist in the system.
            return true;
        } catch (DalException e) {
            return false;
        }
    }

    @Override
    protected Pair<String,Role> getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Pair<>(resultSet.getString(Columns.employee_id.name()),
                Role.valueOf(resultSet.getString(Columns.role.name())));
    }
}
