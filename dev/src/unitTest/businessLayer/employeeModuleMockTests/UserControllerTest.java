package businessLayer.employeeModuleMockTests;

import businessLayer.employeeModule.Controllers.UserController;
import businessLayer.employeeModule.User;
import dataAccessLayer.employeeModule.UserDAO;
import exceptions.DalException;
import exceptions.EmployeeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    public static final String USERNAME = "user1", PASSWORD = "pass1";
    private UserController controller;
    private UserDAO dao;
    private User user;

    @BeforeEach
    void setUp() {
        dao = mock(UserDAO.class);
        controller = new UserController(dao);
        user = new User(USERNAME,PASSWORD);
    }

    @Test
    void createUser() {
        try{
            when(dao.select(USERNAME)).thenReturn(user);
        } catch (DalException e) {
            fail(e);
        }
        assertDoesNotThrow(() -> controller.createUser(USERNAME,PASSWORD));
    }

    @Test
    void createUserAlreadyExists(){
        try{
            Mockito.doThrow(new DalException("Failed to insert an existing user")).when(dao).insert(user);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(EmployeeException.class, () -> controller.createUser(USERNAME,PASSWORD));
    }

    @Test
    void getUser() {
        try{
            when(dao.select(USERNAME)).thenReturn(user);
        } catch (DalException e) {
            fail(e);
        }
        User fetched = assertDoesNotThrow(() -> controller.getUser(USERNAME));
        assertDeepEquals(user, fetched);
    }

    @Test
    void getUserDoesNotExist(){
        try{
            when(dao.select(USERNAME)).thenThrow(new DalException("Failed to select a non existent user"));
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(EmployeeException.class, () -> controller.getUser(USERNAME));
    }

    private void assertDeepEquals(User user1, User user2) {
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertTrue(user1.getAuthorizations().containsAll(user2.getAuthorizations()));
        assertTrue(user2.getAuthorizations().containsAll(user1.getAuthorizations()));
    }
}
