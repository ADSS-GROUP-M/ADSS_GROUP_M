package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import objects.transportObjects.Transport;

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
        return null;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Transport> selectAll() throws DalException {
        return null;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Transport object) throws DalException {

    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Transport object) throws DalException {

    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Transport object) throws DalException {

    }

    @Override
    protected Transport getObjectFromResultSet(OfflineResultSet resultSet) {
        return null;
    }
}
