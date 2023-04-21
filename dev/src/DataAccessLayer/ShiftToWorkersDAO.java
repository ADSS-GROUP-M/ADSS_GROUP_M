package DataAccessLayer;

import employeeModule.BusinessLayer.Employees.Employee;
import employeeModule.BusinessLayer.Employees.Role;
import employeeModule.BusinessLayer.Employees.Shift;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ShiftToWorkersDAO extends DAO {

    private static ShiftToWorkersDAO instance;
    private HashMap<Integer, HashMap<Role,List<Employee>>> cache;
    private EmployeeDAO employeeDAO;

    public enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        EmployeeId,
        Role;
    }

    //needed roles HashMap<Role,Integer>, shiftRequests HashMap<Role,List<Employees>>, shiftWorkers Map<Role,List<Employees>>, cancelCardApplies List<String>, shiftActivities List<String>.
    private ShiftToWorkersDAO() throws Exception {
        super("SHIFT_TO_WORKERS", new String[]{Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name(),Columns.EmployeeId.name()});
        employeeDAO = EmployeeDAO.getInstance();
        this.cache = new HashMap<>();
    }

    public static ShiftToWorkersDAO getInstance() throws Exception {
        if (instance == null)
            instance = new ShiftToWorkersDAO();
        return instance;

    }
    private int getHashCode(LocalDate dt, Shift.ShiftType st, String branch){
        return (formatLocalDate(dt) + st.name() + branch).hashCode();
    }
    void create(Shift shift, String branch) throws Exception {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch)))
                throw new Exception("Key already exists!");
        HashMap<Role,List<Employee>> entries = new HashMap<>();
            for(Role r: shift.getShiftWorkers().keySet()) {
                List<Employee> list = new LinkedList<>();
                    for(Employee e: shift.getShiftWorkers().get(r)) {

                    String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s, %s) VALUES(?,?,?,?,?)",
                            Columns.ShiftDate.name(), Columns.ShiftType.name(), ShiftDAO.Columns.Branch.name(), Columns.EmployeeId.name(), Columns.Role.name());
                    connection = getConnection();
                    ptmt = connection.prepareStatement(queryString);
                    ptmt.setString(1, formatLocalDate(shift.getShiftDate()));
                    ptmt.setString(2, shift.getShiftType().name());
                    ptmt.setString(3, branch);
                    ptmt.setString(4, e.getId());
                    ptmt.setString(5, r.name());
                    ptmt.executeUpdate();
                    list.add(e);
                }
                    entries.put(r,list);
            }
            this.cache.put(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch),entries );

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

    public HashMap<Role,List<Employee>> get(LocalDate dt, Shift.ShiftType st, String branch) throws Exception {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        HashMap<Role,List<Employee>> ans = this.select(dt,st.name(),branch);
        this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }

    /*public List<Shift> getAll() throws Exception {
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
    }*/

    /* public void update(Shift s, String branch) throws Exception {
        try {
        if(!this.cache.containsKey(getHashCode(s.getShiftDate(), s.getShiftType(), branch)))
            throw new Exception("Key doesnt exist! Create if first.");
        HashMap<Role,List<Employee>> entries = new HashMap<>();
        for(Role r: s.getShiftWorkers().keySet()) {
            if(!this.cache.get(getHashCode(s.getShiftDate(),s.getShiftType(),branch)).containsKey(r)) // create
            List<Employee> list = new LinkedList<>();
            for(Employee e: shift.getShiftWorkers().get(r)) {
                Exception ex = null;

                    Object[] key = {s.getShiftDate(), s.getShiftType().name(), branch};
                    String queryString = String.format("UPDATE " + TABLE_NAME + " SET %s = '%s' WHERE",
                            Columns.IsApproved, s.getIsApproved());
                    queryString = queryString.concat(createConditionForPrimaryKey(key));
                    connection = getConnection();
                    ptmt = connection.prepareStatement(queryString);
                    ptmt.executeUpdate();
                }
            }

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

    }*/

    void delete(Shift s, String branch) {// first check if it is in cache, if it is, then delete that object! and remove from cache
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),branch));
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),branch};
        super.delete(keys);
    }

    HashMap<Role,List<Employee>> select(LocalDate date, String shiftType, String branch) throws Exception {
        Object[] keys = {date,shiftType, branch};
        return ((HashMap<Role,List<Employee>>) super.select(keys));
    }

    protected HashMap<Role,List<Employee>> convertReaderToObject(ResultSet reader) {
        HashMap<Role,List<Employee>> ans = null;
        try {

            while (true) {
                Role r = Role.valueOf(reader.getString(Columns.Role.name()));
                Employee e = employeeDAO.get(reader.getString(Columns.Role.name()));
                if (ans.containsKey(r)) {
                    ans.get(r).add(e);
                } else {
                    List<Employee> li = new LinkedList<>();
                    li.add(e);
                    ans.put(r, li);
                }
                if (!reader.next())
                    break;
            }
        }catch (Exception e){ e.printStackTrace();}
        return ans;
    }
}

