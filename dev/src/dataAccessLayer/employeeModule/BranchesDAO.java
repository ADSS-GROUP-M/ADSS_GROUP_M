package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Branch;
import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.SitesDAO;
import exceptions.DalException;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;


public class BranchesDAO extends DAOBase<Branch> {

    public static final String primaryKey = "name";
    public static final String tableName = "branches";

    private final BranchEmployeesDAO branchEmployeesDAO;

    public BranchesDAO(SQLExecutor cursor, BranchEmployeesDAO branchEmployeesDAO) throws DalException {
        super(cursor, tableName);
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
     * Used to insert data into {@link DAOBase#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)}<br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
        createTableQueryBuilder
                .addColumn("name", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("morning_shift_start", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("morning_shift_end", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("evening_shift_start", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("evening_shift_end", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addForeignKey("name", SitesDAO.tableName, SitesDAO.primaryKey);
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

        String query = String.format("SELECT * FROM %s WHERE name = '%s';",
                TABLE_NAME,
                object.name());
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
            throw new DalException("No branch with name " + object.name() + " was found");
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
                object.name(),
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
        String query = String.format("UPDATE %s SET morning_shift_start = '%s', morning_shift_end = '%s', evening_shift_start = '%s', evening_shift_end = '%s' WHERE name = '%s';",
                TABLE_NAME,
                object.getMorningStart(),
                object.getMorningEnd(),
                object.getEveningStart(),
                object.getEveningEnd(),
                object.name());
        try {
            if (cursor.executeWrite(query) == 1) {
                cache.put(object);
            } else {
                throw new DalException("No branch with id " + object.name() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update branch", e);
        }
    }

    /**
     * @param object
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Branch object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE name = '%s';",
                TABLE_NAME,
                object.name());
        try {
            if (cursor.executeWrite(query) == 1) {
                cache.remove(object);
            } else {
                throw new DalException("No branch with id " + object.name() + " was found");
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
    public boolean exists(Branch object) {
        try {
            select(object);
            return true;
        } catch (DalException e) {
            return false;
        }
    }

    @Override
    public Branch getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Branch(
                resultSet.getString("name"),
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
