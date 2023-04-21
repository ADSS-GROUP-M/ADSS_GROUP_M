package DataAccessLayer.DalUtils;

import java.sql.*;
import java.util.LinkedList;

public class SQLExecuter {

    private static final String URL = "jdbc:sqlite:SuperLiDB.db";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public LinkedList<Object[]> executeRead(String query) throws SQLException {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            LinkedList<Object[]> rows = new LinkedList<>();
            while (resultSet.next()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                Object[] row = new Object[columnCount];
                for(int i = 0; i < columnCount; i++){
                    row[i] = resultSet.getObject(i+1);
                }
                rows.add(row);
            }
            return rows;
        }
    }

    public void executeWrite(String query) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
    }
}
