package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import objects.transportObjects.Transport;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TransportsDAO extends ManyToManyDAO<Transport> {

    private static final String[] types = {"INTEGER", "TEXT", "TEXT", "TEXT", "TEXT", "INTEGER"};
    private static final String[] parent_tables = {"drivers", "trucks", "sites"};
    private static final String[] primary_keys = {"id"};
    private static final String[] foreign_keys = {"source_address", "driver_id", "truck_id"};
    private static final String[] references = {"id", "id", "address"};

    private final TransportDestinationsDAO transportDestinationsDAO;

    public TransportsDAO() throws DalException{
        super("transports",
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                "id",
                "source_address",
                "driver_id",
                "truck_id",
                "departure_time",
                "weight"
        );
        transportDestinationsDAO = new TransportDestinationsDAO();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public TransportsDAO(String dbName) throws DalException{
        super(dbName,
                "transports",
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                "id",
                "source_address",
                "driver_id",
                "truck_id",
                "departure_time",
                "weight"
        );
        transportDestinationsDAO = new TransportDestinationsDAO(dbName);
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Transport select(Transport object) throws DalException {
        List<TransportDestination> transportDestinations = transportDestinationsDAO.selectAllRelated(object);
        String query = String.format("SELECT * FROM %s WHERE id = %d",
                TABLE_NAME,
                object.id()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (resultSet.next()){
            return getObjectFromResultSet(resultSet, transportDestinations);
        } else {
            throw new DalException("No transport with id " + object.id() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Transport> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s", TABLE_NAME);
        List<Transport> transports = new LinkedList<>();
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        while (resultSet.next()){
            Transport lookupObject = Transport.getLookupObject(resultSet.getInt("id"));
            transports.add(getObjectFromResultSet(
                    resultSet,
                    transportDestinationsDAO.selectAllRelated(lookupObject)));
        }
        return transports;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Transport object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', %d)",
                TABLE_NAME,
                object.id(),
                object.source(),
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight()
        );
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int i = 1;
        for(String destination : object.destinations()){
            transportDestinationsDAO.insert(new TransportDestination(
                    object.id(),
                    i++,
                    destination,
                    object.itemLists().get(destination)
            ));
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Transport object) throws DalException {
        String query = String.format("UPDATE %s SET source_address = '%s', driver_id = '%s', truck_id = '%s', departure_time = '%s', weight = %d WHERE id = %d",
                TABLE_NAME,
                object.source(),
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight(),
                object.id()
        );
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        transportDestinationsDAO.deleteAllRelated(object);
        int i = 1;
        for(String destination : object.destinations()){
            transportDestinationsDAO.insert(new TransportDestination(
                    object.id(),
                    i++,
                    destination,
                    object.itemLists().get(destination)
            ));
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Transport object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE id = %d",
                TABLE_NAME,
                object.id()
        );
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        transportDestinationsDAO.deleteAllRelated(object);
    }

    protected Transport getObjectFromResultSet(OfflineResultSet resultSet, List<TransportDestination> transportDestinations){
        LinkedList<String> destinations = new LinkedList<>();
        HashMap<String,Integer> itemLists = new HashMap<>();
        for(TransportDestination transportDestination : transportDestinations){
            destinations.add(transportDestination.address());
            itemLists.put(transportDestination.address(), transportDestination.itemListId());
        }

        return new Transport(
                resultSet.getInt("id"),
                resultSet.getString("source_address"),
                destinations,
                itemLists,
                resultSet.getString("driver_id"),
                resultSet.getString("truck_id"),
                resultSet.getLocalDateTime("departure_time"),
                resultSet.getInt("weight")
        );
    }

    /**
     * @deprecated use {@link #getObjectFromResultSet(OfflineResultSet, List)} instead
     */
    @Deprecated
    protected Transport getObjectFromResultSet(OfflineResultSet resultSet) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
