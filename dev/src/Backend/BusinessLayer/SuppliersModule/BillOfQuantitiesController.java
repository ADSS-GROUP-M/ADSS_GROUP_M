package Backend.BusinessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.DataAccessLayer.SuppliersModule.BillOfQuantitiesDataMappers.BillOfQuantitiesDataMapper;
import Backend.DataAccessLayer.dalUtils.DalException;

import java.sql.SQLException;
import java.util.Map;

public class BillOfQuantitiesController {
    private static BillOfQuantitiesController instance;
    private BillOfQuantitiesDataMapper billOfQuantitiesDataMapper;

    public static BillOfQuantitiesController getInstance(){
        if(instance == null)
            instance = new BillOfQuantitiesController();
        return instance;
    }

    private BillOfQuantitiesController(){
        billOfQuantitiesDataMapper = new BillOfQuantitiesDataMapper();
    }

    public boolean billOfQuantitiesExist(String bnNumber) throws SQLException, DalException {
        return getBillOfQuantities(bnNumber) != null;
    }


    public BillOfQuantities getBillOfQuantities(String bnNumber) throws SQLException, DalException {
        return billOfQuantitiesDataMapper.getBillOfQuantities(bnNumber);
    }

    private BillOfQuantities addBillOfQuantities(String bnNumber){
        return billOfQuantitiesDataMapper.addBillOfQuantities(bnNumber);
    }

    public void setOrderOfDiscounts(String bnNumber, boolean amountBeforeTotal) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        billOfQuantitiesDataMapper.setOrderOfDiscounts(bnNumber, amountBeforeTotal);
        billOfQuantities.setOrderOfDiscounts(amountBeforeTotal);
    }

    public void setDiscountOnTotalOrder(String bnNumber, double priceToActivateDiscount, Discount discount) throws SQLException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        billOfQuantitiesDataMapper.setDiscountOnTotalOrder(bnNumber, priceToActivateDiscount, discount);
        billOfQuantities.setDiscountOnTotalOrder(priceToActivateDiscount, discount);
    }

    public void removeDiscountOnTotalOrder(String bnNumber) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        billOfQuantitiesDataMapper.removeDiscountOnTotalOrder(bnNumber);
        billOfQuantities.removeDiscountOnTotalOrder();
    }

    public void addProductDiscount(String bnNumber, String catalogNumber, int amount, Discount discount) throws SQLException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        billOfQuantitiesDataMapper.addProductDiscount(bnNumber, catalogNumber, amount, discount);
        billOfQuantities.addProductDiscount(catalogNumber, amount, discount);
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber, int amount) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        billOfQuantitiesDataMapper.removeProductDiscount(bnNumber, catalogNumber, amount);
        billOfQuantities.removeProductDiscount(catalogNumber, amount);
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        billOfQuantitiesDataMapper.removeProductDiscount(bnNumber, catalogNumber);
        billOfQuantities.removeProductDiscount(catalogNumber);
    }

    public void setProductsDiscounts(String bnNumber, Map<String, Map<Integer, Discount>> productsDiscounts) throws SQLException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        billOfQuantitiesDataMapper.setProductsDiscounts(bnNumber, productsDiscounts);
        billOfQuantities.setProductsDiscounts(productsDiscounts);
    }

    public double getProductPriceAfterDiscount(String bnNumber, String catalogNumber, int amount, double price) throws SQLException, DalException {
        return getBillOfQuantities(bnNumber).getProductPriceAfterDiscount(catalogNumber, amount, price);
    }

    public double getPriceAfterDiscounts(String bnNumber, int amount, double priceBefore) throws SQLException, DalException {
        return getBillOfQuantities(bnNumber).getPriceAfterDiscounts(amount, priceBefore);
    }

    public void setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, Discount discount) throws SQLException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        billOfQuantitiesDataMapper.setDiscountOnAmountOfProducts(bnNumber, amountOfProductsForDiscount, discount);
        billOfQuantities.setDiscountOnAmountOfProducts(amountOfProductsForDiscount, discount);
    }

    public void removeDiscountOnAmountOfProducts(String bnNumber) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        billOfQuantitiesDataMapper.removeDiscountOnAmountOfProducts(bnNumber);
        billOfQuantities.removeDiscountOnAmountOfProducts();
    }

    public boolean getAmountBeforeTotal(String bnNumber) throws SQLException, DalException {
        return getBillOfQuantities(bnNumber).getAmountBeforeTotal();
    }

    public void removeBillOfQuantities(String bnNumber) throws SQLException, DalException {
        billOfQuantitiesDataMapper.removeBillOfQuantities(bnNumber);
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
