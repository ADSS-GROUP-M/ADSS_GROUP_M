package dataAccessLayer;

import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.SQLExecutorProductionImpl;
import dataAccessLayer.dalUtils.SQLExecutorTestingImpl;
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
    private SupplierDataMapper supplierDataMapper;
    private OrderHistoryDataMapper orderHistoryDataMapper;
    private AgreementDataMapper agreementDataMapper;
    private BillOfQuantitiesDataMapper billOfQuantitiesDataMapper;
    private PeriodicOrderDataMapper periodicOrderDataMapper;
    private ProductManagerMapper productManagerMapper;
    private DiscountManagerMapper discountManagerMapper;
    private CategoryManagerMapper categoryManagerMapper;
    private BranchDataMapper branchDataMapper;

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
        branchDataMapper = new BranchDataMapper(cursor);  // independent

        //============== dependencies ================== |
        /*(1)*/ ProductsDataMapper productsDataMapper = new ProductsDataMapper(cursor);
        /*(1)*/ ProductItemDataMapper productItemDataMapper = new ProductItemDataMapper(cursor);
        /*(1)*/ ProductPairBranchDataMapper productPairBranchDataMapper = new ProductPairBranchDataMapper(cursor);
        /*(1)*/ DeliveryAgreementDataMapper deliveryAgreementDataMapper = new DeliveryAgreementDataMapper(cursor);
        /*(1)*/ SupplierProductDataMapper supplierProductDataMapper = new SupplierProductDataMapper(cursor);
        /*(2)*/ agreementDataMapper = new AgreementDataMapper(
                supplierProductDataMapper,
                deliveryAgreementDataMapper,
                productsDataMapper);
        /*(2)*/ productManagerMapper = new ProductManagerMapper(
                    productsDataMapper,
                    productItemDataMapper,
                    productPairBranchDataMapper);
        /*(3)*/ CategoryDataMapper categoryDataMapper = new CategoryDataMapper(cursor,productManagerMapper);
        /*(3)*/ CategoryHierarchyDataMapper categoryHierarchyDataMapper = new CategoryHierarchyDataMapper(cursor);
        /*(4)*/ categoryManagerMapper = new CategoryManagerMapper(
                    categoryDataMapper,
                    categoryHierarchyDataMapper);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ StoreProductDiscountDataMapper storeProductDiscountDataMapper = new StoreProductDiscountDataMapper(cursor);
        /*(2)*/ discountManagerMapper = new DiscountManagerMapper(storeProductDiscountDataMapper);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ ContactsInfoDataMapper contactsInfoDataMapper = new ContactsInfoDataMapper(cursor);
        /*(1)*/ FieldsDataMapper fieldsDataMapper = new FieldsDataMapper(cursor);
        /*(2)*/ supplierDataMapper = new SupplierDataMapper(
                    cursor,
                    contactsInfoDataMapper,
                    fieldsDataMapper);
        // ============================================= |

        //============== dependencies ================== |
        /*(1)*/ ProductsDiscountsDataMapper productsDiscountsDataMapper = new ProductsDiscountsDataMapper(cursor);
        /*(1)*/ DiscountOnTotalDataMapper discountOnTotalDataMapper = new DiscountOnTotalDataMapper(cursor);
        /*(1)*/ DiscountOnAmountDataMapper discountOnAmountDataMapper = new DiscountOnAmountDataMapper(cursor);
        /*(1)*/ OrderOfDiscountsDataMapper orderOfDiscountsDataMapper = new OrderOfDiscountsDataMapper(cursor);
        /*(2)*/ billOfQuantitiesDataMapper = new BillOfQuantitiesDataMapper(
                    productsDiscountsDataMapper,
                    discountOnTotalDataMapper,
                    discountOnAmountDataMapper,
                    orderOfDiscountsDataMapper);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ NumberOfOrdersCounterDataMapper numberOfOrdersCounterDataMapper = new NumberOfOrdersCounterDataMapper(cursor);
        /*(2)*/ orderHistoryDataMapper = new OrderHistoryDataMapper(cursor, numberOfOrdersCounterDataMapper);
        //============================================== |

        //============== dependencies ================== |
        /*(1)*/ PeriodicOrderDetailsDataMapper periodicOrderDetailsDataMapper = new PeriodicOrderDetailsDataMapper(cursor);
        /*(2)*/ periodicOrderDataMapper = new PeriodicOrderDataMapper(
                cursor,
                periodicOrderDetailsDataMapper);
        //============================================== |

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
            factory.sitesRoutesDAO().clearTable();
            factory.sitesDAO().clearTable();

            factory.driversDAO().clearTable();
            factory.employeeDAO().clearTable();

        } catch (DalException e) {
            e.printStackTrace();
        }
    }
}
