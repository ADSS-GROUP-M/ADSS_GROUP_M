package DataAccessLayer.transportModule;

import DataAccessLayer.DalUtils.DalException;
import transportModule.records.Site;

import java.util.List;

public class SitesDAO extends DAO<Site>{

    protected SitesDAO() {
        super("Sites", new String[]{"SiteAddress"}, "SiteAddress", "TransportZone","ContactName","ContactPhone","SiteType");
    }

    /**
     * Initialize the table if it doesn't exist
     */
    @Override
    protected void initTable() {

    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Site select(Site object) throws DalException {
        return null;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Site> selectAll() throws DalException {
        return null;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Site object) throws DalException {

    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Site object) throws DalException {

    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Site object) throws DalException {

    }
}
