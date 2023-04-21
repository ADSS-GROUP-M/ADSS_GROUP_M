package DataAccessLayer.DalUtils;

import java.sql.*;

public class SQLExecuter {

    private static final String URL = "jdbc:mysql:SuperLiDB";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public Object[] executeRead(String query) throws SQLException {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int size = resultSet.getFetchSize();
            Object[] rows = new Object[size];
            int i = 0;
            while (resultSet.next()) {
                rows[i++] = resultSet.getRow();
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
