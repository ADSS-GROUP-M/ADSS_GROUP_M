package Backend.DataAccessLayer.SuppliersModule;

import Backend.DataAccessLayer.dalUtils.AbstractDataMapper;

public class DeliveryAgreementDataMapper  extends AbstractDataMapper {
    public DeliveryAgreementDataMapper(String tableName, String columns) {
        super("Delivery_agreement", new String[] {"bn_number", "have_transport", "by_invitation", "days"});
    }
}
