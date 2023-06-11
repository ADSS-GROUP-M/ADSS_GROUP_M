package dataAccessLayer.dalUtils;

import dataAccessLayer.dalAbstracts.SQLExecutor;
import org.sqlite.SQLiteConfig;
import utils.FileUtils;

import java.sql.*;
import java.util.Properties;

public class SQLExecutorImpl implements SQLExecutor {
    private static final String URL_PREFIX = "jdbc:sqlite:";
    private final String URL;
    private final Properties properties;
    private volatile Connection transactionConnection;

    /**
     * @param dbName the name of the database to connect to
     */
    public SQLExecutorImpl(String dbName) {
        URL = URL_PREFIX + FileUtils.getParentFolderPath() + dbName;
        properties = getProperties();
    }

    private Properties getProperties() {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        return config.toProperties();
    }

    @Override
    public void beginTransaction() throws SQLException {
        transactionConnection = DriverManager.getConnection(URL, properties);
        transactionConnection.setAutoCommit(false);
    }

    @Override
    public void commit() throws SQLException {
        if (transactionConnection == null || transactionConnection.isClosed()){
            throw new IllegalStateException("commit() called when not in a transaction");
        }
        try{
            transactionConnection.commit();
        } finally {
            transactionConnection.close();
        }
    }

    @Override
    public void rollback() throws SQLException {

        if (transactionConnection == null || transactionConnection.isClosed()) {
            throw new IllegalStateException("rollback() called when not in a transaction");
        }
        try{
            transactionConnection.rollback();
        } finally {
            transactionConnection.close();
        }
    }

    @Override
    public OfflineResultSet executeRead(String query) throws SQLException {

        query = cleanQuery(query);

        if(transactionConnection != null && transactionConnection.isClosed() == false){
            return new OfflineResultSet(transactionConnection.createStatement().executeQuery(query));
        } else {
            try (Connection connection = DriverManager.getConnection(URL)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                return new OfflineResultSet(resultSet);
            }
        }
    }

    @Override
    public int executeWrite(String query) throws SQLException {

        query = cleanQuery(query);

        if(transactionConnection != null && transactionConnection.isClosed() == false){
            try{
                return transactionConnection.createStatement().executeUpdate(query);
            } catch (SQLException e) {
                rollback();
                throw e;
            }
        } else {
            try (Connection connection = DriverManager.getConnection(URL, properties)) {
                connection.setAutoCommit(false);
                Statement statement = connection.createStatement();
                try{
                    int rowsChanged = statement.executeUpdate(query);
                    connection.commit();
                    return rowsChanged;
                }catch (SQLException e){
                    connection.rollback();
                    throw e;
                }
            }
        }
    }

    @Override
    public SQLExecutor clone() {
        try {
            return (SQLExecutor) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Something went wrong while cloning SQLExecutor"); // should never happen
        }
    }

    private static String cleanQuery(String query) {
        query = query.strip();
        query = query.charAt(query.length()-1) == ';' ? query : query + ";";
        return query;
    }
}
