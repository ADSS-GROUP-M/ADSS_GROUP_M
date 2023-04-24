package DataAccessLayer.employeeModule;

import BusinessLayer.employeeModule.Shift;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class ShiftToCancelsDAO extends DAO{
    private static ShiftToCancelsDAO instance;
    private HashMap<Integer, List<String>> cache;
    private enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        CancelAction;
    }
    private ShiftToCancelsDAO()throws Exception{
        super("SHIFT_CANCELS", new String[]{Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name()});
        this.cache = new HashMap<>();
    }
    static ShiftToCancelsDAO getInstance() throws Exception {
       if(instance == null)
          instance = new ShiftToCancelsDAO();
       return instance;
    }
    private int getHashCode(LocalDate dt, Shift.ShiftType st, String branch){
        return (formatLocalDate(dt) + st.name() + branch).hashCode();
    }

    List<String> getAll(LocalDate dt, Shift.ShiftType st, String branch) throws Exception {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        List<String> ans = this.select(dt,st.name(),branch);
        this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }

    void create(Shift shift, String branch) throws Exception {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch)))
                throw new Exception("Key already exists!");
            List<String> entries = new LinkedList<>();
            for(String str: shift.getShiftCancels()) {
                String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s) VALUES(?,?,?,?)",
                        ShiftToCancelsDAO.Columns.ShiftDate.name(), ShiftToCancelsDAO.Columns.ShiftType.name(), ShiftToCancelsDAO.Columns.Branch.name(), Columns.CancelAction.name());
                connection = getConnection();
                ptmt = connection.prepareStatement(queryString);
                ptmt.setString(1, formatLocalDate(shift.getShiftDate()));
                ptmt.setString(2, shift.getShiftType().name());
                ptmt.setString(3, branch);
                ptmt.setString(4, str);
                ptmt.executeUpdate();
                entries.add(str);
            }
            this.cache.put(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch),entries );
        } catch (SQLException e) {
            //e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                throw  new Exception("Failed closing connection to DB.");
            }
        }
    }

    void update(Shift s, String branch) throws Exception {
        if(!this.cache.containsKey(getHashCode(s.getShiftDate(), s.getShiftType(), branch)))
            throw new Exception("Key doesnt exist! Create it first.");
        this.delete(s,branch);
        this.create(s,branch);
    }

    void delete(Shift s, String branch) {
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),branch));
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),branch};
        super.delete(keys);
    }

    List<String> select(LocalDate date, String shiftType, String branch) throws Exception {
        Object[] keys = {date,shiftType, branch};
        return ((List<String>) super.select(keys));
    }

    protected List<String> convertReaderToObject(ResultSet reader) {
        List<String> ans = new LinkedList<>();
        try {

            while (reader.next()) {
                String str = reader.getString(Columns.CancelAction.name());
                if(str == null)
                    continue;
                ans.add(str);

            }
        }catch (Exception e){ }
        return ans;
    }
}
