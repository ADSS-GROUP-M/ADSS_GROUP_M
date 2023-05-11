package dataAccessLayer;

import exceptions.DalException;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.SQLExecutorProductionImpl;
import dataAccessLayer.dalUtils.SQLExecutorTestingImpl;
import dataAccessLayer.employeeModule.*;
import dataAccessLayer.transportModule.*;

public class DalFactory {

    public static String TESTING_DB_NAME = "TestingDB.db";

    private EmployeeDAO employeeDAO;
    private UserDAO userDAO;
    private UserAuthorizationsDAO userAuthorizationsDAO;
    private ShiftDAO shiftDAO;
    private TrucksDAO trucksDAO;
    private ItemListsDAO itemListsDAO;
    private SitesDAO sitesDAO;
    private BranchesDAO branchesDAO;
    private BranchEmployeesDAO branchEmployeesDAO;
    private DriversDAO driversDAO;
    private TransportsDAO transportsDAO;
    private SitesRoutesDAO sitesRoutesDAO;

    private TransportDestinationsDAO transportDestinationsDAO;

    private ItemListsItemsDAO itemListsItemsDAO;

    private final SQLExecutor cursor;

    public DalFactory() throws DalException {
        cursor = new SQLExecutorProductionImpl();
        buildInstances(cursor);
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public DalFactory(String dbName) throws DalException {
        cursor = new SQLExecutorTestingImpl(dbName);
        buildInstances(cursor);
    }

    private void buildInstances(SQLExecutor cursor) throws DalException {

        itemListsItemsDAO = new ItemListsItemsDAO(cursor);
        trucksDAO = new TrucksDAO(cursor);
        driversDAO = new DriversDAO(cursor);

        //============== dependencies ============== |
        /*(1)*/ userAuthorizationsDAO = new UserAuthorizationsDAO(cursor);
        /*(2)*/ userDAO = new UserDAO(cursor,userAuthorizationsDAO);
        //========================================== |

        //============== dependencies ============== |
        /*(1)*/ ShiftToActivityDAO shiftToActivityDAO = new ShiftToActivityDAO(cursor);
        /*(1)*/ ShiftToCancelsDAO shiftToCancelsDAO = new ShiftToCancelsDAO(cursor);
        /*(1)*/ ShiftToNeededRolesDAO shiftToNeededRolesDAO = new ShiftToNeededRolesDAO(cursor);
        /*(1)*/ EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO(cursor);
        /*(2)*/ employeeDAO = new EmployeeDAO(cursor,employeeRolesDAO);
        /*(3)*/ ShiftToWorkersDAO shiftToWorkersDAO = new ShiftToWorkersDAO(cursor,employeeDAO);
        /*(3)*/ ShiftToRequestsDAO shiftToRequestsDAO = new ShiftToRequestsDAO(cursor,employeeDAO);
        /*(4)*/ shiftDAO = new ShiftDAO(cursor, shiftToNeededRolesDAO, shiftToRequestsDAO, shiftToWorkersDAO, shiftToCancelsDAO, shiftToActivityDAO);
        //========================================== |

        //============== dependencies ============== |
        /*(1)*/ ItemListIdCounterDAO itemListIdCounterDAO = new ItemListIdCounterDAO(cursor);
        /*(2)*/ itemListsDAO = new ItemListsDAO(cursor, itemListsItemsDAO, itemListIdCounterDAO);
        //========================================== |

        //============== dependencies ============== |
        /*(1)*/ sitesDAO = new SitesDAO(cursor);
        /*(2)*/ sitesRoutesDAO = new SitesRoutesDAO(cursor);
        /*(2)*/ branchEmployeesDAO = new BranchEmployeesDAO(cursor);
        /*(3)*/ branchesDAO = new BranchesDAO(cursor,branchEmployeesDAO);
        //========================================== |

        //============== dependencies ============== |
        /*(1)*/ transportDestinationsDAO = new TransportDestinationsDAO(cursor);
        /*(1)*/ TransportIdCounterDAO transportIdCounterDAO = new TransportIdCounterDAO(cursor);
        /*(2)*/ transportsDAO = new TransportsDAO(cursor, transportDestinationsDAO, transportIdCounterDAO);
        //========================================== |
    }

    public EmployeeDAO employeeDAO() {
        return employeeDAO;
    }

    public UserDAO userDAO() {
        return userDAO;
    }

    public UserAuthorizationsDAO userAuthorizationsDAO() {
        return userAuthorizationsDAO;
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

    public SitesRoutesDAO sitesDistancesDAO() {
        return sitesRoutesDAO;
    }

    public TransportDestinationsDAO transportDestinationsDAO() {
        return transportDestinationsDAO;
    }

    public ItemListsItemsDAO itemListsItemsDAO() {
        return itemListsItemsDAO;
    }

    public SQLExecutor cursor() {
        return cursor;
    }

    public static void clearTestDB(){
        clearDB(TESTING_DB_NAME);
    }

    public static void clearDB(String dbName){
        try {
            DalFactory factory = new DalFactory(dbName);
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
