package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.DAO;

import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalAssociationClasses.transportModule.SiteRoute;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;

public class SitesRoutesDAO extends DAO<SiteRoute> {
    public static final String[] primaryKey = {"source", "destination"};
    public static final String tableName = "sites_routes";

    public SitesRoutesDAO(SQLExecutor cursor) throws DalException{
        super(cursor, tableName);
    }

    /**
     * Used to insert data into {@link DAO#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String, String[], String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
        createTableQueryBuilder.addColumn("source", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY);
        createTableQueryBuilder.addColumn("destination", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY);
        createTableQueryBuilder.addColumn("distance", ColumnType.REAL, ColumnModifier.NOT_NULL);
        createTableQueryBuilder.addColumn("duration", ColumnType.REAL, ColumnModifier.NOT_NULL);
        createTableQueryBuilder.addForeignKey(SitesDAO.tableName, "source", "address");
        createTableQueryBuilder.addForeignKey(SitesDAO.tableName, "destination", "address");
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public SiteRoute select(SiteRoute object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE source = '%s' AND destination = '%s';",
                TABLE_NAME,
                object.source(),
                object.destination()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select route between sites", e);
        }
        if(resultSet.next()) {
            SiteRoute fetched = getObjectFromResultSet(resultSet);
            cache.put(fetched);
            return fetched;
        } else {
            throw new DalException("No route between sites  with source " + object.source()+ " and destination " + object.destination()+ " was found");
        }
       
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<SiteRoute> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all site routes ", e);
        }
        List<SiteRoute> distances = new LinkedList<>();
        while(resultSet.next()) {
            distances.add(getObjectFromResultSet(resultSet));
        }
        cache.putAll(distances);
        return distances;
    }


    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(SiteRoute object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s','%s',%f,%f);",
                TABLE_NAME,
                object.source(),
                object.destination(),
                object.distance(),
                object.duration()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new RuntimeException("Unexpected error while trying to insert site route");
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert site route", e);
        }
    }

    public void insertAll(List<SiteRoute> objects) throws DalException {
        StringBuilder query = new StringBuilder();
        for(SiteRoute object : objects) {
            query.append(String.format("INSERT INTO %s VALUES ('%s','%s', %f,%f);",
                    TABLE_NAME,
                    object.source(),
                    object.destination(),
                    object.distance(),
                    object.duration()
            ));
        }
        try {
            if(cursor.executeWrite(query.toString()) != objects.size()){
                throw new RuntimeException("Unexpected error while trying to insert site routes");
            } else {
                cache.putAll(objects);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insertAll site routes", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(SiteRoute object) throws DalException {
        String query = String.format("UPDATE %s SET distance = %f, duration = %f WHERE source = '%s' AND destination = '%s';",
                TABLE_NAME,
                object.distance(),
                object.duration(),
                object.source(),
                object.destination()

        );
        try {
            if(cursor.executeWrite(query) != 1) {
                throw new DalException("Failed to update route between source " + object.source()+ " and destination " + object.destination());
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update site routes", e);
        }
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(SiteRoute object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE source = '%s' AND destination = '%s';",
                TABLE_NAME,
                object.source(),
                object.destination()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new DalException("Failed to delete route between source " + object.source()+ " and destination " + object.destination());
            } else {
                cache.remove(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete site route", e);
        }
    }

    @Override
    public boolean exists(SiteRoute object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE source = '%s' AND destination = '%s';",
                TABLE_NAME,
                object.source(),
                object.destination()
        );
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if site route exists", e);
        }
    }

    @Override
    protected SiteRoute getObjectFromResultSet(OfflineResultSet resultSet) {
        return new SiteRoute(
                resultSet.getString("source"),
                resultSet.getString("destination"),
                resultSet.getDouble("distance"),
                resultSet.getDouble("duration")
        );
    }
}
