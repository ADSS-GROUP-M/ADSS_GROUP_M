package DataAccessLayer.transportModule;

import DataAccessLayer.DalUtils.DalException;

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
