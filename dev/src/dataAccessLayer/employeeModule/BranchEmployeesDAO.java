package dataAccessLayer.employeeModule;

import dataAccessLayer.dalAbstracts.ManyToManyDAO;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BranchEmployeesDAO extends ManyToManyDAO<Pair<String,String>> {
    private static final String[] types = new String[]{"TEXT", "TEXT"};
    private static final String[] parent_table_names = {"branches","employees"};
    private static final String[] primary_keys = {"name","employee_id"};
    private static final String[][] foreign_keys = {{"name"},{"employee_id"}};
    private static final String[][] references = {{"name"},{"id"}};
    public static final String tableName = "branch_employees";

    public BranchEmployeesDAO(SQLExecutor cursor) throws DalException {
        super(cursor,
                tableName,
                parent_table_names,
                types,
                primary_keys,
                foreign_keys,
                references,
                "name",
                "employee_id");
        initTable();
    }

    /**
     * @param branchAddress the branch name
     * @param employeeId the employee's id
     * @return the pair with the given identifier
     * @throws DalException if an error occurred while trying to select the pair
     */
    public Pair<String,String> select(String branchAddress, String employeeId) throws DalException {
        return select(new Pair<>(branchAddress,employeeId));
    }

    /**
     * @param branchAddress the branch name
     * @param employeeIds the list of employee ids
     * @return the list of pairs with the given identifiers
     * @throws DalException if an error occurred while trying to select the pair
     */
    public List<Pair<String,String>> select(String branchAddress, List<String> employeeIds) throws DalException {
        List<Pair<String,String>> result = new ArrayList<>();
        for (String employeeId : employeeIds) {
            result.add(select(new Pair<>(branchAddress, employeeId)));
        }
        return result;
    }

    /**
     * @param object pair of branch_address and employee_id
     * @return the pair with the given identifier, from the database
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Pair<String,String> select(Pair<String,String> object) throws DalException {
        if(cache.contains(object)) {
            return cache.get(object);
        }

        String query = String.format("SELECT * FROM %s WHERE name = '%s' AND employee_id = '%s';",
                TABLE_NAME,
                object.getKey(),
                object.getValue());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select branch employee", e);
        }
        if (resultSet.next()) {
            Pair<String,String> selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            return selected;
        } else {
            throw new DalException("No employee with id " + object.getValue() + " was found in branch " + object.getKey());
        }
    }

    /**
     * @return All the pairs in the table
     * @throws DalException if an error occurred while trying to select the pairs
     */
    @Override
    public List<Pair<String,String>> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all branch employees", e);
        }
        List<Pair<String,String>> branchEmployees = new LinkedList<>();
        while (resultSet.next()) {
            branchEmployees.add(getObjectFromResultSet(resultSet));
        }
        cache.putAll(branchEmployees);
        return branchEmployees;
    }

    /**
     * @param object - the pair to insert
     * @throws DalException if an error occurred while trying to insert the pair
     */
    @Override
    public void insert(Pair<String,String> object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s');",
                TABLE_NAME,
                object.getKey(),
                object.getValue());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while trying to insert branch employee");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert branch employee", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Pair<String, String> object) throws DalException { // TODO: probably shouldn't allow updates - keys are the only columns
        String query = String.format("UPDATE %s SET name = '%s', employee_id = '%s';",
                TABLE_NAME,
                object.getKey(),
                object.getValue());
        try {
            if (cursor.executeWrite(query) == 1) {
                cache.put(object);
            } else {
                throw new DalException("No employee with id " + object.getValue() + " was found in branch " + object.getKey());
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update branch employee", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Pair<String,String> object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE name = '%s' AND employee_id = '%s';",
                TABLE_NAME,
                object.getKey(),
                object.getValue());
        try {
            if (cursor.executeWrite(query) == 1) {
                cache.remove(object);
            } else {
                throw new DalException("No employee with id " + object.getValue() + " was found in branch " + object.getKey());
            }

        } catch (SQLException e) {
            throw new DalException("Failed to delete branch employee", e);
        }
    }

    public void deleteAll() throws DalException {
        String query = "DELETE FROM " + TABLE_NAME;
        try {
            cursor.executeWrite(query);
            cache.clear();
        } catch (SQLException e) {
            throw new DalException("Failed to delete branch employees", e);
        }
    }

    @Override
    public boolean exists(Pair<String,String> object) throws DalException {
        try {
            select(object); // Throws a DAL exception if the given object doesn't exist in the system.
            return true;
        } catch (DalException e) {
            return false;
        }
    }

    @Override
    public Pair<String,String> getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Pair<>(
                resultSet.getString("name"),
                resultSet.getString("employee_id"));
    }
}
