package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import exceptions.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalAssociationClasses.employeeModule.ShiftWorker;
import dataAccessLayer.dalAbstracts.ManyToManyDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ShiftToWorkersDAO extends ManyToManyDAO<ShiftWorker> {

    private static final String[] types = {"TEXT", "TEXT" , "TEXT", "TEXT", "TEXT"};
    private static final String[] parent_tables = {"SHIFTS", "employees"};
    private static final String[] primary_keys = {Columns.BranchId.name(), Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.EmployeeId.name()};
    private static final String[][] foreign_keys = {{Columns.BranchId.name(), Columns.ShiftDate.name(), Columns.ShiftType.name()}, {Columns.EmployeeId.name()}};
    private static final String[][] references = {{"Branch", "ShiftDate", "ShiftType"}, {"id"}};
    public static final String tableName = "SHIFT_WORKERS";
    private final EmployeeDAO employeeDAO;

    private enum Columns {
        BranchId,
        ShiftDate,
        ShiftType,
        EmployeeId,
        Role
    }

    public ShiftToWorkersDAO(SQLExecutor cursor, EmployeeDAO employeeDAO) throws DalException{
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
                Columns.EmployeeId.name(),
                Columns.Role.name()
        );
        initTable();
        this.employeeDAO = employeeDAO;
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ShiftWorker select(ShiftWorker object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.BranchId.name(),
                object.branchId(),
                Columns.ShiftDate.name(),
                object.shiftDate().toString(),
                Columns.ShiftType.name(),
                object.shiftType().name(),
                Columns.EmployeeId.name(),
                object.employeeId()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select shift worker", e);
        }
        if(resultSet.next()) {
            ShiftWorker fetched = getObjectFromResultSet(resultSet);
            cache.put(fetched);
            return fetched;
        } else {
            throw new DalException("The employee " + object.employeeId() + " was not found as a shift worker in " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
        }

    }

    /**
     * @return All the objects in the table corresponding to the given shift identifiers, as a map from Role to role amounts
     * @throws DalException if an error occurred while trying to select the objects
     */
    public Map<Role,List<Employee>> selectAllByShiftIds(String branchId, LocalDate shiftDate, Shift.ShiftType shiftType) throws DalException {
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
            throw new DalException("Failed to select all shifts requests", e);
        }
        Map<Role,List<Employee>> result = new HashMap<>();
        while(resultSet.next()) {
            ShiftWorker selected = getObjectFromResultSet(resultSet);
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
    public List<ShiftWorker> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all shifts requests", e);
        }
        List<ShiftWorker> result = new LinkedList<>();
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
        for (Map.Entry<Role,List<Employee>> entry : shift.getShiftWorkers().entrySet()) {
            for (Employee employee : entry.getValue()) {
                insert(new ShiftWorker(shift.getBranch(), shift.getShiftDate(), shift.getShiftType(), employee.getId(), entry.getKey()));
            }
        }
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(ShiftWorker object) throws DalException {
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
                throw new RuntimeException("Unexpected error while trying to insert shift worker");
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert shift worker", e);
        }
    }

    /**
     * @param shift - the object to update its shift workers
     * @throws DalException if an error occurred while trying to update the object
     */
    public void update(Shift shift) throws DalException {
        delete(shift);
        insert(shift);
    }

    /**
     * Updates a single shift worker object in the database
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(ShiftWorker object) throws DalException {
        String query = String.format("UPDATE %s SET %s = %d WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.Role.name(),
                object.role(),
                Columns.BranchId.name(),
                object.branchId(),
                Columns.ShiftDate.name(),
                object.shiftDate().toString(),
                Columns.ShiftType.name(),
                object.shiftType().name(),
                Columns.EmployeeId.name(),
                object.employeeId()
        );
        try {
            if(cursor.executeWrite(query) != 1) {
                throw new DalException("Failed to update role " + object.role() + " of shift worker " + object.employeeId() + "in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update shift worker", e);
        }
    }

    /**
     * @param shift the shift to delete its workers
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
                for(Map.Entry<Role,List<Employee>> entry : shift.getShiftWorkers().entrySet()) {
                    for(Employee employee : entry.getValue()) {
                        cache.remove(ShiftWorker.getLookupObject(shift.getBranch(), shift.getShiftDate(), shift.getShiftType(), employee.getId()));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete shift workers", e);
        }
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(ShiftWorker object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.BranchId.name(),
                object.branchId(),
                Columns.ShiftDate.name(),
                object.shiftType(),
                Columns.ShiftType.name(),
                object.shiftType().name(),
                Columns.EmployeeId.name(),
                object.employeeId()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new DalException("Failed to delete shift worker " + object.employeeId() + " in shift " + object.shiftDate() + " " + object.shiftType() + " in branch " + object.branchId());
            } else {
                cache.remove(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete shift worker", e);
        }
    }

    @Override
    public boolean exists(ShiftWorker object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.BranchId.name(),
                object.branchId(),
                Columns.ShiftDate.name(),
                object.shiftDate().toString(),
                Columns.ShiftType.name(),
                object.shiftType().name(),
                Columns.EmployeeId.name(),
                object.employeeId()
        );
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if shift worker exists", e);
        }
    }

    @Override
    protected ShiftWorker getObjectFromResultSet(OfflineResultSet resultSet) {
        return new ShiftWorker(
                resultSet.getString(Columns.BranchId.name()),
                resultSet.getLocalDate(Columns.ShiftDate.name()),
                Shift.ShiftType.valueOf(resultSet.getString(Columns.ShiftType.name())),
                resultSet.getString(Columns.EmployeeId.name()),
                Role.valueOf(resultSet.getString(Columns.Role.name()))
        );
    }
}

