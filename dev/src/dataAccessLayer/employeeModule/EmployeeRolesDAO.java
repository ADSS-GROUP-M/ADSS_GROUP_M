package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.*;

public class EmployeeRolesDAO extends ManyToManyDAO<Pair<String,Role>> {

    private static final String[] types = new String[]{"TEXT", "TEXT"};
    private static final String[] parent_table_names = {"employees"};
    private static final String[] primary_keys = {Columns.employee_id.name(), Columns.role.name()};
    private static final String[][] foreign_keys = {{Columns.employee_id.name()}};
    private static final String[][] references = {{"id"}};
    public static final String tableName = "employee_roles";

    private enum Columns {
        employee_id,
        role;
    }

    public EmployeeRolesDAO(SQLExecutor cursor) throws DalException {
        super(cursor,
                tableName,
                parent_table_names,
                types,
                primary_keys,
                foreign_keys,
                references,
                Columns.employee_id.name(),
                Columns.role.name());
        initTable();
    }

    /**
     * @param id the employee id
     * @return the set of roles of the employee with the given identifier
     * @throws DalException if an error occurred while trying to select the objects
     */
    public Set<Role> selectAllByEmployeeId(String id) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = '%s';",
                TABLE_NAME, Columns.employee_id.name(),id);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select employee roles", e);
        }
        Set<Role> roles = new HashSet<>();
        while (resultSet.next()) {
            Role role = getObjectFromResultSet(resultSet).getValue();
            roles.add(role);
            cache.put(new Pair<>(id,role));
        }
        return roles;
    }

    /**
     * @param employee
     * @return the set of roles of the given employee
     * @throws DalException if an error occurred while trying to select the objects
     */
    public Set<Role> selectAll(Employee employee) throws DalException {
        return selectAllByEmployeeId(employee.getId());
    }

    /**
     * This method returns a role instance if exists in the database, but this method should not be used,
     * use selectByEmployeeId(String employeeId) to receive the set of roles of the given employee
     * @param object - the pair of employeeId and role to select
     * @return the pair object corresponding to the given employeeId and role
     * @throws DalException if an error occurred while trying to select the object
     */
    @Deprecated
    @Override
    public Pair<String,Role> select(Pair<String,Role> object) throws DalException {
        if(cache.contains(object))
            return object;

        String query = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.employee_id.name(),
                object.getKey(),
                Columns.role.name(),
                object.getValue().name());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select employee role", e);
        }
        if (resultSet.next()) {
            return getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("The employee " + object.getKey() + " doesn't have the role " + object.getValue().name());
        }
    }

    /**
     * @return All the employees roles in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Pair<String,Role>> selectAll() throws DalException {
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select employee roles", e);
        }
        List<Pair<String,Role>> authorizations = new ArrayList<>();
        while (resultSet.next()) {
            Pair<String,Role> selected = getObjectFromResultSet(resultSet);
            authorizations.add(selected);
            cache.put(selected);
        }
        return authorizations;
    }

    /**
     * @param employee - the employee to insert its roles
     * @throws DalException if an error occurred while trying to insert the object
     */
    public void insert(Employee employee) throws DalException {
        for(Role role : employee.getRoles()) {
            insert(new Pair<>(employee.getId(),role));
        }
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Pair<String,Role> object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s');",
                TABLE_NAME,
                object.getKey(),
                object.getValue().name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while inserting employee roles");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert employee roles", e);
        }
    }

    /**
     * @param employee - the employee to update its roles
     * @throws DalException if an error occurred while trying to update the object
     */
    public void update(Employee employee) throws DalException {
        delete(employee);
        insert(employee);
    }

    /** This method should not be used to update the employee role, use delete and insert instead.
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Deprecated
    @Override
    public void update(Pair<String,Role> object) throws DalException {
        String query = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.role.name(),
                object.getValue().name(),
                Columns.employee_id.name(),
                object.getKey(),
                Columns.role.name(),
                object.getValue().name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new DalException("The employee " + object.getKey() + " doesn't have the role " + object.getValue().name());
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update employee role", e);
        }
    }

    /**
     * @param employee - the employee to delete its roles
     * @throws DalException if an error occurred while trying to delete the object
     */
    public void delete(Employee employee) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s';",
                TABLE_NAME,
                Columns.employee_id.name(),
                employee.getId());
        try {
            if(cursor.executeWrite(query) != 0) {
                for(Role role : Role.values()) {
                    cache.remove(new Pair<>(employee.getId(), role));
                }
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete employee roles", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Pair<String,Role> object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE %s = '%s' AND %s = '%s';",
                TABLE_NAME,
                Columns.employee_id.name(),
                object.getKey(),
                Columns.role.name(),
                object.getValue().name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.remove(object);
            } else {
                throw new DalException("The employee " + object.getKey() + " doesn't have the role " + object.getValue().name());
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete employee role", e);
        }
    }

    @Override
    public boolean exists(Pair<String,Role> object) throws DalException {
        try {
            select(object); // Throws a DAL exception if the given role object doesn't exist in the system.
            return true;
        } catch (DalException e) {
            return false;
        }
    }

    @Override
    protected Pair<String,Role> getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Pair<>(resultSet.getString(Columns.employee_id.name()),
                Role.valueOf(resultSet.getString(Columns.role.name())));
    }

    //public static final String[] primaryKeys = {Columns.EmployeeId.name(), Columns.Role.name()};
    //public static final String tableName = "EMPLOYEE_ROLES";
    //public static final String[] types = {"TEXT", "TEXT"};
    //private HashMap<Integer, Set<Role>> cache;
    //private static final String[] FOREIGN_KEYS = new String[]{"EmployeeId"};
    //private static final String[] PARENT_TABLE_NAME = new String[]{"EMPLOYEES"};
    //private static final String[] REFERENCES = new String[]{"Id"};
//
    //private enum Columns {
    //   EmployeeId,
    //   Role;
    //}
//
    //public EmployeeRolesDAO(SQLExecutor cursor)throws DalException {
    //    super(cursor,
	//            tableName,
    //            primaryKeys,
    //            types,
    //            "EmployeeId",
    //            "Role"
    //    );
    //    this.cache = new HashMap<>();
    //}
//
    //@Override
    //protected void initTable() throws DalException {
//
    //    StringBuilder query = new StringBuilder();
//
    //    query.append(String.format("CREATE TABLE IF NOT EXISTS %s (\n", TABLE_NAME));
//
    //    for (int i = 0; i < ALL_COLUMNS.length; i++) {
    //        query.append(String.format("\"%s\" %s NOT NULL,\n", ALL_COLUMNS[i], TYPES[i]));
    //    }
//
    //    query.append("PRIMARY KEY(");
    //    for(int i = 0; i < PRIMARY_KEYS.length; i++) {
    //        query.append(String.format("\"%s\"",PRIMARY_KEYS[i]));
    //        if (i != PRIMARY_KEYS.length-1) {
    //            query.append(",");
    //        } else {
    //            query.append("),\n");
    //        }
    //    }
//
    //    for(int i = 0; i < FOREIGN_KEYS.length ; i++){
    //        query.append(String.format("CONSTRAINT FK_%s FOREIGN KEY(\"%s\") REFERENCES \"%s\"(\"%s\")",
    //                FOREIGN_KEYS[i],
    //                FOREIGN_KEYS[i],
    //                PARENT_TABLE_NAME[i],
    //                REFERENCES[i]
    //        ));
    //        if(i != FOREIGN_KEYS.length-1){
    //            query.append(",\n");
    //        } else {
    //            query.append("\n");
    //        }
    //    }
//
    //    query.append(");");
//
    //    try {
    //        cursor.executeWrite(query.toString());
    //    } catch (SQLException e) {
    //        throw new DalException("Failed to initialize table "+TABLE_NAME, e);
    //    }
    //}
//
//
    //private int getHashCode(String id){
    //    return (id).hashCode();
    //}
//
    //Set<Role> getAll(String id) throws DalException {
    //    if (this.cache.get(getHashCode(id))!=null)
    //        return this.cache.get(getHashCode(id));
    //    Set<Role> ans = this.select(id);
    //    this.cache.put(getHashCode(id),ans);
    //    return ans;
    //}
//
    //void create(Employee emp) throws DalException {
    //    try {
    //        if(this.cache.containsKey(getHashCode(emp.getId())))
    //            throw new DalException("Key already exists!");
    //        Set<Role> entries = new HashSet<>();
    //        for(Role str: emp.getRoles()) {
    //            String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s) VALUES('%s','%s')",
    //                    Columns.EmployeeId.name(), Columns.Role.name(),
    //                    emp.getId(), str.name());
    //            if(cursor.executeWrite(queryString) != 1){
    //                throw new RuntimeException("Unexpected error while inserting role");
    //            }
    //            entries.add(str);
    //        }
    //        this.cache.put(getHashCode(emp.getId()),entries );
    //    } catch(SQLException e) {
    //        throw new DalException(e);
    //    }
    //}
//
    //void update(Employee emp) throws DalException {
    //    if(!this.cache.containsKey(getHashCode(emp.getId())))
    //        throw new DalException("Key doesnt exist! Create it first.");
    //    this.delete(emp);
    //    this.create(emp);
    //}
//
    //void delete(Employee emp) throws DalException{
    //    Object[] keys = {emp.getId()};
    //    super.delete(keys);
    //    this.cache.remove(getHashCode(emp.getId()));
    //}
//
    //Set<Role> select(String id) throws DalException {
    //    Object[] keys = {id};
    //    return ((Set<Role>) super.select(keys));
    //}
//
    //protected Set<Role> convertReaderToObject(OfflineResultSet reader) {
    //    Set<Role> ans = new HashSet<>();
    //    try {
//
    //        while (reader.next()) {
    //            String roleString = reader.getString(Columns.Role.name());
    //            if(roleString == null)
    //                continue;
    //            ans.add(Role.valueOf(reader.getString(Columns.Role.name())));
    //        }
    //    }catch (Exception e){ }
    //    return ans;
    //}
//
    //public void clearTable() throws DalException {
    //    super.clearTable();
    //    this.cache.clear();
    //}
}
