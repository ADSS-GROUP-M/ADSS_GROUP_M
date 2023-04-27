package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import businessLayer.employeeModule.Branch;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;



public class BranchesDAO extends ManyToManyDAO<Branch> {

    private static final String[] types = new String[]{"TEXT", "TEXT", "TEXT", "TEXT", "TEXT"};
    private static final String[] parent_table_names = {"sites"};
    private static final String[] primary_keys = {"address"};
    private static final String[] foreign_keys = {"address"};
    private static final String[] references = {"address"};

    private BranchEmployeesDAO branchEmployeesDAO;

    public BranchesDAO(BranchEmployeesDAO branchEmployeesDAO) throws DalException {
        super("branches",
                parent_table_names,
                types,
                primary_keys,
                foreign_keys,
                references,
                "address",
                "morning_shift_start",
                "morning_shift_end",
                "evening_shift_start",
                "evening_shift_end");
        this.branchEmployeesDAO = branchEmployeesDAO;
    }

    public BranchesDAO(String dbName, BranchEmployeesDAO branchEmployeesDAO) throws DalException{
        super(dbName,
                "branches",
                parent_table_names,
                types,
                primary_keys,
                foreign_keys,
                references,
                "address",
                "morning_shift_start",
                "morning_shift_end",
                "evening_shift_start",
                "evening_shift_end");
        this.branchEmployeesDAO = branchEmployeesDAO;
    }

    /**
     * @param branchAddress the identifier of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    public Branch select(String branchAddress) throws DalException {
        return select(Branch.getLookupObject(branchAddress));
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Branch select(Branch object) throws DalException {

        if(cache.contains(object)) {
            return cache.get(object);
        }

        String query = String.format("SELECT * FROM %s WHERE address = '%s';",
                TABLE_NAME,
                object.address());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select branch", e);
        }
        if (resultSet.next()) {
            Branch selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            return selected;
        } else {
            throw new DalException("No branch with address " + object.address() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Branch> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all branches", e);
        }
        List<Branch> branches = new LinkedList<>();
        while (resultSet.next()) {
            branches.add(getObjectFromResultSet(resultSet));
        }
        cache.putAll(branches);
        return branches;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Branch object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s', '%s', '%s', '%s');",
                TABLE_NAME,
                object.address(),
                object.getMorningStart(),
                object.getMorningEnd(),
                object.getEveningStart(),
                object.getEveningEnd());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while trying to insert branch");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert branch", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Branch object) throws DalException {
        String query = String.format("UPDATE %s SET morning_shift_start = '%s', morning_shift_end = '%s', evening_shift_start = '%s', evening_shift_end = '%s' WHERE address = '%s';",
                TABLE_NAME,
                object.getMorningStart(),
                object.getMorningEnd(),
                object.getEveningStart(),
                object.getEveningEnd(),
                object.address());
        try {
            if (cursor.executeWrite(query) == 1) {
                cache.put(object);
            } else {
                throw new DalException("No branch with id " + object.address() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update branch", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Branch object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE address = '%s';",
                TABLE_NAME,
                object.address());
        try {
            if (cursor.executeWrite(query) == 1) {
                cache.remove(object);
            } else {
                throw new DalException("No branch with id " + object.address() + " was found");
            }

        } catch (SQLException e) {
            throw new DalException("Failed to delete branch", e);
        }
    }

    @Override
    public void clearTable() {
        try {
            branchEmployeesDAO.deleteAll();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        super.clearTable();
    }

    @Override
    public boolean exists(Branch object) throws DalException {
        //TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Branch getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Branch(
                resultSet.getString("address"),
                resultSet.getLocalTime("morning_shift_start"),
                resultSet.getLocalTime("morning_shift_end"),
                resultSet.getLocalTime("evening_shift_start"),
                resultSet.getLocalTime("evening_shift_end"));
    }

    public Pair<String,String> selectBranchEmployee(String branchId, String employeeId) throws DalException {
        return branchEmployeesDAO.select(branchId, employeeId);
    }

    public List<Pair<String, String>> selectBranchEmployees(String branchId, List<String> employeeIds) throws DalException {
        return branchEmployeesDAO.select(branchId, employeeIds);
    }

    public void insertEmployee(String branchId, String employeeId) throws DalException {
        branchEmployeesDAO.insert(new Pair(branchId, employeeId));
    }
}
