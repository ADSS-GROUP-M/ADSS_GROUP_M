package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.*;

class EmployeeRolesDAO extends DAO {

    private static EmployeeRolesDAO instance;
    private HashMap<Integer, Set<Role>> cache;
    private enum Columns {
       EmployeeId,
       Role;
    }

    private EmployeeRolesDAO()throws DalException {
        super("EMPLOYEE_ROLES", new String[]{Columns.EmployeeId.name()});
        this.cache = new HashMap<>();
    }

    private EmployeeRolesDAO(String dbName) throws DalException {
        super(dbName,"EMPLOYEE_ROLES", new String[]{Columns.EmployeeId.name()});
        this.cache = new HashMap<>();
    }

    static EmployeeRolesDAO getInstance() throws DalException {
        if(instance == null)
            instance = new EmployeeRolesDAO();
        return instance;
    }

    static EmployeeRolesDAO getTestingInstance(String dbName) throws DalException {
        if(instance == null)
            instance = new EmployeeRolesDAO(dbName);
        return instance;
    }

    private int getHashCode(String id){
        return (id).hashCode();
    }

    Set<Role> getAll(String id) throws DalException {
        if (this.cache.get(getHashCode(id))!=null)
            return this.cache.get(getHashCode(id));
        Set<Role> ans = this.select(id);
        this.cache.put(getHashCode(id),ans);
        return ans;
    }

    void create(Employee emp) throws DalException {
        try {
            if(this.cache.containsKey(getHashCode(emp.getId())))
                throw new DalException("Key already exists!");
            Set<Role> entries = new HashSet<>();
            for(Role str: emp.getRoles()) {
                String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s) VALUES('%s','%s')",
                        Columns.EmployeeId.name(), Columns.Role.name(),
                        emp.getId(), str.name());
                if(cursor.executeWrite(queryString) != 1){
                    throw new RuntimeException("Unexpected error while inserting role");
                }
                entries.add(str);
            }
            this.cache.put(getHashCode(emp.getId()),entries );
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }

    void update(Employee emp) throws DalException {
        if(!this.cache.containsKey(getHashCode(emp.getId())))
            throw new DalException("Key doesnt exist! Create it first.");
        this.delete(emp);
        this.create(emp);
    }

    void delete(Employee emp) throws DalException{
        this.cache.remove(getHashCode(emp.getId()));
        Object[] keys = {emp.getId()};
        super.delete(keys);
    }

    Set<Role> select(String id) throws DalException {
        Object[] keys = {id};
        return ((Set<Role>) super.select(keys));
    }

    protected Set<Role> convertReaderToObject(OfflineResultSet reader) {
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

    public void clearTable() throws DalException {
        super.clearTable();
        this.cache.clear();
    }
}
