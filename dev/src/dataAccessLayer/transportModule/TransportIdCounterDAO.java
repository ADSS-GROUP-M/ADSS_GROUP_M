package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.transportModule.abstracts.CounterDAOBase;

public class TransportIdCounterDAO extends CounterDAOBase {

    private static final String tableName = "transport_id_counter";
    private static final String COLUMN_NAME = "counter";

    public TransportIdCounterDAO(SQLExecutor cursor) throws DalException {
        super(cursor,
                tableName,
                COLUMN_NAME);
        initTable();
    }
}
