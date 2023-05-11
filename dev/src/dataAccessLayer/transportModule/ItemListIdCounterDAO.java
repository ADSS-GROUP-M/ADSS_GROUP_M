package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.CounterDAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import exceptions.DalException;

public class ItemListIdCounterDAO extends CounterDAOBase {

    private static final String tableName = "item_list_id_counter";
    private static final String COLUMN_NAME = "counter";

    public ItemListIdCounterDAO(SQLExecutor cursor) throws DalException {
        super(cursor,
                tableName,
                COLUMN_NAME);
        initTable();
    }
}
