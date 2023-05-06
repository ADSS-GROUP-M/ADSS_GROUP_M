package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Shift;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ShiftToActivityDAO extends DAO{
    private static final String[] primaryKeys = {Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name()};
    private static final String[] types = {"TEXT", "TEXT", "TEXT", "TEXT"};
    private static final String tableName = "SHIFT_ACTIVITIES";
    private static ShiftToActivityDAO instance;
    private final HashMap<Integer, List<String>> cache;
    private enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        Activity;
    }
    public ShiftToActivityDAO(SQLExecutor cursor) throws DalException {
        super(cursor,
				tableName,
                primaryKeys,
                types,
                "ShiftDate",
                "ShiftType",
                "Branch",
                "Activity"
        );
        this.cache = new HashMap<>();
    }

    private int getHashCode(LocalDate dt, Shift.ShiftType st, String branch){
        return (formatLocalDate(dt) + st.name() + branch).hashCode();
    }

    List<String> getAll(LocalDate dt, Shift.ShiftType st, String branch) throws DalException {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        List<String> ans = this.select(dt,st.name(),branch);
        this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }

    void create(Shift shift) throws DalException {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), shift.getBranch())))
                throw new DalException("Key already exists!");
            List<String> entries = new LinkedList<>();
            for(String str: shift.getShiftActivities()) {
                String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s) VALUES('%s','%s','%s','%s')",
                        Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name(), Columns.Activity.name(),
                        formatLocalDate(shift.getShiftDate()), shift.getShiftType().name(), shift.getBranch(), str);
                cursor.executeWrite(queryString);
                entries.add(str);
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

    void delete(Shift s) throws DalException {
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),s.getBranch()};
        super.delete(keys);
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),s.getBranch()));
    }

    List<String> select(LocalDate date, String shiftType, String branch) throws DalException {
        Object[] keys = {date,shiftType, branch};
        return ((List<String>) super.select(keys));
    }
    protected List<String> convertReaderToObject(OfflineResultSet reader) {
        List<String> ans = new LinkedList<String>();
        try {
            while (reader.next()) {
                String str = reader.getString(Columns.Activity.name());
                if(str == null)
                    continue;
                ans.add(str);
            }
        }catch (Exception e){}
        return ans;
    }

    public void clearTable() throws DalException{
        super.clearTable();
        cache.clear();
    }
}
