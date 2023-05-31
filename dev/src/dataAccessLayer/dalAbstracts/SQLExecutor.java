package dataAccessLayer.dalAbstracts;

import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;

public interface SQLExecutor extends Cloneable{
    void beginTransaction() throws SQLException;
    void commit() throws SQLException;
    void rollback() throws SQLException;
    OfflineResultSet executeRead(String query) throws SQLException;
    int executeWrite(String query) throws SQLException;
    SQLExecutor clone();
}
