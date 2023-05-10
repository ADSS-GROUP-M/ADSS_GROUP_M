package Backend.DataAccessLayer.SuppliersModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class ContactsInfoDataMapper extends AbstractDataMapper {
    public ContactsInfoDataMapper() {
        super("Contacts_info", new String[]{"bn_number", "name", "phoneNumber"});
    }
}
