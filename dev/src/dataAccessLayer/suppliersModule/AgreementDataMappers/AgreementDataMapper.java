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
    private final SupplierProductDataMapper supplierProductDataMapper;
    private final DeliveryAgreementDataMapper deliveryAgreementDataMapper;
    private final ProductsDataMapper productsDataMapper;
    private final Map<String, Agreement> suppliersAgreement;

    public AgreementDataMapper(SupplierProductDataMapper supplierProductDataMapper,
                               DeliveryAgreementDataMapper deliveryAgreementDataMapper,
                               ProductsDataMapper productsDataMapper) {
        this.supplierProductDataMapper = supplierProductDataMapper;
        this.deliveryAgreementDataMapper = deliveryAgreementDataMapper;
        this.productsDataMapper = productsDataMapper;
        suppliersAgreement = new HashMap<>();
    }

    public Agreement getAgreement(String bnNumber) throws DalException{
        if(suppliersAgreement.containsKey(bnNumber)) {
            return suppliersAgreement.get(bnNumber);
        }
        Agreement agreement = find(bnNumber);
        suppliersAgreement.put(bnNumber, agreement);
        return agreement;
    }

    private Agreement find(String bnNumber) throws DalException {
        List<Product> suppliersProducts = supplierProductDataMapper.find(bnNumber);
        DeliveryAgreement deliveryAgreement = deliveryAgreementDataMapper.find(bnNumber);
        if(!suppliersProducts.isEmpty() && deliveryAgreement != null) {
            return new Agreement(suppliersProducts, deliveryAgreement);
        }
        return null;
    }

    public Agreement insertAgreement(String bnNumber, List<Product> products, DeliveryAgreement deliveryAgreement) throws DalException {
        for (Product p : products) {
            supplierProductDataMapper.insert(bnNumber, p.getCatalogNumber(), p.getSuppliersCatalogNumber(), p.getNumberOfUnits(), p.getPrice());
        }
        boolean byInvitation = deliveryAgreement instanceof DeliveryByInvitation;
        String days = byInvitation ? ((DeliveryByInvitation) deliveryAgreement).getNumberOfDays() + "" :
                (((DeliveryFixedDays) deliveryAgreement).getDaysOfTheWeek()).stream().map(i -> String.valueOf(i)).collect(Collectors.joining(","));
        deliveryAgreementDataMapper.insert(bnNumber, deliveryAgreement.getHavaTransport(), byInvitation, days);
        Agreement agreement = new Agreement(products, deliveryAgreement);
        suppliersAgreement.put(bnNumber, agreement);
        return agreement;
    }

    public void setFixedDeliveryAgreement(String bnNumber, boolean haveTransport, List<Integer> days) throws DalException {
        deliveryAgreementDataMapper.delete(bnNumber);
        deliveryAgreementDataMapper.insert(bnNumber, haveTransport, false, days.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(",")));
    }

    public void setByInvitationDeliveryAgreement(String bnNumber, boolean haveTransport, int numOfDays) throws DalException {
        deliveryAgreementDataMapper.delete(bnNumber);
        deliveryAgreementDataMapper.insert(bnNumber, haveTransport, true, numOfDays + "");
    }

    public void addProduct(String bnNumber, String catalogNumber, String supplierCatalogNumber,double price, int numberOfUnits) throws DalException {
        if(!productsDataMapper.isExists(catalogNumber)) {
            throw new DalException("no such product - " + catalogNumber);
        }
        supplierProductDataMapper.insert(bnNumber, catalogNumber, supplierCatalogNumber, numberOfUnits, price);
    }

    public void removeProduct(String bnNumber, String catalogNumber) throws DalException {
        supplierProductDataMapper.delete(bnNumber, catalogNumber);
    }

    public void setSuppliersCatalogNumber(String bnNumber, String catalogNumber, String newSuppliersCatalogNumber) throws DalException {
        supplierProductDataMapper.updateSuppliersCatalogNumber(bnNumber, catalogNumber, newSuppliersCatalogNumber);
    }

    public void setPrice(String bnNumber, String catalogNumber, double price) throws DalException {
        supplierProductDataMapper.updatePrice(bnNumber, catalogNumber, price);
    }

    public void setNumberOfUnits(String bnNumber, String catalogNumber, int numberOfUnits) throws DalException {
        supplierProductDataMapper.updateQuantity(bnNumber, catalogNumber, numberOfUnits);
    }

    public void removeAgreement(String bnNumber) throws DalException {
        deliveryAgreementDataMapper.delete(bnNumber);
        supplierProductDataMapper.delete(bnNumber);
    }

    public void clearTable(){
        supplierProductDataMapper.clearTable();
        deliveryAgreementDataMapper.clearTable();
        productsDataMapper.clearTable();
    }
}
