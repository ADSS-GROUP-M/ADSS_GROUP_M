package dataAccessLayer;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.employeeModule.BranchesDAO;
import dataAccessLayer.employeeModule.EmployeeDAO;
import dataAccessLayer.employeeModule.ShiftDAO;
import dataAccessLayer.employeeModule.UserDAO;
import dataAccessLayer.transportModule.*;

public class DalFactory {

    private final EmployeeDAO employeeDAO;
    private final UserDAO userDAO;
    private final ShiftDAO shiftDAO;
    private final TrucksDAO trucksDAO;
    private final ItemListsDAO itemListsDAO;
    private final SitesDAO sitesDAO;
    private final BranchesDAO branchesDAO;
    private final DriversDAO driversDAO;
    private final TransportsDAO transportsDAO;


    public DalFactory() throws DalException {
        try {
            employeeDAO = EmployeeDAO.getInstance();
            userDAO = UserDAO.getInstance();
            shiftDAO = ShiftDAO.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        trucksDAO = new TrucksDAO();
        driversDAO = new DriversDAO();
        itemListsDAO = new ItemListsDAO();
        sitesDAO = new SitesDAO();
        branchesDAO = new BranchesDAO();
        transportsDAO = new TransportsDAO();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public DalFactory(String dbName) throws DalException {
        try {
            employeeDAO = EmployeeDAO.getInstance();
            userDAO = UserDAO.getInstance();
            shiftDAO = ShiftDAO.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        trucksDAO = new TrucksDAO(dbName);
        driversDAO = new DriversDAO(dbName);
        itemListsDAO = new ItemListsDAO(dbName);
        sitesDAO = new SitesDAO(dbName);
        branchesDAO = new BranchesDAO(dbName);
        transportsDAO = new TransportsDAO(dbName);
    }
    public EmployeeDAO employeeDAO() {
        return employeeDAO;
    }

    public UserDAO userDAO() {
        return userDAO;
    }

    public ShiftDAO shiftDAO() {
        return shiftDAO;
    }

    public TrucksDAO trucksDAO() {
        return trucksDAO;
    }

    public DriversDAO driversDAO() {
        return driversDAO;
    }

    public ItemListsDAO itemListsDAO() {
        return itemListsDAO;
    }

    public SitesDAO sitesDAO() {
        return sitesDAO;
    }

    public BranchesDAO branchesDAO() {
        return branchesDAO;
    }

    public TransportsDAO transportsDAO() {
        return transportsDAO;
    }
}
