package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Shift;
import businessLayer.employeeModule.Shift.ShiftType;
import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class ShiftDAO extends DAOBase<Shift> {

    public static final String[] primaryKey = {Columns.branch.name(), Columns.shift_date.name(), Columns.shift_type.name()};
    public static final String tableName = "shifts";

    private final ShiftToNeededRolesDAO shiftToNeededRolesDAO;
    private final ShiftToRequestsDAO shiftToRequestsDAO;
    private final ShiftToWorkersDAO  shiftToWorkersDAO;
    private final ShiftToCancelsDAO shiftToCancelsDAO;
    private final ShiftToActivityDAO shiftToActivityDAO;

    private enum Columns {
        branch,
        shift_date,
        shift_type,
        is_approved
    }

    public ShiftDAO(SQLExecutor cursor, ShiftToNeededRolesDAO shiftToNeededRolesDAO, ShiftToRequestsDAO shiftToRequestsDAO,
                    ShiftToWorkersDAO shiftToWorkersDAO, ShiftToCancelsDAO shiftToCancelsDAO, ShiftToActivityDAO shiftToActivityDAO) throws DalException {
        super(cursor, tableName);
        this.shiftToNeededRolesDAO = shiftToNeededRolesDAO;
        this.shiftToRequestsDAO = shiftToRequestsDAO;
        this.shiftToWorkersDAO = shiftToWorkersDAO;
        this.shiftToCancelsDAO = shiftToCancelsDAO;
        this.shiftToActivityDAO = shiftToActivityDAO;
    }

    /**
     * @param branchId the branch id of the shift to select
     * @param shiftDate the shift date of the shift to select
     * @param shiftType the shift type of the shift to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    public Shift select(String branchId, LocalDate shiftDate, ShiftType shiftType) throws DalException {
        return select(new Shift(branchId, shiftDate, shiftType));
    }

    /**
     * Used to insert data into {@link DAOBase#createTableQueryBuilder}. <br/>
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
                .addColumn(Columns.is_approved.name(), ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addForeignKey(Columns.branch.name(), BranchesDAO.tableName, BranchesDAO.primaryKey);

    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Shift select(Shift object) throws DalException {
        if(cache.contains(object)) {
            return cache.get(object);
        }

        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.getBranch(),
                Columns.shift_date.name(),
                object.getShiftDate().toString(),
                Columns.shift_type.name(),
                object.getShiftType().name());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select branch", e);
        }
        if (resultSet.next()) {
            Shift selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            return selected;
        } else {
            throw new DalException("No shift in branch " + object.getBranch() + " on " + object.getShiftDate().toString() + " " + object.getShiftType().name() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Shift> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all branches", e);
        }
        List<Shift> shifts = new ArrayList<>();
        while (resultSet.next()) {
            shifts.add(getObjectFromResultSet(resultSet));
        }
        cache.putAll(shifts);
        return shifts;
    }

    public List<Shift> getEmployeeShifts(Employee employee) throws DalException {
        return selectAll().stream().filter(s->s.isEmployeeWorking(employee)).collect(Collectors.toList());
    }

    public List<Shift> getEmployeeRequests(Employee employee) throws DalException {
        return selectAll().stream().filter(s->s.isEmployeeRequesting(employee)).collect(Collectors.toList());
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Shift object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s','%s','%s','%s');",
                TABLE_NAME,
                object.getBranch(),
                object.getShiftDate().toString(),
                object.getShiftType().name(),
                object.getIsApproved());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while trying to insert shift");
            }
            this.shiftToNeededRolesDAO.insert(object);
            this.shiftToRequestsDAO.insert(object);
            this.shiftToWorkersDAO.insert(object);
            this.shiftToCancelsDAO.insert(object);
            this.shiftToActivityDAO.insert(object);
        } catch (SQLException e) {
            throw new DalException("Failed to insert shift", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Shift object) throws DalException {
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.is_approved.name(),
                object.getIsApproved(),
                Columns.branch.name(),
                object.getBranch(),
                Columns.shift_date.name(),
                object.getShiftDate().toString(),
                Columns.shift_type.name(),
                object.getShiftType().name());
        try {
            if (cursor.executeWrite(query) == 1) {
                cache.put(object);
            } else {
                throw new DalException("No shift in branch " + object.getBranch() + " on " + object.getShiftDate().toString() + " " + object.getShiftType().name() + " was found");
            }
            this.shiftToNeededRolesDAO.update(object);
            this.shiftToRequestsDAO.update(object);
            this.shiftToWorkersDAO.update(object);
            this.shiftToCancelsDAO.update(object);
            this.shiftToActivityDAO.update(object);
        } catch (SQLException e) {
            throw new DalException("Failed to update shift", e);
        }
    }

    /**
     * @param object
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Shift object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.branch.name(),
                object.getBranch(),
                Columns.shift_date.name(),
                object.getShiftDate().toString(),
                Columns.shift_type.name(),
                object.getShiftType().name());
        try {
            this.shiftToNeededRolesDAO.delete(object);
            this.shiftToRequestsDAO.delete(object);
            this.shiftToWorkersDAO.delete(object);
            this.shiftToCancelsDAO.delete(object);
            this.shiftToActivityDAO.delete(object);
            if (cursor.executeWrite(query) == 1) {
                cache.remove(object);
            } else {
                throw new DalException("No shift in branch " + object.getBranch() + " on " + object.getShiftDate().toString() + " " + object.getShiftType().name() + " was found");
            }

        } catch (SQLException e) {
            throw new DalException("Failed to delete shift", e);
        }
    }

    public void clearTable() {
        this.cache.clear();
        this.shiftToNeededRolesDAO.clearTable();
        this.shiftToRequestsDAO.clearTable();
        this.shiftToWorkersDAO.clearTable();
        this.shiftToCancelsDAO.clearTable();
        this.shiftToActivityDAO.clearTable();
        super.clearTable();
    }

    @Override
    public boolean exists(Shift object) {
        try {
            select(object);
            return true;
        } catch (DalException e) {
            return false;
        }
    }

    public Shift getObjectFromResultSet(OfflineResultSet resultSet) {
        String branchId = resultSet.getString(Columns.branch.name());
        LocalDate shiftDate = resultSet.getLocalDate(Columns.shift_date.name());
        ShiftType shiftType = ShiftType.valueOf(resultSet.getString(Columns.shift_type.name()));
        Shift result = new Shift(branchId,shiftDate,shiftType);
        result.setApproved(Boolean.parseBoolean(resultSet.getString(Columns.is_approved.name())));
        try {
            result.setNeededRoles(this.shiftToNeededRolesDAO.selectAllByShiftIds(branchId, shiftDate, shiftType));
            result.setShiftRequests(this.shiftToRequestsDAO.selectAllByShiftIds(branchId, shiftDate, shiftType));
            result.setShiftWorkers(this.shiftToWorkersDAO.selectAllByShiftIds(branchId, shiftDate, shiftType));
            result.setCancelCardApplies(this.shiftToCancelsDAO.selectAllByShiftIds(branchId, shiftDate, shiftType));
            result.setShiftActivities(this.shiftToActivityDAO.selectAllByShiftIds(branchId, shiftDate, shiftType));
        } catch (DalException e) {
            throw new RuntimeException("An error has occurred while reading shift data from database", e);
        }
        return result;
    }

//    private static final String[] primaryKeys = {Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name()};
//    private static final String[] types = {"TEXT", "TEXT", "TEXT", "TEXT"};
//    public static final String tableName = "SHIFTS";
//    private HashMap<Integer, Shift> cache;
//    private ShiftToNeededRolesDAO shiftToNeededRolesDAO;
//    private ShiftToRequestsDAO shiftToRequestsDAO;
//    private ShiftToWorkersDAO  shiftToWorkersDAO;
//    private ShiftToCancelsDAO shiftToCancelsDAO;
//    private ShiftToActivityDAO shiftToActivityDAO;
//
//    private enum Columns {
//        ShiftDate,
//        ShiftType,
//        Branch,
//        IsApproved;
//    }
//
//    //needed roles HashMap<Role,Integer>, shiftRequests HashMap<Role,List<Employees>>, shiftWorkers Map<Role,List<Employees>>, cancelCardApplies List<String>, shiftActivities List<String>.
//    public ShiftDAO(SQLExecutor cursor, ShiftToNeededRolesDAO shiftToNeededRolesDAO,
//                    ShiftToRequestsDAO shiftToRequestsDAO,
//                    ShiftToWorkersDAO shiftToWorkersDAO,
//                    ShiftToCancelsDAO shiftToCancelsDAO,
//                    ShiftToActivityDAO shiftToActivityDAO) throws DalException {
//        super(cursor,
//                tableName,
//                primaryKeys,
//                types,
//                "ShiftDate",
//                "ShiftType",
//                "Branch",
//                "IsApproved"
//        );
//        this.shiftToNeededRolesDAO = shiftToNeededRolesDAO;
//        this.shiftToRequestsDAO = shiftToRequestsDAO;
//        this.shiftToWorkersDAO = shiftToWorkersDAO;
//        this.shiftToCancelsDAO = shiftToCancelsDAO;
//        this.shiftToActivityDAO = shiftToActivityDAO;
//        this.cache = new HashMap<>();
//    }
//
//    private int getHashCode(LocalDate dt, ShiftType st, String branch){
//        return (formatLocalDate(dt) + st.name() + branch).hashCode();
//    }
//    public void create(Shift shift) throws DalException {
//        try {
//            String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s) VALUES('%s','%s','%s','%s')",
//                    Columns.ShiftDate.name(), Columns.ShiftType.name(), ShiftDAO.Columns.Branch.name(),Columns.IsApproved.name(),
//                    formatLocalDate(shift.getShiftDate()), shift.getShiftType().name(), shift.getBranch(), String.valueOf(shift.getIsApproved()));
//            cursor.executeWrite(queryString);
//            this.cache.put(getHashCode(shift.getShiftDate(),shift.getShiftType(),shift.getBranch()), shift);
//
//            this.shiftToNeededRolesDAO.insert(shift);
//            this.shiftToRequestsDAO.insert(shift);
//            this.shiftToWorkersDAO.insert(shift);
//            this.shiftToCancelsDAO.insert(shift);
//            this.shiftToActivityDAO.insert(shift);
//        } catch (SQLException e) {
//           throw new DalException(e);
//        }
//    }
//
//    public Shift get(String branch, LocalDate dt, ShiftType st) throws DalException {
//        if (this.cache.get(getHashCode(dt,st,branch))!=null)
//            return this.cache.get(getHashCode(dt,st,branch));
//        Shift ans = this.select(dt,st.name(),branch);
//        if (ans != null)
//            this.cache.put(getHashCode(dt,st,branch),ans);
//        return ans;
//    }
//
//    public List<Shift> getAll() throws DalException {
//        List<Shift> list = new LinkedList<>();
//        for(Object o: selectAll()) {
//            if(!(o instanceof Shift))
//                throw new DalException("Something went wrong");
//            Shift s = ((Shift)o);
//            if (this.cache.get(getHashCode(s.getShiftDate(), s.getShiftType(), s.getBranch())) != null)
//                list.add(this.cache.get(getHashCode(s.getShiftDate(), s.getShiftType(), s.getBranch())));
//            else {
//                list.add(s);
//                this.cache.put(getHashCode(s.getShiftDate(), s.getShiftType(), s.getBranch()), s);
//            }
//        }
//        return list;
//    }
//
//    public List<Shift> getEmployeeShifts(Employee employee) throws DalException {
//        return getAll().stream().filter(s->s.isEmployeeWorking(employee)).collect(Collectors.toList());
//    }
//
//    public List<Shift> getEmployeeRequests(Employee employee) throws DalException {
//        return getAll().stream().filter(s->s.isEmployeeRequesting(employee)).collect(Collectors.toList());
//    }
//
//    public void update(Shift s) throws DalException {
//        if(!this.cache.containsValue(s))
//            throw new DalException("Object doesn't exist in the database! Create it first.");
//        if(!this.cache.containsKey(getHashCode(s.getShiftDate(),s.getShiftType(),s.getBranch())) || this.cache.get(getHashCode(s.getShiftDate(),s.getShiftType(),s.getBranch())) != s)
//            throw new DalException("Cannot change primary key of an object. You must delete it and then create a new one.");
//        Exception ex = null;
//        try {
//            Object[] key = {s.getShiftDate(), s.getShiftType().name(), s.getBranch()};
//            this.shiftToNeededRolesDAO.update(s);
//            this.shiftToRequestsDAO.update(s);
//            this.shiftToWorkersDAO.update(s);
//            this.shiftToCancelsDAO.update(s);
//            this.shiftToActivityDAO.update(s);
//            String queryString = String.format("UPDATE "+TABLE_NAME+" SET %s = '%s' WHERE",
//            Columns.IsApproved, s.getIsApproved());
//            queryString = queryString.concat(createConditionForPrimaryKey(key));
//            cursor.executeWrite(queryString);
//        } catch(SQLException e) {
//            throw new DalException(e);
//        }
//    }
//    public void delete(Shift s) throws DalException {// first check if it is in cache, if it is, then delete that object! and remove from cache
//        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),s.getBranch()};
//        this.shiftToNeededRolesDAO.delete(s);
//        this.shiftToRequestsDAO.delete(s);
//        this.shiftToWorkersDAO.delete(s);
//        this.shiftToCancelsDAO.delete(s);
//        this.shiftToActivityDAO.delete(s);
//        super.delete(keys);
//        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),s.getBranch()));
//    }
//
//    Shift select(LocalDate date, String shiftType, String branch) throws DalException {
//        Object[] keys = {date,shiftType, branch};
//        return ((Shift) super.select(keys));
//    }
//
//    protected Shift convertReaderToObject(OfflineResultSet reader) throws DalException {
//        Shift ans = null;
//            LocalDate dt = LocalDate.parse(reader.getString(Columns.ShiftDate.name()));
//            ShiftType st = ShiftType.valueOf(reader.getString(Columns.ShiftType.name()));
//            String branch =reader.getString(Columns.Branch.name());
//        ans = new Shift(branch,dt,st);
//        Map<Role,Integer> neededRoles = null;
//        Map<Role,List<Employee>> shiftRequests = null;
//        Map<Role,List<Employee>> shiftWorkers = null;
//        List<String> cancelApplies = null;
//        List<String> activities = null;
//        neededRoles = this.shiftToNeededRolesDAO.selectAllByShiftIds(branch,dt,st);
//        shiftRequests = this.shiftToRequestsDAO.selectAllByShiftIds(branch,dt,st);
//        shiftWorkers = this.shiftToWorkersDAO.selectAllByShiftIds(branch,dt,st);
//        cancelApplies = this.shiftToCancelsDAO.selectAllByShiftIds(branch,dt,st);
//        activities = this.shiftToActivityDAO.selectAllByShiftIds(branch,dt,st);
//        ans.setApproved(Boolean.valueOf(reader.getString(Columns.IsApproved.name())));
//        ans.setNeededRoles(neededRoles);
//        ans.setShiftRequests(shiftRequests);
//        ans.setShiftWorkers(shiftWorkers);
//        ans.setCancelCardApplies(cancelApplies);
//        ans.setShiftActivities(activities);
//        return ans;
//    }
//
//    public void clearTable() throws DalException{
//        this.cache = new HashMap<>();
//        this.shiftToNeededRolesDAO.clearTable();
//        this.shiftToRequestsDAO.clearTable();
//        this.shiftToWorkersDAO.clearTable();
//        this.shiftToCancelsDAO.clearTable();
//        this.shiftToActivityDAO.clearTable();
//        super.clearTable();
//    }

     /*void update(String date, String shiftType, String branch, String attributeName, String attributeValue) throws DalException {
        Object[] keys = {date,shiftType,branch};
        super.update(keys, attributeName, attributeValue);
    }

    void update(String date, String shiftType, String branch, String attributeName, Integer attributeValue) throws DalException {
        Object[] keys = {date,shiftType,branch};
        super.update(keys, attributeName, attributeValue);
    }

    void update(LocalDate date, String shiftType, String branch, String attributeName, boolean attributeValue) throws DalException {
        Object[] keys = {date,shiftType,branch};
        super.update(keys, attributeName, attributeValue);
    }*/

}


