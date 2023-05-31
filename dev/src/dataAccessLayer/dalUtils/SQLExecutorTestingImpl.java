package dataAccessLayer.dalUtils;

import dataAccessLayer.dalAbstracts.SQLExecutor;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.Properties;
@Deprecated
public class SQLExecutorTestingImpl implements SQLExecutor {
    private static final String URL_PREFIX = "jdbc:sqlite:";
    private final String URL;
    private final Properties properties;
    private boolean inTransaction;
    private Connection connection;

    /**
     * @param dbName the name of the database to connect to
     */
    public SQLExecutorTestingImpl(String dbName) {
        URL = URL_PREFIX + dbName;
        properties = getProperties();
        try {
            connection = DriverManager.getConnection(URL, properties);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties getProperties() {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        return config.toProperties();
    }

    @Override
    public void beginTransaction() throws SQLException {
        inTransaction = true;
    }

    @Override
    public void commit() throws SQLException {
        if (!inTransaction) {
            throw new IllegalStateException("commit() called without beginTransaction()");
        }
        inTransaction = false;
    }

    @Override
    public void rollback() throws SQLException {
        if (!inTransaction) {
            throw new IllegalStateException("rollback() called without beginTransaction()");
        }
        connection.rollback();
        inTransaction = false;
    }

    @Override
    public OfflineResultSet executeRead(String query) throws SQLException {
        query = query.strip();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return new OfflineResultSet(resultSet);
    }

    @Override
    public int executeWrite(String query) throws SQLException {
        query = query.strip();
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public SQLExecutor clone() {
        return this;
    }
}
