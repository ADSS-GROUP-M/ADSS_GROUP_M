package DataAccessLayer.DalUtils;

import java.sql.*;
import java.util.LinkedList;

public class SQLExecutor {

    private static final String URL = "jdbc:sqlite:SuperLiDB.db";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public OfflineResultSet executeRead(String query) throws SQLException {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return new OfflineResultSet(resultSet);
        }
    }

    public void executeWrite(String query) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
    }
}
