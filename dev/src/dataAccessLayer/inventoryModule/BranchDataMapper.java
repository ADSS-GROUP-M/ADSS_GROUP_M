package dataAccessLayer.inventoryModule;

import dataAccessLayer.dalAbstracts.AbstractDataMapper;

public class BranchDataMapper extends AbstractDataMapper {
    public BranchDataMapper() {
        super("branch", new String[]{"branch_name"});
    }
}
