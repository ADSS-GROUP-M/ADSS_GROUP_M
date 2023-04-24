package DataAccessLayer.employeeModule;

import BusinessLayer.employeeModule.Employee;
import BusinessLayer.employeeModule.Role;
import BusinessLayer.employeeModule.Shift;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

class EmployeeRolesDAO extends DAO {

    private static EmployeeRolesDAO instance;
    private HashMap<Integer, Set<Role>> cache;
    private enum Columns {
       EmployeeId,
       Role;
    }

    private EmployeeRolesDAO()throws Exception{
        super("EMPLOYEE_ROLES", new String[]{Columns.EmployeeId.name()});
        this.cache = new HashMap<>();
    }

    static EmployeeRolesDAO getInstance() throws Exception {
        if(instance == null)
            instance = new EmployeeRolesDAO();
        return instance;
    }

    private int getHashCode(String id){
        return (id).hashCode();
    }

    Set<Role> getAll(String id) throws Exception {
        if (this.cache.get(getHashCode(id))!=null)
            return this.cache.get(getHashCode(id));
        Set<Role> ans = this.select(id);
        this.cache.put(getHashCode(id),ans);
        return ans;
    }

    void create(Employee emp) throws Exception {
        try {
            if(this.cache.containsKey(getHashCode(emp.getId())))
                throw new Exception("Key already exists!");
            Set<Role> entries = new HashSet<>();
            for(Role str: emp.getRoles()) {
                String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s) VALUES(?,?)",
                        Columns.EmployeeId.name(), Columns.Role.name());
                connection = getConnection();
                ptmt = connection.prepareStatement(queryString);
                ptmt.setString(1, emp.getId());
                ptmt.setString(2, str.name());
                ptmt.executeUpdate();
                entries.add(str);
            }
            this.cache.put(getHashCode(emp.getId()),entries );
        } catch (SQLException e) {
           // e.printStackTrace();
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

    void update(Employee emp) throws Exception {
        if(!this.cache.containsKey(getHashCode(emp.getId())))
            throw new Exception("Key doesnt exist! Create it first.");
        this.delete(emp);
        this.create(emp);
    }

    void delete(Employee emp) {
        this.cache.remove(getHashCode(emp.getId()));
        Object[] keys = {emp.getId()};
        super.delete(keys);
    }

    Set<Role> select(String id) throws Exception {
        Object[] keys = {id};
        return ((Set<Role>) super.select(keys));
    }

    protected Set<Role> convertReaderToObject(ResultSet reader) {
        Set<Role> ans = new HashSet<>();
        try {

            while (reader.next()) {
                String roleString = reader.getString(Columns.Role.name());
                if(roleString == null)
                    continue;
                ans.add(Role.valueOf(reader.getString(Columns.Role.name())));
            }
        }catch (Exception e){ }
        return ans;
    }
}
