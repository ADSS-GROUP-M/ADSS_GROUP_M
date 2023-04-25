package dataAccessLayer.employeeModule;

//import org.sqlite.SQLiteConnection;
import dataAccessLayer.dalUtils.DalException;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public DAO(String tableName, String[] keyFields){
        this.TABLE_NAME = tableName;
        path = (new File("").getAbsolutePath()).concat("\\SuperLiDB.db");
        connectionString = "jdbc:sqlite:".concat(path);
        this.primaryKey = keyFields;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }

    String formatLocalDate (LocalDate dt){
        return dt.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    protected String createConditionForPrimaryKey (Object[] idValues) throws DalException{
        String ans = "";
        if(idValues[0] instanceof String)
           ans =  ans.concat(String.format(" %s = '%s' ",this.primaryKey[0],idValues[0]));
        else if(idValues[0] instanceof Integer)
           ans =  ans.concat(String.format(" %s = %d ",this.primaryKey[0],idValues[0]));
        else if(idValues[0] instanceof Boolean)
            ans = ans.concat(String.format(" %s = '%s' ",this.primaryKey[0],String.valueOf((boolean)idValues[0])));
        else if(idValues[0] instanceof LocalDate)
            ans = ans.concat(String.format(" %s = '%s' ",this.primaryKey[0],formatLocalDate((LocalDate)idValues[0])));
        else
            throw new DalException("illegal idValues. should be String, Integer or Boolean");
        for(int i =1; i< idValues.length; i++){
            if(idValues[i] instanceof String)
               ans =  ans.concat(String.format(" AND %s = '%s' ",this.primaryKey[i],idValues[i]));
            else if(idValues[i] instanceof Integer)
                ans = ans.concat(String.format(" AND %s = %d ",this.primaryKey[i],idValues[i]));
            else if(idValues[i] instanceof Boolean)
                ans = ans.concat(String.format(" AND %s = '%s' ",this.primaryKey[i],String.valueOf((boolean)idValues[i])));
            else if(idValues[i] instanceof LocalDate)
                ans = ans.concat(String.format(" %s = '%s' ",this.primaryKey[i],formatLocalDate((LocalDate)idValues[i])));
            else
                throw new DalException("illegal idValues. Should be String, Integer or Boolean");
        }
        return ans;
    }
    protected Object select(Object[] idValues) throws DalException {
        Exception ex = null;
        ResultSet result = null;
        Object ans = null;
        try {
            String queryString = String.format("SELECT * FROM %s WHERE",TABLE_NAME);
            queryString = queryString.concat(createConditionForPrimaryKey(idValues));
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            result = ptmt.executeQuery();
            ans = convertReaderToObject(result);
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
                //e.printStackTrace();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return ans;
    }
    protected List<Object> selectAll() throws SQLException {
        ResultSet result = null;
        Exception ex = null;
        List<Object> ans = new LinkedList<>();
        try {
            String queryString = String.format("SELECT * FROM %s",TABLE_NAME);
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            result = ptmt.executeQuery();
            if(result!= null){
                while(result.next()){
                    ans.add(convertReaderToObject(result));
                }
            }
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

        return ans;
    }
    protected void update(Object[] idValues, String attributeName, String attributeValue) throws DalException {
        Exception ex = null;
        try {
            String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"='?' WHERE";
            queryString = queryString.concat(createConditionForPrimaryKey(idValues));
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

    protected void update(Object[] idValues, String attributeName, Integer attributeValue) throws DalException
    {
        try {
            String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE";
            queryString = queryString.concat(createConditionForPrimaryKey(idValues));
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
    protected void update(Object[] idValues, String attributeName, boolean attributeValue) throws DalException
    {
        try {
            String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE";
            queryString = queryString.concat(createConditionForPrimaryKey(idValues));
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

    protected void delete(Object[] idValues)
    {
        try {
            String queryString = String.format("DELETE FROM %s WHERE", this.TABLE_NAME);
            queryString = queryString.concat(createConditionForPrimaryKey(idValues));
            connection = getConnection();
            System.out.println(queryString);
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

    protected abstract Object convertReaderToObject(ResultSet reader);

}