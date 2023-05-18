package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;

import dataAccessLayer.dalAbstracts.DAO;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalAssociationClasses.employeeModule.ShiftRequest;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class ShiftToRequestsDAO extends DAO<ShiftRequest> {

    public static final String[] primaryKey = {Columns.branch.name(), Columns.shift_date.name(), Columns.shift_type.name(), Columns.employee_id.name(), Columns.role.name()};
    public static final String tableName = "shift_requests";
    private static final String[] shift_foreignKeyColumns = {Columns.branch.name(), Columns.shift_date.name(), Columns.shift_type.name()};
    private final EmployeeDAO employeeDAO;

    private enum Columns {
        branch,
        shift_date,
        shift_type,
        employee_id,
        role
    }

    public ShiftToRequestsDAO(SQLExecutor cursor, EmployeeDAO employeeDAO) throws DalException{
        super(cursor, tableName);
        this.employeeDAO = employeeDAO;
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
                .addColumn(Columns.branch.name(), ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn(Columns.shift_date.name(), ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn(Columns.shift_type.name(), ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn(Columns.employee_id.name(), ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn(Columns.role.name(), ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addCompositeForeignKey(shift_foreignKeyColumns, ShiftDAO.tableName, ShiftDAO.primaryKey)
                .addForeignKey(Columns.employee_id.name(), EmployeeDAO.tableName, EmployeeDAO.primaryKey);
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ShiftRequest select(ShiftRequest object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftDate().toString(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.employee_id.name(),
                object.employeeId(),
                Columns.role.name(),
                object.role().name()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select shift request", e);
        }
        if(resultSet.next()) {
            ShiftRequest fetched = getObjectFromResultSet(resultSet);
            cache.put(fetched);
            return fetched;
        } else {
            throw new DalException("The role " + object.role() + " was not found as a requested role by employee " + object.employeeId() + " in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
        }

    }

    /**
     * @return All the objects in the table corresponding to the given shift identifiers, as a map from Role to role amounts
     * @throws DalException if an error occurred while trying to select the objects
     */
    public Map<Role,List<Employee>> selectAllByShiftIds(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                branchId,
                Columns.shift_date.name(),
                shiftDate.toString(),
                Columns.shift_type.name(),
                shiftType.name()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all shifts requests", e);
        }
        Map<Role,List<Employee>> result = new HashMap<>();
        while(resultSet.next()) {
            ShiftRequest selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            if(!result.containsKey(selected.role())) {
                result.put(selected.role(), new ArrayList<>());
            }
            result.get(selected.role()).add(employeeDAO.select(selected.employeeId()));
        }
        return result;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<ShiftRequest> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all shifts requests", e);
        }
        List<ShiftRequest> result = new LinkedList<>();
        while(resultSet.next()) {
            result.add(getObjectFromResultSet(resultSet));
        }
        cache.putAll(result);
        return result;
    }

    /**
     * @param shift - the shift to insert its needed roles
     * @throws DalException if an error occurred while trying to insert the object
     */
    public void insert(Shift shift) throws DalException {
        for (Map.Entry<Role,List<Employee>> entry : shift.getShiftRequests().entrySet()) {
            for (Employee employee : entry.getValue()) {
                insert(new ShiftRequest(shift.getBranch(), shift.getShiftDate(), shift.getShiftType(), employee.getId(), entry.getKey()));
            }
        }
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(ShiftRequest object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s','%s','%s','%s','%s');",
                TABLE_NAME,
                object.branchId(),
                object.shiftDate().toString(),
                object.shiftType().name(),
                object.employeeId(),
                object.role().name()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new RuntimeException("Unexpected error while trying to insert shift request");
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert shift request", e);
        }
    }

    /**
     * @param shift - the object to update its shift requests
     * @throws DalException if an error occurred while trying to update the object
     */
    public void update(Shift shift) throws DalException {
        delete(shift);
        insert(shift);
    }

    /**
     * Updates a single shift request object in the database, but do not use this method, it is deprecated,
     * use delete and insert methods instead.
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Deprecated
    @Override
    public void update(ShiftRequest object) throws DalException {
        String query = String.format("UPDATE %s SET %s = %d WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.role.name(),
                object.role(),
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftDate().toString(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.employee_id.name(),
                object.employeeId()
        );
        try {
            if(cursor.executeWrite(query) != 1) {
                throw new DalException("Failed to update shift request of role " + object.role() + " by employee " + object.employeeId() + "in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update shift request", e);
        }
    }

    /**
     * @param shift the shift to delete its requests
     * @throws DalException if an error occurred while trying to delete the object
     */
    public void delete(Shift shift) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                shift.getBranch(),
                Columns.shift_date.name(),
                shift.getShiftDate().toString(),
                Columns.shift_type.name(),
                shift.getShiftType().name());
        try {
            if(cursor.executeWrite(query) != 0) {
                for(Map.Entry<Role,List<Employee>> entry : shift.getShiftRequests().entrySet()) {
                    for(Employee employee : entry.getValue()) {
                        cache.remove(new ShiftRequest(shift.getBranch(), shift.getShiftDate(), shift.getShiftType(), employee.getId(), entry.getKey()));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete shift requests", e);
        }
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(ShiftRequest object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftType(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.employee_id.name(),
                object.employeeId(),
                Columns.role.name(),
                object.role().name()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new DalException("Failed to delete shift request of role " + object.role() + " by employee " + object.employeeId() + " in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.remove(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete shift request", e);
        }
    }

    @Override
    public boolean exists(ShiftRequest object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftDate().toString(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.employee_id.name(),
                object.employeeId(),
                Columns.role.name(),
                object.role().name()
        );
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if shift needed role exists", e);
        }
    }

    @Override
    protected ShiftRequest getObjectFromResultSet(OfflineResultSet resultSet) {
        return new ShiftRequest(
                resultSet.getString(Columns.branch.name()),
                resultSet.getLocalDate(Columns.shift_date.name()),
                Shift.ShiftType.valueOf(resultSet.getString(Columns.shift_type.name())),
                resultSet.getString(Columns.employee_id.name()),
                Role.valueOf(resultSet.getString(Columns.role.name()))
        );
    }
}
