package Backend.DataAccessLayer.InventoryModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class BranchDataMapper extends AbstractDataMapper {
    public BranchDataMapper() {
        super("branch", new String[]{"branch_name"});
    }
}
