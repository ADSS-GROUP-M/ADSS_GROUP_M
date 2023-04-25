package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import objects.transportObjects.Transport;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TransportDestinationsDAO extends ManyToManyDAO<TransportDestination> {

    private static final String[] types = {"INTEGER", "INTEGER" , "TEXT", "INTEGER"};
    public static final String[] parent_tables = {"transports", "sites", "item_lists"};
    public static final String[] primary_keys = {"transport_id", "index"};
    public static final String[] references = {"id", "address", "id"};
    public static final String[] foreign_keys = {"transport_id", "destination_address", "item_list_id"};

    public TransportDestinationsDAO() throws DalException{
        super("transport_destinations",
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                "transport_id",
                "index",
                "destination_address",
                "item_list_id"
        );
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public TransportDestinationsDAO(String dbName) throws DalException{
        super(dbName,
                "transport_destinations",
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                "transport_id",
                "index",
                "destination_address",
                "item_list_id"
        );
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public TransportDestination select(TransportDestination object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE %s = %s AND %s = %s",
                TABLE_NAME,
                primary_keys[0],
                object.transportId(),
                primary_keys[1],
                object.index()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select transport destination", e);
        }
        if(resultSet.next()) {
            return getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("No transport destination with transport id " + object.transportId() + " and index " + object.index() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<TransportDestination> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all transport destinations", e);
        }
        List<TransportDestination> transportDestinations = new LinkedList<>();
        while(resultSet.next()) {
            transportDestinations.add(getObjectFromResultSet(resultSet));
        }
        return transportDestinations;
    }

    /**
     * @return All the objects that are related to the given object
     * @throws DalException if an error occurred while trying to select the objects
     */
    public List<TransportDestination> selectAllRelated(Transport object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE transport_id = %s ORDER BY index", TABLE_NAME, object.id());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select related transport destinations", e);
        }
        List<TransportDestination> transportDestinations = new LinkedList<>();
        while(resultSet.next()) {
            transportDestinations.add(getObjectFromResultSet(resultSet));
        }
        return transportDestinations;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(TransportDestination object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES (%s, %s, %s, %s)",
                TABLE_NAME,
                object.transportId(),
                object.index(),
                object.address(),
                object.itemListId()
        );
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to insert transport destination", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(TransportDestination object) throws DalException {
        String query = String.format("UPDATE %s SET destination_address = '%s', item_list_id = %d WHERE transport_id = %d AND index = %d",
                TABLE_NAME,
                object.address(),
                object.itemListId(),
                object.transportId(),
                object.index()
        );
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to update transport destination", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(TransportDestination object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE transport_id = %d AND index = %d",
                TABLE_NAME,
                object.transportId(),
                object.index()
        );
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to delete transport destination", e);
        }
    }

    public void deleteAllRelated(Transport object) throws DalException{
        for (TransportDestination destination : selectAllRelated(object)) {
                delete(destination);
        }
    }

    @Override
    protected TransportDestination getObjectFromResultSet(OfflineResultSet resultSet) {
        return new TransportDestination(
                resultSet.getInt("transport_id"),
                resultSet.getInt("index"),
                resultSet.getString("destination_address"),
                resultSet.getInt("item_list_id")
        );
    }
}
