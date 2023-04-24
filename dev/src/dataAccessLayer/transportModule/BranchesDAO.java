package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import businessLayer.employeeModule.Branch;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;



public class BranchesDAO extends ManyToManyDAO<Branch> {

    private static final String[] types = new String[]{"TEXT", "TEXT", "TEXT", "TEXT", "TEXT"};

    public BranchesDAO() throws DalException{
        super("branches",
                new String[]{"sites"},
                types,
                new String[]{"address"},
                new String[]{"address"},
                new String[]{"address"},
                "address",
                "morning_shift_start",
                "morning_shift_end",
                "evening_shift_start",
                "evening_shift_end");
    }

    public BranchesDAO(String dbName) throws DalException{
        super(dbName,
                "branches",
                new String[]{"sites"},
                types,
                new String[]{"address"},
                new String[]{"address"},
                new String[]{"address"},
                "address",
                "morning_shift_start",
                "morning_shift_end",
                "evening_shift_start",
                "evening_shift_end");
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Branch select(Branch object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE address = '%s'",
                TABLE_NAME,
                object.address());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select branch", e);
        }
        if (resultSet.next()) {
            return getObjectFromResultSet(resultSet);
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
        String query = String.format("SELECT * FROM %s", TABLE_NAME);
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
        return branches;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Branch object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s', '%s', '%s', '%s')",
                TABLE_NAME,
                object.address(),
                object.getMorningStart(),
                object.getMorningEnd(),
                object.getEveningStart(),
                object.getEveningEnd());
        try {
            cursor.executeWrite(query);
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
        String query = String.format("UPDATE %s SET morning_shift_start = '%s', morning_shift_end = '%s', evening_shift_start = '%s', evening_shift_end = '%s' WHERE address = '%s'",
                TABLE_NAME,
                object.getMorningStart(),
                object.getMorningEnd(),
                object.getEveningStart(),
                object.getEveningEnd(),
                object.address());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to update branch", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Branch object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE address = '%s'",
                TABLE_NAME,
                object.address());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to delete branch", e);
        }
    }

    @Override
    protected Branch getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Branch(
                resultSet.getString("address"),
                resultSet.getLocalTime("morning_shift_start"),
                resultSet.getLocalTime("morning_shift_end"),
                resultSet.getLocalTime("evening_shift_start"),
                resultSet.getLocalTime("evening_shift_end"));
    }
}
