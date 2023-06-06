package businessLayer.employeeModuleMockTests;

import businessLayer.employeeModule.Controllers.EmployeesController;
import businessLayer.employeeModule.Controllers.ShiftsController;
import businessLayer.employeeModule.Employee;
import dataAccessLayer.employeeModule.BranchesDAO;
import dataAccessLayer.employeeModule.EmployeeDAO;
import exceptions.DalException;
import exceptions.EmployeeException;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeesControllerTest {
    public static final String EMPLOYEE_ID = "1";
    public static final String BRANCH_ID = "testBranch";
    private EmployeesController controller;
    private EmployeeDAO dao;
    private BranchesDAO branchesDAO;
    private ShiftsController shiftsController;
    private Employee employee;


    @BeforeEach
    void setUp() {
        dao = mock(EmployeeDAO.class);
        branchesDAO = mock(BranchesDAO.class);
        shiftsController = mock(ShiftsController.class);
        controller = new EmployeesController(shiftsController,branchesDAO,dao);
        employee = new Employee("Yossi Biton",EMPLOYEE_ID,"Hapoalim 23 250",
                30, LocalDate.of(2020,2,2),"Employment Conditions","Details");
    }

    @Test
    void recruitEmployee() {
        try{
            when(dao.select(EMPLOYEE_ID)).thenReturn(employee);
        } catch (DalException e) {
            fail(e);
        }
        assertDoesNotThrow(() -> controller.recruitEmployee(BRANCH_ID,employee.getName(),employee.getId(),employee.getBankDetails(),
                employee.getHourlySalaryRate(), employee.getEmploymentDate(),employee.getEmploymentConditions(),employee.getDetails()));
    }

    @Test
    void recruitEmployeeAlreadyExists(){
        try{
            Mockito.doThrow(new DalException("Failed to insert an existing employee")).when(dao).insert(employee);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(EmployeeException.class, () -> controller.recruitEmployee(BRANCH_ID,employee.getName(),employee.getId(),employee.getBankDetails(),
                employee.getHourlySalaryRate(), employee.getEmploymentDate(),employee.getEmploymentConditions(),employee.getDetails()));
    }

    @Test
    void updateEmployee() {
        try{
            when(dao.select(EMPLOYEE_ID)).thenReturn(employee);
        } catch (DalException e) {
            fail(e);
        }
        String newDetails = "New details";
        String newBankDetails = "Leumi 15 112";
        String newEmploymentConditions = "New employment conditions";
        assertDoesNotThrow(() -> controller.updateEmployeeDetails(employee.getId(), newDetails));
        assertDoesNotThrow(() -> controller.updateEmployeeBankDetails(employee.getId(), newBankDetails));
        assertDoesNotThrow(() -> controller.updateEmployeeEmploymentConditions(employee.getId(), newEmploymentConditions));
    }

    @Test
    void updateEmployeeDoesNotExist(){
        try{
            when(dao.select(EMPLOYEE_ID)).thenThrow(new DalException("Failed to select a non existent employee"));
        } catch (DalException e) {
            fail(e);
        }
        String newDetails = "New details";
        String newBankDetails = "Leumi 15 112";
        String newEmploymentConditions = "New employment conditions";
        assertThrows(EmployeeException.class, () -> controller.updateEmployeeDetails(employee.getId(), newDetails));
        assertThrows(EmployeeException.class, () -> controller.updateEmployeeBankDetails(employee.getId(), newBankDetails));
        assertThrows(EmployeeException.class, () -> controller.updateEmployeeEmploymentConditions(employee.getId(), newEmploymentConditions));
    }

    @Test
    void getEmployee() {
        try{
            when(dao.select(EMPLOYEE_ID)).thenReturn(employee);
        } catch (DalException e) {
            fail(e);
        }
        Employee fetched = assertDoesNotThrow(() -> controller.getEmployee(EMPLOYEE_ID));
        assertDeepEquals(employee, fetched);
    }

    @Test
    void getEmployeeDoesNotExist(){
        try{
            when(dao.select(EMPLOYEE_ID)).thenThrow(new DalException("Failed to select a non existent employee"));
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(EmployeeException.class, () -> controller.getEmployee(EMPLOYEE_ID));
    }

    @Test
    void getBranchEmployees() {
        Employee employee2 = new Employee("Avraham Cohen","2","Leumi 102 25",25,LocalDate.of(2020,3,3),
                "Employment Conditions 2","Details 2");
        List<String> employeeIds = List.of(EMPLOYEE_ID, employee2.getId());
        try{
            when(branchesDAO.selectBranchEmployees(BRANCH_ID,employeeIds)).thenReturn(List.of(new Pair<>(BRANCH_ID,EMPLOYEE_ID), new Pair<>(BRANCH_ID,employee2.getId())));
            when(dao.select(EMPLOYEE_ID)).thenReturn(employee);
            when(dao.select(employee2.getId())).thenReturn(employee2);
        } catch (DalException e) {
            fail(e);
        }
        List<Employee> fetched = assertDoesNotThrow(() -> controller.getBranchEmployees(BRANCH_ID,List.of(EMPLOYEE_ID,employee2.getId())));
        assertEquals(2, fetched.size());
        assertDeepEquals(employee, fetched.get(0));
        assertDeepEquals(employee2, fetched.get(1));
    }

    private void assertDeepEquals(Employee employee1, Employee employee2) {
        assertEquals(employee1.getId(), employee2.getId());
        assertEquals(employee1.getName(), employee2.getName());
        assertEquals(employee1.getHourlySalaryRate(), employee2.getHourlySalaryRate());
        assertTrue(employee1.getRoles().containsAll(employee2.getRoles()));
        assertTrue(employee2.getRoles().containsAll(employee1.getRoles()));
        assertEquals(employee1.getMonthlyHours(), employee2.getMonthlyHours());
        assertEquals(employee1.getSalaryBonus(), employee2.getSalaryBonus());
        assertEquals(employee1.getEmploymentDate(), employee2.getEmploymentDate());
        assertEquals(employee1.getEmploymentConditions(), employee2.getEmploymentConditions());
        assertEquals(employee1.getDetails(), employee2.getDetails());
    }
}
