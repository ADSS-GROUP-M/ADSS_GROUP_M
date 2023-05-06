package dataAccessLayer.dalUtils;

import java.sql.SQLException;

public interface SQLExecutor {
    OfflineResultSet executeRead(String query) throws SQLException;
    int executeWrite(String query) throws SQLException;
}
