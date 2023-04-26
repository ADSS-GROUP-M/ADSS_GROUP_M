package dataAccessLayer;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.employeeModule.BranchesDAO;
import dataAccessLayer.employeeModule.EmployeeDAO;
import dataAccessLayer.employeeModule.ShiftDAO;
import dataAccessLayer.employeeModule.UserDAO;
import dataAccessLayer.transportModule.*;

import dataAccessLayer.employeeModule.*;

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

        ShiftToActivityDAO shiftToActivityDAO = new ShiftToActivityDAO();
        ShiftToCancelsDAO shiftToCancelsDAO = new ShiftToCancelsDAO();
        ShiftToNeededRolesDAO shiftToNeededRolesDAO = new ShiftToNeededRolesDAO();
        UserAuthorizationsDAO userAuthorizationsDAO = new UserAuthorizationsDAO();

        userDAO = new UserDAO(userAuthorizationsDAO);
        employeeDAO = new EmployeeDAO();
        ShiftToWorkersDAO shiftToWorkersDAO = new ShiftToWorkersDAO(employeeDAO);
        ShiftToRequestsDAO shiftToRequestsDAO = new ShiftToRequestsDAO(employeeDAO);
        shiftDAO = new ShiftDAO(shiftToNeededRolesDAO, shiftToRequestsDAO, shiftToWorkersDAO, shiftToCancelsDAO, shiftToActivityDAO);
        itemListsDAO = new ItemListsDAO();
        trucksDAO = new TrucksDAO();
        driversDAO = new DriversDAO();
        sitesDAO = new SitesDAO();
        branchesDAO = new BranchesDAO();
        transportsDAO = new TransportsDAO();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public DalFactory(String dbName) throws DalException {

        ShiftToActivityDAO shiftToActivityDAO = new ShiftToActivityDAO(dbName);
        ShiftToCancelsDAO shiftToCancelsDAO = new ShiftToCancelsDAO(dbName);
        ShiftToNeededRolesDAO shiftToNeededRolesDAO = new ShiftToNeededRolesDAO(dbName);
        UserAuthorizationsDAO userAuthorizationsDAO = new UserAuthorizationsDAO(dbName);

        userDAO = new UserDAO(dbName, userAuthorizationsDAO);
        employeeDAO = new EmployeeDAO(dbName);
        ShiftToWorkersDAO shiftToWorkersDAO = new ShiftToWorkersDAO(dbName, employeeDAO);
        ShiftToRequestsDAO shiftToRequestsDAO = new ShiftToRequestsDAO(dbName, employeeDAO);
        shiftDAO = new ShiftDAO(dbName, shiftToNeededRolesDAO, shiftToRequestsDAO, shiftToWorkersDAO, shiftToCancelsDAO, shiftToActivityDAO);

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

    public static void clearTestDB(){
        try {
            DalFactory factory = new DalFactory("TestingDB.db");
            factory.transportsDAO().clearTable();
            factory.branchesDAO().clearTable();
            factory.sitesDAO().clearTable();
            factory.driversDAO().clearTable();
            factory.trucksDAO().clearTable();
            factory.itemListsDAO().clearTable();
            factory.employeeDAO().clearTable();
        } catch (DalException e) {
            e.printStackTrace();
        }
    }
}
