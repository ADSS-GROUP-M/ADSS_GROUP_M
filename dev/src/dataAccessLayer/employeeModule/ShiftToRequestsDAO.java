package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class ShiftToRequestsDAO extends DAO{
    private static ShiftToRequestsDAO instance;
    private HashMap<Integer, HashMap<Role,List<Employee>>> cache;
    private EmployeeDAO employeeDAO;
    private enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        EmployeeId,
        Role;
    }
    private ShiftToRequestsDAO() throws DalException {
        super("SHIFT_REQUESTS", new String[]{ShiftToRequestsDAO.Columns.ShiftDate.name(), ShiftToRequestsDAO.Columns.ShiftType.name(), ShiftToRequestsDAO.Columns.Branch.name(), ShiftToRequestsDAO.Columns.EmployeeId.name()});
        employeeDAO = EmployeeDAO.getInstance();
        this.cache = new HashMap<>();
    }
    static ShiftToRequestsDAO getInstance() throws DalException {
       if(instance == null)
          instance = new ShiftToRequestsDAO();
       return instance;
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

    void create(Shift shift, String branch) throws DalException {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch)))
                throw new DalException("Key already exists!");
            HashMap<Role,List<Employee>> entries = new HashMap<>();
            for(Role r: shift.getShiftRequests().keySet()) {
                List<Employee> list = new LinkedList<>();
                for(Employee e: shift.getShiftRequests().get(r)) {

                    String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s, %s) VALUES('%s','%s','%s','%s','%s')",
                            ShiftToRequestsDAO.Columns.ShiftDate.name(), ShiftToRequestsDAO.Columns.ShiftType.name(), ShiftToRequestsDAO.Columns.Branch.name(), ShiftToRequestsDAO.Columns.EmployeeId.name(), ShiftToRequestsDAO.Columns.Role.name(),
                            formatLocalDate(shift.getShiftDate()), shift.getShiftType().name(), branch, e.getId(), r.name());
                    cursor.executeWrite(queryString);
                    list.add(e);
                }
                entries.put(r,list);
            }
            this.cache.put(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch),entries );

        } catch(SQLException e) {
            throw new DalException(e);
        } 
    }
    void update(Shift s, String branch) throws DalException {
        if(!this.cache.containsKey(getHashCode(s.getShiftDate(), s.getShiftType(), branch)))
            throw new DalException("Key doesnt exist! Create it first.");
        this.delete(s,branch);
        this.create(s,branch);
    }

    void delete(Shift s, String branch) throws DalException{// first check if it is in cache, if it is, then delete that object! and remove from cache
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),branch));
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),branch};
        super.delete(keys);
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

    public void deleteAll() throws DalException{
        super.deleteAll();
        cache.clear();
    }
}
