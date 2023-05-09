package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SitesDistancesDAO extends ManyToManyDAO<DistanceBetweenSites> {

    private static final String[] types = {"TEXT", "TEXT" , "REAL","REAL"};
    private static final String[] parent_tables = {"sites", "sites"};
    private static final String[] primary_keys = {"source", "destination"};
    private static final String[][] foreign_keys = {{"source"}, {"destination"}};
    private static final String[][] references = {{"address"}, {"address"}};
    public static final String tableName = "sites_distances";

    public SitesDistancesDAO(SQLExecutor cursor) throws DalException{
        super(cursor,
                tableName,
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                "source",
                "destination",
                "distance",
                "duration"
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
            DistanceBetweenSites fetched = getObjectFromResultSet(resultSet);
            cache.put(fetched);
            return fetched;
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
        List<DistanceBetweenSites> distances = new LinkedList<>();
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
    public void insert(DistanceBetweenSites object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s','%s',%f,%f);",
                TABLE_NAME,
                object.source(),
                object.destination(),
                object.distance(),
                object.duration()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new RuntimeException("Unexpected error while trying to insert distance between sites");
            } else {
                cache.put(object);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert distance between sites", e);
        }
    }

    public void insertAll(List<DistanceBetweenSites> objects) throws DalException {
        StringBuilder query = new StringBuilder();
        for(DistanceBetweenSites object : objects) {
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
                throw new RuntimeException("Unexpected error while trying to insert distance between sites");
            } else {
                cache.putAll(objects);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insertAll distance between sites", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(DistanceBetweenSites object) throws DalException {
        String query = String.format("UPDATE %s SET distance = %f, duration = %f WHERE source = '%s' AND destination = '%s';",
                TABLE_NAME,
                object.distance(),
                object.duration(),
                object.source(),
                object.destination()

        );
        try {
            if(cursor.executeWrite(query) != 1) {
                throw new DalException("Failed to update distance between sites with source " + object.source()+ " and destination " + object.destination());
            } else {
                cache.put(object);
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
            } else {
                cache.remove(object);
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
                resultSet.getInt("distance"),
                resultSet.getDouble("duration")
        );
    }
}
