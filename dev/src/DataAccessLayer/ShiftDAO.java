package DataAccessLayer;

import employeeModule.BusinessLayer.Employees.Employee;
import employeeModule.BusinessLayer.Employees.Role;
import employeeModule.BusinessLayer.Employees.Shift;
import employeeModule.BusinessLayer.Employees.Shift.ShiftType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShiftDAO extends DAO {
    private static ShiftDAO instance;
    private HashMap<Integer, Shift> cache;
    private ShiftToNeededRolesDAO shiftToNeededRolesDAO;
    private ShiftToRequestsDAO shiftToRequestsDAO;
    private ShiftToWorkersDAO  shiftToWorkersDAO;
    private ShiftToCancelsDAO shiftToCancelsDAO;
    private ShiftToActivityDAO shiftToActivityDAO;

    public enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        IsApproved;
    }

    //needed roles HashMap<Role,Integer>, shiftRequests HashMap<Role,List<Employees>>, shiftWorkers Map<Role,List<Employees>>, cancelCardApplies List<String>, shiftActivities List<String>.
    private ShiftDAO() throws Exception {
        super("SHIFTS", new String[]{ShiftDAO.Columns.ShiftDate.name(), ShiftDAO.Columns.ShiftType.name(), Columns.Branch.name()});
        shiftToNeededRolesDAO = ShiftToNeededRolesDAO.getInstance();
        shiftToRequestsDAO = ShiftToRequestsDAO.getInstance();
         shiftToWorkersDAO = ShiftToWorkersDAO.getInstance();
         shiftToCancelsDAO = ShiftToCancelsDAO.getInstance();
         shiftToActivityDAO = ShiftToActivityDAO.getInstance();
         this.cache = new HashMap<>();
    }

    public static ShiftDAO getInstance() throws Exception {
        if (instance == null)
            instance = new ShiftDAO();
        return instance;

    }
    private int getHashCode(LocalDate dt, ShiftType st, String branch){
        return (formatLocalDate(dt) + st.name() + branch).hashCode();
    }
    public void create(Shift shift, String branch) throws Exception {
        try {
            this.shiftToNeededRolesDAO.create(shift, branch);
            this.shiftToRequestsDAO.create(shift, branch);
            this.shiftToWorkersDAO.create(shift, branch);
            this.shiftToCancelsDAO.create(shift, branch);
            this.shiftToActivityDAO.create(shift, branch);
            String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s) VALUES(?,?,?,?)",
                    Columns.ShiftDate.name(), Columns.ShiftType.name(), ShiftDAO.Columns.Branch.name(),Columns.IsApproved.name());
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, formatLocalDate(shift.getShiftDate()));
            ptmt.setString(2, shift.getShiftType().name());
            ptmt.setString(3, branch);
            ptmt.setString(4, String.valueOf(shift.getIsApproved()));
            ptmt.executeUpdate();
            this.cache.put(getHashCode(shift.getShiftDate(),shift.getShiftType(),branch), shift);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Shift get(LocalDate dt, ShiftType st, String branch) throws Exception {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        Shift ans = this.select(dt,st.name(),branch);
        this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }

    public List<Shift> getAll() throws Exception {
        List<Shift> list = new LinkedList<>();
        for(Object o: selectAll()) {
            if(!(o instanceof Shift))
                throw new Exception("Something went wrong");
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

    public void update(Shift s, String branch) throws Exception {
        if(!this.cache.containsValue(s))
            throw new Exception("Object doesn't exist in the database! Create it first.");
        if(!this.cache.containsKey(getHashCode(s.getShiftDate(),s.getShiftType(),branch)) || this.cache.get(getHashCode(s.getShiftDate(),s.getShiftType(),branch)) != s)
            throw new Exception("Cannot change primary key of an object. You must delete it and then create a new one.");
        Exception ex = null;
        try {
            Object[] key = {s.getShiftDate(), s.getShiftType().name(), branch};
            this.shiftToNeededRolesDAO.update(s, branch);
            this.shiftToRequestsDAO.update(s, branch);
            this.shiftToWorkersDAO.update(s, branch);
            this.shiftToCancelsDAO.update(s, branch);
            this.shiftToActivityDAO.update(s, branch);
            String queryString = String.format("UPDATE "+TABLE_NAME+" SET %s = '%s' WHERE",
            Columns.IsApproved, s.getIsApproved());
            queryString = queryString.concat(createConditionForPrimaryKey(key));
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void delete(Shift s, String branch) {// first check if it is in cache, if it is, then delete that object! and remove from cache
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),branch));
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),branch};
        this.shiftToNeededRolesDAO.delete(s, branch);
        this.shiftToRequestsDAO.delete(s, branch);
        this.shiftToWorkersDAO.delete(s, branch);
        this.shiftToCancelsDAO.delete(s, branch);
        this.shiftToActivityDAO.delete(s, branch);
        super.delete(keys);
    }

    Shift select(LocalDate date, String shiftType, String branch) throws Exception {
        Object[] keys = {date,shiftType, branch};
        return ((Shift) super.select(keys));
    }

    protected Shift convertReaderToObject(ResultSet reader) {
        Shift ans = null;
        try{
            LocalDate dt = LocalDate.parse(reader.getString(Columns.ShiftDate.name()));
            ShiftType st = ShiftType.valueOf(reader.getString(Columns.ShiftType.name()));
            String branch =reader.getString(Columns.Branch.name());
        ans = new Shift(dt,st);
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
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return ans;
    }

    public void deleteAll(){
        this.cache = new HashMap<>();
        this.shiftToNeededRolesDAO.deleteAll();
        this.shiftToRequestsDAO.deleteAll();
        this.shiftToWorkersDAO.deleteAll();
        this.shiftToCancelsDAO.deleteAll();
        this.shiftToActivityDAO.deleteAll();
        super.deleteAll();
    }

     /*void update(String date, String shiftType, String branch, String attributeName, String attributeValue) throws Exception {
        Object[] keys = {date,shiftType,branch};
        super.update(keys, attributeName, attributeValue);
    }

    void update(String date, String shiftType, String branch, String attributeName, Integer attributeValue) throws Exception {
        Object[] keys = {date,shiftType,branch};
        super.update(keys, attributeName, attributeValue);
    }

    void update(LocalDate date, String shiftType, String branch, String attributeName, boolean attributeValue) throws Exception {
        Object[] keys = {date,shiftType,branch};
        super.update(keys, attributeName, attributeValue);
    }*/

}


