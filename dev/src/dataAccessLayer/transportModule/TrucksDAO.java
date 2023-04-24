package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.DAO;
import objects.transportObjects.Truck;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TrucksDAO extends DAO<Truck> {

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
        String query = String.format("SELECT * FROM %s WHERE id = '%s';", TABLE_NAME, object.id());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select Truck", e);
        }
        if (resultSet.next()) {
            return getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("Truck not found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Truck> selectAll() throws DalException {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all Trucks", e);
        }
        List<Truck> trucks = new LinkedList<>();
        while (resultSet.next()) {
            trucks.add(getObjectFromResultSet(resultSet));
        }
        return trucks;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Truck object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s', %d, %d, '%s');",
                TABLE_NAME,
                object.id(),
                object.model(),
                object.baseWeight(),
                object.maxWeight(),
                object.coolingCapacity().toString());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to insert Truck", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Truck object) throws DalException {
        String query = String.format("UPDATE %s SET model = '%s', base_weight = %d, max_weight = %d, cooling_capacity = '%s' WHERE id = '%s';",
                TABLE_NAME,
                object.model(),
                object.baseWeight(),
                object.maxWeight(),
                object.coolingCapacity().toString(),
                object.id());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to update Truck", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Truck object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE id = '%s';", TABLE_NAME, object.id());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to delete Truck", e);
        }
    }

    protected Truck getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Truck(
                resultSet.getString("id"),
                resultSet.getString("model"),
                resultSet.getInt("base_weight"),
                resultSet.getInt("max_weight"),
                Truck.CoolingCapacity.valueOf(resultSet.getString("cooling_capacity"))
        );
    }
}
