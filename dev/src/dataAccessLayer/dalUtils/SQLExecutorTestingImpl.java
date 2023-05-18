package dataAccessLayer.dalUtils;

import dataAccessLayer.dalAbstracts.SQLExecutor;

import java.sql.*;

public class SQLExecutorTestingImpl implements SQLExecutor {

    private static final String DEFAULT_DB_NAME = "TestingDB.db";
    private static final String URL_PREFIX = "jdbc:sqlite:";
    private final String URL;
    private Connection connection;

    /**
     * connects to the {@link #DEFAULT_DB_NAME} database
     */
    public SQLExecutorTestingImpl() {
        URL = URL_PREFIX + DEFAULT_DB_NAME;
        initConnection();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public SQLExecutorTestingImpl(String dbName) {
        URL = URL_PREFIX + dbName;
        initConnection();
    }

    private void initConnection() {
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public OfflineResultSet executeRead(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return new OfflineResultSet(resultSet);
    }

    public int executeWrite(String query) throws SQLException {
        Statement statement = connection.createStatement();
        query = "PRAGMA foreign_keys = ON;"+query;
        return statement.executeUpdate(query);
    }
}
