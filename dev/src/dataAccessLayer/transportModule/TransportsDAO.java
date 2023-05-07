package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.transportModule.abstracts.CounterDAO;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import objects.transportObjects.DeliveryRoute;
import objects.transportObjects.Transport;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TransportsDAO extends ManyToManyDAO<Transport> implements CounterDAO {

    private static final String[] types = {"INTEGER", "TEXT", "TEXT", "TEXT", "TEXT", "INTEGER"};
    private static final String[] parent_tables = {"truck_drivers", "trucks", "sites"};
    private static final String[] primary_keys = {"id"};
    private static final String[][] foreign_keys = {{"driver_id"}, {"truck_id"},{"source_address"}};
    private static final String[][] references = {{"id"}, {"id"}, {"address"}};
    public static final String tableName = "transports";

    private final TransportDestinationsDAO destinationsDAO;
    private final TransportIdCounterDAO counterDAO;

    public TransportsDAO(SQLExecutor cursor, TransportDestinationsDAO destinationsDAO, TransportIdCounterDAO counterDAO) throws DalException{
        super(cursor,
				tableName,
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
        this.destinationsDAO = destinationsDAO;
        this.counterDAO = counterDAO;
        initTable();
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Transport select(Transport object) throws DalException {

        if(cache.contains(object)) {
            return cache.get(object);
        }

        List<TransportDestination> transportDestinations = destinationsDAO.selectAllRelated(object);
        String query = String.format("SELECT * FROM %s WHERE id = %d;",
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
            Transport selected = getObjectFromResultSet(resultSet, transportDestinations);
            cache.put(selected);
            return selected;
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
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
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
                    destinationsDAO.selectAllRelated(lookupObject)));
        }
        cache.putAll(transports);
        return transports;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Transport object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', %d);",
                TABLE_NAME,
                object.id(),
                object.source(),
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight()
        );
        try {
            if(cursor.executeWrite(query) == 1){
                LinkedList<TransportDestination> transportDestinations = generateTransportDestinations(object);
                destinationsDAO.insertAll(transportDestinations);
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while inserting transport");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert transport");
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Transport object) throws DalException {
        String query = String.format("UPDATE %s SET source_address = '%s', driver_id = '%s', truck_id = '%s', departure_time = '%s', weight = %d WHERE id = %d;",
                TABLE_NAME,
                object.source(),
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight(),
                object.id()
        );
        try {
            destinationsDAO.deleteAllRelated(object);
            if(cursor.executeWrite(query) == 1){
                LinkedList<TransportDestination> transportDestinations = generateTransportDestinations(object);
                destinationsDAO.insertAll(transportDestinations);
                cache.put(object);
            } else {
                throw new DalException("No transport with id " + object.id() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update transport");
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Transport object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE id = %d;",
                TABLE_NAME,
                object.id()
        );
        try {
            destinationsDAO.deleteAllRelated(object);
            if(cursor.executeWrite(query) == 1){
                cache.remove(object);
            } else {
                throw new DalException("No transport with id " + object.id() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete transport");
        }
    }

    @Override
    public boolean exists(Transport object) throws DalException {

        if(cache.contains(object)) {
            return true;
        }

        String query = String.format("SELECT * FROM %s WHERE id = %d;",
                TABLE_NAME,
                object.id()
        );
        try{
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if transport exists");
        }
    }

    protected Transport getObjectFromResultSet(OfflineResultSet resultSet, List<TransportDestination> transportDestinations){
        LinkedList<String> destinations = new LinkedList<>();
        HashMap<String,Integer> itemLists = new HashMap<>();
        HashMap<String, LocalTime> estimatedTimesOfArrival = new HashMap<>();
        for(TransportDestination transportDestination : transportDestinations){
            destinations.add(transportDestination.address());
            itemLists.put(transportDestination.address(), transportDestination.itemListId());
            estimatedTimesOfArrival.put(transportDestination.address(), transportDestination.expectedArrivalTime());
        }

        String sourceAddress = resultSet.getString("source_address");
        return new Transport(
                resultSet.getInt("id"),
                new DeliveryRoute(sourceAddress, destinations,itemLists, estimatedTimesOfArrival),
                resultSet.getString("driver_id"),
                resultSet.getString("truck_id"),
                resultSet.getLocalDateTime("departure_time"),
                resultSet.getInt("weight")
        );
    }

    private LinkedList<TransportDestination> generateTransportDestinations(Transport object) {
        LinkedList<TransportDestination> transportDestinations = new LinkedList<>();
        DeliveryRoute deliveryRoute = object.deliveryRoute();
        int i = 1;
        for(String destination : object.destinations()){
            transportDestinations.add(new TransportDestination(
                    object.id(),
                    i++,
                    destination,
                    object.itemLists().get(destination),
                    deliveryRoute.getEstimatedTimeOfArrival(destination)
            ));
        }
        return transportDestinations;
    }
    @Override
    public void clearTable() {
        destinationsDAO.clearTable();
        try {
            resetCounter();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        super.clearTable();
    }

    /**
     * @deprecated use {@link #getObjectFromResultSet(OfflineResultSet, List)} instead
     */
    @Deprecated
    protected Transport getObjectFromResultSet(OfflineResultSet resultSet) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Integer selectCounter() throws DalException {
        return counterDAO.selectCounter();
    }

    @Override
    public void insertCounter(Integer value) throws DalException {
        counterDAO.insertCounter(value);
    }

    @Override
    public void incrementCounter() throws DalException {
        counterDAO.incrementCounter();
    }

    @Override
    public void resetCounter() throws DalException {
        counterDAO.resetCounter();
    }
}
