package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.User;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private UserDAO userDAO;
    private UserAuthorizationsDAO userAuthorizationsDAO;

    private User user1, user2, user3, user4;
    private DalFactory factory;

    @BeforeEach
    void setUp() {
        user1 = new User("username1","password1");
        user2 = new User("username2","password2");
        user3 = new User("username3","password3");
        user4 = new User("username1","password4");
        user1.authorize(Authorization.Cashier);
        user1.authorize(Authorization.Storekeeper);
        user3.authorize(Authorization.ShiftManager);

        try {
            factory = new DalFactory(TESTING_DB_NAME);
            userDAO = factory.userDAO();
            // Inserting only user1, user3 at setUp
            userDAO.insert(user1);
            userDAO.insert(user3);
            userAuthorizationsDAO = factory.userAuthorizationsDAO();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void insert() {
        try {
            userDAO.insert(user2);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void insert_fail() {
        try {
            userDAO.insert(user4);
            fail("Expected an error after attempting to insert a user with the same username.");
        } catch (DalException ignore) { }
    }

    @Test
    void update() {
        String updatedPassword1 = "updatedPassword1";
        User updatedUser1 = new User(user1.getUsername(),updatedPassword1);
        try {
            userDAO.update(updatedUser1);
            assertDeepEquals(updatedUser1, userDAO.select(updatedUser1.getUsername()));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void update_fail() {
        String invalidUsername = "1945613";
        String updatedPassword = "updatedPassword";
        User invalidUser = new User(invalidUsername,updatedPassword);
        try {
            userDAO.update(invalidUser);
            fail("Expected an error after attempting to update a non existent user.");
        } catch (DalException ignore) { }
    }

    @Test
    void delete() {
        try {
            userDAO.delete(user1);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertThrows(DalException.class, () -> userDAO.select(user1.getUsername()));
        //try {
        //    assertEquals(null, userDAO.select(user2.getUsername())); // Should probably be updated if we switch to the other DAO implementation
        //} catch (DalException e) {}
    }

    @Test
    void select() {
        try {
            assertDeepEquals(user1, userDAO.select(user1.getUsername()));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void selectAll() {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user1);
        expectedUsers.add(user3);

        try {
            List<User> selectedUsers = userDAO.selectAll();
            assertEquals(expectedUsers.size(), selectedUsers.size());
            for (int i = 0; i < expectedUsers.size(); i++) {
                assertDeepEquals(expectedUsers.get(i), selectedUsers.get(i));
            }
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    @Test
    void convertReaderToObject() {
        SQLExecutor cursor = factory.cursor();
        try {
            OfflineResultSet resultSet = cursor.executeRead("SELECT * FROM Users WHERE username = '" + user1.getUsername() + "'");
            resultSet.next();
            assertDeepEquals(user1, userDAO.getObjectFromResultSet(resultSet));
        } catch (SQLException e) {
            fail(e.getMessage(),e.getCause());
        }
    }

    private void assertDeepEquals(User user1, User user2) {
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertTrue(user1.getAuthorizations().containsAll(user2.getAuthorizations()));
        assertTrue(user2.getAuthorizations().containsAll(user1.getAuthorizations()));
    }
}