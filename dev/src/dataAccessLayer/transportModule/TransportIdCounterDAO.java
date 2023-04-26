package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.abstracts.CounterDAOBase;

public class TransportIdCounterDAO extends CounterDAOBase {

    private static final String TABLE_NAME = "transport_id_counter";
    private static final String COLUMN_NAME = "counter";

    public TransportIdCounterDAO() throws DalException {
        super(TABLE_NAME,
                COLUMN_NAME);
    }

    /**
     * used for testing
     * @param dbName     the name of the database to connect to
     */
    public TransportIdCounterDAO(String dbName) throws DalException {
        super(dbName,
                TABLE_NAME,
                COLUMN_NAME);
    }
}
