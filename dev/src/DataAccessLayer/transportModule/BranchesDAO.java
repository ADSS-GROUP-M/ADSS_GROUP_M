package DataAccessLayer.transportModule;

import DataAccessLayer.DalUtils.DalException;
import employeeModule.BusinessLayer.Employees.Branch;

import java.time.LocalTime;
import java.util.List;

public class BranchesDAO extends ManyToManyDAO<Branch> {
    public BranchesDAO() {
        super("Branches",
                "Sites",
                new String[]{"Branch_Address"},
                new String[]{"Branch_Address"},
                new String[]{"Site_Address"},
                "Branch_Address",
                "Moring_Shift_Start",
                "Moring_Shift_End",
                "Evening_Shift_Start",
                "Evening_Shift_End"
        );
    }

    /**
     * Initialize the table if it doesn't exist
     */
    @Override
    protected void initTable() {

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
}
