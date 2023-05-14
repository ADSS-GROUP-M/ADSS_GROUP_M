package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Shift;
import dataAccessLayer.dalAbstracts.ManyToManyDAO;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalAssociationClasses.employeeModule.ShiftCancel;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShiftToCancelsDAO extends ManyToManyDAO<ShiftCancel> {

    private static final String[] types = {"TEXT", "TEXT" , "TEXT", "TEXT"};
    private static final String[] parent_tables = {"shifts"};
    private static final String[] primary_keys = {Columns.branch.name(), Columns.shift_date.name(), Columns.shift_type.name(), Columns.cancel_action.name()};
    private static final String[][] foreign_keys = {{Columns.branch.name(), Columns.shift_date.name(), Columns.shift_type.name()}};
    private static final String[][] references = {{"branch", "shift_date", "shift_type"}};
    public static final String tableName = "shift_cancels";

    private enum Columns {
        branch,
        shift_date,
        shift_type,
        cancel_action
    }

    public ShiftToCancelsDAO(SQLExecutor cursor) throws DalException{
        super(cursor,
                tableName,
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                Columns.branch.name(),
                Columns.shift_date.name(),
                Columns.shift_type.name(),
                Columns.cancel_action.name()
        );
        initTable();
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ShiftCancel select(ShiftCancel object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftDate().toString(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.cancel_action.name(),
                object.cancelAction()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select shift cancel action", e);
        }
        if(resultSet.next()) {
            ShiftCancel fetched = getObjectFromResultSet(resultSet);
            cache.put(fetched);
            return fetched;
        } else {
            throw new DalException("The cancel action was not found in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
        }

    }

    /**
     * @return All the objects in the table corresponding to the given shift identifiers, as a list of strings
     * @throws DalException if an error occurred while trying to select the objects
     */
    public List<String> selectAllByShiftIds(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType) throws DalException {
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
            throw new DalException("Failed to select all shifts cancel actions", e);
        }
        List<String> result = new ArrayList<>();
        while(resultSet.next()) {
            ShiftCancel selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            result.add(selected.cancelAction());
        }
        return result;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<ShiftCancel> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all shifts cancel actions", e);
        }
        List<ShiftCancel> result = new LinkedList<>();
        while(resultSet.next()) {
            result.add(getObjectFromResultSet(resultSet));
        }
        cache.putAll(result);
        return result;
    }

    /**
     * @param shift - the shift to insert its cancel actions
     * @throws DalException if an error occurred while trying to insert the object
     */
    public void insert(Shift shift) throws DalException {
        for (String cancelAction : shift.getShiftCancels()) {
            insert(new ShiftCancel(shift.getBranch(), shift.getShiftDate(), shift.getShiftType(), cancelAction));
        }
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(ShiftCancel object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s','%s','%s','%s');",
                TABLE_NAME,
                object.branchId(),
                object.shiftDate().toString(),
                object.shiftType().name(),
                object.cancelAction()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new RuntimeException("Unexpected error while trying to insert shift cancel action");
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert shift cancel action", e);
        }
    }

    /**
     * @param shift - the object to update its needed roles
     * @throws DalException if an error occurred while trying to update the object
     */
    public void update(Shift shift) throws DalException {
        delete(shift);
        insert(shift);
    }

    /** Updates a shift cancel object in the database, but do not use this method, it is deprecated,
     * use delete and insert instead.
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    @Deprecated
    public void update(ShiftCancel object) throws DalException {
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.cancel_action.name(),
                object.cancelAction(),
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftDate().toString(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.cancel_action.name(),
                object.cancelAction()
        );
        try {
            if(cursor.executeWrite(query) != 1) {
                throw new DalException("Failed to update cancel action in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update cancel action", e);
        }
    }

    /**
     * @param shift the shift to delete its cancel actions
     * @throws DalException if an error occurred while trying to delete the objects
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
                for (String cancelAction : shift.getShiftCancels()) {
                    cache.remove(new ShiftCancel(shift.getBranch(), shift.getShiftDate(), shift.getShiftType(), cancelAction));
                }
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete shift's cancel actions", e);
        }
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(ShiftCancel object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftType(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.cancel_action.name(),
                object.cancelAction()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new DalException("Failed to delete cancel action in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.remove(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete cancel action", e);
        }
    }

    @Override
    public boolean exists(ShiftCancel object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftDate().toString(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.cancel_action.name(),
                object.cancelAction()
        );
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if the shift's cancel action exists", e);
        }
    }

    @Override
    protected ShiftCancel getObjectFromResultSet(OfflineResultSet resultSet) {
        return new ShiftCancel(
                resultSet.getString(Columns.branch.name()),
                resultSet.getLocalDate(Columns.shift_date.name()),
                Shift.ShiftType.valueOf(resultSet.getString(Columns.shift_type.name())),
                resultSet.getString(Columns.cancel_action.name())
        );
    }
}
