package dataAccessLayer;

import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.SQLExecutorProductionImpl;
import dataAccessLayer.dalUtils.SQLExecutorTestingImpl;
import dataAccessLayer.employeeModule.*;
import dataAccessLayer.transportModule.*;
import exceptions.DalException;

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
    private SitesRoutesDAO sitesRoutesDAO;
    private ItemListsItemsDAO itemListsItemsDAO;
    private final SQLExecutor cursor;
    private TransportsDAO transportsDAO;
    private DeliveryRoutesDAO deliveryRoutesDAO;
    private TransportsMetaDataDAO transportsMetaDataDAO;

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

        trucksDAO = new TrucksDAO(cursor);                // independent
        driversDAO = new DriversDAO(cursor);              // independent
        sitesDAO = new SitesDAO(cursor);                  // independent
        sitesRoutesDAO = new SitesRoutesDAO(cursor);      // independent

        //============== dependencies ================== |
        /*(1)*/ TransportIdCounterDAO transportIdCounterDAO = new TransportIdCounterDAO(cursor);
        /*(2)*/ transportsMetaDataDAO = new TransportsMetaDataDAO(cursor, transportIdCounterDAO);
        /*(2)*/ deliveryRoutesDAO = new DeliveryRoutesDAO(cursor);
        /*(3)*/ transportsDAO = new TransportsDAO(transportsMetaDataDAO, deliveryRoutesDAO);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ userAuthorizationsDAO = new UserAuthorizationsDAO(cursor);
        /*(2)*/ userDAO = new UserDAO(cursor,userAuthorizationsDAO);
        //========================================== |

        //============== dependencies ================== |
        /*(1)*/ ShiftToActivityDAO shiftToActivityDAO = new ShiftToActivityDAO(cursor);
        /*(1)*/ ShiftToCancelsDAO shiftToCancelsDAO = new ShiftToCancelsDAO(cursor);
        /*(1)*/ ShiftToNeededRolesDAO shiftToNeededRolesDAO = new ShiftToNeededRolesDAO(cursor);
        /*(1)*/ EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO(cursor);
        /*(2)*/ employeeDAO = new EmployeeDAO(cursor,employeeRolesDAO);
        /*(3)*/ ShiftToWorkersDAO shiftToWorkersDAO = new ShiftToWorkersDAO(cursor,employeeDAO);
        /*(3)*/ ShiftToRequestsDAO shiftToRequestsDAO = new ShiftToRequestsDAO(cursor,employeeDAO);
        /*(4)*/ shiftDAO = new ShiftDAO(cursor,
                    shiftToNeededRolesDAO,
                    shiftToRequestsDAO,
                    shiftToWorkersDAO,
                    shiftToCancelsDAO,
                    shiftToActivityDAO);
        //============================================= |

        //============== dependencies ================== |
        /*(1)*/ ItemListIdCounterDAO itemListIdCounterDAO = new ItemListIdCounterDAO(cursor);
        /*(1)*/ itemListsItemsDAO = new ItemListsItemsDAO(cursor);
        /*(2)*/ itemListsDAO = new ItemListsDAO(cursor, itemListsItemsDAO, itemListIdCounterDAO);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ branchEmployeesDAO = new BranchEmployeesDAO(cursor);
        /*(2)*/ branchesDAO = new BranchesDAO(cursor,branchEmployeesDAO);
        //============================================== |

    }

    public DeliveryRoutesDAO deliveryRoutesDAO() {
        return deliveryRoutesDAO;
    }

    public TransportsMetaDataDAO transportsMetaDataDAO() {
        return transportsMetaDataDAO;
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

    public SitesRoutesDAO sitesRoutesDAO() {
        return sitesRoutesDAO;
    }

    public ItemListsItemsDAO itemListsItemsDAO() {
        return itemListsItemsDAO;
    }

    public TransportsDAO transportsDAO() {
        return transportsDAO;
    }

    public SQLExecutor cursor() {
        return cursor;
    }

    public static void clearTestDB(){
        clearDB(TESTING_DB_NAME);
    }

    public static void clearDB(String dbName){
        try {

            //note: the order of the calls is important because of the foreign key constraints

            DalFactory factory = new DalFactory(dbName);

            //============== dependencies ================== |
            /*(-)*/ factory.userDAO().clearTable();

            /*(1)*/ factory.shiftDAO().clearTable();
            /*(1)*/ factory.transportsDAO().clearTable();

            /*(2)*/ factory.trucksDAO().clearTable();
            /*(2)*/ factory.driversDAO().clearTable();
            /*(2)*/ factory.branchEmployeesDAO().clearTable();
            /*(2)*/ factory.sitesRoutesDAO().clearTable();

            /*(3)*/ factory.branchesDAO().clearTable();
            /*(3)*/ factory.itemListsDAO().clearTable();
            /*(3)*/ factory.employeeDAO().clearTable();

            /*(4)*/ factory.sitesDAO().clearTable();
            //============================================== |

        } catch (DalException e) {
            e.printStackTrace();
        }
    }
}
