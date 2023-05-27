package businessLayer.suppliersModule;

import businessLayer.suppliersModule.Discounts.Discount;
import dataAccessLayer.suppliersModule.BillOfQuantitiesDataMappers.BillOfQuantitiesDataMapper;
import exceptions.DalException;
import exceptions.SupplierException;

import java.sql.SQLException;
import java.util.Map;

public class BillOfQuantitiesController {
    
    private final BillOfQuantitiesDataMapper billOfQuantitiesDataMapper;

    public BillOfQuantitiesController(BillOfQuantitiesDataMapper billOfQuantitiesDataMapper){
        this.billOfQuantitiesDataMapper = billOfQuantitiesDataMapper;
    }

    public boolean billOfQuantitiesExist(String bnNumber) throws SupplierException {
        return getBillOfQuantities(bnNumber) != null;
    }


    private BillOfQuantities getBillOfQuantities(String bnNumber) throws SupplierException {
        try {
            return billOfQuantitiesDataMapper.getBillOfQuantities(bnNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    private BillOfQuantities addBillOfQuantities(String bnNumber){
        return billOfQuantitiesDataMapper.addBillOfQuantities(bnNumber);
    }

    public void setOrderOfDiscounts(String bnNumber, boolean amountBeforeTotal) throws SupplierException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        try {
            billOfQuantitiesDataMapper.setOrderOfDiscounts(bnNumber, amountBeforeTotal);
            billOfQuantities.setOrderOfDiscounts(amountBeforeTotal);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setDiscountOnTotalOrder(String bnNumber, double priceToActivateDiscount, Discount discount) throws SupplierException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        try {
            billOfQuantitiesDataMapper.setDiscountOnTotalOrder(bnNumber, priceToActivateDiscount, discount);
            billOfQuantities.setDiscountOnTotalOrder(priceToActivateDiscount, discount);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void removeDiscountOnTotalOrder(String bnNumber) throws SupplierException {
        try {
            BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
            billOfQuantitiesDataMapper.removeDiscountOnTotalOrder(bnNumber);
            billOfQuantities.removeDiscountOnTotalOrder();
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void addProductDiscount(String bnNumber, String catalogNumber, int amount, Discount discount) throws SupplierException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        try {
            billOfQuantitiesDataMapper.addProductDiscount(bnNumber, catalogNumber, amount, discount);
            billOfQuantities.addProductDiscount(catalogNumber, amount, discount);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber, int amount) throws SupplierException {
        try {
            BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
            billOfQuantitiesDataMapper.removeProductDiscount(bnNumber, catalogNumber, amount);
            billOfQuantities.removeProductDiscount(catalogNumber, amount);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber) throws SupplierException {
        try {
            BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
            billOfQuantitiesDataMapper.removeProductDiscount(bnNumber, catalogNumber);
            billOfQuantities.removeProductDiscount(catalogNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void setProductsDiscounts(String bnNumber, Map<String, Map<Integer, Discount>> productsDiscounts) throws SupplierException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        try {
            billOfQuantitiesDataMapper.setProductsDiscounts(bnNumber, productsDiscounts);
            billOfQuantities.setProductsDiscounts(productsDiscounts);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public double getProductPriceAfterDiscount(String bnNumber, String catalogNumber, int amount, double price) throws SupplierException {
        return getBillOfQuantities(bnNumber).getProductPriceAfterDiscount(catalogNumber, amount, price);
    }

    public double getPriceAfterDiscounts(String bnNumber, int amount, double priceBefore) throws SupplierException {
        return getBillOfQuantities(bnNumber).getPriceAfterDiscounts(amount, priceBefore);
    }

    public void setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, Discount discount) throws SupplierException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        try {
            billOfQuantitiesDataMapper.setDiscountOnAmountOfProducts(bnNumber, amountOfProductsForDiscount, discount);
            billOfQuantities.setDiscountOnAmountOfProducts(amountOfProductsForDiscount, discount);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public void removeDiscountOnAmountOfProducts(String bnNumber) throws SupplierException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        try {
            billOfQuantitiesDataMapper.removeDiscountOnAmountOfProducts(bnNumber);
            billOfQuantities.removeDiscountOnAmountOfProducts();
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public boolean getAmountBeforeTotal(String bnNumber) throws SupplierException {
        return getBillOfQuantities(bnNumber).getAmountBeforeTotal();
    }

    public void removeBillOfQuantities(String bnNumber) throws SupplierException {
        try {
            billOfQuantitiesDataMapper.removeBillOfQuantities(bnNumber);
        } catch (DalException e) {
            throw new SupplierException(e.getMessage(),e);
        }
    }

    public String toString(String bnNumber) {
        try {
            return getBillOfQuantities(bnNumber).toString();
        }
        catch (Exception e){
            return "";
        }
    }
}
