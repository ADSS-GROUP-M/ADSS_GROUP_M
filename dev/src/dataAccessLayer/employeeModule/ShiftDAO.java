package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import businessLayer.employeeModule.Shift.ShiftType;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShiftDAO extends DAO {
    private static final String[] primaryKeys = {Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name()};
    private static final String[] types = {"TEXT", "TEXT", "TEXT", "TEXT"};
    private HashMap<Integer, Shift> cache;
    private ShiftToNeededRolesDAO shiftToNeededRolesDAO;
    private ShiftToRequestsDAO shiftToRequestsDAO;
    private ShiftToWorkersDAO  shiftToWorkersDAO;
    private ShiftToCancelsDAO shiftToCancelsDAO;
    private ShiftToActivityDAO shiftToActivityDAO;

    private enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        IsApproved;
    }

    //needed roles HashMap<Role,Integer>, shiftRequests HashMap<Role,List<Employees>>, shiftWorkers Map<Role,List<Employees>>, cancelCardApplies List<String>, shiftActivities List<String>.
    public ShiftDAO(ShiftToNeededRolesDAO shiftToNeededRolesDAO,
                    ShiftToRequestsDAO shiftToRequestsDAO,
                    ShiftToWorkersDAO shiftToWorkersDAO,
                    ShiftToCancelsDAO shiftToCancelsDAO,
                    ShiftToActivityDAO shiftToActivityDAO){
        super("SHIFTS",
                primaryKeys,
                types,
                "ShiftDate",
                "ShiftType",
                "Branch",
                "IsApproved"
        );
        this.shiftToNeededRolesDAO = shiftToNeededRolesDAO;
        this.shiftToRequestsDAO = shiftToRequestsDAO;
        this.shiftToWorkersDAO = shiftToWorkersDAO;
        this.shiftToCancelsDAO = shiftToCancelsDAO;
        this.shiftToActivityDAO = shiftToActivityDAO;
        this.cache = new HashMap<>();
    }

    /**
     * Can be used for testing in a different database
     *
     * @param dbName                the name of the database to connect to
     * @param shiftToNeededRolesDAO
     * @param shiftToRequestsDAO
     * @param shiftToWorkersDAO
     * @param shiftToCancelsDAO
     * @param shiftToActivityDAO
     */
    public ShiftDAO(String dbName,
                     ShiftToNeededRolesDAO shiftToNeededRolesDAO,
                     ShiftToRequestsDAO shiftToRequestsDAO,
                     ShiftToWorkersDAO shiftToWorkersDAO,
                     ShiftToCancelsDAO shiftToCancelsDAO,
                     ShiftToActivityDAO shiftToActivityDAO){
        super(dbName,
                "SHIFTS",
                primaryKeys,
                types,
                "ShiftDate",
                "ShiftType",
                "Branch",
                "IsApproved"
        );
        this.shiftToNeededRolesDAO = shiftToNeededRolesDAO;
        this.shiftToRequestsDAO = shiftToRequestsDAO;
        this.shiftToWorkersDAO = shiftToWorkersDAO;
        this.shiftToCancelsDAO = shiftToCancelsDAO;
        this.shiftToActivityDAO = shiftToActivityDAO;
        this.cache = new HashMap<>();
    }

    private int getHashCode(LocalDate dt, ShiftType st, String branch){
        return (formatLocalDate(dt) + st.name() + branch).hashCode();
    }
    public void create(Shift shift) throws DalException {
        try {
            this.shiftToNeededRolesDAO.create(shift);
            this.shiftToRequestsDAO.create(shift);
            this.shiftToWorkersDAO.create(shift);
            this.shiftToCancelsDAO.create(shift);
            this.shiftToActivityDAO.create(shift);
            String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s) VALUES('%s','%s','%s','%s')",
                    Columns.ShiftDate.name(), Columns.ShiftType.name(), ShiftDAO.Columns.Branch.name(),Columns.IsApproved.name(),
                    formatLocalDate(shift.getShiftDate()), shift.getShiftType().name(), shift.getBranch(), String.valueOf(shift.getIsApproved()));
            cursor.executeWrite(queryString);
            this.cache.put(getHashCode(shift.getShiftDate(),shift.getShiftType(),shift.getBranch()), shift);
        } catch (SQLException e) {
           throw new DalException(e);
        } 
    }

    public Shift get(String branch, LocalDate dt, ShiftType st) throws DalException {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        Shift ans = this.select(dt,st.name(),branch);
        if (ans != null)
            this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }

    public List<Shift> getAll() throws DalException {
        List<Shift> list = new LinkedList<>();
        for(Object o: selectAll()) {
            if(!(o instanceof Shift))
                throw new DalException("Something went wrong");
            Shift s = ((Shift)o);
            if (this.cache.get(getHashCode(s.getShiftDate(), s.getShiftType(), s.getBranch())) != null)
                list.add(this.cache.get(getHashCode(s.getShiftDate(), s.getShiftType(), s.getBranch())));
            else {
                list.add(s);
                this.cache.put(getHashCode(s.getShiftDate(), s.getShiftType(), s.getBranch()), s);
            }
        }
        return list;
    }

    public List<Shift> getEmployeeShifts(Employee employee) throws DalException {
        return getAll().stream().filter(s->s.isEmployeeWorking(employee)).collect(Collectors.toList());
    }

    public List<Shift> getEmployeeRequests(Employee employee) throws DalException {
        return getAll().stream().filter(s->s.isEmployeeRequesting(employee)).collect(Collectors.toList());
    }

    public void update(Shift s) throws DalException {
        if(!this.cache.containsValue(s))
            throw new DalException("Object doesn't exist in the database! Create it first.");
        if(!this.cache.containsKey(getHashCode(s.getShiftDate(),s.getShiftType(),s.getBranch())) || this.cache.get(getHashCode(s.getShiftDate(),s.getShiftType(),s.getBranch())) != s)
            throw new DalException("Cannot change primary key of an object. You must delete it and then create a new one.");
        Exception ex = null;
        try {
            Object[] key = {s.getShiftDate(), s.getShiftType().name(), s.getBranch()};
            this.shiftToNeededRolesDAO.update(s);
            this.shiftToRequestsDAO.update(s);
            this.shiftToWorkersDAO.update(s);
            this.shiftToCancelsDAO.update(s);
            this.shiftToActivityDAO.update(s);
            String queryString = String.format("UPDATE "+TABLE_NAME+" SET %s = '%s' WHERE",
            Columns.IsApproved, s.getIsApproved());
            queryString = queryString.concat(createConditionForPrimaryKey(key));
            cursor.executeWrite(queryString);
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }
    public void delete(Shift s) throws DalException {// first check if it is in cache, if it is, then delete that object! and remove from cache
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),s.getBranch()};
        this.shiftToNeededRolesDAO.delete(s);
        this.shiftToRequestsDAO.delete(s);
        this.shiftToWorkersDAO.delete(s);
        this.shiftToCancelsDAO.delete(s);
        this.shiftToActivityDAO.delete(s);
        super.delete(keys);
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),s.getBranch()));
    }

    Shift select(LocalDate date, String shiftType, String branch) throws DalException {
        Object[] keys = {date,shiftType, branch};
        return ((Shift) super.select(keys));
    }

    protected Shift convertReaderToObject(OfflineResultSet reader) throws DalException {
        Shift ans = null;
            LocalDate dt = LocalDate.parse(reader.getString(Columns.ShiftDate.name()));
            ShiftType st = ShiftType.valueOf(reader.getString(Columns.ShiftType.name()));
            String branch =reader.getString(Columns.Branch.name());
        ans = new Shift(branch,dt,st);
        Map<Role,Integer> neededRoles = null;
        Map<Role,List<Employee>> shiftRequests = null;
        Map<Role,List<Employee>> shiftWorkers = null;
        List<String> cancelApplies = null;
        List<String> activities = null;
        neededRoles = this.shiftToNeededRolesDAO.getAll(dt,st,branch);
        shiftRequests = this.shiftToRequestsDAO.getAll(dt,st,branch);
        shiftWorkers = this.shiftToWorkersDAO.getAll(dt,st,branch);
        cancelApplies = this.shiftToCancelsDAO.getAll(dt,st,branch);
        activities = this.shiftToActivityDAO.getAll(dt,st,branch);
        ans.setApproved(Boolean.valueOf(reader.getString(Columns.IsApproved.name())));
        ans.setNeededRoles(neededRoles);
        ans.setShiftRequests(shiftRequests);
        ans.setShiftWorkers(shiftWorkers);
        ans.setCancelCardApplies(cancelApplies);
        ans.setShiftActivities(activities);
        return ans;
    }

    public void clearTable() throws DalException{
        this.cache = new HashMap<>();
        this.shiftToNeededRolesDAO.clearTable();
        this.shiftToRequestsDAO.clearTable();
        this.shiftToWorkersDAO.clearTable();
        this.shiftToCancelsDAO.clearTable();
        this.shiftToActivityDAO.clearTable();
        super.clearTable();
    }

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


