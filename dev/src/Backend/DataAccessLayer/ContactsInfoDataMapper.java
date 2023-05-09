package Backend.DataAccessLayer;

public class ContactsInfoDataMapper extends AbstractDataMapper{
    public ContactsInfoDataMapper() {
        super("Contacts_info", new String[]{"bn_number", "name", "phoneNumber"});
    }
}
