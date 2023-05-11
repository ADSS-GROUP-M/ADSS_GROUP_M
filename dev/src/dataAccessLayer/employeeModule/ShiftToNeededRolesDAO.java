package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.employeeModule.records.ShiftNeededRole;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShiftToNeededRolesDAO extends ManyToManyDAO<ShiftNeededRole> {
    private static final String[] types = {"TEXT", "TEXT" , "TEXT", "TEXT", "INTEGER"};
    private static final String[] parent_tables = {"SHIFTS"};
    private static final String[] primary_keys = {Columns.BranchId.name(), Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Role.name()};
    private static final String[][] foreign_keys = {{Columns.BranchId.name(), Columns.ShiftDate.name(), Columns.ShiftType.name()}};
    private static final String[][] references = {{"Branch", "ShiftDate", "ShiftType"}};
    public static final String tableName = "SHIFT_ROLES";

    private enum Columns {
        BranchId,
        ShiftDate,
        ShiftType,
        Role,
        Amount
    }

    public ShiftToNeededRolesDAO(SQLExecutor cursor) throws DalException{
        super(cursor,
                tableName,
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                Columns.BranchId.name(),
                Columns.ShiftDate.name(),
                Columns.ShiftType.name(),
                Columns.Role.name(),
                Columns.Amount.name()
        );
        initTable();
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ShiftNeededRole select(ShiftNeededRole object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.BranchId.name(),
                object.branchId(),
                Columns.ShiftDate.name(),
                object.shiftDate().toString(),
                Columns.ShiftType.name(),
                object.shiftType().name(),
                Columns.Role.name(),
                object.role().name()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select shift needed role", e);
        }
        if(resultSet.next()) {
            ShiftNeededRole fetched = getObjectFromResultSet(resultSet);
            cache.put(fetched);
            return fetched;
        } else {
            throw new DalException("The role " + object.role() + " was not found as a needed role in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
        }

    }

    /**
     * @return All the objects in the table corresponding to the given shift identifiers, as a map from Role to role amounts
     * @throws DalException if an error occurred while trying to select the objects
     */
    public Map<Role,Integer> selectAllByShiftIds(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.BranchId.name(),
                branchId,
                Columns.ShiftDate.name(),
                shiftDate.toString(),
                Columns.ShiftType.name(),
                shiftType.name()
                );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all shifts needed roles ", e);
        }
        Map<Role,Integer> result = new HashMap<>();
        while(resultSet.next()) {
            ShiftNeededRole selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            result.put(selected.role(),selected.amount());
        }
        return result;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<ShiftNeededRole> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all shifts needed roles ", e);
        }
        List<ShiftNeededRole> result = new LinkedList<>();
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
        for (Map.Entry<Role,Integer> entry : shift.getShiftNeededRoles().entrySet()) {
            insert(new ShiftNeededRole(shift.getBranch(), shift.getShiftDate(), shift.getShiftType(), entry.getKey(), entry.getValue()));
        }
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(ShiftNeededRole object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s','%s','%s','%s',%d);",
                TABLE_NAME,
                object.branchId(),
                object.shiftDate().toString(),
                object.shiftType().name(),
                object.role().name(),
                object.amount()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new RuntimeException("Unexpected error while trying to insert shift needed role");
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert shift needed role", e);
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

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(ShiftNeededRole object) throws DalException {
        String query = String.format("UPDATE %s SET %s = %d WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.Amount.name(),
                object.amount(),
                Columns.BranchId.name(),
                object.branchId(),
                Columns.ShiftDate.name(),
                object.shiftDate().toString(),
                Columns.ShiftType.name(),
                object.shiftType().name(),
                Columns.Role.name(),
                object.role().name()

        );
        try {
            if(cursor.executeWrite(query) != 1) {
                throw new DalException("Failed to update shift needed amount of role " + object.role() + " in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update shift needed amount", e);
        }
    }

    /**
     * @param shift the shift to delete its needed roles
     * @throws DalException if an error occurred while trying to delete the object
     */
    public void delete(Shift shift) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.BranchId.name(),
                shift.getBranch(),
                Columns.ShiftDate.name(),
                shift.getShiftDate().toString(),
                Columns.ShiftType.name(),
                shift.getShiftType().name());
        try {
            if(cursor.executeWrite(query) != 0) {
                for(Role role : Role.values()) {
                    cache.remove(ShiftNeededRole.getLookupObject(shift.getBranch(),shift.getShiftDate(),shift.getShiftType(), role));
                }
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete employee roles", e);
        }
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(ShiftNeededRole object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.BranchId.name(),
                object.branchId(),
                Columns.ShiftDate.name(),
                object.shiftType(),
                Columns.ShiftType.name(),
                object.shiftType().name(),
                Columns.Role.name(),
                object.role().name()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new DalException("Failed to delete shift needed amount of role " + object.role() + " in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.remove(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete shift needed role", e);
        }
    }

    @Override
    public boolean exists(ShiftNeededRole object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.BranchId.name(),
                object.branchId(),
                Columns.ShiftDate.name(),
                object.shiftDate().toString(),
                Columns.ShiftType.name(),
                object.shiftType().name(),
                Columns.Role.name(),
                object.role().name()
        );
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if shift needed role exists", e);
        }
    }

    @Override
    protected ShiftNeededRole getObjectFromResultSet(OfflineResultSet resultSet) {
        return new ShiftNeededRole(
                resultSet.getString(Columns.BranchId.name()),
                resultSet.getLocalDate(Columns.ShiftDate.name()),
                Shift.ShiftType.valueOf(resultSet.getString(Columns.ShiftType.name())),
                Role.valueOf(resultSet.getString(Columns.Role.name())),
                resultSet.getInt(Columns.Amount.name())
        );
    }
}
