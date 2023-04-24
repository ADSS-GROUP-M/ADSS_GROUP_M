package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.DAO;
import objects.transportObjects.Site;
import objects.transportObjects.Truck;

import java.sql.SQLException;
import java.util.LinkedList;
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
        String query = String.format("SELECT * FROM %s WHERE address = %s", TABLE_NAME, object.address());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select site", e);
        }
        if(resultSet.next()) {
            return getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("No site with address " + object.address() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Site> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all Sites", e);
        }
        List<Site> sites = new LinkedList<>();
        while (resultSet.next()) {
            sites.add(getObjectFromResultSet(resultSet));
        }
        return sites;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Site object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s', '%s', '%s', '%s');",
                TABLE_NAME,
                object.address(),
                object.transportZone(),
                object.contactName(),
                object.phoneNumber(),
                object.siteType().toString());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to insert site", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Site object) throws DalException {
        String query = String.format("UPDATE %s SET transport_zone = '%s', contact_name = '%s', contact_phone = '%s', site_type = '%s' WHERE address = '%s';",
                TABLE_NAME,
                object.transportZone(),
                object.contactName(),
                object.phoneNumber(),
                object.siteType(),
                object.address());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to update site", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Site object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE address = '%s';", TABLE_NAME, object.address());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to delete site", e);
        }
    }

    @Override
    protected Site getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Site(
                resultSet.getString("address"),
                resultSet.getString("transport_zone"),
                resultSet.getString("contact_name"),
                resultSet.getString("contact_phone"),
                Site.SiteType.valueOf(resultSet.getString("site_type")));
    }
}
