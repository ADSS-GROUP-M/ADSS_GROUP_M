package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Branch;
import dataAccessLayer.DalFactory;
import exceptions.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.transportModule.SitesDAO;
import objects.transportObjects.Site;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class BranchesDAOTest {

    private BranchesDAO branchesDAO;
    private BranchEmployeesDAO branchEmployeesDAO;
    private SitesDAO sitesDAO;

    private Branch branch1, branch2, branch3, branch4;
    private DalFactory factory;

    @BeforeEach
    void setUp() {
        DalFactory.clearTestDB();
        Site site1 = new Site("testbranch1", "address1", "zone1", "name1","phone1", Site.SiteType.BRANCH);
        Site site2 = new Site("testbranch2", "address2", "zone2", "name2","phone2", Site.SiteType.BRANCH);
        Site site3 = new Site("testbranch3", "address3", "zone3", "name3","phone3", Site.SiteType.BRANCH);

        branch1 = new Branch("testbranch1");
        branch2 = new Branch("testbranch2");
        branch3 = new Branch("testbranch3", LocalTime.of(9,0),LocalTime.of(13,0),LocalTime.of(13,0),LocalTime.of(20,0));
        branch4 = new Branch("testbranch2"); // Same name as branch2

        try {
            factory = new DalFactory(TESTING_DB_NAME);
            sitesDAO = factory.sitesDAO();
            sitesDAO.insert(site1);
            sitesDAO.insert(site2);
            sitesDAO.insert(site3);
            branchEmployeesDAO = factory.branchEmployeesDAO();
            branchesDAO = factory.branchesDAO();
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
            assertDeepEquals(branch1, branchesDAO.select(branch1.name()));
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
            fail("Expected an error after attempting to insert a branch with the same name.");
        } catch (DalException ignore) { }
    }

    @Test
    void update() {
        LocalTime updatedMorningStart = LocalTime.of(10,0);
        LocalTime updatedEveningEnd = LocalTime.of(23,0);
        Branch updatedBranch1 = new Branch(branch1.name(),updatedMorningStart,branch1.getMorningEnd(),branch1.getEveningStart(),updatedEveningEnd);
        try {
            branchesDAO.update(updatedBranch1);
            assertDeepEquals(updatedBranch1, branchesDAO.select(updatedBranch1.name()));
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
        assertThrows(DalException.class, () -> branchesDAO.select(branch2.name()));
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = factory.cursor();
        try {
            OfflineResultSet resultSet = cursor.executeRead("SELECT * FROM Branches WHERE name = '" + branch1.name() + "'");
            resultSet.next();
            assertDeepEquals(branch1, branchesDAO.getObjectFromResultSet(resultSet));
        } catch (SQLException e) {
            fail(e);
        }
    }

    private void assertDeepEquals(Branch branch1, Branch branch2) {
        assertEquals(branch1.name(), branch2.name());
        assertEquals(branch1.getMorningStart(), branch2.getMorningStart());
        assertEquals(branch1.getMorningEnd(), branch2.getMorningEnd());
        assertEquals(branch1.getEveningStart(), branch2.getEveningStart());
        assertEquals(branch1.getEveningEnd(), branch2.getEveningEnd());
    }
}