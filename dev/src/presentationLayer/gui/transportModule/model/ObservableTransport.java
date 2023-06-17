package presentationLayer.gui.transportModule.model;

import dataAccessLayer.dalAssociationClasses.transportModule.DeliveryRoute;
import dataAccessLayer.dalAssociationClasses.transportModule.TransportMetaData;
import domainObjects.transportModule.Transport;
import presentationLayer.gui.plAbstracts.ObjectObserver;
import presentationLayer.gui.plAbstracts.ObservableObject;
import presentationLayer.gui.plAbstracts.Searchable;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class ObservableTransport extends Transport implements Searchable, ObservableObject<Transport> {
    /**
     * This constructor allows setting all fields.
     * Generally, this constructor should not be used outside of special cases
     * that require preserving some state (e.g. when updating a transport).
     *
     * @param id
     * @param route
     * @param destinations_itemListIds
     * @param driverId
     * @param truckId
     * @param departureTime
     * @param weight
     * @param estimatedArrivalTimes
     * @param manualOverride
     */
    public ObservableTransport(int id, List<String> route, Map<String, Integer> destinations_itemListIds, String driverId, String truckId, LocalDateTime departureTime, int weight, Map<String, LocalTime> estimatedArrivalTimes, boolean manualOverride) {
        super(id, route, destinations_itemListIds, driverId, truckId, departureTime, weight, estimatedArrivalTimes, manualOverride);
    }

    public ObservableTransport(int id, List<String> route, Map<String, Integer> destinations_itemListIds, String driverId, String truckId, LocalDateTime departureTime, int weight, Map<String, LocalTime> estimatedArrivalTimes) {
        super(id, route, destinations_itemListIds, driverId, truckId, departureTime, weight, estimatedArrivalTimes);
    }

    /**
     * this constructor sets the id to be -1
     *
     * @param route
     * @param destinations_itemListIds
     * @param driverId
     * @param truckId
     * @param departureTime
     * @param weight
     * @param estimatedArrivalTimes
     */
    public ObservableTransport(List<String> route, Map<String, Integer> destinations_itemListIds, String driverId, String truckId, LocalDateTime departureTime, int weight, Map<String, LocalTime> estimatedArrivalTimes) {
        super(route, destinations_itemListIds, driverId, truckId, departureTime, weight, estimatedArrivalTimes);
    }

    public ObservableTransport(TransportMetaData metaData, DeliveryRoute route) {
        super(metaData, route);
    }

    public ObservableTransport(int newId, Transport old) {
        super(newId, old);
    }

    public ObservableTransport(int id, List<String> route, Map<String, Integer> itemLists, String driverId, String truckId, LocalDateTime departureTime, int weight) {
        super(id, route, itemLists, driverId, truckId, departureTime, weight);
    }

    /**
     * this constructor sets the id to be -1
     *
     * @param route
     * @param destinations_itemListIds
     * @param driverId
     * @param truckId
     * @param departureTime
     * @param weight
     */
    public ObservableTransport(List<String> route, Map<String, Integer> destinations_itemListIds, String driverId, String truckId, LocalDateTime departureTime, int weight) {
        super(route, destinations_itemListIds, driverId, truckId, departureTime, weight);
    }

    @Override
    public void subscribe(ObjectObserver<Transport> observer) {

    }

    @Override
    public void unsubscribe(ObjectObserver<Transport> observer) {

    }

    @Override
    public void notifyObservers() {

    }

    @Override
    public Transport getUpdate() {
        return null;
    }

    @Override
    public boolean isMatch(String query) {
        return false;
    }

    @Override
    public String getShortDescription() {
        return null;
    }

    @Override
    public String getLongDescription() {
        return null;
    }
}
