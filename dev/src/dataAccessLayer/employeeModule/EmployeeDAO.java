package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.DAO;

import java.sql.SQLException;
import java.time.LocalDate;

import java.util.*;

public class EmployeeDAO extends DAO<Employee> {

    private static EmployeeDAO instance;

    private static final String[] types = new String[]{"TEXT", "TEXT", "TEXT", "REAL", "REAL", "REAL", "TEXT", "TEXT", "TEXT"};
    private static final String[] primary_keys = {"id"};
    private HashMap<String, Employee> cache;
    private EmployeeRolesDAO employeeRolesDAO;

    private EmployeeDAO() throws DalException{
        super("employees",
                primary_keys,
                types,
                "id",
                "name",
                "bank_details",
                "hourly_salary_rate",
                "monthly_hours",
                "salary_bonus",
                "employment_date",
                "employment_conditions",
                "details"
        );
        employeeRolesDAO = EmployeeRolesDAO.getInstance();
        cache = new HashMap<>();
    }

    private EmployeeDAO(String dbName) throws DalException{
        super(dbName,
                "employees",
                primary_keys,
                types,
                "id",
                "name",
                "bank_details",
                "hourly_salary_rate",
                "monthly_hours",
                "salary_bonus",
                "employment_date",
                "employment_conditions",
                "details"
        );
        employeeRolesDAO = EmployeeRolesDAO.getInstance();
        cache = new HashMap<>();
    }

    public Employee select(String id) throws DalException {
        return select(Employee.getLookupObject(id));
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Employee select(Employee object) throws DalException {
        if (cache.containsKey(object.getId())) {
            return cache.get(object.getId());
        }
        String query = String.format("""
                SELECT * FROM %s
                INNER JOIN EMPLOYEE_ROLES ON EMPLOYEE_ROLES.%s = %s.%s
                WHERE %s = '%s'
                """,
                TABLE_NAME, "employee_id",
                TABLE_NAME, "id",
                PRIMARY_KEYS[0], object.getId());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select Employee from database");
        }
        Employee ans = getObjectFromResultSet(resultSet);
        cache.put(object.getId(),ans);
        return ans;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Employee> selectAll() throws DalException {
        String query = String.format("""
                SELECT * FROM %s
                INNER JOIN EMPLOYEE_ROLES ON EMPLOYEE_ROLES.%s = %s.%s
                """,
                TABLE_NAME,
                "employee_id",
                TABLE_NAME, "id");
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all Employees from database");
        }
        List<Employee> ans = new LinkedList<>();
        while (resultSet.next()) {
            Employee emp = getObjectFromResultSet(resultSet);
            ans.add(emp);
            cache.put(emp.getId(), emp);
        }
        return ans;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Employee object) throws DalException {
        try {
            this.employeeRolesDAO.create(object);
            String queryString = String.format("""
                INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES ('%s','%s','%s',%f,%f,%f,'%s','%s','%s')
                        """, TABLE_NAME, ALL_COLUMNS[0], ALL_COLUMNS[1], ALL_COLUMNS[2], ALL_COLUMNS[3],
                    ALL_COLUMNS[4], ALL_COLUMNS[5], ALL_COLUMNS[6], ALL_COLUMNS[7], ALL_COLUMNS[8],
                    object.getId(), object.getName(), object.getBankDetails(), object.getHourlySalaryRate(),
                    object.getMonthlyHours(), object.getSalaryBonus(), object.getEmploymentDate().toString(),
                    object.getEmploymentConditions(), object.getDetails());
            cursor.executeWrite(queryString);
            cache.put(object.getId(), object);
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    public void update(Employee emp) throws DalException {
        if(!cache.containsValue(emp)) {
            throw new DalException("Object doesn't exist in the database! Create it first.");
        }
        if(!cache.containsKey(emp.getId()) || cache.get(emp.getId())!= emp) {
            throw new DalException("Cannot change primary key of an object. You must delete it and then create a new one.");
        }
        Exception ex = null;
        try {
            Object[] key = {emp.getId()};
            this.employeeRolesDAO.update(emp);
            String queryString = String.format("""
                                    UPDATE %s
                                    SET %s = '%s',%s = '%s',%s = %f,%s = %f,%s = %f,%s = '%s',%s = '%s',%s = '%s'
                                    WHERE %s = '%s'
                                    """,
                    TABLE_NAME,
                    ALL_COLUMNS[1], emp.getName(),
                    ALL_COLUMNS[2], emp.getBankDetails(),
                    ALL_COLUMNS[3], emp.getHourlySalaryRate(),
                    ALL_COLUMNS[4], emp.getMonthlyHours(),
                    ALL_COLUMNS[5], emp.getSalaryBonus(),
                    ALL_COLUMNS[6], emp.getEmploymentDate().toString(),
                    ALL_COLUMNS[7], emp.getEmploymentConditions(),
                    ALL_COLUMNS[8], emp.getDetails(),
                    PRIMARY_KEYS[0], emp.getId());
            cursor.executeWrite(queryString);
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }
    public void delete(Employee emp) throws DalException{
        cache.remove(emp.getId());
        Object[] keys = {emp.getId()};
        this.employeeRolesDAO.delete(emp);
    }

    @Override
    protected Employee getObjectFromResultSet(OfflineResultSet resultSet){
        Employee ans = new Employee(
                resultSet.getString(ALL_COLUMNS[0]),
                resultSet.getString(ALL_COLUMNS[1]),
                resultSet.getString(ALL_COLUMNS[2]),
                resultSet.getDouble(ALL_COLUMNS[3]),
                LocalDate.parse(resultSet.getString(ALL_COLUMNS[6])),
                resultSet.getString(ALL_COLUMNS[7]),
                resultSet.getString(ALL_COLUMNS[8])
        );
        double monthlyHours = resultSet.getDouble(ALL_COLUMNS[4]);
        double salaryBonus = resultSet.getDouble(ALL_COLUMNS[5]);
        ans.setMonthlyHours(monthlyHours);
        ans.setSalaryBonus(salaryBonus);

        Set<Role> roles = null;
        try {
            roles = this.employeeRolesDAO.getAll(ans.getId());
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        ans.setRoles(roles);
        return ans;
    }

    public static EmployeeDAO getInstance() throws DalException{
        if (instance == null) {
            instance = new EmployeeDAO();
        }
        return instance;
    }

    public static EmployeeDAO getTestingInstance(String dbName) throws DalException{
        if (instance == null) {
            instance = new EmployeeDAO(dbName);
        }
        return instance;
    }
    
    public static void deleteInstance() {
        instance = null;
    }

    public void deleteAll() {
        cache.clear();
        try {
            cursor.executeWrite("DELETE FROM EMPLOYEE_ROLES");
            cursor.executeWrite("DELETE FROM EMPLOYEES");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


