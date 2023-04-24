package DataAccessLayer.transportModule;

import DataAccessLayer.DalUtils.DalException;
import transportModule.records.Truck;

import java.sql.SQLException;
import java.util.List;

public class TrucksDAO extends DAO<Truck>{

    public TrucksDAO() throws DalException {
        super("trucks",
                new String[]{"id"},
                "id",
                "model",
                "base_weight",
                "max_weight",
                "cooling_capacity");
    }

    public TrucksDAO(String dbName) throws DalException {
        super(dbName,
                "trucks",
                new String[]{"id"},
                "id",
                "model",
                "base_weight",
                "max_weight",
                "cooling_capacity");
    }

    /**
     * Initialize the table if it doesn't exist
     */
    @Override
    protected void initTable() throws DalException {
        String query = """
                CREATE TABLE IF NOT EXISTS trucks (
                    id TEXT PRIMARY KEY,
                    model TEXT NOT NULL,
                    base_weight INTEGER NOT NULL,
                    max_weight INTEGER NOT NULL,
                    cooling_capacity TEXT NOT NULL,
                    PRIMARY KEY (id)
                );
                """;
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to initialize Trucks table", e);
        }
    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Truck select(Truck object) throws DalException {
        return null;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Truck> selectAll() throws DalException {
        return null;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Truck object) throws DalException {

    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Truck object) throws DalException {

    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Truck object) throws DalException {

    }
}
