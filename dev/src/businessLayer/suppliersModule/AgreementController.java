package businessLayer.suppliersModule;

import businessLayer.suppliersModule.DeliveryAgreements.DeliveryAgreement;
import dataAccessLayer.suppliersModule.AgreementDataMappers.AgreementDataMapper;
import exceptions.DalException;
import exceptions.SupplierException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AgreementController {
    
    private final AgreementDataMapper agreementDataMapper;
    public AgreementController(AgreementDataMapper agreementDataMapper){
        this.agreementDataMapper = agreementDataMapper;
    }

    public void addAgreement(String bnNumber, List<Product> productsList, DeliveryAgreement deliveryAgreement) throws SupplierException {
        try {
            agreementDataMapper.insertAgreement(bnNumber ,productsList, deliveryAgreement);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    private Agreement getAgreement(String bnNumber) throws SupplierException{
        try {
            return agreementDataMapper.getAgreement(bnNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
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

    public DeliveryAgreement getDeliveryAgreement(String bnNumber) throws SupplierException {
        return getAgreement(bnNumber).getDeliveryAgreement();
    }

    public void setFixedDeliveryAgreement(String bnNumber, boolean haveTransport, List<Integer> days) throws SupplierException {
        Agreement agreement = getAgreement(bnNumber);
        try {
            agreementDataMapper.setFixedDeliveryAgreement(bnNumber, haveTransport, days);
            agreement.setFixedDeliveryAgreement(haveTransport, days);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setByInvitationDeliveryAgreement(String bnNumber, boolean haveTransport, int numOfDays) throws SupplierException {
        try {
            Agreement agreement = getAgreement(bnNumber);
            agreementDataMapper.setByInvitationDeliveryAgreement(bnNumber, haveTransport, numOfDays);
            agreement.setByInvitationDeliveryAgreement(haveTransport, numOfDays);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void addProduct(String bnNumber, String catalogNumber, String supplierCatalogNumber,double price, int numberOfUnits) throws SupplierException {
        try{
            Agreement agreement = getAgreement(bnNumber);
            if(!agreement.productExist(catalogNumber)){
                agreementDataMapper.addProduct(bnNumber, catalogNumber, supplierCatalogNumber, price, numberOfUnits);
                agreement.addProduct(catalogNumber, supplierCatalogNumber, price, numberOfUnits);
            }
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void removeProduct(String bnNumber, String catalogNumber) throws SupplierException {
        try {
            Agreement agreement = getAgreement(bnNumber);
            agreementDataMapper.removeProduct(bnNumber, catalogNumber);
            agreement.removeProduct(catalogNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public boolean productExist(String bnNumber, String catalogNumber) throws SupplierException {
        return getAgreement(bnNumber).productExist(catalogNumber);
    }

    public void setSuppliersCatalogNumber(String bnNumber, String catalogNumber, String newSuppliersCatalogNumber) throws SupplierException {
        try {
            Agreement agreement = getAgreement(bnNumber);
            agreementDataMapper.setSuppliersCatalogNumber(bnNumber, catalogNumber, newSuppliersCatalogNumber);
            agreement.setSuppliersCatalogNumber(catalogNumber, newSuppliersCatalogNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setPrice(String bnNumber, String catalogNumber, double price) throws SupplierException {
        try {
            Agreement agreement = getAgreement(bnNumber);
            agreementDataMapper.setPrice(bnNumber, catalogNumber, price);
            agreement.setPrice(catalogNumber, price);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setNumberOfUnits(String bnNumber, String catalogNumber, int numberOfUnits) throws SupplierException {
        try {
            Agreement agreement = getAgreement(bnNumber);
            agreementDataMapper.setNumberOfUnits(bnNumber, catalogNumber, numberOfUnits);
            agreement.setNumberOfUnits(catalogNumber, numberOfUnits);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public String getSuppliersCatalogNumber(String bnNumber, String catalogNumber) throws SupplierException {
        return getAgreement(bnNumber).getSuppliersCatalogNumber(catalogNumber);
    }

    public int getNumberOfUnits(String bnNumber, String catalogNumber) throws SupplierException {
        return getAgreement(bnNumber).getNumberOfUnits(catalogNumber);
    }

    public void removeAgreement(String bnNumber) throws SupplierException {
        try {
            agreementDataMapper.removeAgreement(bnNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public String toString(String bnNumber) throws SupplierException {
        return getAgreement(bnNumber).toString();
    }
}
