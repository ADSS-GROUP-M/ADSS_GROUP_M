package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.transportModule.abstracts.DAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EmployeeDAO extends DAO<Employee> {

    private static final String[] types = new String[]{"TEXT", "TEXT", "TEXT", "REAL", "REAL", "REAL", "TEXT", "TEXT", "TEXT"};
    private static final String[] primary_keys = {"Id"};
    private static final String tableName = "EMPLOYEES";
    private final EmployeeRolesDAO employeeRolesDAO;

    public EmployeeDAO(SQLExecutor cursor, EmployeeRolesDAO employeeRolesDAO) throws DalException{
        super(cursor,
                tableName,
                types,
                primary_keys,
                "Id",
                "Name",
                "BankDetails",
                "HourlySalaryRate",
                "MonthlyHours",
                "SalaryBonus",
                "EmploymentDate",
                "EmploymentConditions",
                "Details"
        );
        initTable();
        this.employeeRolesDAO = employeeRolesDAO;
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
        if (cache.contains(object)) {
            return cache.get(object);
        }
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, PRIMARY_KEYS[0], object.getId());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select Employee from database");
        }

        Employee ans;
        if (resultSet.next()) {
            ans = getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("No truck with id " + object.getId() + " was found");//TODO: Is this a mistake?
        }
        cache.put(ans);
        return ans;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Employee> selectAll() throws DalException {
        String query = "SELECT * FROM " +  TABLE_NAME;
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
            cache.put(emp);
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
            String queryString = String.format("""
                INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES ('%s','%s','%s',%f,%f,%f,'%s','%s','%s')
                        """, TABLE_NAME, ALL_COLUMNS[0], ALL_COLUMNS[1], ALL_COLUMNS[2], ALL_COLUMNS[3],
                    ALL_COLUMNS[4], ALL_COLUMNS[5], ALL_COLUMNS[6], ALL_COLUMNS[7], ALL_COLUMNS[8],
                    object.getId(), object.getName(), object.getBankDetails(), object.getHourlySalaryRate(),
                    object.getMonthlyHours(), object.getSalaryBonus(), object.getEmploymentDate().toString(),
                    object.getEmploymentConditions(), object.getDetails());
            cursor.executeWrite(queryString);
            cache.put(object);
            this.employeeRolesDAO.create(object); // Should insert the dependent values only after creating the employee in the database
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    public void update(Employee emp) throws DalException {
        try {
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
            if (cursor.executeWrite(queryString) == 0)
                throw new DalException("No employee with id " + emp.getId() + " was found");
            cache.put(emp);
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }

    /**
     * @param emp@throws DalException if an error occurred while trying to delete the employee
     */
    @Override
    public void delete(Employee emp) throws DalException{
        cache.remove(emp);
        Object[] keys = {emp.getId()};
        this.employeeRolesDAO.delete(emp);

        String query = String.format("DELETE FROM %s WHERE id = '%s';", TABLE_NAME, emp.getId());
        try {
            if (cursor.executeWrite(query) == 0)
                throw new DalException("No employee with id " + emp.getId() + " was found");
        } catch (SQLException e) {
            throw new DalException("Failed to delete Employee", e);
        }
    }

    @Override
    public boolean exists(Employee object) throws DalException {
        // TODO: implement
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    protected Employee getObjectFromResultSet(OfflineResultSet resultSet){
        Employee ans = new Employee(
                resultSet.getString(ALL_COLUMNS[1]),
                resultSet.getString(ALL_COLUMNS[0]),
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

    @Override
    public void clearTable() {
        try {
            employeeRolesDAO.clearTable();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        super.clearTable();
    }
}


