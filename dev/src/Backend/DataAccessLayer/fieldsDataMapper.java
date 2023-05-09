package Backend.DataAccessLayer;


public class fieldsDataMapper extends AbstractDataMapper{
        public fieldsDataMapper(String tableName, String columns) {
                super("Fields", new String[] {"bn_number", "field"});

        }

}
