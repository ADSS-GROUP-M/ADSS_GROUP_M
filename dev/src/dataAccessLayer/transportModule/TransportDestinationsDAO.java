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
    private static final String[] parent_tables = {"transports", "sites", "item_lists"};
    private static final String[] primary_keys = {"transport_id", "destination_index"};
    private static final String[][] foreign_keys = {{"transport_id"}, {"destination_address"}, {"item_list_id"}};
    private static final String[][] references = {{"id"}, {"address"}, {"id"}};

    public TransportDestinationsDAO() throws DalException{
        super("transport_destinations",
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                "transport_id",
                "destination_index",
                "destination_address",
                "item_list_id"
        );
        initTable();
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
                "destination_index",
                "destination_address",
                "item_list_id"
        );
        initTable();
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public TransportDestination select(TransportDestination object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE transport_id = %d AND destination_index = %d;",
                TABLE_NAME,
                object.transportId(),
                object.destination_index()
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
            throw new DalException("No transport destination with transport id " + object.transportId() + " and index " + object.destination_index() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<TransportDestination> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
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
        String query = String.format("SELECT * FROM %s WHERE transport_id = %d ORDER BY destination_index;", TABLE_NAME, object.id());
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
     * @throws RuntimeException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(TransportDestination object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES (%d, %d, '%s', %d);",
                TABLE_NAME,
                object.transportId(),
                object.destination_index(),
                object.address(),
                object.itemListId()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new RuntimeException("Unexpected error while trying to insert transport destination");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert transport destination", e);
        }
    }

    /**
     * @param objects - the objects to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    public void insertAll(List<TransportDestination> objects) throws DalException {
        StringBuilder query = new StringBuilder();
        for(TransportDestination object : objects) {
            query.append(String.format("INSERT INTO %s VALUES (%d, %d, '%s', %d);\n",
                    TABLE_NAME,
                    object.transportId(),
                    object.destination_index(),
                    object.address(),
                    object.itemListId()));
        }
        try {
            if(cursor.executeWrite(query.toString()) != objects.size()){
                throw new RuntimeException("Unexpected error while trying to insert transport destination");
            }
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
        String query = String.format("UPDATE %s SET destination_address = '%s', item_list_id = %d WHERE transport_id = %d AND destination_index = %d;",
                TABLE_NAME,
                object.address(),
                object.itemListId(),
                object.transportId(),
                object.destination_index()
        );
        try {
            if(cursor.executeWrite(query) != 1) {
                throw new DalException("Failed to update transport destination");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update transport destination", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(TransportDestination object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE transport_id = %d AND destination_index = %d;",
                TABLE_NAME,
                object.transportId(),
                object.destination_index()
        );
        try {
            if(cursor.executeWrite(query) != 1){
                throw new DalException("No transport destination with transport id "
                        + object.transportId() + " and index " + object.destination_index() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete transport destination", e);
        }
    }

    @Override
    public boolean exists(TransportDestination object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE transport_id = %d AND destination_index = %d;",
                TABLE_NAME,
                object.transportId(),
                object.destination_index()
        );
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if transport destination exists", e);
        }
    }

    public void deleteAllRelated(Transport object) throws DalException{

        String query = String.format("DELETE FROM %s WHERE transport_id = %d;", TABLE_NAME, object.id());
        try {
            if(cursor.executeWrite(query) == 0){
                throw new DalException("No transport destination with transport_id "
                        + object.id() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete transport destination", e);
        }
    }

    @Override
    protected TransportDestination getObjectFromResultSet(OfflineResultSet resultSet) {
        return new TransportDestination(
                resultSet.getInt("transport_id"),
                resultSet.getInt("destination_index"),
                resultSet.getString("destination_address"),
                resultSet.getInt("item_list_id")
        );
    }
}
