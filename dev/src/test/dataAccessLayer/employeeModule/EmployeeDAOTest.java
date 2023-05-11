package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    private EmployeeDAO dao;
    private Employee employee1, employee2, employee3, employee4;
    private DalFactory factory;

    @BeforeEach
    void setUp() {
        employee1 = new Employee("Test", "12345", "TestBank", 21, LocalDate.now(),"","");
        employee1.addRole(Role.GeneralWorker);
        employee1.addRole(Role.Cashier);
        employee2 = new Employee("Test2", "123456", "TestBank2", 22, LocalDate.now(),"Test","Testing");
        employee3 = new Employee("Test3", "1234567", "TestBank3", 23, LocalDate.now(),"","");
        employee4 = new Employee("Test4", "12345", "TestBank4", 24, LocalDate.now(),"","");
        try {
            factory = new DalFactory(TESTING_DB_NAME);

            dao = factory.employeeDAO();
            dao.clearTable();
            // Inserting only employee1 and employee2 at setUp
            dao.insert(employee1);
            dao.insert(employee2);
        } catch (DalException | RuntimeException e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void select() {
        try {
            assertDeepEquals(employee1,dao.select(employee1.getId()));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void selectAll() {
        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(employee1);
        expectedEmployees.add(employee2);

        try {
            List<Employee> selectedEmployees = dao.selectAll();
            assertEquals(expectedEmployees.size(), selectedEmployees.size());
            for (int i = 0; i < expectedEmployees.size(); i++) {
                assertDeepEquals(expectedEmployees.get(i), selectedEmployees.get(i));
            }
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insert() {
        try {
            dao.insert(employee3);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insert_fail() {
        try {
            dao.insert(employee4);
            fail("Expected an error after attempting to insert an employee with the same employee id.");
        } catch (DalException ignore) { }
    }

    @Test
    void update() {
        String updatedBankDetails = "TestBank3";
        double updatedHourlyRate = 27.0;
        Employee updatedEmployee1 = new Employee(employee1.getName(),employee1.getId(),updatedBankDetails,updatedHourlyRate,employee1.getEmploymentDate(),employee1.getEmploymentConditions(),employee1.getDetails());
        try {
            dao.update(updatedEmployee1);
            assertDeepEquals(updatedEmployee1, dao.select(updatedEmployee1.getId()));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void update_fail() {
        String invalidId = "1945613";
        String updatedBankDetails = "TestBank3";
        double updatedHourlyRate = 27.0;
        Employee updatedEmployee1 = new Employee(employee1.getName(),invalidId,updatedBankDetails,updatedHourlyRate,employee1.getEmploymentDate(),employee1.getEmploymentConditions(),employee1.getDetails());
        try {
            dao.update(updatedEmployee1);
            fail("Expected an error after attempting to update a non existent employee.");
        } catch (DalException ignore) { }
    }

    @Test
    void delete() {
        try {
            dao.delete(employee2);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(DalException.class, () -> dao.select(employee2.getId()));
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = factory.cursor();
        try {
            OfflineResultSet resultSet = cursor.executeRead("SELECT * FROM Employees WHERE id = '" + employee1.getId() + "'");
            resultSet.next();
            assertDeepEquals(employee1, dao.getObjectFromResultSet(resultSet));
        } catch (SQLException e) {
            fail(e);
        }
    }

    private void assertDeepEquals(Employee employee1, Employee employee2) {
        assertEquals(employee1.getId(), employee2.getId());
        assertEquals(employee1.getName(), employee2.getName());
        assertEquals(employee1.getBankDetails(), employee2.getBankDetails());
        assertEquals(employee1.getHourlySalaryRate(), employee2.getHourlySalaryRate());
        assertEquals(employee1.getEmploymentDate(), employee2.getEmploymentDate());
        assertEquals(employee1.getEmploymentConditions(), employee2.getEmploymentConditions());
        assertEquals(employee1.getDetails(), employee2.getDetails());
    }
}