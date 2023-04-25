package dataAccessLayer.employeeModule;

//import org.sqlite.SQLiteConnection;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class DAO {
    protected SQLExecutor cursor;

    protected final String TABLE_NAME;
    private String[] primaryKey;

    public DAO(String tableName, String[] keyFields){
        this.TABLE_NAME = tableName;
        this.primaryKey = keyFields;
        cursor = new SQLExecutor("TestingDB.db");
    }

    String formatLocalDate (LocalDate dt){
        return dt.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    protected String createConditionForPrimaryKey (Object[] idValues) throws DalException{
        String ans = "";
        if(idValues[0] instanceof String)
           ans =  ans.concat(String.format(" %s = '%s' NOT NULL",this.primaryKey[0],idValues[0]));
        else if(idValues[0] instanceof Integer)
           ans =  ans.concat(String.format(" %s = %d NOT NULL",this.primaryKey[0],idValues[0]));
        else if(idValues[0] instanceof Boolean)
            ans = ans.concat(String.format(" %s = '%s' NOT NULL",this.primaryKey[0],String.valueOf((boolean)idValues[0])));
        else if(idValues[0] instanceof LocalDate)
            ans = ans.concat(String.format(" %s = '%s' NOT NULL",this.primaryKey[0],formatLocalDate((LocalDate)idValues[0])));
        else
            throw new DalException("illegal idValues. should be String, Integer or Boolean");
        for(int i =1; i< idValues.length; i++){
            if(idValues[i] instanceof String)
               ans =  ans.concat(String.format(" AND %s = '%s' NOT NULL",this.primaryKey[i],idValues[i]));
            else if(idValues[i] instanceof Integer)
                ans = ans.concat(String.format(" AND %s = %d NOT NULL",this.primaryKey[i],idValues[i]));
            else if(idValues[i] instanceof Boolean)
                ans = ans.concat(String.format(" AND %s = '%s' NOT NULL",this.primaryKey[i],String.valueOf((boolean)idValues[i])));
            else if(idValues[i] instanceof LocalDate)
                ans = ans.concat(String.format(" %s = '%s' NOT NULL",this.primaryKey[i],formatLocalDate((LocalDate)idValues[i])));
            else
                throw new DalException("illegal idValues. Should be String, Integer or Boolean");
        }
        return ans;
    }
    protected Object select(Object[] idValues) throws DalException {
        Object ans = null;
        try {
            String queryString = String.format("SELECT * FROM %s WHERE",TABLE_NAME);
            queryString = queryString.concat(createConditionForPrimaryKey(idValues));
            OfflineResultSet resultSet = cursor.executeRead(queryString);
            ans = convertReaderToObject(resultSet);
        } catch (SQLException e) {
            throw new DalException(e);
        }
        return ans;
    }
    protected List<Object> selectAll() throws DalException {
        String queryString = String.format("SELECT * FROM %s",TABLE_NAME);
        OfflineResultSet resultSet;
        List<Object> ans = new LinkedList<>();
        try {
            resultSet = cursor.executeRead(queryString);
        } catch (SQLException e) {
            throw new DalException(e);
        }
        while(resultSet.next()){
            ans.add(convertReaderToObject(resultSet));
        }
        return ans;
    }
    protected void update(Object[] idValues, String attributeName) throws DalException {
        String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"='?' WHERE";
        queryString = queryString.concat(createConditionForPrimaryKey(idValues));
        try {
            cursor.executeWrite(queryString);
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }

//    protected void update(Object[] idValues, String attributeName) throws DalException
//    {
//        String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE";
//        queryString = queryString.concat(createConditionForPrimaryKey(idValues));
//        try {
//            cursor.executeWrite(queryString);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    protected void update(Object[] idValues, String attributeName, boolean attributeValue) throws DalException
    {
        String queryString = "UPDATE "+TABLE_NAME+" SET "+attributeName+"=? WHERE";
        queryString = queryString.concat(createConditionForPrimaryKey(idValues));
        try {
            cursor.executeWrite(queryString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void delete(Object[] idValues) throws DalException {
        try {
            String queryString = String.format("DELETE FROM %s WHERE", this.TABLE_NAME);
            queryString = queryString.concat(createConditionForPrimaryKey(idValues));
            cursor.executeWrite(queryString);
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    public void deleteAll() throws DalException {
        String queryString = String.format("DELETE FROM %s ", this.TABLE_NAME);
        try {
            cursor.executeWrite(queryString);
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    protected abstract Object convertReaderToObject(OfflineResultSet reader);

}