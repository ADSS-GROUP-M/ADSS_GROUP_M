package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ShiftToWorkersDAO extends DAO {

    private static final String[] primaryKeys = {Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name(), Columns.EmployeeId.name()};
    private static final String tableName = "SHIFT_WORKERS";
    private static final String[] types = {"TEXT", "TEXT", "TEXT", "TEXT", "TEXT"};
    private HashMap<Integer, HashMap<Role,List<Employee>>> cache;
    private EmployeeDAO employeeDAO;

    private enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        EmployeeId,
        Role;
    }

    //needed roles HashMap<Role,Integer>, shiftRequests HashMap<Role,List<Employees>>, shiftWorkers Map<Role,List<Employees>>, cancelCardApplies List<String>, shiftActivities List<String>.
    public ShiftToWorkersDAO(EmployeeDAO employeeDAO) throws DalException {
        super(tableName,
                primaryKeys,
                types,
                "ShiftDate",
                "ShiftType",
                "Branch",
                "EmployeeId",
                "Role"
        );
        this.employeeDAO = employeeDAO;
        this.cache = new HashMap<>();
    }

    public ShiftToWorkersDAO(String dbName, EmployeeDAO employeeDAO) throws DalException {
        super(dbName,
                tableName,
                primaryKeys,
                types,
                "ShiftDate",
                "ShiftType",
                "Branch",
                "EmployeeId",
                "Role"
        );
        this.employeeDAO = employeeDAO;
        this.cache = new HashMap<>();
    }

    private int getHashCode(LocalDate dt, Shift.ShiftType st, String branch){
        return (formatLocalDate(dt) + st.name() + branch).hashCode();
    }
    void create(Shift shift) throws DalException {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), shift.getBranch())))
                throw new DalException("Key already exists!");
        HashMap<Role,List<Employee>> entries = new HashMap<>();
            for(Role r: shift.getShiftWorkers().keySet()) {
                List<Employee> list = new LinkedList<>();
                    for(Employee e: shift.getShiftWorkers().get(r)) {

                    String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s, %s) VALUES('%s','%s','%s','%s','%s')",
                            Columns.ShiftDate.name(), Columns.ShiftType.name(), ShiftToWorkersDAO.Columns.Branch.name(), Columns.EmployeeId.name(), Columns.Role.name(),
                            formatLocalDate(shift.getShiftDate()), shift.getShiftType().name(), shift.getBranch(), e.getId(), r.name());
                    cursor.executeWrite(queryString);
                    list.add(e);
                }
                    entries.put(r,list);
            }
            this.cache.put(getHashCode(shift.getShiftDate(), shift.getShiftType(), shift.getBranch()),entries );

        } catch(SQLException e) {
            throw new DalException(e);
        }
    }

    public HashMap<Role,List<Employee>> getAll(LocalDate dt, Shift.ShiftType st, String branch) throws DalException {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        HashMap<Role,List<Employee>> ans = this.select(dt,st.name(),branch);
        this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }

    /*public List<Shift> getAll() throws DalException {
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
    }*/
    void update(Shift s) throws DalException {
        if(!this.cache.containsKey(getHashCode(s.getShiftDate(), s.getShiftType(), s.getBranch())))
            throw new DalException("Key doesnt exist! Create it first.");
        this.delete(s);
        this.create(s);
    }

    void delete(Shift s) throws DalException {// first check if it is in cache, if it is, then delete that object! and remove from cache
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),s.getBranch()};
        super.delete(keys);
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),s.getBranch()));
    }

    HashMap<Role,List<Employee>> select(LocalDate date, String shiftType, String branch) throws DalException {
        Object[] keys = {date,shiftType, branch};
        return ((HashMap<Role,List<Employee>>) super.select(keys));
    }

    protected HashMap<Role,List<Employee>> convertReaderToObject(OfflineResultSet reader) {
        HashMap<Role,List<Employee>> ans = new HashMap<>();
        try {

            while (reader.next()) {
                Role r = Role.valueOf(reader.getString(Columns.Role.name()));
                Employee e = employeeDAO.select(reader.getString(Columns.EmployeeId.name()));
                if(r == null || e == null)
                    continue;
                if (ans.containsKey(r)) {
                    ans.get(r).add(e);
                } else {
                    List<Employee> li = new LinkedList<>();
                    li.add(e);
                    ans.put(r, li);
                }
            }
        }catch (Exception e){}
        return ans;
    }

    public void clearTable() throws DalException {
        super.clearTable();
        cache.clear();
    }
}

