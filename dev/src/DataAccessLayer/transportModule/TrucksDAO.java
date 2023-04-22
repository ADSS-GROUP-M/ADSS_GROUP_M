package DataAccessLayer.transportModule;

import DataAccessLayer.DalUtils.DalException;

import java.util.List;

public class TrucksDAO extends DAO{

    public TrucksDAO() {
        super("Trucks", new String[]{"TruckId"}, "TruckId","Model","BaseWeight","MaxWeight","CoolingCapacity");
    }

    /**
     * Initialize the table if it doesn't exist
     */
    @Override
    protected void initTable() {

    }

    /**
     * @param values getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Object select(Object values) throws DalException {
        return null;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List selectAll() throws DalException {
        return null;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Object object) throws DalException {

    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Object object) throws DalException {

    }

    /**
     * @param values getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Object values) throws DalException {

    }
}
