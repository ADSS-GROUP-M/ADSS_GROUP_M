package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import objects.transportObjects.Driver;

import java.sql.SQLException;
import java.util.List;

public class DriversDAO extends ManyToManyDAO<Driver> {

    public static final String[] types = new String[]{"TEXT", "TEXT"};

    public DriversDAO() throws DalException {
        super("truck_drivers",
                new String[]{"EMPLOYEES"},
                types,
                new String[]{"id"},
                new String[]{"id"},
                new String[]{"Id"},
                "id",
                "license_type");
    }

    public DriversDAO(String dbName) throws DalException {
        super(dbName,
                "truck_drivers",
                new String[]{"EMPLOYEES"},
                types,
                new String[]{"id"},
                new String[]{"id"},
                new String[]{"Id"},
                "id",
                "license_type");
    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Driver select(Driver object) throws DalException {
        return null;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Driver> selectAll() throws DalException {
        return null;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Driver object) throws DalException {

    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Driver object) throws DalException {

    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Driver object) throws DalException {

    }

    @Override
    protected Driver getObjectFromResultSet(OfflineResultSet resultSet) {
        return null;
    }
}
