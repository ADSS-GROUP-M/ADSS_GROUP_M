package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.BillOfQuantities;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.BusinessLayer.SuppliersModule.Pair;
import Backend.DataAccessLayer.dalUtils.DalException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BillOfQuantitiesDataMapper {
    private ProductsDiscountsDataMapper productsDiscountsDataMapper;
    private DiscountOnTotalDataMapper discountOnTotalDataMapper;
    private DiscountOnAmountDataMapper discountOnAmountDataMapper;
    private OrderOfDiscountsDataMapper orderOfDiscountsDataMapper;
    private Map<String, BillOfQuantities> suppliersBillOfQuantities;

    public BillOfQuantitiesDataMapper(){
        productsDiscountsDataMapper = new ProductsDiscountsDataMapper();
        discountOnAmountDataMapper = new DiscountOnAmountDataMapper();
        discountOnTotalDataMapper = new DiscountOnTotalDataMapper();
        orderOfDiscountsDataMapper = new OrderOfDiscountsDataMapper();
        suppliersBillOfQuantities = new HashMap<>();
    }

    public BillOfQuantities getBillOfQuantities(String bnNumber) throws SQLException, DalException {
        if(suppliersBillOfQuantities.containsKey(bnNumber))
            return suppliersBillOfQuantities.get(bnNumber);
        BillOfQuantities billOfQuantities = find(bnNumber);
        if(billOfQuantities == null)
            throw new DalException("no such Bill Of Quantities for supplier - " + bnNumber);
        suppliersBillOfQuantities.put(bnNumber, billOfQuantities);
        return billOfQuantities;
    }

    private BillOfQuantities find(String bnNumber) throws SQLException {
        BillOfQuantities billOfQuantities = null;
        Map<String, Map<Integer, Discount>> productsDiscounts = productsDiscountsDataMapper.find(bnNumber);
        Pair<Integer, Discount> discountOnAmount = discountOnAmountDataMapper.find(bnNumber);
        Pair<Double, Discount> discountOnTotal = discountOnTotalDataMapper.find(bnNumber);
        Boolean order = orderOfDiscountsDataMapper.find(bnNumber);
        boolean amountBeforeTotal = order != null && order;
        if(!productsDiscounts.isEmpty() || discountOnAmount != null || discountOnTotal != null) {
            billOfQuantities = new BillOfQuantities();
            if(!productsDiscounts.isEmpty())
                billOfQuantities.setProductsDiscounts(productsDiscounts);
            if (discountOnAmount != null)
                billOfQuantities.setDiscountOnAmountOfProducts(discountOnAmount.getFirst(), discountOnAmount.getSecond());
            if (discountOnTotal != null)
                billOfQuantities.setDiscountOnTotalOrder(discountOnTotal.getFirst(), discountOnTotal.getSecond());
            billOfQuantities.setOrderOfDiscounts(amountBeforeTotal);
        }
        return billOfQuantities;
    }

    private BillOfQuantities addBillOfQuantities(String bnNumber){
        suppliersBillOfQuantities.put(bnNumber, new BillOfQuantities());
        return suppliersBillOfQuantities.get(bnNumber);
    }

    public void setOrderOfDiscounts(String bnNumber, boolean amountBeforeTotal) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        orderOfDiscountsDataMapper.delete(bnNumber);
        orderOfDiscountsDataMapper.insert(bnNumber, amountBeforeTotal);
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
        discountOnTotalDataMapper.delete(bnNumber);
        discountOnTotalDataMapper.insert(bnNumber, priceToActivateDiscount, discount);
        billOfQuantities.setDiscountOnTotalOrder(priceToActivateDiscount, discount);
    }

    public void removeDiscountOnTotalOrder(String bnNumber) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        discountOnTotalDataMapper.delete(bnNumber);
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
        productsDiscountsDataMapper.insert(bnNumber, catalogNumber, amount, discount);
        billOfQuantities.addProductDiscount(catalogNumber, amount, discount);
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber, int amount) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        productsDiscountsDataMapper.delete(bnNumber, catalogNumber, amount);
        billOfQuantities.removeProductDiscount(catalogNumber, amount);
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        productsDiscountsDataMapper.delete(bnNumber, catalogNumber);
        billOfQuantities.removeProductDiscount(catalogNumber);    }

    public void setProductsDiscounts(String bnNumber, Map<String, Map<Integer, Discount>> productsDiscounts) throws SQLException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        for (Map.Entry<String, Map<Integer, Discount>> productDiscount : productsDiscounts.entrySet()){
            String catalogNumber = productDiscount.getKey();
            Map<Integer, Discount> amountsDiscounts = productDiscount.getValue();
            for(Map.Entry<Integer, Discount> amountDiscount : amountsDiscounts.entrySet()){
                int amount = amountDiscount.getKey();
                Discount discount = amountDiscount.getValue();
                productsDiscountsDataMapper.insert(bnNumber, catalogNumber, amount, discount);
            }
        }
        billOfQuantities.setProductsDiscounts(productsDiscounts);
    }

    public void setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, Discount discount) throws SQLException {
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        discountOnAmountDataMapper.insert(bnNumber, amountOfProductsForDiscount, discount);
        billOfQuantities.setDiscountOnAmountOfProducts(amountOfProductsForDiscount, discount);
    }

    public void removeDiscountOnAmountOfProducts(String bnNumber) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        discountOnAmountDataMapper.delete(bnNumber);
        billOfQuantities.removeDiscountOnAmountOfProducts();
    }

    public boolean getAmountBeforeTotal(String bnNumber){
        BillOfQuantities billOfQuantities = null;
        try {
            billOfQuantities = getBillOfQuantities(bnNumber);
        }
        catch (Exception e){
            billOfQuantities = addBillOfQuantities(bnNumber);
        }
        return billOfQuantities.getAmountBeforeTotal();
    }

    public void removeBillOfQuantities(String bnNumber) throws SQLException, DalException {
        BillOfQuantities billOfQuantities = getBillOfQuantities(bnNumber);
        productsDiscountsDataMapper.delete(bnNumber);
        discountOnAmountDataMapper.delete(bnNumber);
        discountOnTotalDataMapper.delete(bnNumber);
        orderOfDiscountsDataMapper.delete(bnNumber);
        suppliersBillOfQuantities.remove(bnNumber);
    }


}
