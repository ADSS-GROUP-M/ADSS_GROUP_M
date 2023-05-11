package dataAccessLayer.dalAbstracts;

import exceptions.DalException;

public interface CounterDAO {
    Integer selectCounter() throws DalException;

    void insertCounter(Integer value) throws DalException;

    void incrementCounter() throws DalException;

    void resetCounter() throws DalException;
}
