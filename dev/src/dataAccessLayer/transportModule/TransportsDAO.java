package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.CounterDAO;
import dataAccessLayer.dalAbstracts.DAO;
import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAssociationClasses.transportModule.TransportMetaData;
import exceptions.DalException;
import objects.transportObjects.DeliveryRoute;
import objects.transportObjects.Transport;

import java.util.LinkedList;
import java.util.List;

public class TransportsDAO implements DAO<Transport>,CounterDAO {

    private final TransportsMetaDataDAO metaDataDAO;
    private final DeliveryRoutesDAO deliveryRoutesDAO;

    public TransportsDAO(TransportsMetaDataDAO metaDataDAO, DeliveryRoutesDAO deliveryRoutesDAO) {
        this.metaDataDAO = metaDataDAO;
        this.deliveryRoutesDAO = deliveryRoutesDAO;
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
        DeliveryRoute route = deliveryRoutesDAO.select(DeliveryRoute.getLookupObject(object.id()));
        TransportMetaData metaData =  metaDataDAO.select(TransportMetaData.getLookupObject(object.id()));
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
        return transports;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Transport object) throws DalException {
        TransportMetaData metaData = new TransportMetaData(
                object.id(),
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight()
        );
        metaDataDAO.insert(metaData);
        deliveryRoutesDAO.insert(object.deliveryRoute());
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Transport object) throws DalException {
        TransportMetaData metaData = new TransportMetaData(
                object.id(),
                object.driverId(),
                object.truckId(),
                object.departureTime(),
                object.weight()
        );
        metaDataDAO.update(metaData);

        DeliveryRoute newDeliveryRoute = object.deliveryRoute();
        DeliveryRoute oldDeliveryRoute =  deliveryRoutesDAO.select(DeliveryRoute.getLookupObject(object.id()));

        if(oldDeliveryRoute.deepEquals(newDeliveryRoute) == false){
            deliveryRoutesDAO.update(newDeliveryRoute);
        }
    }

    /**
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Transport object) throws DalException {
        deliveryRoutesDAO.delete(DeliveryRoute.getLookupObject(object.id()));
        metaDataDAO.delete(TransportMetaData.getLookupObject(object.id()));
    }

    @Override
    public boolean exists(Transport object) throws DalException {
        return metaDataDAO.exists(TransportMetaData.getLookupObject(object.id()));
    }

    /**
     * used for testing
     */
    public void clearTable() {
        deliveryRoutesDAO.clearTable();
        metaDataDAO.clearTable();
    }

    public void clearCache() {
        deliveryRoutesDAO.clearCache();
        metaDataDAO.clearCache();
    }
}
