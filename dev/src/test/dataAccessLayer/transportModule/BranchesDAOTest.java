package dataAccessLayer.transportModule;

import businessLayer.employeeModule.Branch;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.employeeModule.BranchesDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchesDAOTest {

    private BranchesDAO dao;

    private Branch branch;

    @BeforeEach
    void setUp() {
        try {
            dao = new BranchesDAO("TestingDB.db");
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        branch = new Branch("address1");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void select() {
    }

    @Test
    void selectAll() {
    }

    @Test
    void insert() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getObjectFromResultSet() {
    }
}