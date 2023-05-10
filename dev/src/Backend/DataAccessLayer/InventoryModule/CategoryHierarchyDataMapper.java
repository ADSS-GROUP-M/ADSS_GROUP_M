package Backend.DataAccessLayer.InventoryModule;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class CategoryHierarchyDataMapper extends AbstractDataMapper {
    public CategoryHierarchyDataMapper() {
        super("category_hierarchy", new String[]{"category", "sub_category"});
    }

}
