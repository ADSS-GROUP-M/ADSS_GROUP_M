package dataAccessLayer.dalUtils;

import java.sql.*;

public class SQLExecutor {

    private static final String DEFAULT_DB_NAME = "SuperLiDB.db";
    private static final String URL_PREFIX = "jdbc:sqlite:";
    private final String URL;

    /**
     * used for production - connects to the {@link #DEFAULT_DB_NAME} database
     */
    public SQLExecutor() {
        URL = URL_PREFIX + DEFAULT_DB_NAME;
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    public SQLExecutor(String dbName) {
        URL = URL_PREFIX + dbName;
    }

    public OfflineResultSet executeRead(String query) throws SQLException {

        try (Connection connection = DriverManager.getConnection(URL)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return new OfflineResultSet(resultSet);
        }
    }

    public int executeWrite(String query) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL)) {
            Statement statement = connection.createStatement();
            query = "PRAGMA foreign_keys = ON;"+query;
            return statement.executeUpdate(query);
        }
    }
}
