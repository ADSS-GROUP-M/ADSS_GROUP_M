package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;
import objects.transportObjects.Site;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class SitesDAO extends DAOBase<Site> {

    public static final String tableName = "sites";
    public static final String primaryKey = "name";

    public SitesDAO(SQLExecutor cursor) throws DalException {
        super(cursor, tableName);
    }

    /**
     * Used to insert data into {@link DAOBase#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
        createTableQueryBuilder
                .addColumn("name", ColumnType.TEXT, ColumnModifier.NOT_NULL, ColumnModifier.PRIMARY_KEY)
                .addColumn("address", ColumnType.TEXT, ColumnModifier.NOT_NULL, ColumnModifier.UNIQUE)
                .addColumn("transport_zone", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("contact_name", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("contact_phone", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("site_type", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("latitude", ColumnType.REAL, ColumnModifier.NOT_NULL)
                .addColumn("longitude", ColumnType.REAL, ColumnModifier.NOT_NULL);
    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Site select(Site object) throws DalException {

        if(cache.contains(object)) {
            return cache.get(object);
        }

        String query = String.format("SELECT * FROM %s WHERE name = '%s';", TABLE_NAME, object.name());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select site", e);
        }
        if(resultSet.next()) {
            Site selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            return selected;
        } else {
            throw new DalException("No site with name " + object.name() + " was found");
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
        cache.putAll(sites);
        return sites;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Site object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s','%s', '%s', '%s', '%s', '%s', %f, %f);",
                TABLE_NAME,
                object.name(),
                object.address(),
                object.transportZone(),
                object.contactName(),
                object.phoneNumber(),
                object.siteType().toString(),
                object.latitude(),
                object.longitude());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while inserting site");
            }
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
        String query = String.format("UPDATE %s SET address = '%s', transport_zone = '%s', contact_name = '%s', contact_phone = '%s', site_type = '%s', latitude = %f, longitude = %f WHERE name = '%s';",
                TABLE_NAME,
                object.address(),
                object.transportZone(),
                object.contactName(),
                object.phoneNumber(),
                object.siteType(),
                object.latitude(),
                object.longitude(),
                object.name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new DalException("No site with name " + object.name() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update site", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Site object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE name = '%s';", TABLE_NAME, object.name());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.remove(object);
            } else {
                throw new DalException("No site with name " + object.name() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete site", e);
        }
    }

    @Override
    public boolean exists(Site object) throws DalException {

        if(cache.contains(object)) {
            return true;
        }

        String query = String.format("SELECT * FROM %s WHERE name = '%s';", TABLE_NAME, object.name());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
            if(resultSet.next()) {
                Site selected = getObjectFromResultSet(resultSet);
                cache.put(selected);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DalException("Failed to check if Site exists", e);
        }
    }

    @Override
    protected Site getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Site(
                resultSet.getString("name"),
                resultSet.getString("address"),
                resultSet.getString("transport_zone"),
                resultSet.getString("contact_phone"),
                resultSet.getString("contact_name"),
                Site.SiteType.valueOf(resultSet.getString("site_type")),
                resultSet.getDouble("latitude"),
                resultSet.getDouble("longitude"));
    }

    public boolean isAddressUnique(String address) throws DalException{
        String query = String.format("SELECT * FROM %s WHERE address = '%s';", TABLE_NAME, address);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
            return resultSet.next() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if Site exists", e);
        }
    }
}
