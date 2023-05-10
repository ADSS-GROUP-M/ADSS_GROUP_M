package Backend.DataAccessLayer.InventoryModule;
import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class CategoryDataMapper extends AbstractDataMapper {
    public CategoryDataMapper() {
        super("category", new String[]{"category_name"});
    }
}
