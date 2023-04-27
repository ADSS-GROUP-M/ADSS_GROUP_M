package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.*;

public class EmployeeRolesDAO extends DAO {

    public static final String[] primaryKeys = {Columns.EmployeeId.name()};
    public static final String tableName = "EMPLOYEE_ROLES";
    public static final String[] types = {"TEXT", "TEXT"};
    private static EmployeeRolesDAO instance;
    private HashMap<Integer, Set<Role>> cache;
    private static final String[] FOREIGN_KEYS = new String[]{"EmployeeId"};
    private static final String[] PARENT_TABLE_NAME = new String[]{"EMPLOYEES"};
    private static final String[] REFERENCES = new String[]{"Id"};

    private enum Columns {
       EmployeeId,
       Role;
    }

    public EmployeeRolesDAO()throws DalException {
        super(tableName,
                primaryKeys,
                types,
                "EmployeeId",
                "Role"
        );
        this.cache = new HashMap<>();
    }

    public EmployeeRolesDAO(String dbName) throws DalException {
        super(dbName,
                tableName,
                primaryKeys,
                types,
                "EmployeeId",
                "Role"
        );
        this.cache = new HashMap<>();
    }

    @Override
    protected void initTable() throws DalException {

        StringBuilder query = new StringBuilder();

        query.append(String.format("CREATE TABLE IF NOT EXISTS %s (\n", TABLE_NAME));

        for (int i = 0; i < ALL_COLUMNS.length; i++) {
            query.append(String.format("\"%s\" %s NOT NULL,\n", ALL_COLUMNS[i], TYPES[i]));
        }

        query.append("PRIMARY KEY(");
        for(int i = 0; i < PRIMARY_KEYS.length; i++) {
            query.append(String.format("\"%s\"",PRIMARY_KEYS[i]));
            if (i != PRIMARY_KEYS.length-1) {
                query.append(",");
            } else {
                query.append("),\n");
            }
        }

        for(int i = 0; i < FOREIGN_KEYS.length ; i++){
            query.append(String.format("CONSTRAINT FK_%s FOREIGN KEY(\"%s\") REFERENCES \"%s\"(\"%s\")",
                    FOREIGN_KEYS[i],
                    FOREIGN_KEYS[i],
                    PARENT_TABLE_NAME[i],
                    REFERENCES[i]
            ));
            if(i != FOREIGN_KEYS.length-1){
                query.append(",\n");
            } else {
                query.append("\n");
            }
        }

        query.append(");");

        try {
            cursor.executeWrite(query.toString());
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+TABLE_NAME, e);
        }
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
