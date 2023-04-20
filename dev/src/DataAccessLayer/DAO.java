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
    private String[] primaryKey;


    public DAO(String tableName, String[] keyFields) throws SQLException{
        this.TABLE_NAME = tableName;
        path = (new File("").getAbsolutePath()).concat("\\SuperLiDB.db");
        connectionString = "jdbc:sqlite:".concat(path);
        this.primaryKey = keyFields;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }

    //public abstract void create(T dto);
    private String createConditionForPrimaryKey (Object[] idValues) throws Exception{
        if(idValues.length != this.primaryKey.length || this.primaryKey.length == 0)
            throw new Exception ("Illegal set of identifying values");
        String ans = "";
        if(idValues[0] instanceof String)
            ans.concat(String.format(" %s = %s ",this.primaryKey[0],idValues[0]));
        else if(idValues[0] instanceof Integer)
            ans.concat(String.format(" %s = %d ",this.primaryKey[0],idValues[0]));
        else if(idValues[0] instanceof Boolean)
            ans.concat(String.format(" %s = %s ",this.primaryKey[0],idValues[0]));
        else
            throw new Exception("illegal idValues. should be String, Integer or Boolean");
        for(int i =1; i< idValues.length; i++){
            if(idValues[i] instanceof String)
                ans.concat(String.format(" , %s = %s ",this.primaryKey[i],idValues[i]));
            else if(idValues[i] instanceof Integer)
                ans.concat(String.format(" , %s = %d ",this.primaryKey[i],idValues[i]));
            else if(idValues[i] instanceof Boolean)
                ans.concat(String.format(" , %s = %s ",this.primaryKey[i],idValues[i]));
            else
                throw new Exception("illegal idValues. Should be String, Integer or Boolean");
        }
        return ans;
    }
    protected DTO select(Object[] idValues) throws Exception {
        Exception ex = null;
        ResultSet result = null;
        try {
            String queryString = String.format("SELECT * FROM %s WHERE",TABLE_NAME);
            queryString.concat(createConditionForPrimaryKey(idValues));
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);

            result = ptmt.executeQuery();
        } catch (SQLException e) {
            ex = e;
        } catch (Exception e) {
            ex = e;
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
        Exception ex = null;
        try {
            String queryString = String.format("SELECT * FROM %s",TABLE_NAME);
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            result = ptmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            ex = e;
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            }
            catch (SQLException e) {
               ex = e;
            } catch (Exception e) {
                ex = e;
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
    public void update(Object[] idValues, String attributeName, String attributeValue) throws Exception {
        Exception ex = null;
        try {
            String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE";
            queryString.concat(createConditionForPrimaryKey(idValues));
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, attributeValue);
            ptmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }finally
         {
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

    public void update(Object[] idValues, String attributeName, Integer attributeValue) throws Exception
    {
        try {
            String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE";
            queryString.concat(createConditionForPrimaryKey(idValues));
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setInt(1, attributeValue);
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
    public void update(Object[] idValues, String attributeName, boolean attributeValue) throws Exception
    {
        try {
            String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE";
            queryString.concat(createConditionForPrimaryKey(idValues));
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setBoolean(1, attributeValue);
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

    public void delete(Object[] idValues)
    {
        try {
            String queryString = String.format("DELETE FROM %s WHERE", this.TABLE_NAME);
            queryString.concat(createConditionForPrimaryKey(idValues));
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.executeUpdate();
        } catch (Exception e) {
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