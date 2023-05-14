package businessLayer.employeeModuleMockTests;

import businessLayer.employeeModule.Controllers.ShiftsController;
import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.Role;
import businessLayer.employeeModule.Shift;
import dataAccessLayer.employeeModule.ShiftDAO;
import exceptions.DalException;
import exceptions.EmployeeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShiftsControllerTest {
    private final String BRANCH_ID = "testBranch";
    private final LocalDate SHIFT_DATE = LocalDate.of(2020,2,2);
    private final Shift.ShiftType SHIFT_TYPE = Shift.ShiftType.Morning;
    private ShiftDAO dao;
    private ShiftsController controller;
    private Shift shift;


    @BeforeEach
    void setUp() {
        dao = mock(ShiftDAO.class);
        controller = new ShiftsController(dao);
        shift = new Shift(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE);
    }

    @Test
    void createShift() {
        try{
            when(dao.select(BRANCH_ID,SHIFT_DATE, SHIFT_TYPE)).thenReturn(shift);
        } catch (DalException e) {
            fail(e);
        }
        assertDoesNotThrow(() -> controller.createShift(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE));
    }

    @Test
    void createShiftAlreadyExists(){
        try{
            Mockito.doThrow(new DalException("Failed to insert an existing shift")).when(dao).insert(shift);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(EmployeeException.class, () -> controller.createShift(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE));
    }

    @Test
    void updateShift() {
        Employee testEmployee = new Employee("Yossi Biton","1","Hapoalim 23 250",
                30, LocalDate.of(2020,2,2),"Employment Conditions","Details");
        try{
            when(dao.select(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE)).thenReturn(shift);
            testEmployee.addRole(Role.GeneralWorker);
            controller.requestShift(testEmployee,BRANCH_ID,SHIFT_DATE,SHIFT_TYPE,Role.GeneralWorker);
        } catch (DalException | EmployeeException e) {
            fail(e);
        }
         assertDoesNotThrow(() -> controller.setShiftNeededAmount(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE,Role.GeneralWorker,3));
         assertDoesNotThrow(() -> controller.setShiftNeededAmount(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE,Role.Cashier,2));
         assertDoesNotThrow(() -> controller.setShiftEmployees(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE,Role.GeneralWorker, List.of(testEmployee)));
    }

    @Test
    void updateShiftDoesNotExist(){
        try{
            when(dao.select(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE)).thenThrow(new DalException("Failed to select a non existent shift"));
        } catch (DalException e) {
            fail(e);
        }
        Employee testEmployee = new Employee("Yossi Biton","1","Hapoalim 23 250",
                30, LocalDate.of(2020,2,2),"Employment Conditions","Details");
        testEmployee.addRole(Role.GeneralWorker);
        assertThrows(EmployeeException.class, () -> controller.setShiftNeededAmount(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE,Role.GeneralWorker,3));
        assertThrows(EmployeeException.class, () -> controller.setShiftNeededAmount(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE,Role.Cashier,2));
        assertThrows(EmployeeException.class, () -> controller.setShiftEmployees(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE,Role.GeneralWorker, List.of(testEmployee)));
    }

    @Test
    void getShift() {
        try{
            when(dao.select(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE)).thenReturn(shift);
        } catch (DalException e) {
            fail(e);
        }
        Shift fetched = assertDoesNotThrow(() -> controller.getShift(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE));
        assertDeepEquals(shift, fetched);
    }

    @Test
    void getShiftDoesNotExist(){
        try{
            when(dao.select(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE)).thenThrow(new DalException("Failed to select a non existent shift"));
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(EmployeeException.class, () -> controller.getShift(BRANCH_ID,SHIFT_DATE,SHIFT_TYPE));
    }

    private void assertDeepEquals(Shift shift1, Shift shift2) {
        assertEquals(shift1.getBranch(), shift2.getBranch());
        assertEquals(shift1.getShiftDate(), shift2.getShiftDate());
        assertEquals(shift1.getShiftType(), shift2.getShiftType());
        assertEquals(shift1.getIsApproved(), shift2.getIsApproved());
        assertTrue(shift1.getShiftActivities().containsAll(shift2.getShiftActivities()));
        assertTrue(shift2.getShiftActivities().containsAll(shift1.getShiftActivities()));
        assertTrue(shift1.getShiftCancels().containsAll(shift2.getShiftCancels()));
        assertTrue(shift2.getShiftCancels().containsAll(shift1.getShiftCancels()));
        assertEquals(shift1.getShiftRequests(), shift2.getShiftRequests());
        assertEquals(shift1.getShiftWorkers(), shift2.getShiftWorkers());
        assertEquals(shift1.getNeededRoles(), shift2.getNeededRoles());
    }
}
