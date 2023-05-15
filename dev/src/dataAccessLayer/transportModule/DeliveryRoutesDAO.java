package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.ManyToManyDAO;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;
import objects.transportObjects.DeliveryRoute;
import objects.transportObjects.Transport;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

public class DeliveryRoutesDAO extends ManyToManyDAO<DeliveryRoute> {

    private static final String[] types = {"INTEGER", "INTEGER" , "TEXT", "INTEGER", "TEXT"};
    private static final String[] parent_tables = {"transports", "sites", "item_lists"};
    private static final String[] primary_keys = {"transport_id", "destination_index"};
    private static final String[][] foreign_keys = {{"transport_id"}, {"destination_name"}, {"item_list_id"}};
    private static final String[][] references = {{"id"}, {"name"}, {"id"}};
    public static final String tableName = "delivery_routes";

    public DeliveryRoutesDAO(SQLExecutor cursor) throws DalException{
        super(cursor,
				tableName,
                parent_tables,
                types,
                primary_keys,
                foreign_keys,
                references,
                "transport_id",
                "destination_index",
                "destination_name",
                "item_list_id",
                "expected_arrival_time"
        );
        initTable();
    }

    //TODO: ADD CACHING

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public DeliveryRoute select(DeliveryRoute object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE transport_id = %d ORDER BY destination_index;",
                TABLE_NAME,
                object.transportId()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select delivery route", e);
        }
        if(resultSet.isEmpty() == false) {
            DeliveryRoute selected =  getObjectFromResultSet(resultSet);
            cache.put(selected);
            return selected;
        } else {
            throw new DalException("No delivery route with transport id " + object.transportId() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<DeliveryRoute> selectAll() throws DalException {

        String idQuery = String.format("SELECT DISTINCT transport_id FROM %s;", TABLE_NAME);
        OfflineResultSet idResultSet;
        try {
            idResultSet = cursor.executeRead(idQuery);
        } catch (SQLException e) {
            throw new DalException("Failed to select all delivery routes", e);
        }
        List<Integer> ids = new LinkedList<>();
        while(idResultSet.next()) {
            ids.add(idResultSet.getInt("transport_id"));
        }
        List<DeliveryRoute> deliveryRoutes = new LinkedList<>();
        for(Integer id : ids){
            String query = String.format("SELECT * FROM %s where transport_id = %d;", TABLE_NAME, id);
            OfflineResultSet resultSet;
            try {
                resultSet = cursor.executeRead(query);
            } catch (SQLException e) {
                throw new DalException("Failed to select all delivery routes", e);
            }
            if(resultSet.isEmpty() == false) {
                deliveryRoutes.add(getObjectFromResultSet(resultSet));
            }
        }
        cache.putAll(deliveryRoutes);
        return deliveryRoutes;
    }

    /**
     * @param object - the object to insert
     * @throws RuntimeException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(DeliveryRoute object) throws DalException {
        String query = buildInsertQuery(object);
        try {
            if(cursor.executeWrite(query) != object.route().size()){
                throw new RuntimeException("Unexpected error while trying to insert delivery route");
            }
            cache.put(object);
        } catch (SQLException e) {
            throw new DalException("Failed to insert delivery route", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(DeliveryRoute object) throws DalException {

        if(exists(object) == false){
            throw new DalException("No delivery route with transport id "
                    + object.transportId() + " was found");
        }

        String query = buildDeleteQuery(object) + buildInsertQuery(object);
        try {
            if(cursor.executeWrite(query) == 0){
                throw new DalException("Failed to update delivery route");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update delivery route", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(DeliveryRoute object) throws DalException {
        String query = buildDeleteQuery(object);
        try {
            if(cursor.executeWrite(query) == 0){
                throw new DalException("No delivery route with transport id "
                        + object.transportId() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete related delivery routes", e);
        }
    }

    @Override
    public boolean exists(DeliveryRoute object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE transport_id = %d;",
                TABLE_NAME,
                object.transportId()
        );
        try {
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if delivery route exists", e);
        }
    }

    @Override
    protected DeliveryRoute getObjectFromResultSet(OfflineResultSet resultSet) {
        List<String> route = new LinkedList<>();
        HashMap<String, Integer> itemLists = new HashMap<>();
        HashMap<String, LocalTime> estimatedTimesOfArrival = new HashMap<>();
        while(resultSet.next()) {
            route.add(resultSet.getString("destination_name"));
            itemLists.put(resultSet.getString("destination_name"), resultSet.getInt("item_list_id"));
            estimatedTimesOfArrival.put(resultSet.getString("destination_name"), resultSet.getLocalTime("expected_arrival_time"));
        }
        int transportId = resultSet.getInt("transport_id");
        return new DeliveryRoute(transportId, route, itemLists, estimatedTimesOfArrival);
    }

    private String buildInsertQuery(DeliveryRoute object) {
        StringBuilder queryBuilder = new StringBuilder();
        int i = 0;
        for(String destination : object.route()) {
            queryBuilder.append(String.format("INSERT INTO %s VALUES (%d, %d, '%s', %d, '%s');\n",
                    TABLE_NAME,
                    object.transportId(),
                    i++,
                    destination,
                    object.itemLists().get(destination),
                    object.getEstimatedTimeOfArrival(destination).toString()));
        }
        return queryBuilder.toString();
    }

    private String buildDeleteQuery(DeliveryRoute object) {
        return String.format("DELETE FROM %s WHERE transport_id = %d;\n",
                TABLE_NAME,
                object.transportId());
    }

   }
