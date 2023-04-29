package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class ShiftToNeededRolesDAO extends DAO{
    private static final String[] primaryKeys = {Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name(), Columns.Role.name()};
    private static final String[] types = {"TEXT", "TEXT", "TEXT", "TEXT", "INTEGER"};
    private static final String tableName = "SHIFT_ROLES";
    private HashMap<Integer, HashMap<Role,Integer>> cache;
    private enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        Role,
        Amount;
    }
    public ShiftToNeededRolesDAO() throws DalException {
        super(tableName,
                primaryKeys,
                types,
                "ShiftDate",
                "ShiftType",
                "Branch",
                "Role",
                "Amount"
        );
        this.cache = new HashMap<>();
    }
    public ShiftToNeededRolesDAO(String dbName) throws DalException {
        super(dbName,
                tableName,
                primaryKeys,
                types,
                "ShiftDate",
                "ShiftType",
                "Branch",
                "Role",
                "Amount"
        );
        this.cache = new HashMap<>();
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
    void create(Shift shift) throws DalException {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), shift.getBranch())))
                throw new DalException("Key already exists!");
            HashMap<Role,Integer> entries = new HashMap<>();
            for(Role r: shift.getShiftNeededRoles().keySet()) {
                    String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s, %s) VALUES('%s','%s','%s','%s',%d)",
                            Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name(),Columns.Role.name(), Columns.Amount.name(),
                            formatLocalDate(shift.getShiftDate()), shift.getShiftType().name(), shift.getBranch(), r.name(), shift.getShiftNeededRoles().get(r));
                    cursor.executeWrite(queryString);
                    entries.put(r,shift.getShiftNeededRoles().get(r));
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

    void delete(Shift s) throws DalException {// first check if it is in cache, if it is, then delete that object! and remove from cache
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),s.getBranch()};
        super.delete(keys);
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),s.getBranch()));
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

    public void clearTable() throws DalException {
        super.clearTable();
        cache.clear();
    }
}
