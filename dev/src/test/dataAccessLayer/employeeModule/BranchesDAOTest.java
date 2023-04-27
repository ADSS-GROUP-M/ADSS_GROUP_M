package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Branch;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.employeeModule.BranchesDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static dataAccessLayer.DalFactory.TESTING_DB_NAME;

class BranchesDAOTest {

    private BranchesDAO dao;

    private Branch branch1, branch2, branch3, branch4;

    @BeforeEach
    void setUp() {
        branch1 = new Branch("address1");
        branch2 = new Branch("address2");
        branch3 = new Branch("address3", LocalTime.of(9,0),LocalTime.of(13,0),LocalTime.of(13,0),LocalTime.of(20,0));
        branch4 = new Branch("address1");
        try {
            dao = new BranchesDAO(TESTING_DB_NAME);
            dao.clearTable();
            // Inserting only branch1 and branch2 at setUp
            dao.insert(branch1);
            dao.insert(branch2);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        dao.clearTable();
    }

    @Test
    void select() {
        try {
            assertDeepEquals(branch1,dao.select(branch1.address()));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void selectAll() {
        List<Branch> expectedBranches = new ArrayList<>();
        expectedBranches.add(branch1);
        expectedBranches.add(branch2);

        try {
            List<Branch> selectedBranches = dao.selectAll();
            assertEquals(expectedBranches.size(), selectedBranches.size());
            for (int i = 0; i < expectedBranches.size(); i++) {
                assertDeepEquals(expectedBranches.get(i), selectedBranches.get(i));
            }
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insert() {
        try {
            dao.insert(branch3);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insert_fail() {
        try {
            dao.insert(branch4);
            fail("Expected an error after attempting to insert a branch with the same address.");
        } catch (DalException ignore) { }
    }

    @Test
    void update() {
        LocalTime updatedMorningStart = LocalTime.of(10,0);
        LocalTime updatedEveningEnd = LocalTime.of(23,0);
        Branch updatedBranch1 = new Branch(branch1.address(),updatedMorningStart,branch1.getMorningEnd(),branch1.getEveningStart(),updatedEveningEnd);
        try {
            dao.update(updatedBranch1);
            assertDeepEquals(updatedBranch1, dao.select(updatedBranch1.address()));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void update_fail() {
        String invalidAddress = "1945613";
        LocalTime updatedMorningStart = LocalTime.of(10,0);
        Branch invalidBranch = new Branch(invalidAddress,updatedMorningStart,branch1.getMorningEnd(),branch1.getEveningStart(),branch1.getEveningEnd());
        try {
            dao.update(invalidBranch);
            fail("Expected an error after attempting to update a non existent branch.");
        } catch (DalException ignore) { }
    }

    @Test
    void delete() {
        try {
            dao.delete(branch2);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(DalException.class, () -> dao.select(branch2.address()));
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = new SQLExecutor(TESTING_DB_NAME);
        try {
            OfflineResultSet resultSet = cursor.executeRead("SELECT * FROM Branches WHERE address = '" + branch1.address() + "'");
            resultSet.next();
            assertDeepEquals(branch1, dao.getObjectFromResultSet(resultSet));
        } catch (SQLException e) {
            fail(e);
        }
    }

    private void assertDeepEquals(Branch branch1, Branch branch2) {
        assertEquals(branch1.address(), branch2.address());
        assertEquals(branch1.getMorningStart(), branch2.getMorningStart());
        assertEquals(branch1.getMorningEnd(), branch2.getMorningEnd());
        assertEquals(branch1.getEveningStart(), branch2.getEveningStart());
        assertEquals(branch1.getEveningEnd(), branch2.getEveningEnd());
    }
}