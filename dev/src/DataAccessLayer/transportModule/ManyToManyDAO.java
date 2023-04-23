package DataAccessLayer.transportModule;

import DataAccessLayer.DalUtils.DalException;
import DataAccessLayer.DalUtils.SQLExecutor;

import java.util.List;

public abstract class ManyToManyDAO<T,K>{

    protected final SQLExecutor cursor;

    protected final String TABLE_NAME;
    protected final String[] ALL_COLUMNS;
    protected final String[] PRIMARY_KEYS;
    protected final String[] T_FOREIGN_KEYS;
    protected final String[] K_FOREIGN_KEYS;

    protected ManyToManyDAO(String tableName, String[] primaryKeys, String[] foreignKeys, String[] tForeignKeys, String ... allColumns){
        cursor = new SQLExecutor();
        this.TABLE_NAME = tableName;
        this.PRIMARY_KEYS = primaryKeys;
        this.K_FOREIGN_KEYS = foreignKeys;
        this.T_FOREIGN_KEYS = tForeignKeys;
        this.ALL_COLUMNS = allColumns;
        initTable();
    }


    //TODO: Finish this
    /**
     * Initialize the table if it doesn't exist
     */
    protected abstract void initTable();

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
     * @param k_object object with the identifier to delete
     * @param t_object object with the identifier to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    public abstract void delete(T t_object, K k_object) throws DalException;


}
