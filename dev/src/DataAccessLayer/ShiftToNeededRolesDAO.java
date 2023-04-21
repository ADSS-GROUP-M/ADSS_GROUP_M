package DataAccessLayer;

import employeeModule.BusinessLayer.Employees.Employee;
import employeeModule.BusinessLayer.Employees.Role;
import employeeModule.BusinessLayer.Employees.Shift;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

class ShiftToNeededRolesDAO {
    private static ShiftToNeededRolesDAO instance;
    private ShiftToNeededRolesDAO(){

    }
    static ShiftToNeededRolesDAO getInstance() {
       if(instance == null)
          instance = new ShiftToNeededRolesDAO();
       return instance;
    }

    Map<Role, Integer> getAll(LocalDate dt, Shift.ShiftType st, String branch) throws Exception {
        return null;
    }

   public void create(Shift shift, String branch) throws SQLException {

   }

    public void update(Shift s, String branch) {
    }

    public void delete(Shift s, String branch) {
    }
}
