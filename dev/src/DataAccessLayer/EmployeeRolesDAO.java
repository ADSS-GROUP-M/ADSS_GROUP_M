package DataAccessLayer;

import employeeModule.BusinessLayer.Employees.Employee;
import employeeModule.BusinessLayer.Employees.Role;

import java.sql.ResultSet;
import java.util.Set;

public class EmployeeRolesDAO {
    public void delete(Employee emp) {
    }

    public Set<Role> getAll(String id) {
        return null;
    }

    public void update(Employee emp) {
    }

    public void create(Employee emp) {
    }
    static EmployeeRolesDAO getInstance() {
        return new EmployeeRolesDAO();
    }
}
