package DataAccessLayer;

//import org.sqlite.SQLiteConnection;
import java.io.File;
import java.sql.*;
import java.io.Serializable;
import java.util.*;
import java.sql.DriverManager;

public abstract class DAO {
    protected Connection connection = null;
    protected PreparedStatement ptmt = null;
    protected ResultSet resultSet = null;
    private String path;
    private String connectionString;

    protected final String TABLE_NAME;
    public final String idColumnName = "ID";

    public DAO(String tableName) throws SQLException{
        this.TABLE_NAME = tableName;
        path = (new File("").getAbsolutePath()).concat("\\SuperLiDB.db");
        connectionString = "jdbc:sqlite:".concat(path);

    }
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }

    //public abstract void create(T dto);

    protected DTO select(int id)  {
        ResultSet result = null;
        try {
            String queryString = String.format("SELECT * FROM %s WHERE %s = %d",TABLE_NAME,idColumnName,id);
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            result = ptmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(result!= null){
            return convertReaderToObject(result);
        }
        else
            return null;
    }
    protected List<DTO> selectAll() throws SQLException {
        ResultSet result = null;
        try {
            String queryString = String.format("SELECT * FROM %s",TABLE_NAME);
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            result = ptmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(result!= null){
            List<DTO> list = new LinkedList<>();
            while(result.next()){
                list.add(convertReaderToObject(result));
            }
            return list;
        }
        else
            return new LinkedList<>();
    }
    public void update(int id, String attributeName, String attributeValue) {
        try {
            String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE ID=?";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, attributeValue);
            ptmt.setInt(2, id);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();

            }

            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(int id, String attributeName, Integer attributeValue)
    {
        try {
            String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE ID=?";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setInt(1, attributeValue);
            ptmt.setInt(2, id);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();

            }

            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void update(int id, String attributeName, boolean attributeValue)
    {
        try {
            String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE ID=?";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setBoolean(1, attributeValue);
            ptmt.setInt(2, id);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();

            }

            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(int id)
    {
        try {
            String queryString = String.format("DELETE FROM %s WHERE %s = %d", this.TABLE_NAME, id);
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();

            }

            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteAll()
    {
        try {
            String queryString = String.format("DELETE FROM %s ", this.TABLE_NAME);
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();

            }

            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract DTO convertReaderToObject(ResultSet reader);

}