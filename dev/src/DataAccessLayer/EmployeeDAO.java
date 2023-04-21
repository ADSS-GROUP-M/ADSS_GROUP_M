package DataAccessLayer;

import employeeModule.BusinessLayer.Employees.Employee;
import employeeModule.BusinessLayer.Employees.Role;
import employeeModule.BusinessLayer.Employees.Shift;
import employeeModule.BusinessLayer.Employees.Shift.ShiftType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.*;

public class EmployeeDAO extends DAO {

    private static EmployeeDAO instance;
    private HashMap<Integer, Employee> cache;
    private EmployeeRolesDAO employeeRolesDAO;

    public enum Columns {
        Id,
        Name,
        BankDetails,
        HourlySalaryRate,
        MonthlyHours,
        SalaryBonus,
        EmploymentDate,
        EmploymentConditions,
        Details;

    }

    //needed roles HashMap<Role,Integer>, shiftRequests HashMap<Role,List<Employees>>, shiftWorkers Map<Role,List<Employees>>, cancelCardApplies List<String>, shiftActivities List<String>.
    private EmployeeDAO() throws Exception {
        super("EMPLOYEES", new String[]{Columns.Id.name()});
        employeeRolesDAO = EmployeeRolesDAO.getInstance();
        this.cache = new HashMap<>();
    }

    public static EmployeeDAO getInstance() throws Exception {
        if (instance == null)
            instance = new EmployeeDAO();
        return instance;

    }
    private int getHashCode(String id){
        return (id).hashCode();
    }
    public void create(Employee emp) throws SQLException {
        try {
            this.employeeRolesDAO.create(emp);
            String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s, %s, %s, %s,%s,%s) VALUES(?,?,?,?,?,?,?,?,?)",
                    Columns.Id.name(), Columns.Name.name(), Columns.BankDetails.name(),Columns.HourlySalaryRate.name(),Columns.MonthlyHours.name()
                    ,Columns.SalaryBonus.name(),Columns.EmploymentDate.name(),Columns.EmploymentConditions.name(),Columns.Details.name());
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, emp.getId());
            ptmt.setString(2, emp.getName());
            ptmt.setString(3, emp.getBankDetails());
            ptmt.setInt(4, (int)emp.getHourlySalaryRate());
            ptmt.setInt(5, (int)emp.getMonthlyHours());
            ptmt.setInt(6, (int)emp.getSalaryBonus());
            ptmt.setString(7, formatLocalDate(emp.getEmploymentDate()));
            ptmt.setString(8, emp.getEmploymentConditions());
            ptmt.setString(9, emp.getDetails());
            ptmt.executeUpdate();
            this.cache.put(getHashCode(emp.getId()), emp);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Employee get(String id) throws Exception {
        if (this.cache.get(getHashCode(id))!=null)
            return this.cache.get(getHashCode(id));
        Employee ans = this.select(id);
        this.cache.put(getHashCode(id),ans);
        return ans;
    }

    public List<Employee> getAll() throws Exception {
        List<Employee> list = new LinkedList<>();
        for(Object o: selectAll()) {
            if(!(o instanceof Employee))
                throw new Exception("Something went wrong");
            Employee emp = ((Employee)o);
            if (this.cache.get(getHashCode(emp.getId())) != null)
                list.add(this.cache.get(getHashCode(emp.getId())));
            else {
                list.add(emp);
                this.cache.put(getHashCode(emp.getId()), emp);
            }
        }
        return list;
    }

    public void update(Employee emp) throws Exception {
        if(!this.cache.containsValue(emp))
            throw new Exception("Object doesn't exist in the database! Create it first.");
        if(!this.cache.containsKey(getHashCode(emp.getId())) || this.cache.get(getHashCode(emp.getId()))!= emp)
            throw new Exception("Cannot change primary key of an object. You must delete it and then create a new one.");
        Exception ex = null;
        try {
            Object[] key = {emp.getId()};
            this.employeeRolesDAO.update(emp);
            String queryString = String.format("UPDATE "+TABLE_NAME+" SET %s = ? , %s = ? , %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE",
                    Columns.Id.name(), Columns.Name.name(), Columns.BankDetails.name(),Columns.HourlySalaryRate.name(),Columns.MonthlyHours.name()
                    ,Columns.SalaryBonus.name(),Columns.EmploymentDate.name(),Columns.EmploymentConditions.name(),Columns.Details.name());
            queryString = queryString.concat(createConditionForPrimaryKey(key));
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, emp.getId());
            ptmt.setString(2, emp.getName());
            ptmt.setString(3, emp.getBankDetails());
            ptmt.setInt(4, (int)emp.getHourlySalaryRate());
            ptmt.setInt(5, (int)emp.getMonthlyHours());
            ptmt.setInt(6, (int)emp.getSalaryBonus());
            ptmt.setString(7, formatLocalDate(emp.getEmploymentDate()));
            ptmt.setString(8, emp.getEmploymentConditions());
            ptmt.setString(9, emp.getDetails());
            ptmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void delete(Employee emp) {
        this.cache.remove(getHashCode(emp.getId()));
        Object[] keys = {emp.getId()};
        this.employeeRolesDAO.delete(emp);
        super.delete(keys);
    }

    Employee select(String id) throws Exception {
        Object[] keys = {id};
        return ((Employee) super.select(keys));
    }

    protected Employee convertReaderToObject(ResultSet reader) {
        Employee ans = null;
        try{
            LocalDate dt = LocalDate.parse(reader.getString(Columns.EmploymentDate.name()));
            String id = reader.getString(Columns.Id.name());
            String name = reader.getString(Columns.Name.name());
            String bankDetails = reader.getString(Columns.BankDetails.name());
            int hourlySalaryRate = reader.getInt(Columns.HourlySalaryRate.name());
            int salaryBonus = reader.getInt(Columns.SalaryBonus.name());
            int monthlyHours = reader.getInt(Columns.MonthlyHours.name());
            String details = reader.getString(Columns.Details.name());
            String employmentConditions = reader.getString(Columns.EmploymentConditions.name());

            ans = new Employee(name,id,hourlySalaryRate,bankDetails,dt,employmentConditions,details);
            ans.setMonthlyHours(monthlyHours);
            ans.setSalaryBonus(salaryBonus);

            Set<Role> roles = this.employeeRolesDAO.getAll(id);
            ans.setRoles(roles);

        } catch (Exception throwables) {
           // throwables.printStackTrace();
        }
        return ans;
    }

}


