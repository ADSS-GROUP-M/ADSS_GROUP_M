package dataAccessLayer.dalAbstracts;

import dataAccessLayer.dalUtils.OfflineResultSet;
import exceptions.DalException;

import java.sql.SQLException;

public abstract class CounterDAOBase implements CounterDAO {

    private final SQLExecutor cursor;
    private final String TABLE_NAME;
    private final String COLUMN_NAME;

    protected CounterDAOBase(SQLExecutor cursor, String tableName, String columnName) throws DalException{
        this.cursor = cursor;
        TABLE_NAME = tableName;
        COLUMN_NAME = columnName;
        initTable();
    }

    protected void initTable() throws DalException{
        String query = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                %s INTEGER NOT NULL
                );
                """, TABLE_NAME, COLUMN_NAME);
        try {
            cursor.executeWrite(query);
            query = String.format("SELECT * FROM %s;", TABLE_NAME);
            if(cursor.executeRead(query).isEmpty()){
                resetCounter();
            }
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+ TABLE_NAME,e);
        }
    }

    /**
     * @return the counter's value
     * @throws DalException if an error occurred while trying to select the value
     */
    @Override
    public Integer selectCounter() throws DalException {
        String query = String.format("SELECT %s FROM %s;", COLUMN_NAME, TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select "+ COLUMN_NAME ,e);
        }
        if(resultSet.next()){
            return resultSet.getInt(COLUMN_NAME);
        } else {
            throw new DalException("Failed to select "+ COLUMN_NAME);
        }
    }

    /**
     * @param value - new value for the counter
     * @throws DalException if an error occurred while trying to insert the value
     */
    @Override
    public void insertCounter(Integer value) throws DalException {
        String query = String.format("DELETE FROM %s; INSERT INTO %s (%s) VALUES (%d);",TABLE_NAME, TABLE_NAME, COLUMN_NAME, value);
        try {
            if(cursor.executeWrite(query) == 0){
                throw new RuntimeException("Unexpected error while inserting " + COLUMN_NAME);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert " + COLUMN_NAME,e);
        }
    }

    /**
     * @throws DalException if an error occurred while trying to increment the counter
     */
    @Override
    public void incrementCounter() throws DalException{
        String query = String.format(" UPDATE %s SET %s = (SELECT %s FROM %s) + 1;",
                TABLE_NAME,
                COLUMN_NAME,
                COLUMN_NAME,
                TABLE_NAME);
        try {
            if(cursor.executeWrite(query) != 1){
                throw new RuntimeException("Unexpected error while incrementing " + COLUMN_NAME);
            }
        } catch (SQLException e) {
            throw new DalException("Failed to increment " + COLUMN_NAME,e);
        }
    }

    /**
     * @throws DalException if an error occurred while trying to reset the counter
     */
    @Override
    public void resetCounter() throws DalException {
        insertCounter(1);
    }
}
