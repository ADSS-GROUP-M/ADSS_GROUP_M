package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SitesDistancesDAO extends ManyToManyDAO<DistanceBetweenSites> {

    private static final String[] types = {"TEXT", "TEXT" , "INTEGER"};
    private static final String[] parent_tables = {"sites", "sites"};
    private static final String[] primary_keys = {"source", "destination"};
    private static final String[][] foreign_keys = {{"source"}, {"destination"}};
    private static final String[][] references = {{"address"}, {"address"}};

    public SitesDistancesDAO() throws DalException{
        super("sites_distances",
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                "source",
                "destination",
                "distance"
        );
        initTable();
    }
    public SitesDistancesDAO(String dbName) throws DalException{
        super("sites_distances",
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                "source",
                "destination",
                "distance"

        );
        initTable();
    }


    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public DistanceBetweenSites select(DistanceBetweenSites object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE source = '%s' AND destination = '%s';",
                TABLE_NAME,
                object.source(),
                object.destination()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select distance between sites", e);
        }
        if(resultSet.next()) {
            return getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("No distance between sites  with source " + object.source()+ " and destination " + object.destination()+ " was found");
        }
       
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<DistanceBetweenSites> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all distance between sites", e);
        }
        List<DistanceBetweenSites> distanceBetweenSites = new LinkedList<>();
        while(resultSet.next()) {
            distanceBetweenSites.add(getObjectFromResultSet(resultSet));
        }
        return distanceBetweenSites;
    }


    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(DistanceBetweenSites object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s','%s', %d);",
                TABLE_NAME,
                object.source(),
                object.destination(),
                object.distance()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new RuntimeException("Unexpected error while trying to insert distance between sites");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert distance between sites", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(DistanceBetweenSites object) throws DalException {
        String query = String.format("UPDATE %s SET distance = %d WHERE source = '%s' AND destination = '%s';",
                TABLE_NAME,
                object.distance(),
                object.source(),
                object.destination()

        );
        try {
            if(cursor.executeWrite(query) != 1) {
                throw new DalException("Failed to update distance between sites with source " + object.source()+ " and destination " + object.destination());
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update distance between sites", e);
        }
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(DistanceBetweenSites object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE source = '%s' AND destination = '%s';",
                TABLE_NAME,
                object.source(),
                object.destination()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new DalException("Failed to delete distance between sites with source " + object.source()+ " and destination " + object.destination());
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete distance between sites", e);
        }
    }

    @Override
    public boolean exists(DistanceBetweenSites object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE source = '%s' AND destination = '%s';",
                TABLE_NAME,
                object.source(),
                object.destination()
        );
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if transport destination exists", e);
        }
    }

    @Override
    protected DistanceBetweenSites getObjectFromResultSet(OfflineResultSet resultSet) {
        return new DistanceBetweenSites(
                resultSet.getString("source"),
                resultSet.getString("destination"),
                resultSet.getInt("distance")
        );
    }
}
