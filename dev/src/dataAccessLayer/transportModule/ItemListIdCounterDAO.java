package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.abstracts.CounterDAOBase;

public class ItemListIdCounterDAO extends CounterDAOBase {

    private static final String TABLE_NAME = "item_list_id_counter";
    private static final String COLUMN_NAME = "counter";

    public ItemListIdCounterDAO() throws DalException {
        super(TABLE_NAME,
                COLUMN_NAME);
        initTable();
    }

    /**
     * used for testing
     * @param dbName     the name of the database to connect to
     */
    public ItemListIdCounterDAO(String dbName) throws DalException {
        super(dbName,
                TABLE_NAME,
                COLUMN_NAME);
        initTable();
    }
}
