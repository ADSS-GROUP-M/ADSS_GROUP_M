package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Shift;

import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalAssociationClasses.employeeModule.ShiftActivity;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShiftToActivityDAO extends ManyToManyDAO<ShiftActivity> {
    private static final String[] types = {"TEXT", "TEXT" , "TEXT", "TEXT"};
    private static final String[] parent_tables = {"shifts"};
    private static final String[] primary_keys = {Columns.branch.name(), Columns.shift_date.name(), Columns.shift_type.name(), Columns.activity.name()};
    private static final String[][] foreign_keys = {{Columns.branch.name(), Columns.shift_date.name(), Columns.shift_type.name()}};
    private static final String[][] references = {{"branch", "shift_date", "shift_type"}};
    public static final String tableName = "shift_activities";

    private enum Columns {
        branch,
        shift_date,
        shift_type,
        activity
    }

    public ShiftToActivityDAO(SQLExecutor cursor) throws DalException{
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
                Columns.activity.name()
        );
        initTable();
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ShiftActivity select(ShiftActivity object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftDate().toString(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.activity.name(),
                object.activity()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select shift activity", e);
        }
        if(resultSet.next()) {
            ShiftActivity fetched = getObjectFromResultSet(resultSet);
            cache.put(fetched);
            return fetched;
        } else {
            throw new DalException("The shift activity was not found in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
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
            throw new DalException("Failed to select all shift's activities", e);
        }
        List<String> result = new ArrayList<>();
        while(resultSet.next()) {
            ShiftActivity selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            result.add(selected.activity());
        }
        return result;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<ShiftActivity> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all the shifts activities", e);
        }
        List<ShiftActivity> result = new LinkedList<>();
        while(resultSet.next()) {
            result.add(getObjectFromResultSet(resultSet));
        }
        cache.putAll(result);
        return result;
    }

    /**
     * @param shift - the shift to insert its shift activities
     * @throws DalException if an error occurred while trying to insert the objects
     */
    public void insert(Shift shift) throws DalException {
        for (String activity : shift.getShiftActivities()) {
            insert(new ShiftActivity(shift.getBranch(), shift.getShiftDate(), shift.getShiftType(), activity));
        }
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(ShiftActivity object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s','%s','%s','%s');",
                TABLE_NAME,
                object.branchId(),
                object.shiftDate().toString(),
                object.shiftType().name(),
                object.activity()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new RuntimeException("Unexpected error while trying to insert shift activity");
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert shif activity", e);
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

    /** Updates a shift activity object in the database, but do not use this method, it is deprecated,
     * use delete and insert instead.
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    @Deprecated
    public void update(ShiftActivity object) throws DalException {
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.activity.name(),
                object.activity(),
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftDate().toString(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.activity.name(),
                object.activity()
        );
        try {
            if(cursor.executeWrite(query) != 1) {
                throw new DalException("Failed to update shift activity in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update shift activity", e);
        }
    }

    /**
     * @param shift the shift to delete its shift activities
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
                for (String activity : shift.getShiftActivities()) {
                    cache.remove(new ShiftActivity(shift.getBranch(), shift.getShiftDate(), shift.getShiftType(), activity));
                }
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete shift activities", e);
        }
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(ShiftActivity object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftType(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.activity.name(),
                object.activity()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new DalException("Failed to delete shift activity in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.remove(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete shift activity", e);
        }
    }

    @Override
    public boolean exists(ShiftActivity object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.branchId(),
                Columns.shift_date.name(),
                object.shiftDate().toString(),
                Columns.shift_type.name(),
                object.shiftType().name(),
                Columns.activity.name(),
                object.activity()
        );
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if the shift activity exists", e);
        }
    }

    @Override
    protected ShiftActivity getObjectFromResultSet(OfflineResultSet resultSet) {
        return new ShiftActivity(
                resultSet.getString(Columns.branch.name()),
                resultSet.getLocalDate(Columns.shift_date.name()),
                Shift.ShiftType.valueOf(resultSet.getString(Columns.shift_type.name())),
                resultSet.getString(Columns.activity.name())
        );
    }
}
