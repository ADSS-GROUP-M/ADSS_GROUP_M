package dataAccessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.transportModule.abstracts.ManyToManyDAO;
import businessLayer.employeeModule.Branch;

import java.sql.SQLException;
import java.util.List;



public class BranchesDAO extends ManyToManyDAO<Branch> {

    private static final String[] types = new String[]{"TEXT", "TEXT", "TEXT", "TEXT", "TEXT"};

    public BranchesDAO() throws DalException{
        super("branches",
                new String[]{"sites"},
                types,
                new String[]{"address"},
                new String[]{"address"},
                new String[]{"address"},
                "address",
                "morning_shift_start",
                "morning_shift_end",
                "evening_shift_start",
                "evening_shift_end");
    }

    public BranchesDAO(String dbName) throws DalException{
        super(dbName,
                "branches",
                new String[]{"sites"},
                types,
                new String[]{"address"},
                new String[]{"address"},
                new String[]{"address"},
                "address",
                "morning_shift_start",
                "morning_shift_end",
                "evening_shift_start",
                "evening_shift_end");
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Branch select(Branch object) throws DalException {
        return null;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Branch> selectAll() throws DalException {
        return null;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Branch object) throws DalException {

    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Branch object) throws DalException {

    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Branch object) throws DalException {

    }

    @Override
    protected Branch getObjectFromResultSet(OfflineResultSet resultSet) {
        return null;
    }
}
