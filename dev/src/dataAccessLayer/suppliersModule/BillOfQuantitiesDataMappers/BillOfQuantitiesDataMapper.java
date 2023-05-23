package dataAccessLayer.suppliersModule.BillOfQuantitiesDataMappers;

import businessLayer.suppliersModule.BillOfQuantities;
import businessLayer.suppliersModule.Discounts.Discount;
import businessLayer.suppliersModule.Pair;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BillOfQuantitiesDataMapper {
    private final ProductsDiscountsDataMapper productsDiscountsDataMapper;
    private final DiscountOnTotalDataMapper discountOnTotalDataMapper;
    private final DiscountOnAmountDataMapper discountOnAmountDataMapper;
    private final OrderOfDiscountsDataMapper orderOfDiscountsDataMapper;
    private final Map<String, BillOfQuantities> suppliersBillOfQuantities;

    public BillOfQuantitiesDataMapper(ProductsDiscountsDataMapper productsDiscountsDataMapper,
                                      DiscountOnTotalDataMapper discountOnTotalDataMapper,
                                      DiscountOnAmountDataMapper discountOnAmountDataMapper,
                                      OrderOfDiscountsDataMapper orderOfDiscountsDataMapper){
        this.productsDiscountsDataMapper = productsDiscountsDataMapper;
        this.discountOnTotalDataMapper = discountOnTotalDataMapper;
        this.discountOnAmountDataMapper = discountOnAmountDataMapper;
        this.orderOfDiscountsDataMapper = orderOfDiscountsDataMapper;
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

    public BillOfQuantities addBillOfQuantities(String bnNumber){
        suppliersBillOfQuantities.put(bnNumber, new BillOfQuantities());
        return suppliersBillOfQuantities.get(bnNumber);
    }

    public void setOrderOfDiscounts(String bnNumber, boolean amountBeforeTotal) throws SQLException {
        orderOfDiscountsDataMapper.delete(bnNumber);
        orderOfDiscountsDataMapper.insert(bnNumber, amountBeforeTotal);
    }

    public void setDiscountOnTotalOrder(String bnNumber, double priceToActivateDiscount, Discount discount) throws SQLException {
        discountOnTotalDataMapper.delete(bnNumber);
        discountOnTotalDataMapper.insert(bnNumber, priceToActivateDiscount, discount);
    }

    public void removeDiscountOnTotalOrder(String bnNumber) throws SQLException {
        discountOnTotalDataMapper.delete(bnNumber);
    }

    public void addProductDiscount(String bnNumber, String catalogNumber, int amount, Discount discount) throws SQLException {
        productsDiscountsDataMapper.insert(bnNumber, catalogNumber, amount, discount);
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber, int amount) throws SQLException {
        productsDiscountsDataMapper.delete(bnNumber, catalogNumber, amount);
    }

    public void removeProductDiscount(String bnNumber, String catalogNumber) throws SQLException {
        productsDiscountsDataMapper.delete(bnNumber, catalogNumber);
    }
    public void setProductsDiscounts(String bnNumber, Map<String, Map<Integer, Discount>> productsDiscounts) throws SQLException {
        productsDiscountsDataMapper.delete(bnNumber);
        for (Map.Entry<String, Map<Integer, Discount>> productDiscount : productsDiscounts.entrySet()){
            String catalogNumber = productDiscount.getKey();
            Map<Integer, Discount> amountsDiscounts = productDiscount.getValue();
            for(Map.Entry<Integer, Discount> amountDiscount : amountsDiscounts.entrySet()){
                int amount = amountDiscount.getKey();
                Discount discount = amountDiscount.getValue();
                productsDiscountsDataMapper.insert(bnNumber, catalogNumber, amount, discount);
            }
        }
    }

    public void setDiscountOnAmountOfProducts(String bnNumber, int amountOfProductsForDiscount, Discount discount) throws SQLException {
        discountOnAmountDataMapper.insert(bnNumber, amountOfProductsForDiscount, discount);
    }

    public void removeDiscountOnAmountOfProducts(String bnNumber) throws SQLException {
        discountOnAmountDataMapper.delete(bnNumber);
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
