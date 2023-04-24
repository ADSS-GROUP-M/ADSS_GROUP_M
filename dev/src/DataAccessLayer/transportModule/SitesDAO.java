package DataAccessLayer.transportModule;

import DataAccessLayer.DalUtils.DalException;
import DataAccessLayer.transportModule.abstracts.DAO;
import objects.transportObjects.Site;

import java.sql.SQLException;
import java.util.List;

public class SitesDAO extends DAO<Site> {

    public SitesDAO() throws DalException {
        super("sites",
                new String[]{"address"},
                "address",
                "transport_zone",
                "contact_name",
                "contact_phone",
                "site_type");
    }

    /**
     *  used for testing
     *  @param dbName the name of the database to connect to
     */
    public SitesDAO(String dbName) throws DalException {
        super(dbName,
                "sites",
                new String[]{"address"},
                "address",
                "transport_zone",
                "contact_name",
                "contact_phone",
                "site_type");
    }

    /**
     * Initialize the table if it doesn't exist
     */
    @Override
    protected void initTable() throws DalException {
        String query = """
                CREATE TABLE IF NOT EXISTS sites (
                    site_address TEXT PRIMARY KEY,
                    transport_zone TEXT NOT NULL,
                    contact_name TEXT NOT NULL,
                    contact_phone TEXT NOT NULL,
                    site_type TEXT NOT NULL,
                    PRIMARY KEY (address)
                );
                """;
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to initialize Sites table", e);
        }
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
