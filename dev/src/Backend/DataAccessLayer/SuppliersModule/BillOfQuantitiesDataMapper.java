package Backend.DataAccessLayer.SuppliersModule;

import Backend.BusinessLayer.SuppliersModule.BillOfQuantities;
import Backend.BusinessLayer.SuppliersModule.Discounts.Discount;
import Backend.BusinessLayer.SuppliersModule.Pair;

import java.sql.SQLException;

public class BillOfQuantitiesDataMapper {
    private ProductsDiscountsDataMapper productsDiscountsDataMapper;
    private DiscountOnTotalDataMapper discountOnTotalDataMapper;
    private DiscountOnAmountDataMapper discountOnAmountDataMapper;
    private OrderOfDiscountsDataMapper orderOfDiscountsDataMapper;

    public BillOfQuantitiesDataMapper(){
        productsDiscountsDataMapper = new ProductsDiscountsDataMapper();
        discountOnAmountDataMapper = new DiscountOnAmountDataMapper();
        discountOnTotalDataMapper = new DiscountOnTotalDataMapper();
        orderOfDiscountsDataMapper = new OrderOfDiscountsDataMapper();
    }

    private BillOfQuantities createBillOfQuantities(String bnNumber) throws SQLException {
        BillOfQuantities billOfQuantities = new BillOfQuantities(productsDiscountsDataMapper.find(bnNumber));
        Pair<Integer, Discount> discountOnAmount = discountOnAmountDataMapper.find(bnNumber);
        Pair<Double, Discount> discountOnTotal = discountOnTotalDataMapper.find(bnNumber);
        Boolean order = orderOfDiscountsDataMapper.find(bnNumber);
        boolean amountBeforeTotal = order != null && order;
        if(discountOnAmount != null)
            billOfQuantities.setDiscountOnAmountOfProducts(discountOnAmount.getFirst(), discountOnAmount.getSecond());
        if(discountOnTotal != null)
            billOfQuantities.setDiscountOnTotalOrder(discountOnTotal.getFirst(), discountOnTotal.getSecond());
        billOfQuantities.setOrderOfDiscounts(amountBeforeTotal);
        return billOfQuantities;
    }


}
