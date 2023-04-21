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

class ShiftToNeededRolesDAO extends DAO{
    private static ShiftToNeededRolesDAO instance;
    private HashMap<Integer, HashMap<Role,Integer>> cache;
    public enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        Role,
        Amount;
    }
    private ShiftToNeededRolesDAO() throws Exception{
        super("SHIFT_ROLES", new String[]{ShiftToNeededRolesDAO.Columns.ShiftDate.name(), ShiftToNeededRolesDAO.Columns.ShiftType.name(), ShiftToNeededRolesDAO.Columns.Branch.name(), ShiftToNeededRolesDAO.Columns.Role.name()});
        this.cache = new HashMap<>();
    }
    static ShiftToNeededRolesDAO getInstance() throws Exception {
       if(instance == null)
          instance = new ShiftToNeededRolesDAO();
       return instance;
    }
    private int getHashCode(LocalDate dt, Shift.ShiftType st, String branch){
        return (formatLocalDate(dt) + st.name() + branch).hashCode();
    }

     HashMap<Role,Integer> getAll(LocalDate dt, Shift.ShiftType st, String branch) throws Exception {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        HashMap<Role,Integer> ans = this.select(dt,st.name(),branch);
        this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }
    void create(Shift shift, String branch) throws Exception {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch)))
                throw new Exception("Key already exists!");
            HashMap<Role,Integer> entries = new HashMap<>();
            for(Role r: shift.getShiftNeededRoles().keySet()) {
                    String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s, %s) VALUES(?,?,?,?,?)",
                            Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name(),Columns.Role.name(), Columns.Amount.name());
                    connection = getConnection();
                    ptmt = connection.prepareStatement(queryString);
                    ptmt.setString(1, formatLocalDate(shift.getShiftDate()));
                    ptmt.setString(2, shift.getShiftType().name());
                    ptmt.setString(3, branch);
                    ptmt.setString(4, r.name());
                    ptmt.setInt(5, shift.getShiftNeededRoles().get(r));
                    ptmt.executeUpdate();
                    entries.put(r,shift.getShiftNeededRoles().get(r));
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

    void update(Shift s, String branch) throws Exception {
        if(!this.cache.containsKey(getHashCode(s.getShiftDate(), s.getShiftType(), branch)))
            throw new Exception("Key doesnt exist! Create if first.");
        this.delete(s,branch);
        this.create(s,branch);
    }

    void delete(Shift s, String branch) {// first check if it is in cache, if it is, then delete that object! and remove from cache
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),branch));
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),branch};
        super.delete(keys);
    }
    HashMap<Role,Integer> select(LocalDate date, String shiftType, String branch) throws Exception {
        Object[] keys = {date,shiftType, branch};
        return ((HashMap<Role,Integer>) super.select(keys));
    }

    protected HashMap<Role,Integer> convertReaderToObject(ResultSet reader) {
        HashMap<Role,Integer> ans = null;
        try {

            while (true) {
                Role r = Role.valueOf(reader.getString(ShiftToNeededRolesDAO.Columns.Role.name()));
                int x = reader.getInt(ShiftToNeededRolesDAO.Columns.Amount.name());
                ans.put(r,x);
                if (!reader.next())
                    break;
            }
        }catch (Exception e){ e.printStackTrace();}
        return ans;
    }
}
