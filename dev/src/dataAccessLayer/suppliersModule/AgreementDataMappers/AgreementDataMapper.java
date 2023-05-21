package dataAccessLayer.suppliersModule.AgreementDataMappers;

import businessLayer.suppliersModule.Agreement;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryAgreement;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryByInvitation;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryFixedDays;
import businessLayer.suppliersModule.Product;
import dataAccessLayer.suppliersModule.ProductsDataMapper;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgreementDataMapper {
    private SupplierProductDataMapper supplierProductDataMapper;
    private DeliveryAgreementDataMapper deliveryAgreementDataMapper;
    private ProductsDataMapper productsDataMapper;
    private Map<String, Agreement> suppliersAgreement;

    public AgreementDataMapper(){
        supplierProductDataMapper = new SupplierProductDataMapper();
        deliveryAgreementDataMapper = new DeliveryAgreementDataMapper();
        productsDataMapper = ProductsDataMapper.getInstance();
        suppliersAgreement = new HashMap<>();
    }

    public Agreement getAgreement(String bnNumber) throws SQLException{
        if(suppliersAgreement.containsKey(bnNumber))
            return suppliersAgreement.get(bnNumber);
        Agreement agreement = find(bnNumber);
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

    public void setFixedDeliveryAgreement(String bnNumber, boolean haveTransport, List<Integer> days) throws SQLException {
        deliveryAgreementDataMapper.delete(bnNumber);
        deliveryAgreementDataMapper.insert(bnNumber, haveTransport, false, days.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(",")));
    }

    public void setByInvitationDeliveryAgreement(String bnNumber, boolean haveTransport, int numOfDays) throws SQLException {
        deliveryAgreementDataMapper.delete(bnNumber);
        deliveryAgreementDataMapper.insert(bnNumber, haveTransport, true, numOfDays + "");
    }

    public void addProduct(String bnNumber, String catalogNumber, String supplierCatalogNumber,double price, int numberOfUnits) throws DalException, SQLException {
        if(!productsDataMapper.isExists(catalogNumber))
            throw new DalException("no such product - " + catalogNumber);
        supplierProductDataMapper.insert(bnNumber, catalogNumber, supplierCatalogNumber, numberOfUnits, price);
    }

    public void removeProduct(String bnNumber, String catalogNumber) throws SQLException {
        supplierProductDataMapper.delete(bnNumber, catalogNumber);
    }

    public void setSuppliersCatalogNumber(String bnNumber, String catalogNumber, String newSuppliersCatalogNumber) throws SQLException {
        supplierProductDataMapper.updateSuppliersCatalogNumber(bnNumber, catalogNumber, newSuppliersCatalogNumber);
    }

    public void setPrice(String bnNumber, String catalogNumber, double price) throws SQLException {
        supplierProductDataMapper.updatePrice(bnNumber, catalogNumber, price);
    }

    public void setNumberOfUnits(String bnNumber, String catalogNumber, int numberOfUnits) throws SQLException {
        supplierProductDataMapper.updateQuantity(bnNumber, catalogNumber, numberOfUnits);
    }

    public void removeAgreement(String bnNumber) throws SQLException {
        deliveryAgreementDataMapper.delete(bnNumber);
        supplierProductDataMapper.delete(bnNumber);
    }
}
