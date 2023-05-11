package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ShiftToRequestsDAO extends DAO{
    private static final String[] primaryKeys = {Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name(), Columns.EmployeeId.name()};
    private static final String tableName = "SHIFT_REQUESTS";
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
    public ShiftToRequestsDAO(SQLExecutor cursor, EmployeeDAO employeeDAO) throws DalException {
        super(cursor,
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

    HashMap<Role,List<Employee>> getAll(LocalDate dt, Shift.ShiftType st, String branch) throws DalException {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        HashMap<Role,List<Employee>> ans = this.select(dt,st.name(),branch);
        this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }

    void create(Shift shift) throws DalException {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), shift.getBranch())))
                throw new DalException("Key already exists!");
            HashMap<Role,List<Employee>> entries = new HashMap<>();
            for(Role r: shift.getShiftRequests().keySet()) {
                List<Employee> list = new LinkedList<>();
                for(Employee e: shift.getShiftRequests().get(r)) {

                    String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s, %s) VALUES('%s','%s','%s','%s','%s')",
                            ShiftToRequestsDAO.Columns.ShiftDate.name(), ShiftToRequestsDAO.Columns.ShiftType.name(), ShiftToRequestsDAO.Columns.Branch.name(), ShiftToRequestsDAO.Columns.EmployeeId.name(), ShiftToRequestsDAO.Columns.Role.name(),
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
    void update(Shift s) throws DalException {
        if(!this.cache.containsKey(getHashCode(s.getShiftDate(), s.getShiftType(), s.getBranch())))
            throw new DalException("Key doesnt exist! Create it first.");
        this.delete(s);
        this.create(s);
    }

    void delete(Shift s) throws DalException{// first check if it is in cache, if it is, then delete that object! and remove from cache
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
                Role r = Role.valueOf(reader.getString(ShiftToRequestsDAO.Columns.Role.name()));
                Employee e = employeeDAO.select(reader.getString(ShiftToRequestsDAO.Columns.EmployeeId.name()));
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

    public void clearTable() throws DalException{
        super.clearTable();
        cache.clear();
    }
}
