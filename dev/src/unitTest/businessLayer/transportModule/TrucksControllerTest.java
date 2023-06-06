package businessLayer.transportModule;

import dataAccessLayer.transportModule.TrucksDAO;
import domainObjects.transportModule.Truck;
import exceptions.DalException;
import exceptions.TransportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrucksControllerTest {

    private TrucksController controller;
    private TrucksDAO dao;
    private Truck truck;


    @BeforeEach
    void setUp() {
        dao = mock(TrucksDAO.class);
        controller = new TrucksController(dao);
        truck = new Truck("123abc","chevy", 100, 200, Truck.CoolingCapacity.FROZEN);
    }

    @Test
    void addTruck() {
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertDoesNotThrow(() -> controller.addTruck(truck));
    }

    @Test
    void addTruckAlreadyExists(){
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(true);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertThrows(TransportException.class, () -> controller.addTruck(truck));
    }

    @Test
    void addTruckNegativeBaseWeight(){

        Truck badTruck = new Truck("123abc","chevy", -100, 200, Truck.CoolingCapacity.FROZEN);
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        Throwable ex = assertThrows(TransportException.class, () -> controller.addTruck(badTruck));
        assertEquals("negativeBaseWeight", ex.getCause().getMessage());
    }

    @Test
    void addTruckNegativeMaxWeight(){

        Truck badTruck = new Truck("123abc","chevy", 100, -200, Truck.CoolingCapacity.FROZEN);
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        Throwable ex = assertThrows(TransportException.class, () -> controller.addTruck(badTruck));
        assertEquals("negativeMaxWeight,baseWeightGreaterThanMaxWeight", ex.getCause().getMessage());
    }

    @Test
    void addTruckBaseWeightGreaterThanMaxWeight(){

        Truck badTruck = new Truck("123abc","chevy", 200, 100, Truck.CoolingCapacity.FROZEN);
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        Throwable ex = assertThrows(TransportException.class, () -> controller.addTruck(badTruck));
        assertEquals("baseWeightGreaterThanMaxWeight", ex.getCause().getMessage());
    }

    @Test
    void removeTruck() {
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(true);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertDoesNotThrow(() -> controller.removeTruck(truck.id()));
    }

    @Test
    void removeTruckDoesNotExist(){
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertThrows(TransportException.class, () -> controller.removeTruck(truck.id()));
    }

    @Test
    void updateTruck() {
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(true);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertDoesNotThrow(() -> controller.updateTruck(truck.id(),truck));
    }

    @Test
    void updateTruckDoesNotExist(){
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertThrows(TransportException.class, () -> controller.updateTruck(truck.id(),truck));
    }

    @Test
    void updateTruckNegativeBaseWeight(){
        Truck badTruck = new Truck("123abc","chevy", -100, 200, Truck.CoolingCapacity.FROZEN);
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(true);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        Throwable ex = assertThrows(TransportException.class, () -> controller.updateTruck(truck.id(),badTruck));
        assertEquals("negativeBaseWeight", ex.getCause().getMessage());
    }

    @Test
    void updateTruckNegativeMaxWeight(){
        Truck badTruck = new Truck("123abc","chevy", 100, -200, Truck.CoolingCapacity.FROZEN);
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(true);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        Throwable ex = assertThrows(TransportException.class, () -> controller.updateTruck(truck.id(),badTruck));
        assertEquals("negativeMaxWeight,baseWeightGreaterThanMaxWeight", ex.getCause().getMessage());
    }

    @Test
    void updateTruckBaseWeightGreaterThanMaxWeight(){
        Truck badTruck = new Truck("123abc","chevy", 200, 100, Truck.CoolingCapacity.FROZEN);
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(true);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        Throwable ex = assertThrows(TransportException.class, () -> controller.updateTruck(truck.id(),badTruck));
        assertEquals("baseWeightGreaterThanMaxWeight", ex.getCause().getMessage());
    }

    @Test
    void getTruck() {
        try{
            Truck lookupObject = Truck.getLookupObject(truck.id());
            when(dao.exists(lookupObject)).thenReturn(true);
            when(dao.select(lookupObject)).thenReturn(truck);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        Truck fetched = assertDoesNotThrow(() -> controller.getTruck(truck.id()));
        assertDeepEquals(truck, fetched);
    }

    @Test
    void getTruckDoesNotExist(){
        try{
            when(dao.exists(Truck.getLookupObject(truck.id()))).thenReturn(false);
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        assertThrows(TransportException.class, () -> controller.getTruck(truck.id()));
    }

    @Test
    void getAllTrucks() {
        Truck truck2 = new Truck("123abc","chevy", 100, 200, Truck.CoolingCapacity.FROZEN);
        try{
            when(dao.selectAll()).thenReturn(List.of(truck, truck2));
        } catch (DalException e) {
            fail(e.getMessage(),e.getCause());
        }
        List<Truck> fetched = assertDoesNotThrow(() -> controller.getAllTrucks());
        assertEquals(2, fetched.size());
        assertDeepEquals(truck, fetched.get(0));
        assertDeepEquals(truck2, fetched.get(1));
    }

    private void assertDeepEquals(Truck truck1, Truck truck2) {
        assertEquals(truck1.id(), truck2.id());
        assertEquals(truck1.model(), truck2.model());
        assertEquals(truck1.baseWeight(), truck2.baseWeight());
        assertEquals(truck1.maxWeight(), truck2.maxWeight());
        assertEquals(truck1.coolingCapacity(), truck2.coolingCapacity());
    }
}