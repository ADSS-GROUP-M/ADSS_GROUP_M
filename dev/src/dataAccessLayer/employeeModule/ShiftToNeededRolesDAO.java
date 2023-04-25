package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

class ShiftToNeededRolesDAO extends DAO{
    private static ShiftToNeededRolesDAO instance;
    private HashMap<Integer, HashMap<Role,Integer>> cache;
    private enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        Role,
        Amount;
    }
    private ShiftToNeededRolesDAO() throws DalException {
        super("SHIFT_ROLES", new String[]{ShiftToNeededRolesDAO.Columns.ShiftDate.name(), ShiftToNeededRolesDAO.Columns.ShiftType.name(), ShiftToNeededRolesDAO.Columns.Branch.name(), ShiftToNeededRolesDAO.Columns.Role.name()});
        this.cache = new HashMap<>();
    }
    static ShiftToNeededRolesDAO getInstance() throws DalException {
       if(instance == null)
          instance = new ShiftToNeededRolesDAO();
       return instance;
    }
    private int getHashCode(LocalDate dt, Shift.ShiftType st, String branch){
        return (formatLocalDate(dt) + st.name() + branch).hashCode();
    }

     HashMap<Role,Integer> getAll(LocalDate dt, Shift.ShiftType st, String branch) throws DalException {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        HashMap<Role,Integer> ans = this.select(dt,st.name(),branch);
        this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }
    void create(Shift shift, String branch) throws DalException {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch)))
                throw new DalException("Key already exists!");
            HashMap<Role,Integer> entries = new HashMap<>();
            for(Role r: shift.getShiftNeededRoles().keySet()) {
                    String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s, %s) VALUES(?,?,?,?,?)",
                            Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name(),Columns.Role.name(), Columns.Amount.name());
                    cursor.executeWrite(queryString);
                    entries.put(r,shift.getShiftNeededRoles().get(r));
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

    void delete(Shift s, String branch) throws DalException {// first check if it is in cache, if it is, then delete that object! and remove from cache
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),branch));
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),branch};
        super.delete(keys);
    }
    HashMap<Role,Integer> select(LocalDate date, String shiftType, String branch) throws DalException {
        Object[] keys = {date,shiftType, branch};
        return ((HashMap<Role,Integer>) super.select(keys));
    }

    protected HashMap<Role,Integer> convertReaderToObject(OfflineResultSet reader) {
        HashMap<Role,Integer> ans = new HashMap<>();
        try {
            while (reader.next()) {
                    Role r = Role.valueOf(reader.getString(ShiftToNeededRolesDAO.Columns.Role.name()));
                    Integer x = reader.getInt(ShiftToNeededRolesDAO.Columns.Amount.name());
                    if(x == null || r == null)
                         continue;
                    ans.put(r,x);
            }
        }catch (Exception e){ }
        return ans;
    }

    public void deleteAll() throws DalException {
        super.deleteAll();
        cache.clear();
    }
}
