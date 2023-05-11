package Backend.DataAccessLayer.SuppliersModule;


import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class fieldsDataMapper extends AbstractDataMapper {
        public fieldsDataMapper(String tableName, String columns) {
                super("fields", new String[] {"bn_number", "field"});

        }

}
