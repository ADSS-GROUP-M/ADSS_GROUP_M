package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.CounterDAO;
import dataAccessLayer.dalAbstracts.DAO;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalAssociationClasses.transportModule.DeliveryRoute;
import dataAccessLayer.dalAssociationClasses.transportModule.TransportMetaData;
import dataAccessLayer.dalUtils.Cache;
import domainObjects.transportModule.Transport;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TransportsDAO implements DAO<Transport>,CounterDAO {

    private final TransportsMetaDataDAO metaDataDAO;
    private final DeliveryRoutesDAO deliveryRoutesDAO;
    private final Cache<Transport> cache;
    private final SQLExecutor cursor;

    public TransportsDAO(SQLExecutor cursor, TransportsMetaDataDAO metaDataDAO, DeliveryRoutesDAO deliveryRoutesDAO) {
        this.cursor = cursor;
        this.metaDataDAO = metaDataDAO;
        this.deliveryRoutesDAO = deliveryRoutesDAO;
        this.cache = new Cache<>();
    }

    @Override
    public Integer selectCounter() throws DalException {
        return metaDataDAO.selectCounter();
    }

    @Override
    public void insertCounter(Integer value) throws DalException {
        metaDataDAO.insertCounter(value);
    }

    @Override
    public void incrementCounter() throws DalException {
        metaDataDAO.incrementCounter();
    }

    @Override
    public void resetCounter() throws DalException {
        metaDataDAO.resetCounter();
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Transport select(Transport object) throws DalException {

        if(cache.contains(object)){
            return cache.get(object);
        }

        DeliveryRoute route = deliveryRoutesDAO.select(DeliveryRoute.getLookupObject(object.id()));
        TransportMetaData metaData =  metaDataDAO.select(TransportMetaData.getLookupObject(object.id()));
        Transport selected = new Transport(metaData,route);
        cache.put(selected);
        return new Transport(metaData,route);
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Transport> selectAll() throws DalException {
        List<TransportMetaData> metaDataList = metaDataDAO.selectAll();
        List<Transport> transports = new LinkedList<>();
        for (TransportMetaData metaData : metaDataList) {
            DeliveryRoute route = deliveryRoutesDAO.select(DeliveryRoute.getLookupObject(metaData.transportId()));
            transports.add(new Transport(metaData,route));
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

        if(exists(object)){
            throw new DalException("Transport with id %d already exists".formatted(object.id()));
        }

        TransportMetaData metaData = new TransportMetaData(
                object.id(),
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight()
        );
        DeliveryRoute deliveryRoute =  new DeliveryRoute(
                object.id(),
                object.route(),
                object.itemLists(),
                object.estimatedArrivalTimes()
        );
        try {
            cursor.beginTransaction();
            metaDataDAO.insert(metaData);
            deliveryRoutesDAO.insert(deliveryRoute);
            cursor.commit();
        } catch (SQLException e) {
            throw new DalException("Failed to insert transport",e);
        }
        cache.put(object);
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Transport object) throws DalException {

        if(exists(object) == false){
            throw new DalException("Transport with id %d not found".formatted(object.id()));
        }

        TransportMetaData metaData = new TransportMetaData(
                object.id(),
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight()
        );
        DeliveryRoute newDeliveryRoute = new DeliveryRoute(
                object.id(),
                object.route(),
                object.itemLists(),
                object.estimatedArrivalTimes());

        try {
            cursor.beginTransaction();
            metaDataDAO.update(metaData);
            DeliveryRoute oldDeliveryRoute = deliveryRoutesDAO.select(DeliveryRoute.getLookupObject(object.id()));
            if(oldDeliveryRoute.deepEquals(newDeliveryRoute) == false){
                deliveryRoutesDAO.update(newDeliveryRoute);
            }
            cursor.commit();
        } catch (SQLException e) {
            throw new DalException("Failed to update transport",e);
        }
        cache.put(object);
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Transport object) throws DalException {

        if(exists(object) == false){
            throw new DalException("Transport with id %d not found".formatted(object.id()));
        }

        try {
            cursor.beginTransaction();
            deliveryRoutesDAO.delete(DeliveryRoute.getLookupObject(object.id()));
            metaDataDAO.delete(TransportMetaData.getLookupObject(object.id()));
            cursor.commit();
        } catch (SQLException e) {
            throw new DalException("Failed to delete transport",e);
        }
        cache.remove(object);
    }

    @Override
    public boolean exists(Transport object) throws DalException {
        if(cache.contains(object)){
            return true;
        }

        return metaDataDAO.exists(TransportMetaData.getLookupObject(object.id()));
    }

    /**
     * used for testing
     */
    public void clearTable() {
        deliveryRoutesDAO.clearTable();
        metaDataDAO.clearTable();
        clearCache();
    }

    public void clearCache() {
        cache.clear();
    }
}
