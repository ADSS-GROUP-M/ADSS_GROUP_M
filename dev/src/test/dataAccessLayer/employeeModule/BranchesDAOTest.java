package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Branch;
import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import objects.transportObjects.Site;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.transportModule.ResourceManagementService;
import serviceLayer.transportModule.ServiceFactory;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static dataAccessLayer.DalFactory.TESTING_DB_NAME;

class BranchesDAOTest {

    private BranchesDAO branchesDAO;
    private BranchEmployeesDAO branchEmployeesDAO;
    private ResourceManagementService rms;

    private Branch branch1, branch2, branch3, branch4;

    @BeforeEach
    void setUp() {
        DalFactory.clearTestDB();
        branch1 = new Branch("address1");
        branch2 = new Branch("address2");
        branch3 = new Branch("address3", LocalTime.of(9,0),LocalTime.of(13,0),LocalTime.of(13,0),LocalTime.of(20,0));
        branch4 = new Branch("address1");
        try {
            ServiceFactory serviceFactory = new ServiceFactory(TESTING_DB_NAME);
            rms = serviceFactory.getResourceManagementService();
            rms.addSite(new Site("Zone1","address1","phone1","contact1", Site.SiteType.BRANCH).toJson());
            rms.addSite(new Site("Zone2","address2","phone2","contact2", Site.SiteType.BRANCH).toJson());
            rms.addSite(new Site("Zone3","address3","phone3","contact3", Site.SiteType.BRANCH).toJson());
            branchEmployeesDAO = new BranchEmployeesDAO(TESTING_DB_NAME);
            branchesDAO = new BranchesDAO(TESTING_DB_NAME, branchEmployeesDAO);
            // Inserting only branch1 and branch2 at setUp
            branchesDAO.insert(branch1);
            branchesDAO.insert(branch2);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    void select() {
        try {
            assertDeepEquals(branch1, branchesDAO.select(branch1.address()));
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
            List<Branch> selectedBranches = branchesDAO.selectAll();
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
            branchesDAO.insert(branch3);
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insert_fail() {
        try {
            branchesDAO.insert(branch4);
            fail("Expected an error after attempting to insert a branch with the same address.");
        } catch (DalException ignore) { }
    }

    @Test
    void update() {
        LocalTime updatedMorningStart = LocalTime.of(10,0);
        LocalTime updatedEveningEnd = LocalTime.of(23,0);
        Branch updatedBranch1 = new Branch(branch1.address(),updatedMorningStart,branch1.getMorningEnd(),branch1.getEveningStart(),updatedEveningEnd);
        try {
            branchesDAO.update(updatedBranch1);
            assertDeepEquals(updatedBranch1, branchesDAO.select(updatedBranch1.address()));
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
            branchesDAO.update(invalidBranch);
            fail("Expected an error after attempting to update a non existent branch.");
        } catch (DalException ignore) { }
    }

    @Test
    void delete() {
        try {
            branchesDAO.delete(branch2);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(DalException.class, () -> branchesDAO.select(branch2.address()));
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = new SQLExecutor(TESTING_DB_NAME);
        try {
            OfflineResultSet resultSet = cursor.executeRead("SELECT * FROM Branches WHERE address = '" + branch1.address() + "'");
            resultSet.next();
            assertDeepEquals(branch1, branchesDAO.getObjectFromResultSet(resultSet));
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