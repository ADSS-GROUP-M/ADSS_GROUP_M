package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import objects.transportObjects.Driver;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DriversDAO extends ManyToManyDAO<Driver> {

    private static final String[] types = new String[]{"TEXT", "TEXT"};
    private static final String[] parent_table_names = {"EMPLOYEES"};
    private static final String[] primary_keys = {"id"};
    private static final String[] foreign_keys = {"id"};
    private static final String[] references = {"Id"};

    public DriversDAO() throws DalException {
        super("truck_drivers",
                parent_table_names,
                types,
                primary_keys,
                foreign_keys,
                references,
                "id",
                "license_type");
    }

    public DriversDAO(String dbName) throws DalException {
        super(dbName,
                "truck_drivers",
                parent_table_names,
                types,
                primary_keys,
                foreign_keys,
                references,
                "id",
                "license_type");
    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Driver select(Driver object) throws DalException {
        String query = String.format("SELECT * FROM %s WHERE id = '%s';", TABLE_NAME, object.id());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select Driver", e);
        }
        if (resultSet.next()) {
            return getObjectFromResultSet(resultSet);
        } else {
            throw new DalException("No driver with id " + object.id() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Driver> selectAll() throws DalException {
        String query = String.format("""
                    SELECT id,name,license_type FROM %s
                    INNER JOIN %s ON %s.id = %s.id
                        """, TABLE_NAME, PARENT_TABLE_NAME[0], TABLE_NAME, PARENT_TABLE_NAME[0]);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all Drivers", e);
        }
        List<Driver> drivers = new LinkedList<>();
        while (resultSet.next()) {
            drivers.add(getObjectFromResultSet(resultSet));
        }
        return drivers;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Driver object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s');",
                TABLE_NAME,
                object.id(),
                object.licenseType());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to insert Driver", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Driver object) throws DalException {
        String query = String.format("UPDATE %s SET license_type = '%s' WHERE id = '%s';",
                TABLE_NAME,
                object.licenseType(),
                object.id());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to update Driver", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Driver object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE id = '%s';", TABLE_NAME, object.id());
        try {
            cursor.executeWrite(query);
        } catch (SQLException e) {
            throw new DalException("Failed to delete Driver", e);
        }
    }

    @Override
    protected Driver getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Driver(
                resultSet.getString("id"),
                resultSet.getString("name"),
                Driver.LicenseType.valueOf(resultSet.getString("license_type")));
    }
}
