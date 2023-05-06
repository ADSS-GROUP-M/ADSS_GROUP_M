package dataAccessLayer;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.dalUtils.SQLExecutorProductionImpl;
import dataAccessLayer.dalUtils.SQLExecutorTestingImpl;
import dataAccessLayer.employeeModule.*;
import dataAccessLayer.transportModule.*;

public class DalFactory {

    public static String TESTING_DB_NAME = ":memory:";

    private EmployeeDAO employeeDAO;
    private UserDAO userDAO;
    private ShiftDAO shiftDAO;
    private TrucksDAO trucksDAO;
    private ItemListsDAO itemListsDAO;
    private SitesDAO sitesDAO;
    private BranchesDAO branchesDAO;
    private BranchEmployeesDAO branchEmployeesDAO;
    private DriversDAO driversDAO;
    private TransportsDAO transportsDAO;
    private SitesDistancesDAO sitesDistancesDAO;

    private TransportDestinationsDAO transportDestinationsDAO;

    private ItemListsItemsDAO itemListsItemsDAO;

    public DalFactory() throws DalException {
        SQLExecutor cursor = new SQLExecutorProductionImpl();
        buildInstances(cursor);
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public DalFactory(String dbName) throws DalException {
        SQLExecutor cursor = new SQLExecutorTestingImpl(dbName);
        buildInstances(cursor);
    }

    private void buildInstances(SQLExecutor cursor) throws DalException {
        ShiftToActivityDAO shiftToActivityDAO = new ShiftToActivityDAO(cursor);
        ShiftToCancelsDAO shiftToCancelsDAO = new ShiftToCancelsDAO(cursor);
        ShiftToNeededRolesDAO shiftToNeededRolesDAO = new ShiftToNeededRolesDAO(cursor);
        UserAuthorizationsDAO userAuthorizationsDAO = new UserAuthorizationsDAO(cursor);
        EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO(cursor);

        userDAO = new UserDAO(cursor,userAuthorizationsDAO);
        employeeDAO = new EmployeeDAO(cursor,employeeRolesDAO);
        ShiftToWorkersDAO shiftToWorkersDAO = new ShiftToWorkersDAO(cursor,employeeDAO);
        ShiftToRequestsDAO shiftToRequestsDAO = new ShiftToRequestsDAO(cursor,employeeDAO);
        shiftDAO = new ShiftDAO(cursor, shiftToNeededRolesDAO, shiftToRequestsDAO, shiftToWorkersDAO, shiftToCancelsDAO, shiftToActivityDAO);
        itemListsItemsDAO = new ItemListsItemsDAO(cursor);
        ItemListIdCounterDAO itemListIdCounterDAO = new ItemListIdCounterDAO(cursor);
        itemListsDAO = new ItemListsDAO(cursor, itemListsItemsDAO, itemListIdCounterDAO);
        trucksDAO = new TrucksDAO(cursor);
        driversDAO = new DriversDAO(cursor);
        sitesDAO = new SitesDAO(cursor);
        branchEmployeesDAO = new BranchEmployeesDAO(cursor);
        branchesDAO = new BranchesDAO(cursor,branchEmployeesDAO);
        transportDestinationsDAO = new TransportDestinationsDAO(cursor);
        TransportIdCounterDAO transportIdCounterDAO = new TransportIdCounterDAO(cursor);
        transportsDAO = new TransportsDAO(cursor, transportDestinationsDAO, transportIdCounterDAO);
        sitesDistancesDAO = new SitesDistancesDAO(cursor);
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

    public SitesDistancesDAO sitesDistancesDAO() {
        return sitesDistancesDAO;
    }

    public TransportDestinationsDAO transportDestinationsDAO() {
        return transportDestinationsDAO;
    }

    public ItemListsItemsDAO itemListsItemsDAO() {
        return itemListsItemsDAO;
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
            factory.sitesDistancesDAO().clearTable();
            factory.sitesDAO().clearTable();

            factory.driversDAO().clearTable();
            factory.employeeDAO().clearTable();

        } catch (DalException e) {
            e.printStackTrace();
        }
    }
}
