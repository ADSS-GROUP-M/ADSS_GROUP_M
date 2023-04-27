package dataAccessLayer;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.employeeModule.BranchesDAO;
import dataAccessLayer.employeeModule.EmployeeDAO;
import dataAccessLayer.employeeModule.ShiftDAO;
import dataAccessLayer.employeeModule.UserDAO;
import dataAccessLayer.transportModule.*;

import dataAccessLayer.employeeModule.*;

public class DalFactory {

    public static String TESTING_DB_NAME = "TestingDB.db";

    private final EmployeeDAO employeeDAO;
    private final UserDAO userDAO;
    private final ShiftDAO shiftDAO;
    private final TrucksDAO trucksDAO;
    private final ItemListsDAO itemListsDAO;
    private final SitesDAO sitesDAO;
    private final BranchesDAO branchesDAO;
    private final BranchEmployeesDAO branchEmployeesDAO;
    private final DriversDAO driversDAO;
    private final TransportsDAO transportsDAO;

    public DalFactory() throws DalException {

        ShiftToActivityDAO shiftToActivityDAO = new ShiftToActivityDAO();
        ShiftToCancelsDAO shiftToCancelsDAO = new ShiftToCancelsDAO();
        ShiftToNeededRolesDAO shiftToNeededRolesDAO = new ShiftToNeededRolesDAO();
        UserAuthorizationsDAO userAuthorizationsDAO = new UserAuthorizationsDAO();
        EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO();

        userDAO = new UserDAO(userAuthorizationsDAO);
        employeeDAO = new EmployeeDAO(employeeRolesDAO);
        ShiftToWorkersDAO shiftToWorkersDAO = new ShiftToWorkersDAO(employeeDAO);
        ShiftToRequestsDAO shiftToRequestsDAO = new ShiftToRequestsDAO(employeeDAO);
        shiftDAO = new ShiftDAO(shiftToNeededRolesDAO, shiftToRequestsDAO, shiftToWorkersDAO, shiftToCancelsDAO, shiftToActivityDAO);
        itemListsDAO = new ItemListsDAO();
        trucksDAO = new TrucksDAO();
        driversDAO = new DriversDAO();
        sitesDAO = new SitesDAO();
        branchEmployeesDAO = new BranchEmployeesDAO();
        branchesDAO = new BranchesDAO(branchEmployeesDAO);
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
        EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO(dbName);

        userDAO = new UserDAO(dbName, userAuthorizationsDAO);
        employeeDAO = new EmployeeDAO(dbName, employeeRolesDAO);
        ShiftToWorkersDAO shiftToWorkersDAO = new ShiftToWorkersDAO(dbName, employeeDAO);
        ShiftToRequestsDAO shiftToRequestsDAO = new ShiftToRequestsDAO(dbName, employeeDAO);
        shiftDAO = new ShiftDAO(dbName, shiftToNeededRolesDAO, shiftToRequestsDAO, shiftToWorkersDAO, shiftToCancelsDAO, shiftToActivityDAO);

        trucksDAO = new TrucksDAO(dbName);
        driversDAO = new DriversDAO(dbName);
        itemListsDAO = new ItemListsDAO(dbName);
        sitesDAO = new SitesDAO(dbName);
        branchEmployeesDAO = new BranchEmployeesDAO(dbName);
        branchesDAO = new BranchesDAO(dbName,branchEmployeesDAO);
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

    public BranchEmployeesDAO branchEmployeesDAO() {
        return branchEmployeesDAO;
    }

    public TransportsDAO transportsDAO() {
        return transportsDAO;
    }

    public static void clearTestDB(){
        try {
            DalFactory factory = new DalFactory(TESTING_DB_NAME);
            factory.shiftDAO().clearTable();
            factory.userDAO().clearTable();

            factory.transportsDAO().clearTable();

            factory.trucksDAO().clearTable();
            factory.itemListsDAO().clearTable();

            factory.branchEmployeesDAO().clearTable();
            factory.branchesDAO().clearTable();
            factory.sitesDAO().clearTable();

            factory.driversDAO().clearTable();
            factory.employeeDAO().clearTable();

        } catch (DalException e) {
            e.printStackTrace();
        }
    }
}
