package dataAccessLayer;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.*;

public class DalFactory {

    private final TrucksDAO trucksDAO;
    private final DriversDAO driversDAO;
    private final ItemListsDAO itemListsDAO;
    private final SitesDAO sitesDAO;
    private final BranchesDAO branchesDAO;
    public DalFactory() throws DalException {
        trucksDAO = new TrucksDAO();
        driversDAO = new DriversDAO();
        itemListsDAO = new ItemListsDAO();
        sitesDAO = new SitesDAO();
        branchesDAO = new BranchesDAO();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public DalFactory(String dbName) throws DalException {
        trucksDAO = new TrucksDAO(dbName);
        driversDAO = new DriversDAO(dbName);
        itemListsDAO = new ItemListsDAO(dbName);
        sitesDAO = new SitesDAO(dbName);
        branchesDAO = new BranchesDAO(dbName);
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
}
