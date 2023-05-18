package dataAccessLayer.dalUtils;

import java.sql.*;

public class SQLExecutorProductionImpl implements SQLExecutor {

    private static final String DEFAULT_DB_NAME = "SuperLiDB.db";
    private static final String URL_PREFIX = "jdbc:sqlite::resource:";
    private final String URL;

    /**
     * connects to the {@link #DEFAULT_DB_NAME} database
     */
    public SQLExecutorProductionImpl() {
        URL = URL_PREFIX + DEFAULT_DB_NAME;
    }

    /**
     * @param dbName the name of the database to connect to
     */
    public SQLExecutorProductionImpl(String dbName) {
        URL = URL_PREFIX + dbName;
    }

    @Override
    public OfflineResultSet executeRead(String query) throws SQLException {

        try (Connection connection = DriverManager.getConnection(URL)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return new OfflineResultSet(resultSet);
        }
    }

    @Override
    public int executeWrite(String query) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL)) {
            Statement statement = connection.createStatement();
            query = "PRAGMA foreign_keys = ON;"+query;
            return statement.executeUpdate(query);
        }
    }
}
