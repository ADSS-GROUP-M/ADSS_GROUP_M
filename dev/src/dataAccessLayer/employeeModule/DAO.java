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
    protected final String[] PRIMARY_KEYS;
    protected final String[] TYPES;
    protected final String[] ALL_COLUMNS;

    public DAO(String tableName, String[] keyFields, String[] types, String ... allColumns){
        this.TABLE_NAME = tableName;
        this.PRIMARY_KEYS = keyFields;
        this.TYPES = types;
        this.ALL_COLUMNS = allColumns;
        cursor = new SQLExecutor();
        try {
            initTable();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Can be used for testing in a different database
     * @param dbName the name of the database to connect to
     */
    public DAO(String dbName, String tableName, String[] keyFields, String[] types, String ... allColumns){
        this.TABLE_NAME = tableName;
        this.PRIMARY_KEYS = keyFields;
        this.TYPES = types;
        this.ALL_COLUMNS = allColumns;
        cursor = new SQLExecutor(dbName);
        try {
            initTable();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    protected void initTable() throws DalException{
        StringBuilder query = new StringBuilder();

        query.append(String.format("CREATE TABLE IF NOT EXISTS %s (\n", TABLE_NAME));

        for (int i = 0; i < ALL_COLUMNS.length; i++) {
            query.append(String.format("\"%s\" %s NOT NULL,\n", ALL_COLUMNS[i], TYPES[i]));
        }

        query.append("PRIMARY KEY(");
        for(int i = 0; i < PRIMARY_KEYS.length; i++) {
            query.append(String.format("\"%s\"",PRIMARY_KEYS[i]));
            if (i != PRIMARY_KEYS.length-1) {
                query.append(",");
            } else {
                query.append(")\n");
            }
        }

        query.append(");");

        try {
            cursor.executeWrite(query.toString());
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+TABLE_NAME, e);
        }
    }

    String formatLocalDate (LocalDate dt){
        return dt.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    protected String createConditionForPrimaryKey (Object[] idValues) throws DalException{
        String ans = "";
        if(idValues[0] instanceof String)
           ans =  ans.concat(String.format(" %s = '%s' ",this.PRIMARY_KEYS[0],idValues[0]));
        else if(idValues[0] instanceof Integer)
           ans =  ans.concat(String.format(" %s = %d ",this.PRIMARY_KEYS[0],idValues[0]));
        else if(idValues[0] instanceof Boolean)
            ans = ans.concat(String.format(" %s = '%s' ",this.PRIMARY_KEYS[0],String.valueOf((boolean)idValues[0])));
        else if(idValues[0] instanceof LocalDate)
            ans = ans.concat(String.format(" %s = '%s' ",this.PRIMARY_KEYS[0],formatLocalDate((LocalDate)idValues[0])));
        else
            throw new DalException("illegal idValues. should be String, Integer or Boolean");
        for(int i =1; i< idValues.length; i++){
            if(idValues[i] instanceof String)
               ans =  ans.concat(String.format(" AND %s = '%s' ",this.PRIMARY_KEYS[i],idValues[i]));
            else if(idValues[i] instanceof Integer)
                ans = ans.concat(String.format(" AND %s = %d ",this.PRIMARY_KEYS[i],idValues[i]));
            else if(idValues[i] instanceof Boolean)
                ans = ans.concat(String.format(" AND %s = '%s' ",this.PRIMARY_KEYS[i],String.valueOf((boolean)idValues[i])));
            else if(idValues[i] instanceof LocalDate)
                ans = ans.concat(String.format(" %s = '%s' ",this.PRIMARY_KEYS[i],formatLocalDate((LocalDate)idValues[i])));
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
            if(resultSet.next()){
                ans = convertReaderToObject(resultSet);
            }
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

    protected void delete(Object[] idValues) throws DalException {
        try {
            String queryString = String.format("DELETE FROM %s WHERE", this.TABLE_NAME);
            queryString = queryString.concat(createConditionForPrimaryKey(idValues));
            cursor.executeWrite(queryString);
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    public void clearTable() throws DalException {
        String queryString = String.format("DELETE FROM %s;", this.TABLE_NAME);
        try {
            cursor.executeWrite(queryString);
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    protected abstract Object convertReaderToObject(OfflineResultSet reader) throws DalException;

}