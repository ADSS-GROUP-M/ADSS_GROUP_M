package dataAccessLayer.transportModule.abstracts;

import dataAccessLayer.dalUtils.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @param <T> The child Type
 */
public abstract class ManyToManyDAO<T>{

    protected final SQLExecutor cursor;
    protected final String TABLE_NAME;
    protected final String[] PARENT_TABLE_NAME;
    protected final String[] ALL_COLUMNS;
    protected final String[] TYPES;
    protected final String[] PRIMARY_KEYS;
    protected final String[] FOREIGN_KEYS;

    protected final String[] REFERENCES;

    protected final Cache<T> cache;

    protected ManyToManyDAO(String tableName,
                            String[] parentTableName,
                            String[] types,
                            String[] primaryKeys,
                            String[] foreignKeys,
                            String[] references,
                            String ... allColumns) throws DalException{
        cursor = new SQLExecutor();
        TABLE_NAME = tableName;
        PARENT_TABLE_NAME = parentTableName;
        TYPES = types;
        PRIMARY_KEYS = primaryKeys;
        FOREIGN_KEYS = foreignKeys;
        REFERENCES = references;
        ALL_COLUMNS = allColumns;
        cache = new Cache<>();
        initTable();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    protected ManyToManyDAO(String dbName,
                            String tableName,
                            String[] parentTableName,
                            String[] types,
                            String[] primaryKeys,
                            String[] foreignKeys,
                            String[] references,
                            String ... allColumns) throws DalException{
        cursor = new SQLExecutor(dbName);
        TABLE_NAME = tableName;
        PARENT_TABLE_NAME = parentTableName;
        TYPES = types;
        PRIMARY_KEYS = primaryKeys;
        FOREIGN_KEYS = foreignKeys;
        REFERENCES = references;
        ALL_COLUMNS = allColumns;
        cache = new Cache<>();
        initTable();
    }

    /**
     * Initialize the table if it doesn't exist
     */
    private void initTable() throws DalException{
        StringBuilder query = new StringBuilder();

        query.append(String.format("CREATE TABLE IF NOT EXISTS %s (\n", TABLE_NAME));

        for (int i = 0; i < ALL_COLUMNS.length; i++) {
            query.append(String.format("%s %s NOT NULL,\n", ALL_COLUMNS[i], TYPES[i]));
        }

        query.append("PRIMARY KEY(");
        for(int i = 0; i < PRIMARY_KEYS.length; i++) {
            query.append(String.format("'%s'",PRIMARY_KEYS[i]));
            if (i != PRIMARY_KEYS.length-1) {
                query.append(",");
            } else {
                query.append(")\n");
            }
        }

        for(int i = 0; i < FOREIGN_KEYS.length ; i++){
            query.append(String.format("CONSTRAINT FK_%s FOREIGN KEY('%s') REFERENCES %s('%s')",
                    FOREIGN_KEYS[i],
                    FOREIGN_KEYS[i],
                    PARENT_TABLE_NAME[i],
                    REFERENCES[i]
            ));
            if(i != FOREIGN_KEYS.length-1){
                query.append(",\n");
            } else {
                query.append("\n");
            }
        }

        query.append(");");

        try {
            cursor.executeWrite(query.toString());
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+TABLE_NAME, e);
        }
    };

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    public abstract T select(T object) throws DalException;

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    public abstract List<T> selectAll() throws DalException;

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    public abstract void insert(T object) throws DalException;

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    public abstract void update(T object) throws DalException;

    /**
     *
     * @param object object with the identifier to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    public abstract void delete(T object) throws DalException;

    protected abstract T getObjectFromResultSet(OfflineResultSet resultSet);
}
