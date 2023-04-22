package DataAccessLayer.transportModule;

import DataAccessLayer.DalUtils.DalException;
import transportModule.records.ItemList;

import java.util.List;

public class ItemListsDAO extends DAO<ItemList>{
    protected ItemListsDAO() {
        super("ItemLists", new String[]{"ItemListId"}, "ItemListId", "LoadingType","ItemName","Amount");
    }

    /**
     * Initialize the table if it doesn't exist
     */
    @Override
    protected void initTable() {

    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public ItemList select(ItemList object) throws DalException {
        return null;
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<ItemList> selectAll() throws DalException {
        return null;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(ItemList object) throws DalException {

    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(ItemList object) throws DalException {

    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(ItemList object) throws DalException {

    }
}
