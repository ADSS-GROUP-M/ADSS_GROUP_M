package businessLayer;


import businessLayer.suppliersModule.BillOfQuantities;
import businessLayer.suppliersModule.Discounts.CashDiscount;
import businessLayer.suppliersModule.Discounts.Discount;
import businessLayer.suppliersModule.Discounts.PercentageDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BillOfQuantitiesTest {

    private BillOfQuantities billOfQuantities;
    @BeforeEach
    public void initTest(){
        billOfQuantities = new BillOfQuantities();
    }

    @Test
    public void setOrderOfDiscounts() {
        billOfQuantities.setOrderOfDiscounts(true);
        assertEquals(true, billOfQuantities.getAmountBeforeTotal(),"amount discount should be before total-price discount");
    }



    @Test
    void SetDiscountOnTotalOrder() {
        billOfQuantities.setDiscountOnTotalOrder(1000, new PercentageDiscount(10));
        assertEquals(1800, billOfQuantities.getPriceAfterDiscounts(100, 2000), "price should be 1800");
    }

    @Test
    void RemoveDiscountOnTotalOrder() {
        billOfQuantities.setDiscountOnTotalOrder(1000, new PercentageDiscount(10));
        billOfQuantities.removeDiscountOnTotalOrder();
        assertEquals(2000, billOfQuantities.getPriceAfterDiscounts(100, 2000), "price should be 2000");
    }

    @Test
    void addProductDiscount() {
        billOfQuantities.addProductDiscount("0", 10, new CashDiscount(15));
        assertEquals(1000, billOfQuantities.getProductPriceAfterDiscount("0", 100, 1015), "price should be 1000");
    }

    @Test
    void removeProductDiscount() {
        billOfQuantities.addProductDiscount("0", 10, new CashDiscount(15));
        billOfQuantities.removeProductDiscount("0", 10);
        assertEquals(1000, billOfQuantities.getProductPriceAfterDiscount("0", 100, 1000), "price should be 1000");
    }

    @Test
    void testRemoveProductDiscount() {
        billOfQuantities.addProductDiscount("0", 10, new CashDiscount(15));
        billOfQuantities.addProductDiscount("0", 15, new CashDiscount(15));
        billOfQuantities.removeProductDiscount("0");
        assertEquals(1000, billOfQuantities.getProductPriceAfterDiscount("0", 100, 1000), "price should be 1000");
    }

    @Test
    void setProductsDiscounts() {
        Map<String, Map<Integer, Discount>> productsDiscount = new HashMap<>(){{put("0", new HashMap<>());
            get("0").put(10, new CashDiscount(10)); get("0").put(15, new PercentageDiscount(10));
            put("1", new HashMap<>()); get("1").put(10, new PercentageDiscount(20));}};
        billOfQuantities.setProductsDiscounts(productsDiscount);
        assertEquals(900, billOfQuantities.getProductPriceAfterDiscount("0", 100, 1000), "price should be 900");
        assertEquals(800, billOfQuantities.getProductPriceAfterDiscount("1", 100, 1000), "price should be 800");
    }

    @Test
    void getProductPriceAfterDiscount() {
        assertEquals(1000, billOfQuantities.getProductPriceAfterDiscount("0", 100, 1000), "price should be 1000");
        billOfQuantities.addProductDiscount("0", 10, new CashDiscount(10));
        assertEquals(1000, billOfQuantities.getProductPriceAfterDiscount("0", 11, 1010), "price should be 1000");
    }

    @Test
    void getPriceAfterDiscounts() {
        billOfQuantities.addProductDiscount("0", 10, new CashDiscount(10));
        billOfQuantities.setOrderOfDiscounts(true);
        billOfQuantities.setDiscountOnTotalOrder(1000, new PercentageDiscount(10));
        billOfQuantities.setDiscountOnAmountOfProducts(1000, new CashDiscount(200));
        double price = billOfQuantities.getProductPriceAfterDiscount("0",2000, 2000);
        assertEquals(1611, billOfQuantities.getPriceAfterDiscounts(2000, price));
        billOfQuantities.setOrderOfDiscounts(false);
        assertEquals(1591, billOfQuantities.getPriceAfterDiscounts(2000, price));
    }

    @Test
    void setDiscountOnAmountOfProducts() {
        assertEquals(1100, billOfQuantities.getPriceAfterDiscounts(1001, 1100));
        billOfQuantities.setDiscountOnAmountOfProducts(1000, new CashDiscount(100));
        assertEquals(1000, billOfQuantities.getPriceAfterDiscounts(1001, 1100));
    }

    @Test
    void removeDiscountOnAmountOfProducts() {
        assertEquals(1100, billOfQuantities.getPriceAfterDiscounts(1001, 1100));
        billOfQuantities.setDiscountOnAmountOfProducts(1000, new CashDiscount(100));
        assertEquals(1000, billOfQuantities.getPriceAfterDiscounts(1001, 1100));
        billOfQuantities.removeDiscountOnAmountOfProducts();
        assertEquals(1100, billOfQuantities.getPriceAfterDiscounts(1001, 1100));

    }
}