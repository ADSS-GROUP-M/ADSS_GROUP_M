package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.CounterDAO;
import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalAssociationClasses.transportModule.TransportMetaData;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class TransportsMetaDataDAO extends DAOBase<TransportMetaData> implements CounterDAO {

    public static final String primaryKey = "id";
    public static final String tableName = "transports";

    private final TransportIdCounterDAO counterDAO;

    public TransportsMetaDataDAO(SQLExecutor cursor, TransportIdCounterDAO counterDAO) throws DalException{
        super(cursor, tableName);
        this.counterDAO = counterDAO;
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
                .addColumn("id", ColumnType.INTEGER, ColumnModifier.PRIMARY_KEY)
                .addColumn("driver_id", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("truck_id", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("departure_time", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addColumn("weight", ColumnType.INTEGER, ColumnModifier.NOT_NULL)
                .addForeignKey("driver_id", DriversDAO.tableName, DriversDAO.primaryKey)
                .addForeignKey("truck_id", TrucksDAO.tableName, TrucksDAO.primaryKey);
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public TransportMetaData select(TransportMetaData object) throws DalException {

        if(cache.contains(object)) {
            return cache.get(object);
        }

        String query = String.format("SELECT * FROM %s WHERE id = %d;",
                TABLE_NAME,
                object.transportId()
        );
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select transport", e);
        }
        if (resultSet.next()){
            TransportMetaData selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            return selected;
        } else {
            throw new DalException("No transport with id " + object.transportId() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<TransportMetaData> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        List<TransportMetaData> TransportMetaDatas = new LinkedList<>();
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all transports", e);
        }
        while (resultSet.next()){
            TransportMetaData selected = getObjectFromResultSet(resultSet);
            TransportMetaDatas.add(selected);
        }
        cache.putAll(TransportMetaDatas);
        return TransportMetaDatas;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(TransportMetaData object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES (%d, '%s', '%s', '%s', %d);",
                TABLE_NAME,
                object.transportId(),
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight()
        );
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while inserting transport");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert transport",e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(TransportMetaData object) throws DalException {
        String query = String.format("UPDATE %s SET driver_id = '%s', truck_id = '%s', departure_time = '%s', weight = %d WHERE id = %d;",
                TABLE_NAME,
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight(),
                object.transportId()
        );
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new DalException("No transport with id " + object.transportId() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update transport",e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(TransportMetaData object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE id = %d;",
                TABLE_NAME,
                object.transportId()
        );
        try {
            if(cursor.executeWrite(query) == 1){
                cache.remove(object);
            } else {
                throw new DalException("No transport with id " + object.transportId() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete transport",e);
        }
    }

    @Override
    public boolean exists(TransportMetaData object) throws DalException {

        if(cache.contains(object)) {
            return true;
        }

        String query = String.format("SELECT * FROM %s WHERE id = %d;",
                TABLE_NAME,
                object.transportId()
        );
        try{
            return cursor.executeRead(query).isEmpty() == false;
        } catch (SQLException e) {
            throw new DalException("Failed to check if transport exists",e);
        }
    }

    @Override
    public void clearTable() {
        try {
            resetCounter();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        super.clearTable();
    }

    /**
     * @return Transport object where <br/> DeliveryRoute == null -> true
     */
    @Override
    protected TransportMetaData getObjectFromResultSet(OfflineResultSet resultSet) {
        return new TransportMetaData(
                resultSet.getInt("id"),
                resultSet.getString("driver_id"),
                resultSet.getString("truck_id"),
                resultSet.getLocalDateTime("departure_time"),
                resultSet.getInt("weight")
        );
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
