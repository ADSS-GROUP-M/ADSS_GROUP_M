package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Agreement;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryByInvitation;
import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryFixedDays;
import Backend.BusinessLayer.SuppliersModule.Pair;
import Backend.BusinessLayer.SuppliersModule.Product;
import Backend.DataAccessLayer.dalUtils.DalException;
import org.sqlite.util.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class AgreementDataMapper {
    private SupplierProductDataMapper supplierProductDataMapper;
    private DeliveryAgreementDataMapper deliveryAgreementDataMapper;
    private Map<String, Agreement> suppliersAgreement;

    public AgreementDataMapper(){
        supplierProductDataMapper = new SupplierProductDataMapper();
        deliveryAgreementDataMapper = new DeliveryAgreementDataMapper();
        suppliersAgreement = new HashMap<>();
    }

    public Agreement getAgreement(String bnNumber) throws SQLException, DalException {
        if(suppliersAgreement.containsKey(bnNumber))
            return suppliersAgreement.get(bnNumber);
        Agreement agreement = find(bnNumber);
        if(agreement == null)
            throw new DalException("no such Agreement for supplier - " + bnNumber);
        suppliersAgreement.put(bnNumber, agreement);
        return agreement;
    }

    private Agreement find(String bnNumber) throws SQLException {
        List<Product> suppliersProducts = supplierProductDataMapper.find(bnNumber);
        DeliveryAgreement deliveryAgreement = deliveryAgreementDataMapper.find(bnNumber);
        if(!suppliersProducts.isEmpty() && deliveryAgreement != null)
            return new Agreement(suppliersProducts, deliveryAgreement);
        return null;
    }

    public Agreement insertAgreement(String bnNumber, List<Product> products, DeliveryAgreement deliveryAgreement) throws SQLException {
        for (Product p : products)
            supplierProductDataMapper.insert(bnNumber, p.getCatalogNumber(), p.getSuppliersCatalogNumber(), p.getNumberOfUnits(), p.getPrice());
        boolean byInvitation = deliveryAgreement instanceof DeliveryByInvitation;
        String days = byInvitation ? ((DeliveryByInvitation) deliveryAgreement).getNumberOfDays() + "" :
                (((DeliveryFixedDays) deliveryAgreement).getDaysOfTheWeek()).stream().map(i -> String.valueOf(i)).collect(Collectors.joining(","));
        deliveryAgreementDataMapper.insert(bnNumber, deliveryAgreement.getHavaTransport(), byInvitation, days);
        Agreement agreement = new Agreement(products, deliveryAgreement);
        suppliersAgreement.put(bnNumber, agreement);
        return agreement;
    }
}
