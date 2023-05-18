package businessLayer.suppliersModule;

import businessLayer.suppliersModule.DeliveryAgreements.DeliveryAgreement;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.suppliersModule.AgreementDataMappers.AgreementDataMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AgreementController {
    private static AgreementController instance;
    private AgreementDataMapper agreementDataMapper;
    private AgreementController(){
        agreementDataMapper = new AgreementDataMapper();
    }

    public static AgreementController getInstance(){
        if(instance == null)
            instance = new AgreementController();
        return instance;
    }

    public void addAgreement(String bnNumber, List<Product> productsList, DeliveryAgreement deliveryAgreement) throws SQLException {
        agreementDataMapper.insertAgreement(bnNumber ,productsList, deliveryAgreement);
    }

    private Agreement getAgreement(String bnNumber) throws SQLException{
        return agreementDataMapper.getAgreement(bnNumber);
    }

    public Map<String, Product> getProducts(String bnNumber)  {
        try {
            return getAgreement(bnNumber).getProducts();
        }
        catch (Exception ignored){return null;}
    }

    public Product getProduct(String bnNumber, String catalogNumber) {
        try {
            return getAgreement(bnNumber).getProduct(catalogNumber);
        }
        catch (Exception e){return null;}
    }

    public DeliveryAgreement getDeliveryAgreement(String bnNumber) throws SQLException {
        return getAgreement(bnNumber).getDeliveryAgreement();
    }

    public void setFixedDeliveryAgreement(String bnNumber, boolean haveTransport, List<Integer> days) throws SQLException {
        Agreement agreement = getAgreement(bnNumber);
        agreementDataMapper.setFixedDeliveryAgreement(bnNumber, haveTransport, days);
        agreement.setFixedDeliveryAgreement(haveTransport, days);
    }

    public void setByInvitationDeliveryAgreement(String bnNumber, boolean haveTransport, int numOfDays) throws SQLException {
        Agreement agreement = getAgreement(bnNumber);
        agreementDataMapper.setByInvitationDeliveryAgreement(bnNumber, haveTransport, numOfDays);
        agreement.setByInvitationDeliveryAgreement(haveTransport, numOfDays);
    }

    public void addProduct(String bnNumber, String catalogNumber, String supplierCatalogNumber,double price, int numberOfUnits) throws SQLException, DalException {
        Agreement agreement = getAgreement(bnNumber);
        if(!agreement.productExist(catalogNumber)){
            agreementDataMapper.addProduct(bnNumber, catalogNumber, supplierCatalogNumber, price, numberOfUnits);
            agreement.addProduct(catalogNumber, supplierCatalogNumber, price, numberOfUnits);
        }
    }

    public void removeProduct(String bnNumber, String catalogNumber) throws SQLException {
        Agreement agreement = getAgreement(bnNumber);
        agreementDataMapper.removeProduct(bnNumber, catalogNumber);
        agreement.removeProduct(catalogNumber);
    }

    public boolean productExist(String bnNumber, String catalogNumber) throws SQLException {
        return getAgreement(bnNumber).productExist(catalogNumber);
    }

    public void setSuppliersCatalogNumber(String bnNumber, String catalogNumber, String newSuppliersCatalogNumber) throws SQLException {
        Agreement agreement = getAgreement(bnNumber);
        agreementDataMapper.setSuppliersCatalogNumber(bnNumber, catalogNumber, newSuppliersCatalogNumber);
        agreement.setSuppliersCatalogNumber(catalogNumber, newSuppliersCatalogNumber);
    }

    public void setPrice(String bnNumber, String catalogNumber, double price) throws SQLException {
        Agreement agreement = getAgreement(bnNumber);
        agreementDataMapper.setPrice(bnNumber, catalogNumber, price);
        agreement.setPrice(catalogNumber, price);
    }

    public void setNumberOfUnits(String bnNumber, String catalogNumber, int numberOfUnits) throws SQLException {
        Agreement agreement = getAgreement(bnNumber);
        agreementDataMapper.setNumberOfUnits(bnNumber, catalogNumber, numberOfUnits);
        agreement.setNumberOfUnits(catalogNumber, numberOfUnits);
    }

    public String getSuppliersCatalogNumber(String bnNumber, String catalogNumber) throws SQLException {
        return getAgreement(bnNumber).getSuppliersCatalogNumber(catalogNumber);
    }

    public int getNumberOfUnits(String bnNumber, String catalogNumber) throws SQLException {
        return getAgreement(bnNumber).getNumberOfUnits(catalogNumber);
    }

    public void removeAgreement(String bnNumber) throws SQLException {
        agreementDataMapper.removeAgreement(bnNumber);
    }

    public String toString(String bnNumber) throws SQLException {
        return getAgreement(bnNumber).toString();
    }
}
