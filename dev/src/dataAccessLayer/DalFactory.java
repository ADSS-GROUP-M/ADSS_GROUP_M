package dataAccessLayer;

import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.SQLExecutorImpl;
import dataAccessLayer.employeeModule.*;
import dataAccessLayer.inventoryModule.*;
import dataAccessLayer.suppliersModule.AgreementDataMappers.AgreementDataMapper;
import dataAccessLayer.suppliersModule.AgreementDataMappers.DeliveryAgreementDataMapper;
import dataAccessLayer.suppliersModule.AgreementDataMappers.SupplierProductDataMapper;
import dataAccessLayer.suppliersModule.BillOfQuantitiesDataMappers.*;
import dataAccessLayer.suppliersModule.OrderHistoryDataMappers.NumberOfOrdersCounterDataMapper;
import dataAccessLayer.suppliersModule.OrderHistoryDataMappers.OrderHistoryDataMapper;
import dataAccessLayer.suppliersModule.PeriodicOrderDataMapper;
import dataAccessLayer.suppliersModule.PeriodicOrderDetailsDataMapper;
import dataAccessLayer.suppliersModule.ProductsDataMapper;
import dataAccessLayer.suppliersModule.SuppliersDataMappers.ContactsInfoDataMapper;
import dataAccessLayer.suppliersModule.SuppliersDataMappers.FieldsDataMapper;
import dataAccessLayer.suppliersModule.SuppliersDataMappers.SupplierDataMapper;
import dataAccessLayer.transportModule.*;
import exceptions.DalException;

public class DalFactory {

    public static final String TESTING_DB_NAME = "TestingDB.db";
    public static final String DEFAULT_DB_NAME = "SuperLiDB.db";

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
    private SQLExecutor cursor;
    private TransportsDAO transportsDAO;
    private DeliveryRoutesDAO deliveryRoutesDAO;
    private TransportsMetaDataDAO transportsMetaDataDAO;
    private SupplierDataMapper supplierDataMapper;
    private OrderHistoryDataMapper orderHistoryDataMapper;
    private AgreementDataMapper agreementDataMapper;
    private BillOfQuantitiesDataMapper billOfQuantitiesDataMapper;
    private PeriodicOrderDataMapper periodicOrderDataMapper;
    private ProductManagerMapper productManagerMapper;
    private DiscountManagerMapper discountManagerMapper;
    private CategoryManagerMapper categoryManagerMapper;

    private BranchDataMapper branchDataMapper;

    private SQLExecutor shiftEmployeeCursor;

    /**
     * connect to the default database {@link DalFactory#DEFAULT_DB_NAME}
     */
    public DalFactory() throws DalException {
        this(DEFAULT_DB_NAME);
    }

    /**
     * @param dbName the name of the database to connect to
     */
    public DalFactory(String dbName) throws DalException {
        cursor = new SQLExecutorImpl(dbName);
        buildInstances();
    }

    private DalFactory(SQLExecutor cursor) throws DalException {
        this.cursor = cursor;
        buildInstances();
    }

    private void buildInstances() throws DalException {

        trucksDAO = new TrucksDAO(cursor());                // independent
        driversDAO = new DriversDAO(cursor());              // independent
        sitesDAO = new SitesDAO(cursor());                  // independent
        sitesRoutesDAO = new SitesRoutesDAO(cursor());      // independent
        branchDataMapper = new BranchDataMapper(cursor());  // independent

        //============== dependencies ================== |
        /*(1)*/ ProductsDataMapper productsDataMapper = new ProductsDataMapper(cursor());
        /*(1)*/ ProductItemDataMapper productItemDataMapper = new ProductItemDataMapper(cursor());
        /*(1)*/ ProductPairBranchDataMapper productPairBranchDataMapper = new ProductPairBranchDataMapper(cursor());
        /*(1)*/ DeliveryAgreementDataMapper deliveryAgreementDataMapper = new DeliveryAgreementDataMapper(cursor());
        /*(1)*/ SupplierProductDataMapper supplierProductDataMapper = new SupplierProductDataMapper(cursor());
        /*(2)*/ agreementDataMapper = new AgreementDataMapper(
                supplierProductDataMapper,
                deliveryAgreementDataMapper,
                productsDataMapper);
        /*(2)*/ productManagerMapper = new ProductManagerMapper(
                    productsDataMapper,
                    productItemDataMapper,
                    productPairBranchDataMapper);
        /*(3)*/ CategoryDataMapper categoryDataMapper = new CategoryDataMapper(cursor(),productManagerMapper);
        /*(3)*/ CategoryHierarchyDataMapper categoryHierarchyDataMapper = new CategoryHierarchyDataMapper(cursor());
        /*(4)*/ categoryManagerMapper = new CategoryManagerMapper(
                    categoryDataMapper,
                    categoryHierarchyDataMapper);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ StoreProductDiscountDataMapper storeProductDiscountDataMapper = new StoreProductDiscountDataMapper(cursor());
        /*(2)*/ discountManagerMapper = new DiscountManagerMapper(storeProductDiscountDataMapper);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ ContactsInfoDataMapper contactsInfoDataMapper = new ContactsInfoDataMapper(cursor());
        /*(1)*/ FieldsDataMapper fieldsDataMapper = new FieldsDataMapper(cursor());
        /*(2)*/ supplierDataMapper = new SupplierDataMapper(
                    cursor(),
                    contactsInfoDataMapper,
                    fieldsDataMapper);
        // ============================================= |

        //============== dependencies ================== |
        /*(1)*/ ProductsDiscountsDataMapper productsDiscountsDataMapper = new ProductsDiscountsDataMapper(cursor());
        /*(1)*/ DiscountOnTotalDataMapper discountOnTotalDataMapper = new DiscountOnTotalDataMapper(cursor());
        /*(1)*/ DiscountOnAmountDataMapper discountOnAmountDataMapper = new DiscountOnAmountDataMapper(cursor());
        /*(1)*/ OrderOfDiscountsDataMapper orderOfDiscountsDataMapper = new OrderOfDiscountsDataMapper(cursor());
        /*(2)*/ billOfQuantitiesDataMapper = new BillOfQuantitiesDataMapper(
                    productsDiscountsDataMapper,
                    discountOnTotalDataMapper,
                    discountOnAmountDataMapper,
                    orderOfDiscountsDataMapper);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ NumberOfOrdersCounterDataMapper numberOfOrdersCounterDataMapper = new NumberOfOrdersCounterDataMapper(cursor());
        /*(2)*/ orderHistoryDataMapper = new OrderHistoryDataMapper(cursor(), numberOfOrdersCounterDataMapper);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ PeriodicOrderDetailsDataMapper periodicOrderDetailsDataMapper = new PeriodicOrderDetailsDataMapper(cursor());
        /*(2)*/ periodicOrderDataMapper = new PeriodicOrderDataMapper(
                cursor(),
                periodicOrderDetailsDataMapper);
        //============================================== |

        //============== dependencies ================== |
        SQLExecutor transportCursor = cursor(); // shared SQLExecutor for batched transactions
        /*(1)*/ TransportIdCounterDAO transportIdCounterDAO = new TransportIdCounterDAO(transportCursor);
        /*(2)*/ transportsMetaDataDAO = new TransportsMetaDataDAO(transportCursor, transportIdCounterDAO);
        /*(2)*/ deliveryRoutesDAO = new DeliveryRoutesDAO(transportCursor);
        /*(3)*/ transportsDAO = new TransportsDAO(transportCursor,transportsMetaDataDAO, deliveryRoutesDAO);
        //============================================== |

        //============== dependencies ================== |
        SQLExecutor userCursor = cursor(); // shared SQLExecutor for batched transactions
        /*(1)*/ userAuthorizationsDAO = new UserAuthorizationsDAO(userCursor);
        /*(2)*/ userDAO = new UserDAO(userCursor,userAuthorizationsDAO);
        //========================================== |

        //============== dependencies ================== |
        shiftEmployeeCursor = cursor(); // shared SQLExecutor for batched transactions
        /*(1)*/ ShiftToActivityDAO shiftToActivityDAO = new ShiftToActivityDAO(shiftEmployeeCursor);
        /*(1)*/ ShiftToCancelsDAO shiftToCancelsDAO = new ShiftToCancelsDAO(shiftEmployeeCursor);
        /*(1)*/ ShiftToNeededRolesDAO shiftToNeededRolesDAO = new ShiftToNeededRolesDAO(shiftEmployeeCursor);
        /*(1)*/ EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO(shiftEmployeeCursor);
        /*(2)*/ employeeDAO = new EmployeeDAO(shiftEmployeeCursor,employeeRolesDAO);
        /*(3)*/ ShiftToWorkersDAO shiftToWorkersDAO = new ShiftToWorkersDAO(shiftEmployeeCursor,employeeDAO);
        /*(3)*/ ShiftToRequestsDAO shiftToRequestsDAO = new ShiftToRequestsDAO(shiftEmployeeCursor,employeeDAO);
        /*(4)*/ shiftDAO = new ShiftDAO(shiftEmployeeCursor,
                    shiftToNeededRolesDAO,
                    shiftToRequestsDAO,
                    shiftToWorkersDAO,
                    shiftToCancelsDAO,
                    shiftToActivityDAO);
        //============================================= |

        //============== dependencies ================== |
        SQLExecutor itemListsCursor = cursor(); // shared SQLExecutor for batched transactions
        /*(1)*/ ItemListIdCounterDAO itemListIdCounterDAO = new ItemListIdCounterDAO(itemListsCursor);
        /*(1)*/ itemListsItemsDAO = new ItemListsItemsDAO(itemListsCursor);
        /*(2)*/ itemListsDAO = new ItemListsDAO(itemListsCursor, itemListsItemsDAO, itemListIdCounterDAO);
        //============================================== |

        //============== dependencies ================== |
        SQLExecutor branchCursor = cursor(); // shared SQLExecutor for batched transactions
        /*(1)*/ branchEmployeesDAO = new BranchEmployeesDAO(branchCursor);
        /*(2)*/ branchesDAO = new BranchesDAO(branchCursor,branchEmployeesDAO);
        //============================================== |

    }

    public BranchDataMapper branchDataMapper() {
        return branchDataMapper;
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

    public SupplierDataMapper supplierDataMapper() {
        return supplierDataMapper;
    }

    public OrderHistoryDataMapper orderHistoryDataMapper() {
        return orderHistoryDataMapper;
    }

    public AgreementDataMapper agreementDataMapper() {
        return agreementDataMapper;
    }

    public BillOfQuantitiesDataMapper billOfQuantitiesDataMapper() {
        return billOfQuantitiesDataMapper;
    }

    public PeriodicOrderDataMapper periodicOrderDataMapper() {
        return periodicOrderDataMapper;
    }

    public ProductManagerMapper productManagerMapper() {
        return productManagerMapper;
    }

    public DiscountManagerMapper discountManagerMapper() {
        return discountManagerMapper;
    }

    public CategoryManagerMapper categoryManagerMapper() {
        return categoryManagerMapper;
    }

    public SQLExecutor cursor() {
        return cursor.clone();
    }

    public static void clearTestDB(){
        clearDB(TESTING_DB_NAME);
    }

    public SQLExecutor shiftEmployeeCursor() {
        return shiftEmployeeCursor;
    }

    public static void clearDB(String dbName){
        try {

            //note: the order of the calls is important because of the foreign key constraints
            DalFactory factory = new DalFactory(new SQLExecutorImpl(dbName));

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

            //============== dependencies ================== |
            //TODO: Order these calls so that the foreign key constraints are not violated
            // and that all the tables are cleared correctly.
            // note: there are duplicate calls to clearTable() in the code below for some tables
            // because of the dependencies between the data mappers.
            // need to remove duplicate calls
            /*(-)*/ factory.supplierDataMapper().clearTable();
            /*(-)*/ factory.productManagerMapper().clearTable();
            /*(-)*/ factory.agreementDataMapper().clearTable();
            /*(-)*/ factory.billOfQuantitiesDataMapper().clearTable();
            /*(-)*/ factory.orderHistoryDataMapper().clearTable();
            /*(-)*/ factory.periodicOrderDataMapper().clearTable();
            /*(-)*/ factory.discountManagerMapper().clearTable();
            /*(-)*/ factory.categoryManagerMapper().clearTable();
            /*(-)*/ factory.branchDataMapper().clearTable();
            //============================================== |

        } catch (DalException e) {
            e.printStackTrace();
        }
    }
}
